package com.cardpay.pccredit.intopieces.web;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForCircleForm;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.filter.CustomerApplicationProcessFilter;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.QzSdhjyd;
import com.cardpay.pccredit.intopieces.model.QzShouxin;
import com.cardpay.pccredit.intopieces.model.QzSyjy;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationIntopieceWaitService;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationProcessService;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.constant.JRadConstants;
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

@Controller
@RequestMapping("/intopieces/intopiecesshouxin/*")
@JRadModule("intopieces.intopiecesshouxin")
public class IntoPiecesShouxinControl extends BaseController {

	@Autowired
	private IntoPiecesService intoPiecesService;

	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;
	
	@Autowired
	private ECIFService eCIFService;
	@Autowired
	private CircleService circleService;
	
	/**
	 * 授信岗进件页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "shouxin.page", method = { RequestMethod.GET })
	public AbstractModelAndView shouxin(@ModelAttribute CustomerApplicationProcessFilter filter, HttpServletRequest request) throws SQLException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setLoginId(loginId);
		filter.setNodeName(Constant.status_shouxin);
		QueryResult<CustomerApplicationIntopieceWaitForm> result = customerApplicationIntopieceWaitService.recieveIntopieceWaitForm(filter);
		JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm> pagedResult = new JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm>(filter, result);

		JRadModelAndView mv = new JRadModelAndView(
				"/intopieces/intopieces_wait/intopiecesApprove_shouxin", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		mv.addObject("filter", filter);
		return mv;
	}
	
	/**
	 * 进入授信决议单页面
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create_form.page")
	public AbstractModelAndView createForm(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page8_for_approve", request);
		String customerId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerId)) {
			QzSdhjyd qzSdhjyd = intoPiecesService.getSdhjydForm(customerId);
			mv.addObject("customerId", customerId);
			mv.addObject("result", qzSdhjyd);
		}
		return mv;
	}
	
	/**
	 * 授信决议单保存
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "insert.json")
	public JRadReturnMap insert(@ModelAttribute QzShouxinForm qzShouxinForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String appId = request.getParameter("appId");
				QzShouxin qzShouxin = qzShouxinForm.createModel(QzShouxin.class);
				qzShouxin.setApplicationId(appId);
				intoPiecesService.insertShouxinForm(qzShouxin,appId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	/**
	 * 进入审议纪要页面
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create_syjy_form.page")
	public AbstractModelAndView createSyjyForm(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page10", request);
		String customerId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerId)) {
			QzSyjy qzSyjy = intoPiecesService.getSyjyForm(customerId);
			mv.addObject("customerId", customerId);
			mv.addObject("result", qzSyjy);
		}
		return mv;
	}
	/**
	 * 审议纪要保存
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "syjy_save.json")
	public JRadReturnMap syjySave(@ModelAttribute QzSyjyForm qzSyjyForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = RequestHelper.getStringValue(request, ID);
				QzSyjy QzSyjy = qzSyjyForm.createModel(QzSyjy.class);
				QzSyjy.setCustomerId(customerId);
				QzSyjy.setCreatedTime(new Date());
				intoPiecesService.insertSyjyForm(QzSyjy,customerId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	/**
	 * 申请件审批通过 
	 * 从授信审批岗--中心负责岗
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "pass.json")
	public JRadReturnMap pass(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("id");
			CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(appId);
			request.setAttribute("serialNumber", process.getSerialNumber());
			request.setAttribute("applicationId", process.getApplicationId());
			request.setAttribute("applicationStatus", ApplicationStatusEnum.APPROVE);
			request.setAttribute("objection", "false");
			//查找审批金额
			CustomerApplicationInfo appInfo = intoPiecesService.findCustomerApplicationInfoByApplicationId(appId);
			IESBForECIFReturnMap ecif = eCIFService.findEcifByCustomerId(appInfo.getCustomerId());
			Circle circle = circleService.findCircleByClientNo(ecif.getClientNo());
			
			request.setAttribute("examineAmount", circle.getContractAmt());
			customerApplicationIntopieceWaitService.updateCustomerApplicationProcessBySerialNumberApplicationInfo1(request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 申请件退件
	 * 从授信岗--行政岗
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "returnAppln.json")
	public JRadReturnMap returnAppln(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("appId");
			intoPiecesService.returnAppln(appId, request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/*
	 * 调额
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "shouxin_edu.page")
	public AbstractModelAndView edu(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/shouxin_edu", request);
		
		//查找新开户
		/*List<IESBForECIFReturnMap> ls = eCIFService.findAllECIFByStatus2(com.cardpay.pccredit.QZBankInterface.constant.Constant.STATUS_CIRCLE);
		mv.addObject("ECIF_ls",ls);
		JSONArray json = new JSONArray();
		json = JSONArray.fromObject(ls);
		mv.addObject("ECIF_ls_json",json.toString());*/
		String applicationId = request.getParameter(ID);
		CustomerApplicationInfo appInfo = intoPiecesService.findCustomerApplicationInfoByApplicationId(applicationId);
		IESBForECIFReturnMap ecif = eCIFService.findEcifByCustomerId(appInfo.getCustomerId());
		mv.addObject("ecif",ecif);
				
		Circle circle = circleService.findCircleByClientNo(ecif.getClientNo());
		mv.addObject("circle",circle);
		return mv;
	}
	
	/**
	 * 调额
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "update_edu.json")
	public JRadReturnMap update_edu(@ModelAttribute IESBForCircleForm iesbForCircleForm, HttpServletRequest request) {
		String clientNo = request.getParameter(ID);
		
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				Circle circle = circleService.findCircleByClientNo(clientNo);
				circle.setContractAmt(iesbForCircleForm.getContractAmt());
				
				circleService.updateCustomerInforCircle(circle);
//				returnMap.put(RECORD_ID, id);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}

	/**
	 * 申请件拒件
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "rejectAppln.json")
	public JRadReturnMap rejectAppln(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("appId1");
			intoPiecesService.rejectAppln(appId, request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 进入初审拒件记录页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "chushen_reject.page", method = { RequestMethod.GET })
	public AbstractModelAndView reject(@ModelAttribute CustomerApplicationProcessFilter filter, HttpServletRequest request) throws SQLException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setLoginId(loginId);
		QueryResult<CustomerApplicationIntopieceWaitForm> result = customerApplicationIntopieceWaitService.IntopieceChushenRejectForm();
		JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm> pagedResult = new JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm>(filter, result);

		JRadModelAndView mv = new JRadModelAndView(
				"/intopieces/intopieces_wait/intopiecesApprove_chushen_reject", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	
	/**
	 * 同意初审拒件
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "inToReject.json")
	public JRadReturnMap inToreject(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("id");
			intoPiecesService.inToReject(appId, request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 不同意初审拒件
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "outToReject.json")
	public JRadReturnMap outToreject(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("id");
			intoPiecesService.outToReject(appId, request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 补充上会(添加标记，客户经理补充)
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "return_to_cu.json")
	public JRadReturnMap returnToCu(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();

		try {
			intoPiecesService.returnToCu(request);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("退回失败");
			e.printStackTrace();
		}
		return returnMap;
	}
}
