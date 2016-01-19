package com.cardpay.pccredit.afterloan.model;

import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * 台账信息表
 * @author sc
 *
 */
@ModelParam(table = "O_CLPM_CTR_LOAN_CONT")
public class O_CLPM_CTR_LOAN_CONT {
	private static final long serialVersionUID = 1L;
	private String contAmt;
	private String contBalance;
	private String remainAmt;
	public String getContAmt() {
		return contAmt;
	}
	public void setContAmt(String contAmt) {
		this.contAmt = contAmt;
	}
	public String getContBalance() {
		return contBalance;
	}
	public void setContBalance(String contBalance) {
		this.contBalance = contBalance;
	}
	public String getRemainAmt() {
		return remainAmt;
	}
	public void setRemainAmt(String remainAmt) {
		this.remainAmt = remainAmt;
	}
	
}
