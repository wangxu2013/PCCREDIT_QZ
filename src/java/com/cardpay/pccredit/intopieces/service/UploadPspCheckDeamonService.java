package com.cardpay.pccredit.intopieces.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.afterloan.model.PspCheckTask;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentBatch;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.sunyard.TransEngine.exception.SunTransEngineException;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class UploadPspCheckDeamonService{

	public static final Logger logger = Logger.getLogger(UploadPspCheckDeamonService.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SundsHelper sundsHelper;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutorUploadPspCheck;
	
	private int MAX_THREAD = 3;//最大线程数量
	
	//上传
	public void doUpload(){
		//前一次任务未完跳过本次
		if(taskExecutorUploadPspCheck.getActiveCount() > 0){
			return;
		}
		
		try {
			//查询缓存的batch
			String sql = "select * from PSP_CHECK_TASK where upload_flag = '0'";
			List<PspCheckTask> task_ls = commonDao.queryBySql(PspCheckTask.class, sql, null);
			for(PspCheckTask task : task_ls){
				if(taskExecutorUploadPspCheck.getActiveCount() >= MAX_THREAD){
					return;
				}
				sundsHelper.login();
				//启动后台线程开始上传该批次
				String docId = task.getTaskId() + task.getCusId();
				taskExecutorUploadPspCheck.execute(new UploadThread(task.getTaskId(),task.getCusId(),docId));
				//sundsHelper.logOut();
			}
		} catch (SunTransEngineException e) {
			// TODO Auto-generated catch block
			logger.info("上传任务执行出错:", e);
		}
	}
	
	//上传线程
	public class UploadThread implements Runnable{

		public String task_id;
		public String cus_id;
		public String docId;
		public UploadThread(String task_id,String cus_id,String docId){
			this.task_id = task_id;
			this.cus_id = cus_id;
			this.docId = docId;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//上传
			String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where is_upload is null and batch_id='"+docId+"'";
			List<QzApplnAttachmentDetail> details_ls = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null);
			if(details_ls != null && details_ls.size() > 0){
				try {
					//查询该批次下有无文件，有则追加 无则直接add上传
					String str = sundsHelper.queryBatchFile(task_id,docId);
					if(str.indexOf("FILE_PART") >= 0){
						sundsHelper.addMore(docId, task_id, details_ls, docId);
					}
					else{
						sundsHelper.add(docId, task_id,details_ls,docId);
					}
					
					//如果没有异常 则
					//1 标记该批次为已上传状态
					sql = "update PSP_CHECK_TASK set upload_flag = '1' where task_id='"+task_id+"'";
					commonDao.queryBySql(sql,null);
					//2将对应的detail标记为已上传
					sql = "update QZ_APPLN_ATTACHMENT_DETAIL set is_upload = '1' where batch_id='"+docId+"'";
					commonDao.queryBySql(sql,null);
					
					//3删除缓存的图片
					for(QzApplnAttachmentDetail detail : details_ls){
						UploadFileTool.deleteFile_qz(Constant.FILE_PATH + docId + "/" + detail.getFileName());
					}
					
					
				} catch (SunTransEngineException e) {
					// TODO Auto-generated catch block
					logger.info("批次["+docId+"] 批次号["+task_id+"] 上传异常:" ,e);
				}
			}
		}
	}

}
