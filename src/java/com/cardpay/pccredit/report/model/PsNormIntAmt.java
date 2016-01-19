package com.cardpay.pccredit.report.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

public class PsNormIntAmt extends BusinessModel{
	
	private static final long serialVersionUID = -7513388150092036959L;
	private String rowIndex;
	private String psTime;//计息时间
	private String ps_norm_int_amt;//正常利息
	private String ps_od_int_amt;//逾期利息
	private String setl_norm_int;//已还利息
	private String setl_od_int_amt;//已还逾期利息
	private String ps_amt;//应还利息
	private String cus_id;//客户号
	private String client_name;//客户名称

	public String getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getPsTime() {
		return psTime;
	}
	public void setPsTime(String psTime) {
		this.psTime = psTime;
	}
	public String getPs_norm_int_amt() {
		return ps_norm_int_amt;
	}
	public void setPs_norm_int_amt(String ps_norm_int_amt) {
		this.ps_norm_int_amt = ps_norm_int_amt;
	}
	public String getPs_od_int_amt() {
		return ps_od_int_amt;
	}
	public void setPs_od_int_amt(String ps_od_int_amt) {
		this.ps_od_int_amt = ps_od_int_amt;
	}
	public String getSetl_norm_int() {
		return setl_norm_int;
	}
	public void setSetl_norm_int(String setl_norm_int) {
		this.setl_norm_int = setl_norm_int;
	}
	public String getSetl_od_int_amt() {
		return setl_od_int_amt;
	}
	public void setSetl_od_int_amt(String setl_od_int_amt) {
		this.setl_od_int_amt = setl_od_int_amt;
	}
	public String getCus_id() {
		return cus_id;
	}
	public void setCus_id(String cus_id) {
		this.cus_id = cus_id;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getPs_amt() {
		return ps_amt;
	}
	public void setPs_amt(String ps_amt) {
		this.ps_amt = ps_amt;
	}
}
