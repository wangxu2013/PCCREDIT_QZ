package com.cardpay.pccredit.ipad.model;

import com.wicresoft.jrad.base.database.id.IDType;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * 进件申请表
 */
@ModelParam(table = "CUSTOMER_APPLICATION_INFO",generator=IDType.assigned)
public class CustomerApplicationInfo extends BusinessModel{
	private static final long serialVersionUID = 1L;
    
	private String  customerId;            
	private String  productId;            
	private String  applyQuota;             
	private String  finalApproval;             
	private String  actualQuote;           
	private String  temporaryQuota;         
	private String  cashAdvanceProportion;    
	private String  cardStatus;       
	private String  accountStatus;     
	private String  billingDate;
	private String  repaymentAgreement;  
	private String  automaticRepaymentAgreement;
	private String  customerRiskRating;
	private String  aging;
	private String  debitWay;
	private String  repaymentCardNumber;
	private String  status;            
	private String  code;
	private String  uploadStatus;
	private String  completionTime;
	private String  cardNumber;
	private String  isAutoPay;
	private String  serialNumber;
	private String  isContinue;
	
	private String chl;
	
	public String getChl() {
		return chl;
	}
	public void setChl(String chl) {
		this.chl = chl;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getApplyQuota() {
		return applyQuota;
	}
	public void setApplyQuota(String applyQuota) {
		this.applyQuota = applyQuota;
	}
	public String getFinalApproval() {
		return finalApproval;
	}
	public void setFinalApproval(String finalApproval) {
		this.finalApproval = finalApproval;
	}
	public String getActualQuote() {
		return actualQuote;
	}
	public void setActualQuote(String actualQuote) {
		this.actualQuote = actualQuote;
	}
	public String getTemporaryQuota() {
		return temporaryQuota;
	}
	public void setTemporaryQuota(String temporaryQuota) {
		this.temporaryQuota = temporaryQuota;
	}
	public String getCashAdvanceProportion() {
		return cashAdvanceProportion;
	}
	public void setCashAdvanceProportion(String cashAdvanceProportion) {
		this.cashAdvanceProportion = cashAdvanceProportion;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}
	public String getRepaymentAgreement() {
		return repaymentAgreement;
	}
	public void setRepaymentAgreement(String repaymentAgreement) {
		this.repaymentAgreement = repaymentAgreement;
	}
	public String getAutomaticRepaymentAgreement() {
		return automaticRepaymentAgreement;
	}
	public void setAutomaticRepaymentAgreement(String automaticRepaymentAgreement) {
		this.automaticRepaymentAgreement = automaticRepaymentAgreement;
	}
	public String getCustomerRiskRating() {
		return customerRiskRating;
	}
	public void setCustomerRiskRating(String customerRiskRating) {
		this.customerRiskRating = customerRiskRating;
	}
	public String getAging() {
		return aging;
	}
	public void setAging(String aging) {
		this.aging = aging;
	}
	public String getDebitWay() {
		return debitWay;
	}
	public void setDebitWay(String debitWay) {
		this.debitWay = debitWay;
	}
	public String getRepaymentCardNumber() {
		return repaymentCardNumber;
	}
	public void setRepaymentCardNumber(String repaymentCardNumber) {
		this.repaymentCardNumber = repaymentCardNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getIsContinue() {
		return isContinue;
	}
	public void setIsContinue(String isContinue) {
		this.isContinue = isContinue;
	}
	
	
}
