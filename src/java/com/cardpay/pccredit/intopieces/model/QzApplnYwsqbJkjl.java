/*
 * Copyright 2013 Wicresoft, Inc. All rights reserved.
 */

package com.cardpay.pccredit.intopieces.model;

import java.util.Date;

import com.wicresoft.jrad.base.database.model.ModelParam;
import com.wicresoft.jrad.base.database.model.BusinessModel;

@ModelParam(table = "qz_appln_ywsqb_Jkjl")
public class QzApplnYwsqbJkjl extends BusinessModel {

	private static final long serialVersionUID = 1L;
	private String ywsqbId;
	private int lsh;
	private String bankOrOtherType;
	private String purpose;
	private String totalAmount;
	private Date loanDate;
	private String deadline;
	private String rates;
	private String repayType;
	private String remainSum;
	private String guaranteeMode;
	
	public String getYwsqbId() {
		return ywsqbId;
	}
	public void setYwsqbId(String ywsqbId) {
		this.ywsqbId = ywsqbId;
	}
	public int getLsh() {
		return lsh;
	}
	public void setLsh(int lsh) {
		this.lsh = lsh;
	}
	public String getBankOrOtherType() {
		return bankOrOtherType;
	}
	public void setBankOrOtherType(String bankOrOtherType) {
		this.bankOrOtherType = bankOrOtherType;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getRates() {
		return rates;
	}
	public void setRates(String rates) {
		this.rates = rates;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public String getRemainSum() {
		return remainSum;
	}
	public void setRemainSum(String remainSum) {
		this.remainSum = remainSum;
	}
	public String getGuaranteeMode() {
		return guaranteeMode;
	}
	public void setGuaranteeMode(String guaranteeMode) {
		this.guaranteeMode = guaranteeMode;
	}
}
