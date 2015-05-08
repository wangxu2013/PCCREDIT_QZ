package com.cardpay.pccredit.manager.filter;

import java.util.Date;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

/**
 * ManagerRiskWarning类的描述
 * 
 * @author 王海东
 * @created on 2014-11-12
 * 
 * @version $Id:$
 */
public class ManagerRiskWarningFilter extends BaseQueryFilter {

	private String customerManagerId;
	private String originator;
	private String originatorName;
	private String managerName;
	private Date initiateTime;
	private String competentAdvice;
	private String warningItems;
	private String returnReceipt;
	private String feedback;
	private Date feedbackTime;

	public String getOriginatorName() {
		return originatorName;
	}

	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Date getInitiateTime() {
		return initiateTime;
	}

	public void setInitiateTime(Date initiateTime) {
		this.initiateTime = initiateTime;
	}

	public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getCustomerManagerId() {
		return customerManagerId;
	}

	public void setCustomerManagerId(String customerManagerId) {
		this.customerManagerId = customerManagerId;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getCompetentAdvice() {
		return competentAdvice;
	}

	public void setCompetentAdvice(String competentAdvice) {
		this.competentAdvice = competentAdvice;
	}

	public String getWarningItems() {
		return warningItems;
	}

	public void setWarningItems(String warningItems) {
		this.warningItems = warningItems;
	}

	public String getReturnReceipt() {
		return returnReceipt;
	}

	public void setReturnReceipt(String returnReceipt) {
		this.returnReceipt = returnReceipt;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
