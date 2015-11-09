/*
 * Copyright 2013 Wicresoft, Inc. All rights reserved.
 */

package com.cardpay.pccredit.intopieces.model;

import java.util.Date;

import com.wicresoft.jrad.base.database.model.ModelParam;
import com.wicresoft.jrad.base.database.model.BusinessModel;

@ModelParam(table = "qz_appln_htqdtz")
public class QzApplnHtqdtz extends BusinessModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String customerId;
	private String applicationId;
	private Date slrq;
	private String jkrxm;
	private String pzje;
	private Date yyqdrq;
	private Date sjqdrq;
	private String zbkhjl;
	private String jbr;
	private String bz;
	private String lrz;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getJkrxm() {
		return jkrxm;
	}
	public void setJkrxm(String jkrxm) {
		this.jkrxm = jkrxm;
	}
	public String getPzje() {
		return pzje;
	}
	public void setPzje(String pzje) {
		this.pzje = pzje;
	}
	public String getZbkhjl() {
		return zbkhjl;
	}
	public void setZbkhjl(String zbkhjl) {
		this.zbkhjl = zbkhjl;
	}
	public String getJbr() {
		return jbr;
	}
	public void setJbr(String jbr) {
		this.jbr = jbr;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public Date getSlrq() {
		return slrq;
	}
	public void setSlrq(Date slrq) {
		this.slrq = slrq;
	}
	public Date getYyqdrq() {
		return yyqdrq;
	}
	public void setYyqdrq(Date yyqdrq) {
		this.yyqdrq = yyqdrq;
	}
	public Date getSjqdrq() {
		return sjqdrq;
	}
	public void setSjqdrq(Date sjqdrq) {
		this.sjqdrq = sjqdrq;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getLrz() {
		return lrz;
	}
	public void setLrz(String lrz) {
		this.lrz = lrz;
	}
	
}
