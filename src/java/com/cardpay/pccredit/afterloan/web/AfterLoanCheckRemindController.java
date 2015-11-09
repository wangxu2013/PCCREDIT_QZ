package com.cardpay.pccredit.afterloan.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.afterloan.filter.AfterLoanCheckFilter;
import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.afterloan.service.AfterloanCheckService;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.system.model.Dict;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/** 
 * @author chinh
 * @version 创建时间：2015年7月31日 下午11:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/afterloan/afterloanremind/*")
@JRadModule("afterloan.afterloanremind")
public class AfterLoanCheckRemindController extends BaseController{
	
	@Autowired
	private AfterloanCheckService afterloanCheckService;
	
	@Autowired
	private CustomerInforService customerInforService;

	
	/**
	 * 贷后检查任务查看(提醒)
	 * 
	 * @param filter 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "remind.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView remind(@ModelAttribute AfterLoanCheckFilter filter,HttpServletRequest request){
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		//获取贷后点击通知时限和截止时限
		List<Dict> dict = customerInforService.findDict("afterloan");
		String reminddate="";
		String enddate="";
		for(int i=0;i<dict.size();i++){
			Dict dictd = dict.get(i);
			if("reminddate".equals(dictd.getTypeCode())){
				reminddate=dictd.getTypeName();
			}else if("enddate".equals(dictd.getTypeCode())){
				enddate=dictd.getTypeName();
			}
		}
		filter.setRemindate(reminddate);
		filter.setEnddate(enddate);
		QueryResult<AfterLoaninfo> result = afterloanCheckService.findAfterLoanCheckTaskRemindByFilter(filter);
		JRadPagedQueryResult<AfterLoaninfo> pagedResult = new JRadPagedQueryResult<AfterLoaninfo>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView(
				"/afterloan/afterloan_remind", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
}
