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
public class UpdatePspCheckDeamonService{

	public static final Logger logger = Logger.getLogger(UpdatePspCheckDeamonService.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SundsHelper sundsHelper;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutorUpdatePspCheck;
	
	private int MAX_THREAD = 3;//最大线程数量
	
	//上传
	public void doUpdate(){
		//前一次任务未完跳过本次
		if(taskExecutorUpdatePspCheck.getActiveCount() > 0){
			return;
		}
		
		try {
			//查询缓存的batch
			String sql = "select * from PSP_CHECK_TASK where task_id in (select substr(batch_id,0,16) from QZ_APPLN_ATTACHMENT_DETAIL where file_no is not null)";
			List<PspCheckTask> task_ls = commonDao.queryBySql(PspCheckTask.class, sql, null);
			for(PspCheckTask task : task_ls){
				if(taskExecutorUpdatePspCheck.getActiveCount() >= MAX_THREAD){
					return;
				}
				sundsHelper.login();
				//启动后台线程开始上传该批次
				String docId = task.getTaskId() + task.getCusId();
				taskExecutorUpdatePspCheck.execute(new UpdateThread(task.getTaskId(),task.getCusId(),docId));
				//sundsHelper.logOut();
			}
		} catch (SunTransEngineException e) {
			// TODO Auto-generated catch block
			logger.info("上传任务执行出错:", e);
		}
	}
	
	//上传线程
	public class UpdateThread implements Runnable{

		public String task_id;
		public String cus_id;
		public String docId;
		public UpdateThread(String task_id,String cus_id,String docId){
			this.task_id = task_id;
			this.cus_id = cus_id;
			this.docId = docId;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			//更新
			String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where file_no is not null and batch_id='"+docId+"'";
			List<QzApplnAttachmentDetail> details_ls = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null);
			if(details_ls != null && details_ls.size() > 0){
				try {
					sundsHelper.update(docId, task_id,details_ls,docId);
					
					//如果没有异常 则
					//1将对应的detail的file_no置空
					for(QzApplnAttachmentDetail detail : details_ls){
						sql = "update QZ_APPLN_ATTACHMENT_DETAIL set file_no = null where id='"+detail.getId()+"'";
						commonDao.queryBySql(sql,null);
						//2删除缓存的图片
						UploadFileTool.deleteFile_qz(Constant.FILE_PATH + docId + "/" + detail.getFileName());
					}
					
				} catch (SunTransEngineException e) {
					// TODO Auto-generated catch block
					logger.info("批次["+docId+"] 批次号["+task_id+"] 更新异常:" ,e);
				}
			}
		}
	}

}
