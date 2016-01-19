/**
 * 微贷申请信息查询
 */
package com.cardpay.pccredit.ipad.model;

public class CustomerApplyInfoIpad {
	private String inputWareRegNo;
	private String clientName;
	private String globalId;
	private String productCode;
	private String productName;
	private double applyLimitAmt;
	private String status;
	private String process;
	private String backReason;
	
	private String chl;
	
	public String getChl() {
		return chl;
	}
	public void setChl(String chl) {
		this.chl = chl;
	}
	private String customerId;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getInputWareRegNo() {
		return inputWareRegNo;
	}
	public void setInputWareRegNo(String inputWareRegNo) {
		this.inputWareRegNo = inputWareRegNo;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getGlobalId() {
		return globalId;
	}
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public double getApplyLimitAmt() {
		return applyLimitAmt;
	}
	public void setApplyLimitAmt(double applyLimitAmt) {
		this.applyLimitAmt = applyLimitAmt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getBackReason() {
		return backReason;
	}
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	
	
}
