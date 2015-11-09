/*
 * Copyright 2013 Wicresoft, Inc. All rights reserved.
 */

package com.cardpay.pccredit.intopieces.model;

import java.util.Date;

import com.wicresoft.jrad.base.database.model.ModelParam;
import com.wicresoft.jrad.base.database.model.BusinessModel;

@ModelParam(table = "qz_appln_nbscyjb")
public class QzApplnNbscyjb extends BusinessModel {

	private static final long serialVersionUID = 1L;
	private String customerId;
	private String applicationId;
	private Date checkDate;
	private String clientName;
	private String shopName;
	private String applyAmount;
	private String applyDeadline;
	private String sugContent;
	private String sugAmount;
	private String sugDeadline;
	private String sugRates;
	private String sugGuntType;
	private String needCheckContent;
	private String checkCondition;
	private String checkSug;
	private Date checkSignDate;
	private String isComplete;
	
	private String hcryqz;
	private String hcryqz1;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}
	public String getApplyDeadline() {
		return applyDeadline;
	}
	public void setApplyDeadline(String applyDeadline) {
		this.applyDeadline = applyDeadline;
	}
	public String getSugContent() {
		return sugContent;
	}
	public void setSugContent(String sugContent) {
		this.sugContent = sugContent;
	}
	public String getSugAmount() {
		return sugAmount;
	}
	public void setSugAmount(String sugAmount) {
		this.sugAmount = sugAmount;
	}
	public String getSugDeadline() {
		return sugDeadline;
	}
	public void setSugDeadline(String sugDeadline) {
		this.sugDeadline = sugDeadline;
	}
	public String getSugRates() {
		return sugRates;
	}
	public void setSugRates(String sugRates) {
		this.sugRates = sugRates;
	}
	public String getSugGuntType() {
		return sugGuntType;
	}
	public void setSugGuntType(String sugGuntType) {
		this.sugGuntType = sugGuntType;
	}
	public String getNeedCheckContent() {
		return needCheckContent;
	}
	public void setNeedCheckContent(String needCheckContent) {
		this.needCheckContent = needCheckContent;
	}
	public String getCheckCondition() {
		return checkCondition;
	}
	public void setCheckCondition(String checkCondition) {
		this.checkCondition = checkCondition;
	}
	public String getCheckSug() {
		return checkSug;
	}
	public void setCheckSug(String checkSug) {
		this.checkSug = checkSug;
	}
	public Date getCheckSignDate() {
		return checkSignDate;
	}
	public void setCheckSignDate(Date checkSignDate) {
		this.checkSignDate = checkSignDate;
	}
	public String getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	public String getHcryqz() {
		return hcryqz;
	}
	public void setHcryqz(String hcryqz) {
		this.hcryqz = hcryqz;
	}
	public String getHcryqz1() {
		return hcryqz1;
	}
	public void setHcryqz1(String hcryqz1) {
		this.hcryqz1 = hcryqz1;
	}

}
