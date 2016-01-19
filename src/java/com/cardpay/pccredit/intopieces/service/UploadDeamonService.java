package com.cardpay.pccredit.intopieces.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentBatch;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.sunyard.TransEngine.exception.SunTransEngineException;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class UploadDeamonService{

	public static final Logger logger = Logger.getLogger(UploadDeamonService.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SundsHelper sundsHelper;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutorUpload;
	
	private int MAX_THREAD = 3;//最大线程数量
	
	//上传
	public void doUpload(){
		//前一次任务未完跳过本次
		if(taskExecutorUpload.getActiveCount() > 0){
			return;
		}
		
		try {
			//查询缓存的batch
			String sql = "select * from QZ_APPLN_ATTACHMENT_BATCH where is_upload = '0'";
			List<QzApplnAttachmentBatch> batch_ls = commonDao.queryBySql(QzApplnAttachmentBatch.class, sql, null);
			for(QzApplnAttachmentBatch batch : batch_ls){
				if(taskExecutorUpload.getActiveCount() >= MAX_THREAD){
					return;
				}
				sundsHelper.login();
				//启动后台线程开始上传该批次
				sql = "select * from QZ_APPLN_ATTACHMENT_LIST where id = '"+batch.getAttId()+"'";
				QzApplnAttachmentList att = commonDao.queryBySql(QzApplnAttachmentList.class, sql, null).get(0);
				String docId = att.getDocid() + batch.getType();
				taskExecutorUpload.execute(new UploadThread(batch.getId(),batch.getName(),docId,att,batch));
				//sundsHelper.logOut();
			}
		} catch (SunTransEngineException e) {
			// TODO Auto-generated catch block
			logger.info("上传任务执行出错:", e);
		}
	}
	
	//上传线程
	public class UploadThread implements Runnable{

		public String batch_id;
		public String batch_name;
		public String docId;
		public QzApplnAttachmentBatch batch;
		public QzApplnAttachmentList att;
		public UploadThread(String batch_id,String batch_name,String docId,QzApplnAttachmentList att,QzApplnAttachmentBatch batch){
			this.batch_id = batch_id;
			this.batch_name = batch_name;
			this.docId = docId;
			this.att = att;
			this.batch = batch;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//上传
			String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where is_upload is null and batch_id='"+batch_id+"'";
			List<QzApplnAttachmentDetail> details_ls = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null);
			if(details_ls != null && details_ls.size() > 0){
				try {
					//查询该批次下有无文件，有则追加 无则直接add上传
					String str = sundsHelper.queryBatchFile(batch_id,docId);
					if(str.indexOf("FILE_PART") >= 0){
						sundsHelper.addMore(batch_id, batch_name, details_ls, docId);
					}
					else{
						sundsHelper.add(batch_id, batch_name,details_ls,docId);
					}
					
					//如果没有异常 则
					//1 标记该批次为已上传状态
					sql = "update QZ_APPLN_ATTACHMENT_BATCH set is_upload = '1' where id='"+batch_id+"'";
					commonDao.queryBySql(sql,null);
					//2将对应的detail标记为已上传
					sql = "update QZ_APPLN_ATTACHMENT_DETAIL set is_upload = '1' where batch_id='"+batch_id+"'";
					commonDao.queryBySql(sql,null);
					//3将att表的upload_value做累加
					if((Integer.parseInt(att.getUploadValue()) & Integer.parseInt(batch.getType())) != Integer.parseInt(batch.getType())){
						int value = Integer.parseInt(att.getUploadValue()) + Integer.parseInt(batch.getType());
						sql = "update QZ_APPLN_ATTACHMENT_LIST set upload_value = '"+value+"' where id='"+att.getId()+"'";
						commonDao.queryBySql(sql,null);
					}
					//4删除缓存的图片
					for(QzApplnAttachmentDetail detail : details_ls){
						UploadFileTool.deleteFile_qz(Constant.FILE_PATH + batch_id + "/" + detail.getFileName());
					}
					
					
				} catch (SunTransEngineException e) {
					// TODO Auto-generated catch block
					logger.info("批次["+batch_name+"] 批次号["+batch_id+"] 上传异常:" ,e);
				}
			}
		}
	}

}
