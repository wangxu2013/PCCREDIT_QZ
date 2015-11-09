package com.cardpay.pccredit.afterloan.filter;


import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

public class AfterLoanCheckFilter extends BaseQueryFilter{
	
	private String chineseName;//客户姓名
	private String cardId;//证件号码
	private String productName;//产品编号
	private String userId;//客户经理id
	private String clientNo;//客户号
	private String checkType;//检查类型
	private String userNo;//客户经理号
	private String remindate;//提醒时间
	private String enddate;//截止时间
	
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getRemindate() {
		return remindate;
	}
	public void setRemindate(String remindate) {
		this.remindate = remindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
}
