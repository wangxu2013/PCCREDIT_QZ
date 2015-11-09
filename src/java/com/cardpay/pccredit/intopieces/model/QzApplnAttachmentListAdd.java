/*
 * Copyright 2013 Wicresoft, Inc. All rights reserved.
 */

package com.cardpay.pccredit.intopieces.model;

import com.wicresoft.jrad.base.database.model.ModelParam;
import com.wicresoft.jrad.base.database.model.BusinessModel;

@ModelParam(table = "qz_appln_attachment_list_add")
public class QzApplnAttachmentListAdd extends BusinessModel {

	private static final long serialVersionUID = 1L;
	
	private String attId;
	private String parentValue;
	private String chkValue;
	private String uploadValue;
	
	public String getAttId() {
		return attId;
	}
	public void setAttId(String attId) {
		this.attId = attId;
	}
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	public String getChkValue() {
		return chkValue;
	}
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
	public String getUploadValue() {
		return uploadValue;
	}
	public void setUploadValue(String uploadValue) {
		this.uploadValue = uploadValue;
	}
}
