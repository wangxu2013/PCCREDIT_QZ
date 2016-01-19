package com.cardpay.pccredit.QZBankInterface.model;

import java.util.Date;

import com.wicresoft.jrad.base.database.id.IDType;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * 
 * @author shaoming
 *
 */
@ModelParam(table = "qz_iesb_for_ecif",generator=IDType.assigned)
public class ECIF extends BusinessModel{
	private static final long serialVersionUID = 1L;
	private String globalType;
	private String globalId;
	private String globalDesc;
	private String certAreaCode;
	private String certOrg;
	private Date issDate;
	private Date effectDate;
	private Date expiryDate;
	private String clientName;
	private String clientNameType;
	private String clientType;
	private String clientStatus;
	private Date birthDate;
	private String sex;
	private String custManagerId;
	private String recordTellerNo;
	private Date registeredDate;
	private String clientBelongOrg;
	private String registeredTellerNo;
	private String registOrgNo;
	private Date orgRegisteredDate;
	private String countryCitizen;
	private String nationalityCode;
	private String regPermResidence;
	
	private String regPermResidence_1;
	private String regPermResidence_2;
	private String regPermResidence_3;
	
	private String openAcctBranchId;
	private String openTellerNo;
	private Date openAcctDate;
	private String maritalStatus;
	private String educationLevel;
	private String city;
	
	private String city_1;
	private String city_2;
	private String city_3;
	
	private String areaCode;
	private String incidenceRelation;
	private String identityType;
	private String address;
	private String addressType;
	private String postalCode;
	private String contactModeType;
	private String contactMode;
	private String occupation;
	private String companyName;
	private String userId;

	//ecif返回的信息
	
    //增加贷款所需客户资料字段

    /*//农户标志，是否农户,todo:界面需增加
    private String agriFlag;
    //最高学历，todo:界面需增加
    private String education;
    //最高学位,todo:界面需增加
    private String degree;
    //签约日期,todo:界面需增加
    private String signDate;
    //持卡情况，todo:界面需增加
    private String holdCardMsg;
    //是否持有外国护照，todo：界面需增加
    private String passportFlag;
    //信用等级 todo:默认，界面不用增加
    private String creditLevel;
    //信用到期日期 todo:默认置空
    private String expiryDate;
    //是否关联客户 todo:页面需增加
    private String relClientFlag;
    //与我行关系，为关联客户后需填 todo:页面需增加
    private String ownBranchRelation;
    //我行职务，为关联客户后需填 todo:页面需增加
    private String post;
    //贷款卡标志
    private String loanCardFlag;
    //贷款卡卡号，todo:页面需增加
    private String loanCardNo;
*/
	
	private String clientNo;//客户号
	private String customerId;
	
	
	private String chl;//渠道
	
	
    
	public String getChl() {
		return chl;
	}
	public void setChl(String chl) {
		this.chl = chl;
	}
	public String getGlobalType() {
		return globalType;
	}
	public void setGlobalType(String globalType) {
		this.globalType = globalType;
	}
	public String getGlobalId() {
		return globalId;
	}
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getGlobalDesc() {
		return globalDesc;
	}
	public void setGlobalDesc(String globalDesc) {
		this.globalDesc = globalDesc;
	}
	public String getCertAreaCode() {
		return certAreaCode;
	}
	public void setCertAreaCode(String certAreaCode) {
		this.certAreaCode = certAreaCode;
	}
	public String getCertOrg() {
		return certOrg;
	}
	public void setCertOrg(String certOrg) {
		this.certOrg = certOrg;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientNameType() {
		return clientNameType;
	}
	public void setClientNameType(String clientNameType) {
		this.clientNameType = clientNameType;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCustManagerId() {
		return custManagerId;
	}
	public void setCustManagerId(String custManagerId) {
		this.custManagerId = custManagerId;
	}
	public String getRecordTellerNo() {
		return recordTellerNo;
	}
	public void setRecordTellerNo(String recordTellerNo) {
		this.recordTellerNo = recordTellerNo;
	}
	public String getClientBelongOrg() {
		return clientBelongOrg;
	}
	public void setClientBelongOrg(String clientBelongOrg) {
		this.clientBelongOrg = clientBelongOrg;
	}
	public String getRegisteredTellerNo() {
		return registeredTellerNo;
	}
	public void setRegisteredTellerNo(String registeredTellerNo) {
		this.registeredTellerNo = registeredTellerNo;
	}
	public String getRegistOrgNo() {
		return registOrgNo;
	}
	public void setRegistOrgNo(String registOrgNo) {
		this.registOrgNo = registOrgNo;
	}
	public String getCountryCitizen() {
		return countryCitizen;
	}
	public void setCountryCitizen(String countryCitizen) {
		this.countryCitizen = countryCitizen;
	}
	public String getNationalityCode() {
		return nationalityCode;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public String getRegPermResidence() {
		return regPermResidence;
	}
	public void setRegPermResidence(String regPermResidence) {
		this.regPermResidence = regPermResidence;
	}
	public String getOpenAcctBranchId() {
		return openAcctBranchId;
	}
	public void setOpenAcctBranchId(String openAcctBranchId) {
		this.openAcctBranchId = openAcctBranchId;
	}
	public String getOpenTellerNo() {
		return openTellerNo;
	}
	public void setOpenTellerNo(String openTellerNo) {
		this.openTellerNo = openTellerNo;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getIncidenceRelation() {
		return incidenceRelation;
	}
	public void setIncidenceRelation(String incidenceRelation) {
		this.incidenceRelation = incidenceRelation;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getContactModeType() {
		return contactModeType;
	}
	public void setContactModeType(String contactModeType) {
		this.contactModeType = contactModeType;
	}
	public String getContactMode() {
		return contactMode;
	}
	public void setContactMode(String contactMode) {
		this.contactMode = contactMode;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public Date getIssDate() {
		return issDate;
	}

	public void setIssDate(Date issDate) {
		this.issDate = issDate;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Date getOrgRegisteredDate() {
		return orgRegisteredDate;
	}

	public void setOrgRegisteredDate(Date orgRegisteredDate) {
		this.orgRegisteredDate = orgRegisteredDate;
	}

	public Date getOpenAcctDate() {
		return openAcctDate;
	}

	public void setOpenAcctDate(Date openAcctDate) {
		this.openAcctDate = openAcctDate;
	}
	public String getRegPermResidence_1() {
		return regPermResidence_1;
	}
	public void setRegPermResidence_1(String regPermResidence_1) {
		this.regPermResidence_1 = regPermResidence_1;
	}
	public String getRegPermResidence_2() {
		return regPermResidence_2;
	}
	public void setRegPermResidence_2(String regPermResidence_2) {
		this.regPermResidence_2 = regPermResidence_2;
	}
	public String getRegPermResidence_3() {
		return regPermResidence_3;
	}
	public void setRegPermResidence_3(String regPermResidence_3) {
		this.regPermResidence_3 = regPermResidence_3;
	}
	public String getCity_1() {
		return city_1;
	}
	public void setCity_1(String city_1) {
		this.city_1 = city_1;
	}
	public String getCity_2() {
		return city_2;
	}
	public void setCity_2(String city_2) {
		this.city_2 = city_2;
	}
	public String getCity_3() {
		return city_3;
	}
	public void setCity_3(String city_3) {
		this.city_3 = city_3;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
