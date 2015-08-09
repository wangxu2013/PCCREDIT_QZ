package com.cardpay.pccredit.report.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;

/**
 * @author chinh
 *
 * 2015-8-2上午11:05:27
 */
public class AccLoanCollectInfo extends BusinessModel{
	
	private static final long serialVersionUID = -7513388150092036959L;
	
	private String rowIndex;
	private String increaseLoancount;//新增贷款客户数
	private String loanCustomerSum;//贷款客户总数
	private String increaseCusSum;//新增客户数
	private String centreLoansSun;//中心授信总数
	private String loanBalanceSum;//当前贷款总余额
	private Double loanRealityRate;//贷款实际用信率
	private String increaseLoanCredit;//新增授信金额
	private String increaseBalanceTheMonth;//新增用信余额(日均)
	private String overdueBalanceTheMon;//逾期金额
	private String overdueBalance; //累计逾期总额
	private String increseOverdueCusTheMon;//新增逾期客户数
	private String resvIntAccum;//累计实收利息
	private String overdueCusM0;//累计逾期客户数M0以上
	private String overdueCusM1;//累计逾期客户数M1以上
	private String overdueCusM2;//累计逾期客户数M2以上
	private String overdueCusM3;//累计逾期客户数M3以上
	private String overdueCusM4;//累计逾期客户数M4以上
	

	public String getIncreaseLoancount() {
		return increaseLoancount;
	}
	public void setIncreaseLoancount(String increaseLoancount) {
		this.increaseLoancount = increaseLoancount;
	}
	public String getLoanCustomerSum() {
		return loanCustomerSum;
	}
	public void setLoanCustomerSum(String loanCustomerSum) {
		this.loanCustomerSum = loanCustomerSum;
	}
	public String getIncreaseCusSum() {
		return increaseCusSum;
	}
	public void setIncreaseCusSum(String increaseCusSum) {
		this.increaseCusSum = increaseCusSum;
	}
	public String getCentreLoansSun() {
		return centreLoansSun;
	}
	public void setCentreLoansSun(String centreLoansSun) {
		this.centreLoansSun = centreLoansSun;
	}
	public String getLoanBalanceSum() {
		return loanBalanceSum;
	}
	public void setLoanBalanceSum(String loanBalanceSum) {
		this.loanBalanceSum = loanBalanceSum;
	}
	public String getIncreaseLoanCredit() {
		return increaseLoanCredit;
	}
	public void setIncreaseLoanCredit(String increaseLoanCredit) {
		this.increaseLoanCredit = increaseLoanCredit;
	}
	public String getIncreaseBalanceTheMonth() {
		return increaseBalanceTheMonth;
	}
	public void setIncreaseBalanceTheMonth(String increaseBalanceTheMonth) {
		this.increaseBalanceTheMonth = increaseBalanceTheMonth;
	}
	public String getOverdueBalanceTheMon() {
		return overdueBalanceTheMon;
	}
	public void setOverdueBalanceTheMon(String overdueBalanceTheMon) {
		this.overdueBalanceTheMon = overdueBalanceTheMon;
	}
	public String getOverdueBalance() {
		return overdueBalance;
	}
	public void setOverdueBalance(String overdueBalance) {
		this.overdueBalance = overdueBalance;
	}
	public String getIncreseOverdueCusTheMon() {
		return increseOverdueCusTheMon;
	}
	public void setIncreseOverdueCusTheMon(String increseOverdueCusTheMon) {
		this.increseOverdueCusTheMon = increseOverdueCusTheMon;
	}
	public String getOverdueCusM0() {
		return overdueCusM0;
	}
	public void setOverdueCusM0(String overdueCusM0) {
		this.overdueCusM0 = overdueCusM0;
	}
	public String getOverdueCusM1() {
		return overdueCusM1;
	}
	public void setOverdueCusM1(String overdueCusM1) {
		this.overdueCusM1 = overdueCusM1;
	}
	public String getOverdueCusM2() {
		return overdueCusM2;
	}
	public void setOverdueCusM2(String overdueCusM2) {
		this.overdueCusM2 = overdueCusM2;
	}
	public String getOverdueCusM3() {
		return overdueCusM3;
	}
	public void setOverdueCusM3(String overdueCusM3) {
		this.overdueCusM3 = overdueCusM3;
	}
	public String getOverdueCusM4() {
		return overdueCusM4;
	}
	public void setOverdueCusM4(String overdueCusM4) {
		this.overdueCusM4 = overdueCusM4;
	}
	public String getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getResvIntAccum() {
		return resvIntAccum;
	}
	public void setResvIntAccum(String resvIntAccum) {
		this.resvIntAccum = resvIntAccum;
	}
	public Double getLoanRealityRate() {
		return loanRealityRate;
	}
	public void setLoanRealityRate(Double loanRealityRate) {
		this.loanRealityRate = loanRealityRate;
	}

}
