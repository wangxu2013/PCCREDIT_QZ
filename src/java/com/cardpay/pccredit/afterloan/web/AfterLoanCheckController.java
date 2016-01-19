package com.cardpay.pccredit.afterloan.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.afterloan.filter.AfterLoanCheckFilter;
import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.afterloan.model.Constants;
import com.cardpay.pccredit.afterloan.model.O_CLPM_ACC_LOAN;
import com.cardpay.pccredit.afterloan.model.PspCheckTask;
import com.cardpay.pccredit.afterloan.service.AfterloanCheckService;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.service.AttachmentListService;
import com.cardpay.pccredit.intopieces.web.PicPojo;
import com.cardpay.pccredit.intopieces.web.QzApplnYwsqbForm;
import com.cardpay.pccredit.ipad.util.SundsException;
import com.sunyard.TransEngine.exception.SunTransEngineException;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

/** 
 * @author chinh
 * @version 创建时间：2015年7月31日 下午11:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/afterloan/afterloancheck/*")
@JRadModule("afterloan.afterloancheck")
public class AfterLoanCheckController extends BaseController{
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private AfterloanCheckService afterloanCheckService;
	@Autowired
	private CircleService circleService;
	@Autowired
	private AttachmentListService attachmentListService;
	@Autowired
	private ECIFService eCIFService;
	
	private static final Logger logger = Logger.getLogger(AfterLoanCheckController.class);
	
	/**
	 * 贷后检查任务查看approve.page
	 * 
	 * @param filter iframe.page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })	
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute AfterLoanCheckFilter filter,HttpServletRequest request){
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		QueryResult<AfterLoaninfo> result = afterloanCheckService.findAfterLoanCheckTaskByFilter(filter);
		JRadPagedQueryResult<AfterLoaninfo> pagedResult = new JRadPagedQueryResult<AfterLoaninfo>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView(
				"/afterloan/afterloan_browse", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/**
	 * 检查进入
	 * page0.page
	 */
	@ResponseBody
	@RequestMapping(value = "iframe.page")
	public AbstractModelAndView loanIframe(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/afterloan/iframe", request);
		String clientNo = RequestHelper.getStringValue(request, ID);//客户编号
		String cardId = RequestHelper.getStringValue(request, "cardId");//客户身份证号码
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String type = RequestHelper.getStringValue(request, "type");
		if (StringUtils.isNotEmpty(clientNo)) {
			List<PspCheckTask> pspCheckTaskLsit = afterloanCheckService.findPspCheckTaskByTaskId(taskId);
			if(pspCheckTaskLsit != null &&pspCheckTaskLsit.size()!=0){
				mv.addObject("pspCheckTask", pspCheckTaskLsit.get(0));
				mv.addObject("taskId", pspCheckTaskLsit.get(0).getTaskId());
			}
			mv.addObject("cardId", cardId);
		}
		//根据客户编号查询客户信息
		ECIF ecif = eCIFService.findEcifByClientNo(clientNo);
		mv.addObject("ecif",ecif);
		mv.addObject("clientNo",clientNo);
		mv.addObject("type",type);
		return mv;
	}
	/**
	 * 填写检查页面
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "loanpage0.page")
	public AbstractModelAndView LoanPage0(HttpServletRequest request) {
		
		String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户编号
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String type = RequestHelper.getStringValue(request, "type");//
		JRadModelAndView mv;
		if(type.equals("readonly")){
			mv = new JRadModelAndView("/afterloan/page0_display", request);
		}else{
			mv = new JRadModelAndView("/afterloan/page0_loan", request);
		}
		if (StringUtils.isNotEmpty(clientNo)) {
			List<PspCheckTask> pspCheckTaskLsit = afterloanCheckService.findPspCheckTaskByTaskId(taskId);
			if(pspCheckTaskLsit != null &&pspCheckTaskLsit.size()!=0){
				mv.addObject("pspCheckTask", pspCheckTaskLsit.get(0));
				mv.addObject("taskId", pspCheckTaskLsit.get(0).getTaskId());
			}
		}
		//根据客户编号查询客户信息
		ECIF ecif = eCIFService.findEcifByClientNo(clientNo);
		mv.addObject("ecif",ecif);
		mv.addObject("clientNo",clientNo);
		mv.addObject("type",type);
		return mv;
	}
	
	/**
	 * 插入insert_loanpage1.json
	 */
	@ResponseBody
	@RequestMapping(value = "insert_loanpage0.json")
	public JRadReturnMap insert_loanpage0(@ModelAttribute PspCheckTask spCheckTask, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try{
				String taskId = request.getParameter("taskId");//贷后检查任务编号
				String clientNo = request.getParameter("clientNo");//客户编号
				String type = request.getParameter("type");//
				PspCheckTask pspCheckTask = new PspCheckTask();
				pspCheckTask.setAgreedPerson(spCheckTask.getAgreedPerson());
//				if(type==null){
//					pspCheckTask.setApproveStatus(Constants.approve_status_second);
//				}else{
//					pspCheckTask.setApproveStatus(Constants.approve_status_threed);
//				}
				pspCheckTask.setCheckAddr(spCheckTask.getCheckAddr());
				pspCheckTask.setCheckTime(spCheckTask.getCheckTime());
				pspCheckTask.setCusId(spCheckTask.getCusId());
				pspCheckTask.setLoanBalance(spCheckTask.getLoanBalance());
				pspCheckTask.setLoanTotlAmt(spCheckTask.getLoanTotlAmt());
				pspCheckTask.setManagerBrId(spCheckTask.getManagerBrId());
				pspCheckTask.setManagerId(spCheckTask.getManagerId());
				pspCheckTask.setQnt(spCheckTask.getQnt());
				pspCheckTask.setRemarks(spCheckTask.getRemarks());
				pspCheckTask.setTaskCreateDate(spCheckTask.getTaskCreateDate());
				pspCheckTask.setTaskHorg(spCheckTask.getTaskHorg());
				pspCheckTask.setTaskHuser(spCheckTask.getTaskHuser());
				pspCheckTask.setTaskId(taskId);
				pspCheckTask.setTaskRequestTime(spCheckTask.getTaskRequestTime());
				pspCheckTask.setTaskType(spCheckTask.getTaskType());
				pspCheckTask.setIndustryOutlook(spCheckTask.getIndustryOutlook());
				pspCheckTask.setRepayment(spCheckTask.getRepayment());
				pspCheckTask.setReciprocalType(spCheckTask.getReciprocalType());
				pspCheckTask.setContactInformation(spCheckTask.getContactInformation());
				pspCheckTask.setRepaymentOther(spCheckTask.getRepaymentOther());
				//插入数据库
				afterloanCheckService.update_page0(pspCheckTask);
				
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch(Exception e){
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	/**
	 * 填写检查页面
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "loanpage0_display.page")
	public AbstractModelAndView page0_display(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/afterloan/page0_display", request);
		String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户编号
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String type = RequestHelper.getStringValue(request, "type");
		if (StringUtils.isNotEmpty(clientNo)) {
			List<PspCheckTask> pspCheckTaskLsit = afterloanCheckService.findPspCheckTaskByTaskId(taskId);
			if(pspCheckTaskLsit != null &&pspCheckTaskLsit.size()!=0){
				mv.addObject("pspCheckTask", pspCheckTaskLsit.get(0));
				mv.addObject("taskId", pspCheckTaskLsit.get(0).getTaskId());
			}
		}
		//根据客户编号查询客户信息
		ECIF ecif = eCIFService.findEcifByClientNo(clientNo);
		mv.addObject("ecif",ecif);
		mv.addObject("clientNo",clientNo);
		mv.addObject("type",type);
		return mv;
	}
	
	/**
	 * 填写检查页面(台账信息)
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "loanpage1.page")
	public AbstractModelAndView page1(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/afterloan/page1_display", request);
		String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户编号
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String type = RequestHelper.getStringValue(request, "type");
		if (StringUtils.isNotEmpty(clientNo)) {
			List<O_CLPM_ACC_LOAN> accLoanLsit = afterloanCheckService.findAccLoanByclientNo(clientNo);
			for(int i =0; i < accLoanLsit.size();i++){
				O_CLPM_ACC_LOAN accloan = accLoanLsit.get(i);
				accloan.setRowIndex(i+1+"");
			}
			mv.addObject("accLoanLsit", accLoanLsit);
			mv.addObject("taskId", taskId);
		}
		mv.addObject("clientNo",clientNo);
		mv.addObject("taskId",taskId);
		mv.addObject("type",type);
		return mv;
	}
	/**
	 * 客户经理提交检查到审核
	 */
	@ResponseBody
	@RequestMapping(value = "insert_loanpage1.json")
	public JRadReturnMap insert_loanpage1(@ModelAttribute PspCheckTask spCheckTask, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try{
				String taskId = request.getParameter("taskId");//贷后检查任务编号
				String clientNo = request.getParameter("clientNo");//客户编号
				String type = request.getParameter("type");//
				PspCheckTask pspCheckTask = new PspCheckTask();
				
				if("write".equals(type)){
					pspCheckTask.setApproveStatus(Constants.approve_status_second);
				}else{
					pspCheckTask.setApproveStatus(Constants.approve_status_threed);
				}
				pspCheckTask.setTaskId(taskId);
				//插入数据库
				afterloanCheckService.update_page1(pspCheckTask);
				
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch(Exception e){
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	/**
	 * 团队长退回检查
	 */
	
	@ResponseBody
	@RequestMapping(value = "return_loanpage1.json")
	public JRadReturnMap return_loanpage1(@ModelAttribute PspCheckTask spCheckTask, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try{
				String taskId = request.getParameter("taskId");//贷后检查任务编号
				String clientNo = request.getParameter("clientNo");//客户编号
				String type = request.getParameter("type");//
				PspCheckTask pspCheckTask = new PspCheckTask();
				
				
					pspCheckTask.setApproveStatus(Constants.approve_status_first);
				
				pspCheckTask.setTaskId(taskId);
				//插入数据库
				afterloanCheckService.update_page1(pspCheckTask);
				
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch(Exception e){
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	//贷后 影像资料
	@ResponseBody
	@RequestMapping(value = "browse_folder.page")
	public AbstractModelAndView browse_folder(HttpServletRequest request) {
		JRadModelAndView mv = null;
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户号
		String type = RequestHelper.getStringValue(request, "type");//客户号
		PspCheckTask task = afterloanCheckService.findPspCheckTaskByTaskId(taskId).get(0);
		mv = new JRadModelAndView("/afterloan/sunds_browse_folder", request);
		mv.addObject("type",type);
		mv.addObject("taskId",taskId);
		mv.addObject("clientNo",clientNo);
		return mv;
	}
	
	//浏览图片
	@ResponseBody
	@RequestMapping(value = "browse_folder.json")
	public void browse_folder_json(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
		String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户号
		String type = RequestHelper.getStringValue(request, "type");//客户号
		//更新batch
		attachmentListService.browse_folder(file,taskId + clientNo);
		response.getWriter().write("true");
	}
	
	//浏览图片完毕  开始通知后台上传影像平台
	@ResponseBody
	@RequestMapping(value = "browse_folder_complete.json")
	public JRadReturnMap browse_folder_complete(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String taskId = RequestHelper.getStringValue(request, "taskId");//任务编号
			String clientNo = RequestHelper.getStringValue(request, "clientNo");//客户号
			String type = RequestHelper.getStringValue(request, "type");//客户号
			
			attachmentListService.browse_folder_complete_pspcheck(taskId,request);
			
			returnMap.put(JRadConstants.SUCCESS, true);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			returnMap.put(JRadConstants.SUCCESS, false);
			e.printStackTrace();
		}
		return returnMap;
		
	}	
	
	//查看已上传的图片列表
	@ResponseBody
	@RequestMapping(value = "display_server.page")
	public AbstractModelAndView display_server(@ModelAttribute IntoPiecesFilter filter,HttpServletRequest request) {
		filter.setRequest(request);
		
		JRadModelAndView mv = new JRadModelAndView("/afterloan/sunds_display_server", request);
		
		QueryResult<PicPojo> result = new QueryResult<PicPojo>(0, null);
		try {
			result = attachmentListService.display_server_psp(filter,request);
			List<PicPojo> pic_ls = result.getItems();
			for(PicPojo pojo : pic_ls){
				String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where id = '"+pojo.getFile_name().split("\\.")[0]+"'";
				QzApplnAttachmentDetail details = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null).get(0);
				pojo.setDetail_id(pojo.getFile_name().split("\\.")[0]);
				pojo.setFile_name(details.getOriginalName().split("\\.")[0] + "." + pojo.getFile_name().split("\\.")[1]);
				pojo.setFile_no_local(details.getFileNo());
			}
		} catch (SunTransEngineException e) {
			logger.info("批次["+filter.getTaskId()+"]查询文件异常:" ,e);
		} catch (DocumentException e) {
			logger.info("批次["+filter.getTaskId()+"]解析影像平台xml异常:" ,e);
		} catch (SundsException e) {
			logger.info("批次["+filter.getTaskId()+"]解析影像平台xml异常:" ,e);
		}
		filter.setFirst_flag("0");
		JRadPagedQueryResult<PicPojo> pagedResult = new JRadPagedQueryResult<PicPojo>(filter, result);
		mv.addObject(PAGED_RESULT, pagedResult);
		mv.addObject("type",filter.getType());
		mv.addObject("taskId",filter.getTaskId());
		mv.addObject("clientNo",filter.getClientNo());
		return mv;
	}
	
	//跳转到选择更新图片页面
	@ResponseBody
	@RequestMapping(value = "browse_update_folder.page")
	public AbstractModelAndView browse_update_folder_page(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/afterloan/sunds_browse_update_folder", request);
		String detail_id = RequestHelper.getStringValue(request, "detail_id");
		String file_no = RequestHelper.getStringValue(request, "file_no");
		String type = RequestHelper.getStringValue(request, "type");
		String taskId = RequestHelper.getStringValue(request, "taskId");
		String clientNo = RequestHelper.getStringValue(request, "clientNo");
		String sql = "select * from QZ_APPLN_ATTACHMENT_DETAIL where id = '"+detail_id+"'";
		QzApplnAttachmentDetail detail = commonDao.queryBySql(QzApplnAttachmentDetail.class, sql, null).get(0);
		mv.addObject("detail_id", detail_id);
		mv.addObject("file_no", file_no);
		mv.addObject("batch_id", detail.getBatchId());
		
		mv.addObject("type",type);
		mv.addObject("taskId",taskId);
		mv.addObject("clientNo",clientNo);
		return mv;
	}	
	
	//浏览图片
	@ResponseBody
	@RequestMapping(value = "browse_update_folder.json")
	public void browse_update_folder_json(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String detail_id = RequestHelper.getStringValue(request, "detail_id");
		String file_no = RequestHelper.getStringValue(request, "file_no");
		//更新batch
		attachmentListService.browse_update_folder(file,detail_id,file_no,request);
		response.getWriter().write("true");
	}
		
	//删除影像平台上的文件
	@ResponseBody
	@RequestMapping(value = "delete_server_file.json")
	public JRadReturnMap delete_server_file(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String detail_id = RequestHelper.getStringValue(request, "detail_id");
			String file_no = RequestHelper.getStringValue(request, "file_no");
			String doc_id = RequestHelper.getStringValue(request, "doc_id");
			
			attachmentListService.delete_server_file(detail_id,file_no,doc_id,request);
			
			returnMap.put(JRadConstants.SUCCESS, true);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("删除失败");
			returnMap.put(JRadConstants.SUCCESS, false);
			e.printStackTrace();
		}
		return returnMap;
		
	}	
		
}
