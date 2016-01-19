package com.cardpay.pccredit.intopieces.service;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.divisional.constant.DivisionalProgressEnum;
import com.cardpay.pccredit.divisional.constant.DivisionalTypeEnum;
import com.cardpay.pccredit.divisional.service.DivisionalService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.IntoPiecesException;
import com.cardpay.pccredit.intopieces.dao.AttachmentListDao;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationIntopieceWaitDao;
import com.cardpay.pccredit.intopieces.dao.DbrxxDao;
import com.cardpay.pccredit.intopieces.dao.IntoPiecesDao;
import com.cardpay.pccredit.intopieces.dao.comdao.IntoPiecesComdao;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.filter.MakeCardFilter;
import com.cardpay.pccredit.intopieces.model.ApplicationDataImport;
import com.cardpay.pccredit.intopieces.model.BasicCustomerInformationS;
import com.cardpay.pccredit.intopieces.model.CustomerAccountData;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationCom;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.CustomerCareersInformationS;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.MakeCard;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentBatch;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.QzApplnProcessResult;
import com.cardpay.pccredit.intopieces.model.QzApplnSxjc;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.cardpay.pccredit.intopieces.model.VideoAccessories;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.intopieces.web.Pic;
import com.cardpay.pccredit.intopieces.web.PicPojo;
import com.cardpay.pccredit.intopieces.web.QzApplnSxjcForm;
import com.cardpay.pccredit.intopieces.web.QzDcnrUploadForm;
import com.cardpay.pccredit.intopieces.web.QzSdhjydForm;
import com.cardpay.pccredit.intopieces.web.QzShouxinForm;
import com.cardpay.pccredit.ipad.util.SundsException;
import com.cardpay.pccredit.product.model.AddressAccessories;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.workflow.dao.WfStatusResultDao;
import com.cardpay.workflow.models.WfProcessRecord;
import com.cardpay.workflow.models.WfStatusQueueRecord;
import com.sunyard.TransEngine.exception.SunTransEngineException;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.modules.privilege.model.TreeNode;
import com.wicresoft.util.spring.Beans;

@Service
public class AttachmentListService {

	// TODO 路径使用相对路径，首先获得应用所在路径，之后建立上传文件目录，图片类型使用IMG，文件使用DOC

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IntoPiecesDao intoPiecesDao;

	@Autowired
	private SundsHelper sundsHelper;
	
	@Autowired
	private IntoPiecesComdao intoPiecesComdao;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private DivisionalService divisionalService;
	
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;

	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;
	
	@Autowired
	private WfStatusResultDao wfStatusResultDao;
	
	@Autowired
	private AttachmentListDao attachmentListDao;
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
	/* 查询附件列表 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public QzApplnAttachmentList findAttachmentList(String customerId,String applicationId) {
		return attachmentListDao.findAttachmentList(customerId, applicationId);
	}

	public QzApplnAttachmentList findAttachmentListByAppId(String applicationId) {
		return attachmentListDao.findAttachmentListByAppId(applicationId);
	}
	
	public List<QzApplnAttachmentBatch> findAttachmentBatchByAppId(String applicationId) {
		return attachmentListDao.findAttachmentBatchByAppId(applicationId);
	}
	
	public void addBatchInfo(String appId){
		QzApplnAttachmentList att = this.findAttachmentListByAppId(appId);
		if(att != null){
			//插入batch表
			for(int i=0 ; i<=30 ; i++){
				if(att.getBussType().equals("1")){
					if((Integer.parseInt(att.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
						QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
						batch.setAttId(att.getId());
						batch.setName(Constant.ATT_BATCH_1.get((int)Math.pow(2, i)));
						batch.setType((int)Math.pow(2, i)+"");
						commonDao.insertObject(batch);
					}
				}
				else{
					if((Integer.parseInt(att.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
						QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
						batch.setAttId(att.getId());
						batch.setName(Constant.ATT_BATCH_2.get((int)Math.pow(2, i)));
						batch.setType((int)Math.pow(2, i)+"");
						commonDao.insertObject(batch);
					}
				}
			}
		}
	}
	
	public void insert_page5(QzApplnAttachmentList qzApplnAttachmentList,HttpServletRequest request){
		//保存清单至调查内容表
		commonDao.insertObject(qzApplnAttachmentList);
		
		//插入batch表
		for(int i=0 ; i<=30 ; i++){
			if(qzApplnAttachmentList.getBussType().equals("1")){
				if((Integer.parseInt(qzApplnAttachmentList.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
					QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
					batch.setAttId(qzApplnAttachmentList.getId());
					batch.setName(Constant.ATT_BATCH_1.get((int)Math.pow(2, i)));
					batch.setType((int)Math.pow(2, i)+"");
					commonDao.insertObject(batch);
				}
			}
			else{
				if((Integer.parseInt(qzApplnAttachmentList.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
					QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
					batch.setAttId(qzApplnAttachmentList.getId());
					batch.setName(Constant.ATT_BATCH_2.get((int)Math.pow(2, i)));
					batch.setType((int)Math.pow(2, i)+"");
					commonDao.insertObject(batch);
				}
			}
		}
	}

	public void update_page5(QzApplnAttachmentList qzApplnAttachmentList,HttpServletRequest request){
		//保存清单至调查内容表
		commonDao.updateObject(qzApplnAttachmentList);
		
		String sql = "delete from QZ_APPLN_ATTACHMENT_BATCH where is_upload is null and att_id = '"+qzApplnAttachmentList.getId()+"'";
		commonDao.queryBySql(sql, null);
		
		//插入batch表
		for(int i=0 ; i<=30 ; i++){
			if(qzApplnAttachmentList.getBussType().equals("1")){
				if((Integer.parseInt(qzApplnAttachmentList.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
					QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
					batch.setAttId(qzApplnAttachmentList.getId());
					batch.setName(Constant.ATT_BATCH_1.get((int)Math.pow(2, i)));
					batch.setType((int)Math.pow(2, i)+"");
					commonDao.insertObject(batch);
				}
			}
			else{
				if((Integer.parseInt(qzApplnAttachmentList.getChkValue()) & (int)Math.pow(2, i)) == (int)Math.pow(2, i)){
					QzApplnAttachmentBatch batch = new QzApplnAttachmentBatch();
					batch.setAttId(qzApplnAttachmentList.getId());
					batch.setName(Constant.ATT_BATCH_2.get((int)Math.pow(2, i)));
					batch.setType((int)Math.pow(2, i)+"");
					commonDao.insertObject(batch);
				}
			}
		}
				
	}
	
	public void addAttNode(TreeNode rootNode,QzApplnAttachmentList qzApplnAttachmentList,String pValue,String name){
		for(int i = 0;i<15;i++){
			rootNode.addChildren(new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"_"+pValue+"_"+new Double(Math.pow(2, i)).intValue(), null,name+":"+(i+1), "", "", "", "page.gif",false, true, false, true, false));
		}
	}

	public QzApplnAttachmentBatch findAttachmentBatchById(String batch_id) {
		// TODO Auto-generated method stub
		String sql = "select * from QZ_APPLN_ATTACHMENT_BATCH where id = '"+batch_id+"'";
		return commonDao.queryBySql(QzApplnAttachmentBatch.class, sql, null).get(0);
	}
	
	public QzApplnAttachmentList findAttachmentListById(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from QZ_APPLN_ATTACHMENT_LIST where id = '"+id+"'";
		return commonDao.queryBySql(QzApplnAttachmentList.class, sql, null).get(0);
	}

	//浏览文件并缓存到服务器目录
	public void browse_folder(MultipartFile file,String batch_id) throws Exception {
		// TODO Auto-generated method stub
		String newFileName = UploadFileTool.uploadYxzlFileBySpring_qz(file,batch_id);
		QzApplnAttachmentDetail detail = new QzApplnAttachmentDetail();
		detail.setBatchId(batch_id);
		detail.setOriginalName(file.getOriginalFilename());
		detail.setFileName(newFileName);
		detail.setPicSize(file.getSize() + "");
		commonDao.insertObject(detail);
	}
	
	//浏览文件并缓存到服务器目录
	public void browse_update_folder(MultipartFile file,String detail_id,String file_no,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where id = '"+detail_id+"'";
		QzApplnAttachmentDetail detail =  commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null).get(0);
		
		String newFileName = UploadFileTool.uploadYxzlFileBySpring_qz(file,detail.getBatchId());
		detail.setOriginalName(file.getOriginalFilename());
		detail.setFileName(newFileName);
		detail.setPicSize(file.getSize() + "");
		detail.setFileNo(file_no);
		commonDao.updateObject(detail);
	}

	public void browse_folder_complete(String batch_id,HttpServletRequest request){
		//将is_upload 置为0
		String sql = "update QZ_APPLN_ATTACHMENT_BATCH set is_upload = '0' where id='"+batch_id+"'";
		commonDao.queryBySql(sql, null);
	}
	
	public void browse_folder_complete_pspcheck(String task_id,HttpServletRequest request){
		//将upload_flag 置为0
		String sql = "update PSP_CHECK_TASK set upload_flag = '0' where task_id='"+task_id+"'";
		commonDao.queryBySql(sql, null);
	}
	
	/**
	//用不到了
	public void browse_folder(String batch_id, String folder) throws Exception {
		// TODO Auto-generated method stub
		//上传文件夹内的文件到服务器缓存目录
		QzApplnAttachmentBatch batch = this.findAttachmentBatchById(batch_id);
		if(batch.getIsUpload() == null){//第一次缓存
			this.readFile(folder,batch_id);
		}
		else if(batch.getIsUpload().equals("0")){//重新缓存
			UploadFileTool.deleteFile(Constant.FILE_PATH + batch_id + "/");//先删除
			this.readFile(folder,batch_id);
		}
	
		batch.setFolder(folder);
		batch.setIsUpload("0");//已缓存
		commonDao.updateObject(batch);
	}
	
	//循环读取目录--用不到了
	public void readFile(String folder,String batch_id) throws Exception{
		File file = new File(folder);
		if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0;i<fileList.length;i++){
				File readFile = new File(folder + "\\" + fileList[i]);
				
				if(!readFile.isDirectory()){//读取文件
					UploadFileTool.uploadYxzlFileBySpring_qz(readFile,batch_id);
				}
				else if(readFile.isDirectory()){//循环
					readFile(folder + "\\" + fileList[i],batch_id);
				}
			}
		}
		else{//读取文件
			UploadFileTool.uploadYxzlFileBySpring_qz(file,batch_id);
		}
	}
	
	//public static void main(String[] args) {
	//	readFile("E:\\截图\\","111222333");
	//}
	*/

	public QueryResult<QzApplnAttachmentDetail> display_detail(IntoPiecesFilter filter) {
		List<QzApplnAttachmentDetail> pList = attachmentListDao.findDetailByFilter(filter);
		int size = attachmentListDao.findDetailCountByFilter(filter);
		QueryResult<QzApplnAttachmentDetail> queryResult = new QueryResult<QzApplnAttachmentDetail>(size, pList);
		return queryResult;
	}

	public QueryResult<PicPojo> display_server(IntoPiecesFilter filter,HttpServletRequest request) throws SunTransEngineException, DocumentException, SundsException {
		// TODO Auto-generated mfilter.getBatchId()ethod stub
		String sql = "select * from QZ_APPLN_ATTACHMENT_BATCH where id = '"+filter.getBatchId()+"'";
		QzApplnAttachmentBatch batch = commonDao.queryBySql(QzApplnAttachmentBatch.class, sql, null).get(0);
		sql = "select * from QZ_APPLN_ATTACHMENT_LIST where id = '"+batch.getAttId()+"'";
		QzApplnAttachmentList att = commonDao.queryBySql(QzApplnAttachmentList.class, sql, null).get(0);
		
		String xmlStr = null;
		if(filter.getFirst_flag().equals("1")){
			request.getSession().setAttribute(filter.getBatchId(), null);
		}
		String sessionTmp = request.getSession().getAttribute(filter.getBatchId())==null?null:request.getSession().getAttribute(filter.getBatchId()).toString();
		if(StringUtils.isEmpty(sessionTmp)){
			xmlStr = sundsHelper.queryBatchFile(filter.getBatchId(),att.getDocid() + batch.getType());
			request.getSession().setAttribute(filter.getBatchId(), xmlStr);
		}
		else{
			xmlStr = sessionTmp;
		}
		Pic pic = sundsHelper.parseXml(xmlStr, filter.getPage(), filter.getLimit());
		
		List<PicPojo> pList = pic.getPics();
		int size = pic.getTotalCount();
		QueryResult<PicPojo> queryResult = new QueryResult<PicPojo>(size, pList);
		
		return queryResult;
	}

	public QueryResult<PicPojo> display_server_psp(IntoPiecesFilter filter,HttpServletRequest request) throws SunTransEngineException, DocumentException, SundsException {
		// TODO Auto-generated mfilter.getBatchId()ethod stub
		String xmlStr = null;
		String docId = filter.getTaskId()+filter.getClientNo();
		if(filter.getFirst_flag().equals("1")){
			request.getSession().setAttribute(docId, null);
		}
		String sessionTmp = request.getSession().getAttribute(docId)==null?null:request.getSession().getAttribute(docId).toString();
		if(StringUtils.isEmpty(sessionTmp)){
			xmlStr = sundsHelper.queryBatchFile(filter.getTaskId(),docId);
			request.getSession().setAttribute(docId, xmlStr);
		}
		else{
			xmlStr = sessionTmp;
		}
		Pic pic = sundsHelper.parseXml(xmlStr, filter.getPage(), filter.getLimit());
		
		List<PicPojo> pList = pic.getPics();
		int size = pic.getTotalCount();
		QueryResult<PicPojo> queryResult = new QueryResult<PicPojo>(size, pList);
		
		return queryResult;
	}
	
	public void delete_server_file(String detail_id, String file_no,String doc_id,HttpServletRequest request) throws SunTransEngineException {
		// TODO Auto-generated method stub
		String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where id = '"+detail_id+"'";
		QzApplnAttachmentDetail detail = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null).get(0);
		sundsHelper.del(file_no, doc_id,detail.getBatchId());
		sql = "delete from QZ_APPLN_ATTACHMENT_DETAIL where id = '"+detail_id+"'";
		commonDao.queryBySql(sql, null);
		
		request.getSession().setAttribute(detail.getBatchId(), null);
	}
	
	public void delete_batch(String batchId,HttpServletRequest request) throws SunTransEngineException{
		String sql = "select * from QZ_APPLN_ATTACHMENT_BATCH where id = '"+batchId+"'";
		QzApplnAttachmentBatch batch = commonDao.queryBySql(QzApplnAttachmentBatch.class, sql, null).get(0);
		sql = "select * from QZ_APPLN_ATTACHMENT_LIST where id = '"+batch.getAttId()+"'";
		QzApplnAttachmentList att = commonDao.queryBySql(QzApplnAttachmentList.class, sql, null).get(0);
		String docId = att.getDocid() + batch.getType();
		
		sundsHelper.delBatch(batchId, docId);
		
		//删除对应detail
		sql = "delete from QZ_APPLN_ATTACHMENT_DETAIL where batch_id = '"+batchId+"'";
		commonDao.queryBySql(sql, null);
		//将att的upload_value减去对应的批次值
		int tmp = Integer.parseInt(att.getUploadValue())-Integer.parseInt(batch.getType());
		att.setUploadValue(tmp + "");
		commonDao.updateObject(att);
		//将对应batch的is_upload状态置为null
		sql = "update QZ_APPLN_ATTACHMENT_BATCH set is_upload = null where ID = '"+batchId+"'";
		commonDao.queryBySql(sql, null);
		
		
		request.getSession().setAttribute(batchId, null);
	}
}
