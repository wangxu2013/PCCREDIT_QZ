package com.cardpay.pccredit.intopieces.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.constant.WfProcessInfoType;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.filter.QuotaFreezeOrThawFilter;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.QuotaFreezeInfo;
import com.cardpay.pccredit.intopieces.model.QuotaProcess;
import com.cardpay.pccredit.intopieces.service.QuotaFreezeOrThawService;
import com.cardpay.pccredit.system.constants.NodeAuditTypeEnum;
import com.cardpay.pccredit.system.constants.YesNoEnum;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.pccredit.system.service.NodeAuditService;
import com.cardpay.workflow.constant.ApproveOperationTypeEnum;
import com.cardpay.workflow.models.WfProcessInfo;
import com.cardpay.workflow.models.WfStatusInfo;
import com.cardpay.workflow.models.WfStatusResult;
import com.cardpay.workflow.service.ProcessService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

//额度冻结/解冻（终审）
@Controller
@RequestMapping("/intopieces/quotafreezeorthaw4*")
@JRadModule("intopieces.quotafreezeorthaw4")
public class QuataFreezeOrThaw4Control extends BaseController{
	
	@Autowired
	public QuotaFreezeOrThawService quotaFreezeOrThawService;
	
	@Autowired
	public CircleService circleService;
	
	@Autowired
	private NodeAuditService nodeAuditService;
	
	@Autowired
	private ProcessService processService;
	
	private static final Logger logger = Logger.getLogger(QuataFreezeOrThaw4Control.class);
	
	/**
	 *额度冻结/解冻（终审）
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	public AbstractModelAndView browse(@ModelAttribute QuotaFreezeOrThawFilter filter, HttpServletRequest request) throws SQLException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setUserId(loginId);
		QueryResult<QuotaFreezeInfo> result = quotaFreezeOrThawService.getQzIesbForCircleWFByFilter(filter);
		JRadPagedQueryResult<QuotaFreezeInfo> pagedResult = new JRadPagedQueryResult<QuotaFreezeInfo>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("/intopieces/quotafreezeorthaw_4",
                                                    request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/**
	 * 额度冻结/解冻（终审）
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "quot_operate.json")
	public JRadReturnMap quot_operate(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String retMsg = "";
				String clientNo = request.getParameter("clientNo");
				String cardNo= request.getParameter("cardNo");
				String loanStatus = request.getParameter("loanStatus");
				String contNo = request.getParameter("contNo");
				String circleId = request.getParameter("circleId");
				String processId = request.getParameter("processId");
				String applicationStatus = request.getParameter("applicationStatus");//跟ApproveOperationTypeEnum一致
				logger.info("[clientNo="+clientNo+"],[cardNo="+cardNo+"],[contNo=" +contNo +"]");
				
				//更新流程
				quotaFreezeOrThawService.quot_operate(request);
				returnMap.setSuccess(true);
				returnMap.put("retMsg", "保存成功！");
				
			}catch(Exception e){
				e.printStackTrace();
				returnMap.setSuccess(false);
				returnMap.put("retMsg", "操作失败！"+e.getMessage());
			}
		}
		return returnMap;
	}
}
