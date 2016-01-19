/**
 * 到期微贷信息
 */
package com.cardpay.pccredit.ipad.model;
public class CustomerDqwdtxInfoIpad {
	private String clientName;
	private String clientCode;
	private String duebillNo;
	private double presentBalance;
	private String expiryDate;
	private double receInterest;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getDuebillNo() {
		return duebillNo;
	}
	public void setDuebillNo(String duebillNo) {
		this.duebillNo = duebillNo;
	}
	
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public double getPresentBalance() {
		return presentBalance;
	}
	public void setPresentBalance(double presentBalance) {
		this.presentBalance = presentBalance;
	}
	public double getReceInterest() {
		return receInterest;
	}
	public void setReceInterest(double receInterest) {
		this.receInterest = receInterest;
	}
	
	


}
