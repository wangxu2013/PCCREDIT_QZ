package com.cardpay.pccredit.intopieces.filter;

import java.util.Date;

import com.wicresoft.jrad.base.database.dao.business.BusinessFilter;

/**
 * CustomerApplicationProcess类的描述
 * 
 * @author 王海东
 * @created on 2014-11-28
 * 
 * @version $Id:$
 */
public class CustomerApplicationProcessFilter extends BusinessFilter {

	private String loginId;
	private String applicationId;
	private String auditUser;
	private String serialNumber;
	private String auditOpinion;
	private String refusalReason;
	private String fallbackReason;
	private String processrecordId;
	private String nextNodeId;
	private String delayAuditUser;
	private Date auditTime;
	private String cardId;
	private String nodeName;//进件状态
	private String chineseName;
	
	private String filterTeamLeader;//团队长查看各小组成员的进件 1要过滤 0 不过滤
	
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getFallbackReason() {
		return fallbackReason;
	}

	public void setFallbackReason(String fallbackReason) {
		this.fallbackReason = fallbackReason;
	}

	public String getProcessrecordId() {
		return processrecordId;
	}

	public void setProcessrecordId(String processrecordId) {
		this.processrecordId = processrecordId;
	}

	public String getNextNodeId() {
		return nextNodeId;
	}

	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

	public String getDelayAuditUser() {
		return delayAuditUser;
	}

	public void setDelayAuditUser(String delayAuditUser) {
		this.delayAuditUser = delayAuditUser;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getFilterTeamLeader() {
		return filterTeamLeader;
	}

	public void setFilterTeamLeader(String filterTeamLeader) {
		this.filterTeamLeader = filterTeamLeader;
	}

}
