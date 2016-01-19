/**
 * 
 */
package com.cardpay.pccredit.ipad.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.struts.taglib.TagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.afterloan.model.O_CLPM_ACC_LOAN;
import com.cardpay.pccredit.afterloan.model.O_CLPM_CTR_LOAN_CONT;
import com.cardpay.pccredit.afterloan.model.PspCheckTask;
import com.cardpay.pccredit.afterloan.model.PspCheckTaskVo;
import com.cardpay.pccredit.customer.dao.CustomerInforDao;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.dao.comdao.IntoPiecesComdao;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.cardpay.pccredit.intopieces.model.QzApplnJyxx;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.ipad.constant.IpadConstant;
import com.cardpay.pccredit.ipad.dao.UserIpadDao;
import com.cardpay.pccredit.ipad.model.CustomerApplicationInfo;
import com.cardpay.pccredit.ipad.model.CustomerApplyInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDhJcInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDhJcTxInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDqwdtxInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerInfoIpad;
import com.cardpay.pccredit.ipad.model.UserIpad;
import com.cardpay.pccredit.ipad.util.IpadException;
import com.cardpay.pccredit.product.service.ProductService;
import com.cardpay.pccredit.system.model.Dict;
import com.cardpay.pccredit.system.web.NodeAuditForm;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.jcraft.jsch.Logger;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.modules.dao.modulesComdao;
import com.wicresoft.jrad.modules.dictionary.DictionaryManager;
import com.wicresoft.jrad.modules.dictionary.model.Dictionary;
import com.wicresoft.jrad.modules.dictionary.model.DictionaryItem;
import com.wicresoft.jrad.modules.privilege.filter.UserFilter;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.jrad.modules.privilege.service.UserService;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.web.RequestHelper;

@Service
public class IpadCommonService {
	
	@Autowired
	private CustomerInforDao customerInforDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserIpadDao userIpadDao;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CustomerInforService customerInforservice;
	@Autowired
	private UserService userService;
	@Autowired
	private ECIFService ecifService;
	@Autowired
	private CircleService circleService;
	@Autowired
	private IntoPiecesComdao intoPiecesComdao;
	SimpleDateFormat formatter10 = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	//ipad用户登录
	public CompositeData ipadLogin(CompositeData BODY ) throws IpadException{
		String USER_ID = BODY.getField("USER_ID").strValue();
		User user = this.findByLogin(USER_ID);
		if(user == null){
			throw new IpadException("工号不存在");
		}
		else{
			UserIpad userIpad = this.findInfoByLogin(USER_ID);
			//返回结果
			CompositeData respBody = new CompositeData();
			//CUST_MNGR_FLAG
			Field CUST_MNGR_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
			CUST_MNGR_FLAG.setValue(userIpad.getUserType());
			respBody.addField("CUST_MNGR_FLAG", CUST_MNGR_FLAG);
			//USER_NO
			Field USER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
			USER_NO.setValue(userIpad.getUserId());
			respBody.addField("USER_NO", USER_NO);
			//USER_NAME
			Field USER_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			USER_NAME.setValue(userIpad.getDisplayName());
			respBody.addField("USER_NAME", USER_NAME);
			//ORG_NO
			Field ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
			ORG_NO.setValue(userIpad.getOrgId());
			respBody.addField("ORG_NO", ORG_NO);
			//ORG_NAME
			Field ORG_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			ORG_NAME.setValue(userIpad.getOrgName());
			respBody.addField("ORG_NAME", ORG_NAME);
			//ROLE_CODE
			Field ROLE_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
			ROLE_CODE.setValue(userIpad.getRoleId());
			respBody.addField("ROLE_CODE", ROLE_CODE);
			//ROLE_NAME
			Field ROLE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			ROLE_NAME.setValue(userIpad.getRoleName());
			respBody.addField("ROLE_NAME", ROLE_NAME);
			return respBody;
		}
	}
	
	public User findByLogin(String login){
		return userIpadDao.findByLogin(login);
	}
	
	public UserIpad findInfoByLogin(String findlogin){
		return userIpadDao.findInfoByLogin(findlogin);
	}
		
	
	//ipad新增客户
	public CompositeData ipadAddCustomerInfo(CompositeData BODY) throws ParseException, IpadException {
		// TODO Auto-generated method stub
		Date today = new Date();
		ECIF ecif = new ECIF();
		
		ecif.setGlobalType(BODY.getField("GLOBAL_TYPE").strValue());
		ecif.setGlobalId(BODY.getField("GLOBAL_ID").strValue());
		ecif.setGlobalDesc(BODY.getField("GLOBAL_DESC").strValue());
		ecif.setCertAreaCode(BODY.getField("CERT_AREA_CODE").strValue());
		ecif.setCertOrg(BODY.getField("CERT_ORG").strValue());
		ecif.setIssDate(formatter10.parse(BODY.getField("ISS_DATE").strValue()));
		ecif.setEffectDate(StringUtils.isEmpty(BODY.getField("EFFECT_DATE").strValue())?today:formatter10.parse(BODY.getField("EFFECT_DATE").strValue()));
		ecif.setExpiryDate(StringUtils.isEmpty(BODY.getField("EXPIRY_DATE").strValue())?today:formatter10.parse(BODY.getField("EXPIRY_DATE").strValue()));
		ecif.setClientName(BODY.getField("CLIENT_NAME").strValue());
		ecif.setClientNameType(BODY.getField("CLIENT_NAME_TYPE").strValue());
		ecif.setClientType(BODY.getField("CLIENT_TYPE").strValue());
		ecif.setClientStatus(BODY.getField("CLIENT_STATUS").strValue());
		ecif.setBirthDate(StringUtils.isEmpty(BODY.getField("BIRTH_DATE").strValue())?today:formatter10.parse(BODY.getField("BIRTH_DATE").strValue()));
		ecif.setSex(BODY.getField("SEX").strValue());
		ecif.setCustManagerId(BODY.getField("ACCT_EXEC").strValue());
		ecif.setRecordTellerNo(BODY.getField("RECORD_TELLER_NO").strValue());
		ecif.setRegisteredDate(StringUtils.isEmpty(BODY.getField("REGISTERED_DATE").strValue())?today:formatter10.parse(BODY.getField("REGISTERED_DATE").strValue()));
		ecif.setClientBelongOrg(BODY.getField("CLIENT_BELONG_ORG").strValue());
		ecif.setRegisteredTellerNo(BODY.getField("REGISTERED_TELLER_NO").strValue());
		ecif.setRegistOrgNo(BODY.getField("REGIST_ORG_NO").strValue());
		ecif.setCountryCitizen(BODY.getField("COUNTRY_CITIZEN").strValue());
		ecif.setNationalityCode(BODY.getField("NATIONALITY_CODE").strValue());
		ecif.setRegPermResidence(BODY.getField("AREA_NAME").strValue().split("_")[1]);
		ecif.setRegPermResidence_1(BODY.getField("PROVINCE_NAME").strValue());
		ecif.setRegPermResidence_2(BODY.getField("CITY_NAME").strValue());
		ecif.setRegPermResidence_3(BODY.getField("AREA_NAME").strValue());
		ecif.setOpenAcctBranchId(BODY.getField("OPEN_ACCT_BRANCH_ID").strValue());
		ecif.setOpenTellerNo(BODY.getField("OPEN_TELLER_NO").strValue());
		ecif.setOpenAcctDate(StringUtils.isEmpty(BODY.getField("OPEN_ACCT_DATE").strValue())?today:formatter10.parse(BODY.getField("OPEN_ACCT_DATE").strValue()));
		ecif.setMaritalStatus(BODY.getField("MARITAL_STATUS").strValue());
		ecif.setEducationLevel(BODY.getField("EDUCATION_LEVEL").strValue());
		ecif.setCity(BODY.getField("CITY_AREA_NAME").strValue().split("_")[1]);
		ecif.setCity_1(BODY.getField("CITY_PROVINCE_NAME").strValue());
		ecif.setCity_2(BODY.getField("CITY_CITY_NAME").strValue());
		ecif.setCity_3(BODY.getField("CITY_AREA_NAME").strValue());
		ecif.setAreaCode(BODY.getField("AREA_CODE").strValue());
		ecif.setIncidenceRelation(BODY.getField("INCIDENCE_RELATION").strValue());
		ecif.setIdentityType(BODY.getField("IDENTITY_TYPE").strValue());
		ecif.setAddress(BODY.getField("ADDRESS").strValue());
		ecif.setAddressType(BODY.getField("ADDRESS_TYPE").strValue());
		ecif.setPostalCode(BODY.getField("POSTAL_CODE").strValue());
		ecif.setContactModeType(BODY.getField("CONTACT_MODE_TYPE").strValue());
		ecif.setContactMode(BODY.getField("CONTACT_MODE").strValue());
		ecif.setOccupation(BODY.getField("OCCUPATION").strValue());
		ecif.setCompanyName(BODY.getField("COMPANY_NAME").strValue());
		ecif.setChl("0");//0-ipad待处理;1-ipad已处理 
		//写入数据到basic_customer_information表
		CustomerInfor info = customerInforservice.findCustomerInforByCardId(ecif.getGlobalId());
		//增加判断身份证号码是否存在
		if(info != null){
			throw new IpadException("身份证号码"+ecif.getGlobalId()+"已存在");
		}
		if(info == null){
			info = new CustomerInfor();
		}
		
		info.setUserId(BODY.getField("USER_NO").strValue());
		info.setChineseName(ecif.getClientName());
		info.setBirthday(formatter10.format(ecif.getBirthDate()));
		info.setNationality("NTC00000000156");
		info.setSex(ecif.getSex().equals("01") ? "Male" : "Female");
		info.setCardId(ecif.getGlobalId());
		info.setChl("0");//0-ipad待处理;1-ipad已处理 
		
		ecif.setCreatedBy(BODY.getField("USER_NO").strValue());
		ecif.setUserId(BODY.getField("USER_NO").strValue());
		ecifService.insertCustomerInfor(ecif,info);
		
		return null;
	}

	//ipad 更新微贷客户位置信息
	public CompositeData ipadUpdateCustomerPosition(CompositeData BODY) {
		// TODO Auto-generated method stub
		String CLIENT_CODE = BODY.getField("CLIENT_CODE").strValue();
		//String LONGITUDE = BODY.getField("LONGITUDE").strValue();
		//String LATITUDE = BODY.getField("LATITUDE").strValue();
		String COORDINATE = BODY.getField("COORDINATE").strValue();
		String REMARK = BODY.getField("REMARK").strValue();
		//String sql = "update qz_iesb_for_ecif set COORDINATE='"+COORDINATE+"' where customer_id='"+CLIENT_CODE+"'";
		//commonDao.execute(sql);
		customerInforDao.updateCustomerLocationMsg(COORDINATE, CLIENT_CODE,REMARK);
		return null;
	}

	//ipad查询客户信息列表
	public CompositeData ipadQueryCustomerInfoList(CompositeData BODY,String limit,String page) throws Exception {
		// TODO Auto-generated method stub
		String QUERY_TYPE = BODY.getField("QUERY_TYPE").strValue();
		String USER_NO = BODY.getField("USER_NO").strValue();
		String CLIENT_NAME = BODY.getField("CLIENT_NAME").strValue();
		String PRODUCT_ID = BODY.getField("PRODUCT_CODE").strValue();
		String global_id = BODY.getField("GLOBAL_ID").strValue();
		
		//查找产品对应defaultType
		com.cardpay.pccredit.product.model.ProductAttribute product = null;
		if(StringUtils.isNotEmpty(PRODUCT_ID)){
			product = productService.findProductAttributeById(PRODUCT_ID);
		}
		
		List<CustomerInfoIpad> customerInfoIpad_ls = new ArrayList<CustomerInfoIpad>();
		if(QUERY_TYPE.equals(IpadConstant.QUERY_TYPE_ALL)){
			if(product != null){
				customerInfoIpad_ls = customerInforDao.ipadFindAllCustomerByUserId(USER_NO,CLIENT_NAME,product.getDefaultType(),global_id,limit,page);
			}
			else{
				customerInfoIpad_ls = customerInforDao.ipadFindAllCustomerByUserId(USER_NO,CLIENT_NAME,null,global_id,limit,page);
			}
		}
		else if(QUERY_TYPE.equals(IpadConstant.QUERY_TYPE_WSX)){
			if(product != null){
				customerInfoIpad_ls = customerInforDao.ipadFindWsxCustomerByUserId(USER_NO,CLIENT_NAME,product.getDefaultType(),global_id,limit,page);
			}
			else{
				customerInfoIpad_ls = customerInforDao.ipadFindWsxCustomerByUserId(USER_NO,CLIENT_NAME,null,global_id,limit,page);
			}
		}
		else if(QUERY_TYPE.equals(IpadConstant.QUERY_TYPE_WJQ)){
			if(product != null){
				customerInfoIpad_ls = customerInforDao.ipadFindWjqCustomerByUserId(USER_NO,CLIENT_NAME,product.getDefaultType(),global_id,limit,page);
			}
			else{
				customerInfoIpad_ls = customerInforDao.ipadFindWjqCustomerByUserId(USER_NO,CLIENT_NAME,null,global_id,limit,page);
			}
		}
		else if(QUERY_TYPE.equals(IpadConstant.QUERY_TYPE_YJQ)){
			if(product != null){
				customerInfoIpad_ls = customerInforDao.ipadFindYjqCustomerByUserId(USER_NO,CLIENT_NAME,product.getDefaultType(),global_id,limit,page);
			}
			else{
				customerInfoIpad_ls = customerInforDao.ipadFindYjqCustomerByUserId(USER_NO,CLIENT_NAME,null,global_id,limit,page);
			}
		}
		else{
			throw new IpadException("查询类型QUERY_TYPE错误");
		}
		
		//返回结果
		CompositeData respBody = new CompositeData();
		Array array = new Array();
		
		DictionaryManager dictMgr = Beans.get(DictionaryManager.class);
		Dictionary dictionary = dictMgr.getDictionaryByName("CERT_TYPE");//FIELD_NAME[i]具体数据字典字段
		
		for(CustomerInfoIpad obj : customerInfoIpad_ls ){
			CompositeData struct = new CompositeData();
			
			Field CUSTOMER_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			CUSTOMER_NAME.setValue(obj.getClientName());
			struct.addField("CLIENT_NAME", CUSTOMER_NAME);
			
			Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			List<DictionaryItem> dictItems = dictionary.getItems();
			for(DictionaryItem item : dictItems){
				if(item.getName().equals(obj.getGlobalType())){
					GLOBAL_TYPE.setValue(item.getTitle());
				}
				break;
			}
			struct.addField("GLOBAL_TYPE", GLOBAL_TYPE);
			
			Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			GLOBAL_ID.setValue(obj.getGlobalId());
			struct.addField("GLOBAL_ID", GLOBAL_ID);
			
			Field CLIENT_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			CLIENT_NO.setValue(obj.getClientNo());
			struct.addField("CLIENT_NO", CLIENT_NO);
			
			Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
			CLIENT_CODE.setValue(obj.getCustomerId());
			struct.addField("CLIENT_CODE", CLIENT_CODE);
			
			
			Field COORDINATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			COORDINATE.setValue(obj.getCoordinate());
			struct.addField("COORDINATE", COORDINATE);
			
			Field query_type = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			if(QUERY_TYPE.equals(IpadConstant.QUERY_TYPE_ALL)){
				if(obj.getWjq() != null){
					query_type.setValue(IpadConstant.QUERY_TYPE_WJQ);
				}
				else if(obj.getYjq() != null){
					query_type.setValue(IpadConstant.QUERY_TYPE_YJQ);
				}
				else{
					query_type.setValue(IpadConstant.QUERY_TYPE_WSX);
				}
			}
			else{
				query_type.setValue(QUERY_TYPE);
			}
			struct.addField("QUERY_TYPE", query_type);
			
			Field PROCESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			PROCESS.setValue(obj.getProcess());//TODO
			struct.addField("PROCESS", PROCESS);
			
			Field REMARK = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
			REMARK.setValue(obj.getDescribe());
			struct.addField("REMARK", REMARK);
			
			array.addStruct(struct);
		}
		respBody.addArray("CLIENT_ARRAY", array);
		return respBody;
	}
		
	//ipad 查询微贷客户信息
	public CompositeData ipadQueryCustomerInfo(CompositeData BODY) {
		// TODO Auto-generated method stub
		String USER_NO = BODY.getField("USER_NO").strValue();
		String CLIENT_CODE = BODY.getField("CLIENT_CODE").strValue();
		
		ECIF ecif = ecifService.findEcifByCustomerId(CLIENT_CODE);
		
		if(ecif == null){return null;}
		
		//返回结果
		CompositeData respBody = new CompositeData();
	   
		if(ecif!=null){
			
		Field USER_NO_FIELD = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		USER_NO_FIELD.setValue(USER_NO);
		respBody.addField("USER_NO", USER_NO_FIELD);
			
		Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		GLOBAL_TYPE.setValue(ecif.getGlobalType());
		respBody.addField("GLOBAL_TYPE", GLOBAL_TYPE);
		
		Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		GLOBAL_ID.setValue(ecif.getGlobalId());
		respBody.addField("GLOBAL_ID", GLOBAL_TYPE);
		
		Field GLOBAL_DESC = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		GLOBAL_DESC.setValue(ecif.getGlobalDesc());
		respBody.addField("GLOBAL_DESC", GLOBAL_DESC);
		
		Field CERT_AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CERT_AREA_CODE.setValue(ecif.getCertAreaCode());
		respBody.addField("CERT_AREA_CODE", CERT_AREA_CODE);
		
		Field CERT_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CERT_ORG.setValue(ecif.getCertOrg());
		respBody.addField("CERT_ORG", CERT_ORG);
		
		Field ISS_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		ISS_DATE.setValue(formatter10.format(ecif.getIssDate()));
		respBody.addField("ISS_DATE", ISS_DATE);
		
		Field EFFECT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		EFFECT_DATE.setValue(formatter10.format(ecif.getEffectDate()));
		respBody.addField("EFFECT_DATE", EFFECT_DATE);
		
		Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		EXPIRY_DATE.setValue(formatter10.format(ecif.getExpiryDate()));
		respBody.addField("EXPIRY_DATE", EXPIRY_DATE);
		
		Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CLIENT_NAME.setValue(ecif.getClientName());
		respBody.addField("CLIENT_NAME", CLIENT_NAME);
		
		Field CLIENT_NAME_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CLIENT_NAME_TYPE.setValue(ecif.getClientNameType());
		respBody.addField("CLIENT_NAME_TYPE", CLIENT_NAME_TYPE);
		
		Field CLIENT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CLIENT_TYPE.setValue(ecif.getClientType());
		respBody.addField("CLIENT_TYPE", CLIENT_TYPE);
		
		Field CLIENT_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CLIENT_STATUS.setValue(ecif.getClientStatus());
		respBody.addField("CLIENT_STATUS", CLIENT_STATUS);
		
		Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		BIRTH_DATE.setValue(formatter10.format(ecif.getBirthDate()));
		respBody.addField("BIRTH_DATE", BIRTH_DATE);
		
		Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		SEX.setValue(ecif.getSex());
		respBody.addField("SEX", SEX);
		
		Field ACCT_EXEC = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		ACCT_EXEC.setValue(ecif.getCustManagerId());
		respBody.addField("ACCT_EXEC", ACCT_EXEC);
		
		Field RECORD_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		RECORD_TELLER_NO.setValue(ecif.getRecordTellerNo());
		respBody.addField("RECORD_TELLER_NO", RECORD_TELLER_NO);
		
		Field REGISTERED_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		REGISTERED_DATE.setValue(formatter10.format(ecif.getRegisteredDate()));
		respBody.addField("REGISTERED_DATE", REGISTERED_DATE);
		
		Field CLIENT_BELONG_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CLIENT_BELONG_ORG.setValue(ecif.getClientBelongOrg());
		respBody.addField("CLIENT_BELONG_ORG", CLIENT_BELONG_ORG);
		
		Field REGISTERED_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		REGISTERED_TELLER_NO.setValue(ecif.getRegisteredTellerNo());
		respBody.addField("REGISTERED_TELLER_NO", REGISTERED_TELLER_NO);
		
		Field REGIST_ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		REGIST_ORG_NO.setValue(ecif.getRegistOrgNo());
		respBody.addField("REGIST_ORG_NO", REGIST_ORG_NO);
		
		Field COUNTRY_CITIZEN = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		COUNTRY_CITIZEN.setValue(ecif.getCountryCitizen());
		respBody.addField("COUNTRY_CITIZEN", COUNTRY_CITIZEN);
		
		Field NATIONALITY_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		NATIONALITY_CODE.setValue(ecif.getNationalityCode());
		respBody.addField("NATIONALITY_CODE", NATIONALITY_CODE);
		
		Field REGIST_PERMANENT_RESIDENCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		REGIST_PERMANENT_RESIDENCE.setValue(ecif.getRegPermResidence());
		respBody.addField("REGIST_PERMANENT_RESIDENCE", REGIST_PERMANENT_RESIDENCE);
		
		Field PROVINCE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		PROVINCE_NAME.setValue(ecif.getRegPermResidence_1());
		respBody.addField("PROVINCE_NAME", PROVINCE_NAME);
		
		Field CITY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CITY_NAME.setValue(ecif.getRegPermResidence_2());
		respBody.addField("CITY_NAME", CITY_NAME);
		
		Field AREA_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		AREA_NAME.setValue(ecif.getRegPermResidence_3());
		respBody.addField("AREA_NAME", AREA_NAME);
		
		Field OPEN_ACCT_BRANCH_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		OPEN_ACCT_BRANCH_ID.setValue(ecif.getOpenAcctBranchId());
		respBody.addField("OPEN_ACCT_BRANCH_ID", OPEN_ACCT_BRANCH_ID);
		
		Field OPEN_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		OPEN_TELLER_NO.setValue(ecif.getOpenTellerNo());
		respBody.addField("OPEN_TELLER_NO", OPEN_TELLER_NO);
		
		Field OPEN_ACCT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		OPEN_ACCT_DATE.setValue(formatter10.format(ecif.getOpenAcctDate()));
		respBody.addField("OPEN_ACCT_DATE", OPEN_ACCT_DATE);
		
		Field MARITAL_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		MARITAL_STATUS.setValue(ecif.getMaritalStatus());
		respBody.addField("MARITAL_STATUS", MARITAL_STATUS);
		
		Field EDUCATION_LEVEL = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		EDUCATION_LEVEL.setValue(ecif.getEducationLevel());
		respBody.addField("EDUCATION_LEVEL", EDUCATION_LEVEL);
		
		Field CITY = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CITY.setValue(ecif.getCity());
		respBody.addField("CITY", CITY);
		
		Field CITY_PROVINCE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CITY_PROVINCE_NAME.setValue(ecif.getCity_1());
		respBody.addField("CITY_PROVINCE_NAME", CITY_PROVINCE_NAME);
		
		Field CITY_CITY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CITY_CITY_NAME.setValue(ecif.getCity_2());
		respBody.addField("CITY_CITY_NAME", CITY_CITY_NAME);
		
		Field CITY_AREA_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CITY_AREA_NAME.setValue(ecif.getCity_3());
		respBody.addField("CITY_AREA_NAME", CITY_AREA_NAME);
		
		Field AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		AREA_CODE.setValue(ecif.getAreaCode());
		respBody.addField("AREA_CODE", AREA_CODE);
		
		Field INCIDENCE_RELATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		INCIDENCE_RELATION.setValue(ecif.getIncidenceRelation());
		respBody.addField("INCIDENCE_RELATION", INCIDENCE_RELATION);
		
		Field IDENTITY_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		IDENTITY_TYPE.setValue(ecif.getIdentityType());
		respBody.addField("IDENTITY_TYPE", IDENTITY_TYPE);
		
		Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		ADDRESS.setValue(ecif.getAddress());
		respBody.addField("ADDRESS", ADDRESS);
		
		Field ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		ADDRESS_TYPE.setValue(ecif.getAddressType());
		respBody.addField("ADDRESS_TYPE", ADDRESS_TYPE);
		
		Field POSTAL_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		POSTAL_CODE.setValue(ecif.getPostalCode());
		respBody.addField("POSTAL_CODE", POSTAL_CODE);
		
		Field CONTACT_MODE_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CONTACT_MODE_TYPE.setValue(ecif.getContactModeType());
		respBody.addField("CONTACT_MODE_TYPE", CONTACT_MODE_TYPE);
		
		Field CONTACT_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		CONTACT_MODE.setValue(ecif.getContactMode());
		respBody.addField("CONTACT_MODE", CONTACT_MODE);
		
		Field OCCUPATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		OCCUPATION.setValue(ecif.getOccupation());
		respBody.addField("OCCUPATION", OCCUPATION);
		
		Field COMPANY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		COMPANY_NAME.setValue(ecif.getCompanyName());
		respBody.addField("COMPANY_NAME", COMPANY_NAME);
		}
		return respBody;
	}
	
	//ipad 微贷申请信息查询 
	public CompositeData ipadQueryApplyInfoList(CompositeData BODY,String limit,String page) throws Exception {
		String QUERY_TYPE = BODY.getField("TRAN_TYPE").strValue();
		String USER_NO = BODY.getField("USER_NO").strValue();
		String CLIENT_NAME = BODY.getField("CLIENT_NAME").strValue();
		String global_id = BODY.getField("GLOBAL_ID").strValue();
		String APPROVE_STATUS = BODY.getField("APPROVE_STATUS").strValue();//审批状态
		
		//验证
		if(StringUtils.isEmpty(USER_NO)){throw new IpadException("用户编号必输!");}
		
		if(!StringUtils.isEmpty(APPROVE_STATUS)){
			APPROVE_STATUS = APPROVE_STATUS.substring(0, 2).equals("10")?"0":"1";
		}
		
		List<CustomerApplyInfoIpad> customerInfoIpad_ls = new ArrayList<CustomerApplyInfoIpad>();
		
		if(IpadConstant.QUERY_TYPE_01.equals(QUERY_TYPE)){//01-查询已进件贷款申请列表
			customerInfoIpad_ls = customerInforDao.ipadApplyInfoJjByUserId(QUERY_TYPE,USER_NO,CLIENT_NAME,global_id,APPROVE_STATUS,limit,page);
		}else if(IpadConstant.QUERY_TYPE_02.equals(QUERY_TYPE)){//02-查询贷款申请待处理补退件列表
			customerInfoIpad_ls = customerInforDao.ipadApplyInfoThByUserId(QUERY_TYPE,USER_NO,CLIENT_NAME,global_id,APPROVE_STATUS,limit,page);
		}else if(IpadConstant.QUERY_TYPE_03.equals(QUERY_TYPE)){//03-查询贷中客户列表
			customerInfoIpad_ls = customerInforDao.ipadApplyInfoDzByUserId(QUERY_TYPE,USER_NO,CLIENT_NAME,global_id,limit,page);
		}else {
			throw new IpadException("查询类型QUERY_TYPE错误");
		}
		
		for(CustomerApplyInfoIpad pieces : customerInfoIpad_ls){
			if(pieces.getStatus().equals(Constant.SAVE_INTOPICES_CN)){
				pieces.setProcess("未提交申请");
			} else if(pieces.getStatus().equals(Constant.APPROVE_INTOPICES_CN)||pieces.getStatus().equals(Constant.TRTURN_INTOPICES_CN)){
				String nodeName = intoPiecesComdao.findAprroveProgress(pieces.getInputWareRegNo());
				if(StringUtils.isNotEmpty(nodeName)){
					pieces.setProcess(nodeName);
				} else {
					pieces.setProcess("不在审批中");
				}
			} else if(pieces.getStatus().equals(Constant.RETURN_INTOPICES_CN)){
				pieces.setProcess("退回");
			} else {
				pieces.setProcess("审批结束");
			}
		}
		
		//返回结果
		CompositeData respBody = new CompositeData();
		Array array = new Array();
		for(CustomerApplyInfoIpad obj : customerInfoIpad_ls ){
			CompositeData struct = new CompositeData();
			
			Field INPUT_WARE_REG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			INPUT_WARE_REG_NO.setValue(obj.getInputWareRegNo());
			struct.addField("INPUT_WARE_REG_NO", INPUT_WARE_REG_NO);
			
			Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			CLIENT_CODE.setValue(obj.getCustomerId());
			struct.addField("CLIENT_CODE", CLIENT_CODE);
			
			Field client_name = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			client_name.setValue(obj.getClientName());
			struct.addField("CLIENT_NAME", client_name);
			
			Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			GLOBAL_ID.setValue(obj.getGlobalId());
			struct.addField("GLOBAL_ID", GLOBAL_ID);
			
			Field PRODUCT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			PRODUCT_CODE.setValue(obj.getProductCode());
			struct.addField("PRODUCT_CODE", PRODUCT_CODE);

			Field PRODUCT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			PRODUCT_NAME.setValue(obj.getProductName());
			struct.addField("PRODUCT_NAME", PRODUCT_NAME);
			
			Field APPLY_LIMIT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			APPLY_LIMIT_AMT.setValue(obj.getApplyLimitAmt());
			struct.addField("APPLY_LIMIT_AMT", APPLY_LIMIT_AMT);
			
			Field STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			STATUS.setValue(obj.getStatus());
			struct.addField("STATUS", STATUS);
			
			Field PROCESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
			PROCESS.setValue(obj.getProcess());
			struct.addField("PROCESS", PROCESS);
			
			Field BACK_REASON = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
			BACK_REASON.setValue(obj.getBackReason());
			struct.addField("BACK_REASON", BACK_REASON);
			
			array.addStruct(struct);
		}
		respBody.addArray("LOAN_APPLY_INFO_ARRAY", array);
		return respBody;
	}
	
	
	//ipad 微贷贷后检查任务查询
    public CompositeData ipadQueryDhjcist(CompositeData BODY,String limit,String page) throws Exception {
    	String QUERY_TYPE = BODY.getField("TRAN_TYPE").strValue();
    	String userId = BODY.getField("USER_NO").strValue();
		String chineseName = BODY.getField("CLIENT_NAME").strValue();
		String cardId = BODY.getField("GLOBAL_ID").strValue();
		String clientNo = BODY.getField("CLIENT_NO").strValue();
		String checkType = BODY.getField("CHECK_TYPE").strValue();
		String APPROVE_STATUS = BODY.getField("APPROVE_STATUS").strValue();
		
		//验证必输项
	    if(StringUtils.isEmpty(userId)){throw new IpadException("用户编号必输!");}
	    
    	StringBuffer belongChildIds = new StringBuffer();
		belongChildIds.append("(");
		if("1".equals(APPROVE_STATUS.substring(0, 1))){
			belongChildIds.append("'").append("0").append("'").append(",");
		}
		
		if("1".equals(APPROVE_STATUS.substring(1, 2))){
			belongChildIds.append("'").append("1").append("'").append(",");
		}
		
		if("1".equals(APPROVE_STATUS.substring(2, 3))){
			belongChildIds.append("'").append("2").append("'").append(",");
		}
		belongChildIds = belongChildIds.deleteCharAt(belongChildIds.length() - 1);
		belongChildIds.append(")");
		
		
		
    	List<CustomerDhJcInfoIpad> CustomerDhJcInfoIpad_ls = new ArrayList<CustomerDhJcInfoIpad>();
    	
    	//查询贷后检查任务
        CustomerDhJcInfoIpad_ls = customerInforDao.ipadDhJcByUserId(QUERY_TYPE,
													    			APPROVE_STATUS,
													    			userId,
													    			chineseName,
													    			cardId,
													    			clientNo,
													    			checkType,
													    			belongChildIds.toString(),limit,page);
    	
		//返回结果
		CompositeData respBody = new CompositeData();
		Array array = new Array();
		for(CustomerDhJcInfoIpad obj : CustomerDhJcInfoIpad_ls ){
			CompositeData struct = new CompositeData();

			Field TASK_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			TASK_ID.setValue(obj.getTaskId());
			struct.addField("TASK_ID", TASK_ID);
			
			Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			CLIENT_NAME.setValue(obj.getClientName());
			struct.addField("CLIENT_NAME", CLIENT_NAME);

			Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			GLOBAL_ID.setValue(obj.getGlobalId());
			struct.addField("GLOBAL_ID", GLOBAL_ID);
			
			Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			CLIENT_CODE.setValue(obj.getClientCode());
			struct.addField("CLIENT_CODE", CLIENT_CODE);
			
			Field TASK_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			if(StringUtils.isNotEmpty(obj.getCheckDescribe())){
				TASK_TYPE.setValue(obj.getCheckDescribe());
			}
			else{
				if(obj.getTaskType().equals("1")){
					TASK_TYPE.setValue("首次放款检查");
				}else if(obj.getTaskType().equals("2")){
					TASK_TYPE.setValue("满额用信");
				}else if(obj.getTaskType().equals("3")){
					TASK_TYPE.setValue("授信到期前最高额用信检查");
				}else if(obj.getTaskType().equals("4")){
					TASK_TYPE.setValue("放款次月客户回访");
				}
			}
			struct.addField("TASK_TYPE", TASK_TYPE);
			
			Field TASK_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			TASK_DATE.setValue(obj.getTaskDate());
			struct.addField("TASK_DATE", TASK_DATE);
			
			Field LOAN_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOAN_AMT.setValue(obj.getLoanAmt());
			struct.addField("LOAN_AMT", LOAN_AMT);
			
			Field LOAN_BALANCE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOAN_BALANCE.setValue(obj.getLoanBalance());
			struct.addField("LOAN_BALANCE", LOAN_BALANCE);
			
			Field CNT = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			CNT.setValue(obj.getCnt());
			struct.addField("CNT", CNT);
			
			//审批状态(0:待检查 1:待审核 2:审核通过)
			Field approve_status = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			if(obj.getApproveStatus().equals("0")){
				approve_status.setValue("待检查");
			}else if(obj.getApproveStatus().equals("1")){
				approve_status.setValue("待审核");
			}else if(obj.getApproveStatus().equals("2")){
				approve_status.setValue("审核通过");
			}
			struct.addField("APPROVE_STATUS", approve_status);
			
			Field BACK_REASON = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
			BACK_REASON.setValue(obj.getBackReason());
			struct.addField("BACK_REASON", BACK_REASON);
			
			array.addStruct(struct);

		}
		respBody.addArray("AFTER_LOAN_INFO_ARRAY", array);
		return respBody;
    }
    
    
    //ipad 微贷任务提醒
  	public CompositeData ipadWdtxList(CompositeData BODY) throws Exception {
  		String USER_NO = BODY.getField("USER_NO").strValue();//用户编号
  		
  		//验证必输项
	    if(StringUtils.isEmpty(USER_NO)){throw new IpadException("用户编号必输!");}
	    User user = userService.getUserById(USER_NO);
  		CustomerDhJcTxInfoIpad customerDhJcTxInfoIpad = customerInforDao.ipadWdtxByUserId(USER_NO,user.getLogin()); 
	  	//返回结果
		CompositeData respBody = new CompositeData();
			
		Field LOAN_APPLY_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		LOAN_APPLY_NUM.setValue(customerDhJcTxInfoIpad.getLoanApplyNum());
		respBody.addField("LOAN_APPLY_NUM", LOAN_APPLY_NUM);
		
		Field LOAN_APPLY_BACK_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		LOAN_APPLY_BACK_NUM.setValue(customerDhJcTxInfoIpad.getAfterLoanBackNum());
		respBody.addField("LOAN_APPLY_BACK_NUM", LOAN_APPLY_BACK_NUM);
		
		Field AFTER_LOAN_TASK_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		AFTER_LOAN_TASK_NUM.setValue(customerDhJcTxInfoIpad.getAfterLoanTaskNum());
		respBody.addField("AFTER_LOAN_TASK_NUM", AFTER_LOAN_TASK_NUM);
		
		Field AFTER_LOAN_BACK_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		AFTER_LOAN_BACK_NUM.setValue(customerDhJcTxInfoIpad.getAfterLoanBackNum());
		respBody.addField("AFTER_LOAN_BACK_NUM", AFTER_LOAN_BACK_NUM);
		
		Field EXPIRY_CLIENT_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		EXPIRY_CLIENT_NUM.setValue(customerDhJcTxInfoIpad.getExpiryClientNum());
		respBody.addField("EXPIRY_CLIENT_NUM", EXPIRY_CLIENT_NUM);
		
		Field OVERDUE_CLIENT_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
		OVERDUE_CLIENT_NUM.setValue(customerDhJcTxInfoIpad.getOverdueClinetNum());
		respBody.addField("OVERDUE_CLIENT_NUM", OVERDUE_CLIENT_NUM);

		return respBody;
  	}
  	
  	
  	
	 //ipad 到期微贷信息查询  
	public CompositeData ipadDqwdList(CompositeData BODY,String limit,String page) throws Exception {
		String USER_NO = BODY.getField("USER_NO").strValue();
		String GLOBAL_ID = BODY.getField("GLOBAL_ID").strValue();
		String BUSS_TYPE = BODY.getField("BUSS_TYPE").strValue();
		String CLIENT_NAME = BODY.getField("CLIENT_NAME").strValue();
		
		//验证必输项
	    if(StringUtils.isEmpty(USER_NO)){throw new IpadException("用户编号必输!");}
		
		List<CustomerDqwdtxInfoIpad> CustomerDhJcInfoIpad_ls = new ArrayList<CustomerDqwdtxInfoIpad>();
		CustomerDhJcInfoIpad_ls = customerInforDao.ipadDqwdByUserId(CLIENT_NAME,GLOBAL_ID,BUSS_TYPE,USER_NO,limit,page);
		//返回结果
		CompositeData respBody = new CompositeData();
		Array array = new Array();
		for(CustomerDqwdtxInfoIpad obj : CustomerDhJcInfoIpad_ls ){
			CompositeData struct = new CompositeData();
			
			Field clientName = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			clientName.setValue(obj.getClientName());
			struct.addField("CLIENT_NAME", clientName);
			
			Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			CLIENT_CODE.setValue(obj.getClientCode());
			struct.addField("CLIENT_CODE", CLIENT_CODE);
			
			Field DUEBILL_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			DUEBILL_NO.setValue(obj.getDuebillNo());
			struct.addField("DUEBILL_NO", DUEBILL_NO);
			
			Field PRESENT_BALANCE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			PRESENT_BALANCE.setValue(obj.getPresentBalance());
			struct.addField("PRESENT_BALANCE", PRESENT_BALANCE);
			
			Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			EXPIRY_DATE.setValue(obj.getExpiryDate());
			struct.addField("EXPIRY_DATE", EXPIRY_DATE);
			
			Field RECE_INTEREST = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			RECE_INTEREST.setValue(obj.getReceInterest());
			struct.addField("RECE_INTEREST", RECE_INTEREST);
			
			array.addStruct(struct);
		}
		respBody.addArray("LOAN_MSG_ARRAY", array);
		return respBody;
	}
	
	
	//ipad 进件申请
	public CompositeData ipadAddCustomerApplyInfo(CompositeData BODY) throws ParseException, IpadException {
		CustomerApplicationInfo appInfo = new CustomerApplicationInfo();
		
		String prodId = BODY.getField("PRODUCT_CODE").strValue();//产品id
	    String zaId  = BODY.getField("PROJECT_ID").strValue();//专案
	    String custId  = BODY.getField("CLIENT_CODE").strValue();//客户
	    String userId  = BODY.getField("USER_NO").strValue();//用户信息/客户经理
	    
	    //验证必输项
	    if(StringUtils.isEmpty(prodId)){throw new IpadException("产品代码必输");}
	    if(StringUtils.isEmpty(custId)){throw new IpadException("客户代码必输");}
	    if(StringUtils.isEmpty(userId)){throw new IpadException("用户编号必输");}
	    
	    // find product
	 	com.cardpay.pccredit.product.model.ProductAttribute product = productService.findProductAttributeById(prodId);
	 	if(product == null){
	 		throw new IpadException("无此产品");
	 	}
	 		
	 	//save，audit，RETURNAPPROVE的进件有且只能有一条
	 	if(customerInforservice.checkCanApplyOrNot(custId,userId) >=1){
	 		throw new IpadException("此客户已存在进件，不能重复申请");
	 	}
	 	
	    //生成appId
	    String appId = IDGenerator.generateID();
	    appInfo.setCustomerId(custId);
		appInfo.setProductId(prodId);
		appInfo.setCreatedBy(userId);
		appInfo.setCreatedTime(new Date());
		appInfo.setStatus("save");//暂存
		appInfo.setChl("0");//0-ipad待处理;1-ipad已处理 
		appInfo.setId(appId);
		customerInforservice.insertApplication(appInfo);
		
		if(product.getDefaultType().equals("2")){
			if(StringUtils.isNotEmpty(zaId)){
				//add QZ_APPLN_ZA_YWSQB_R 专栏进件关系表
				QzAppln_Za_Ywsqb_R  za = new QzAppln_Za_Ywsqb_R();
				za.setCustomerId(custId);
				za.setApplicationId(appId);
				
				za.setProductType(product.getDefaultType());
				za.setZaId(zaId);
				commonDao.insertObject(za);
			}
			else{
				throw new IpadException("贷生活必需输入专案!");
			}
		}
		
		return null;
	}
	
	//ipad 进件资料上传 
	public CompositeData ipadApplyUpload(CompositeData BODY) throws ParseException, IpadException {
		//验证
		if(StringUtils.isEmpty(BODY.getField("INPUT_WARE_REG_NO").strValue())){throw new IpadException("进件登记号必输!");}
		
		//进件表
		CustomerApplicationInfo appInfo  = commonDao.findObjectById(CustomerApplicationInfo.class, BODY.getField("INPUT_WARE_REG_NO").strValue());
		appInfo.setChl("1");//1-ipad已处理
		commonDao.updateObject(appInfo);
		
		//贷款申请表 QzApplnYwsqb
		QzApplnYwsqb qz = new QzApplnYwsqb();
		qz.setApplicationId(BODY.getField("INPUT_WARE_REG_NO").strValue());
		qz.setCustomerId(BODY.getField("USER_NO").strValue());
		qz.setOrgName(BODY.getField("APP_BRANCH_NAME").strValue());
		qz.setOrgId(BODY.getField("APP_BRANCH_ID").strValue());
		qz.setApplyTime(StringUtils.isEmpty(BODY.getField("APPLY_DATE").strValue())?new Date():formatter10.parse(BODY.getField("APPLY_DATE").strValue()));
		qz.setName(BODY.getField("CLIENT_NAME").strValue());
		qz.setSex(BODY.getField("SEX").strValue());
		qz.setGlobalId(BODY.getField("GLOBAL_ID").strValue());
		qz.setGlobalType(BODY.getField("GLOBAL_TYPE").strValue());
		qz.setGlobalTypeOther(BODY.getField("REMARK").strValue());
		qz.setEducationLevel(BODY.getField("EDUCATION").strValue());
		qz.setEducationLevelOther(BODY.getField("REMARK1").strValue());
		qz.setMaritalStatus(BODY.getField("MARRIAGE_STATUS").strValue());
		qz.setMaritalStatusOther(BODY.getField("REMARK2").strValue());
		qz.setMaritalName(BODY.getField("MATE_NAME").strValue());
		qz.setMaritalGlobalType(BODY.getField("MATE_GLOBAL_TYPE").strValue());
		qz.setMaritalGlobalTypeOther(BODY.getField("REMARK5").strValue());
		qz.setMaritalGlobalId(BODY.getField("MATE_GLOBAL_ID").strValue());
		qz.setMaritalWorkunit(BODY.getField("MATE_COMPANY").strValue());
		qz.setMaritalPhone(BODY.getField("MATE_TELEPHONE_NO").strValue());
		qz.setFamilyNum(BODY.getField("HOME_NUM").strValue());
		qz.setAddressType(BODY.getField("HOME_ADDRESS_TYPE").strValue());
		qz.setAddressTypeOther(BODY.getField("REMARK3").strValue());
		qz.setAddress(BODY.getField("HOME_ADDRESS").strValue());
		qz.setPhone_1(BODY.getField("MOBILE").strValue());
		qz.setPhone_2(BODY.getField("MOBILE2").strValue());
		qz.setHomePhone(BODY.getField("PHONE_NO").strValue());
		
		qz.setApplyAmount(BODY.getField("LOAN_AMT").strValue());
		qz.setApplyDeadline(BODY.getField("LOAN_TERM").strValue());
		qz.setApplyUse(BODY.getField("LOAN_PURPUSE_CODE").strValue());
		qz.setApplyEndTime(StringUtils.isEmpty(BODY.getField("EXPIRY_DATE").strValue())?new Date():formatter10.parse(BODY.getField("EXPIRY_DATE").strValue()));
		qz.setMonthRepay(BODY.getField("MONTH_REPAY").strValue());
		qz.setGuntThing(BODY.getField("PLEDGE_NAME").strValue());
		qz.setGuntPeople(BODY.getField("GURTER_ACCT_NAME").strValue());
		qz.setYearIncome(BODY.getField("LICENSE_INCOME").strValue());
		qz.setProfitType(BODY.getField("PROFIT_TYPE").strValue());
		qz.setProfit(BODY.getField("PROFIT_AMT").strValue());
		qz.setTotalAssets(BODY.getField("ASSET_TOTAL_AMT").strValue());
		qz.setInMoney(BODY.getField("OUGHT_ACCEPT_AMT").strValue());
		qz.setOutMoney(BODY.getField("OUGHT_PAY_AMT").strValue());
		qz.setOtherOut(BODY.getField("LIBILITY").strValue());
		qz.setMonthOtherIncome(BODY.getField("OTHER_INCOME").strValue());
		qz.setOtherIncomeSrc(BODY.getField("OTHER_SOURCE").strValue());

		//经营信息
		QzApplnJyxx  jyxx = new QzApplnJyxx();
		jyxx.setBussName(BODY.getField("BUSINESS_NAME").strValue());
		jyxx.setBussScope(BODY.getField("BUSINESS_SCOPE").strValue());
		jyxx.setBussStartYear(BODY.getField("OPEN_TIME").strValue());
		jyxx.setBussAddrType(BODY.getField("BSN_ADDRESS_TYPE").strValue());
		jyxx.setBussAddrTypeOther(BODY.getField("REMARK6").strValue());
		jyxx.setBussAddr(BODY.getField("BSN_ADDRESS").strValue());
		jyxx.setBussPhone(BODY.getField("WORK_PHONE").strValue());
		jyxx.setBussEmployeeNum(BODY.getField("EMPLOYEE_NUMBER").strValue());
		jyxx.setBussType(BODY.getField("ORG_FORM").strValue());
		jyxx.setBussTypeOther(BODY.getField("REMARK4").strValue());
		
	    

		//供应商数组
		Array gy = BODY.getArray("SUPPLIER_INFO_ARRAY");
		List<QzApplnYwsqbZygys> gysList = new ArrayList<QzApplnYwsqbZygys>();
		for(int i =0;i<gy.size();i++){
			QzApplnYwsqbZygys gys = new QzApplnYwsqbZygys();
			gys.setName(gy.getStruct(i).getField("SUPPLIER_NAME").strValue());
			gys.setRate(gy.getStruct(i).getField("LOT").strValue());
			gys.setCondition(gy.getStruct(i).getField("DRAW_TYPE").strValue());
			gysList.add(gys);
		}
		
		
		//客户信息数组
		Array kh = BODY.getArray("CLIENT_ARRAY");
		List<QzApplnYwsqbZykh> khList = new ArrayList<QzApplnYwsqbZykh>();
		for(int i =0;i<kh.size();i++){
			QzApplnYwsqbZykh khs = new QzApplnYwsqbZykh();
			khs.setName(gy.getStruct(i).getField("CLIENT_NAME").strValue());
			khs.setRate(gy.getStruct(i).getField("LOT").strValue());
			khs.setCondition(gy.getStruct(i).getField("DRAW_TYPE").strValue());
			khList.add(khs);
		}
		
		//货款标志LOAN_FLAG 
		qz.setBorrowHistory(gy.getStruct(0).getField("LOAN_FLAG").strValue());
		
		//贷款信息明细数组
		Array mx = BODY.getArray("LOAN_MSG_INFO_ARRAY");
		List<QzApplnYwsqbJkjl> jkjlList = new ArrayList<QzApplnYwsqbJkjl>();
		for(int i =0;i<mx.size();i++){
			QzApplnYwsqbJkjl jkjl = new QzApplnYwsqbJkjl();
			jkjl.setBankOrOtherType(gy.getStruct(i).getField("LOAN_SOURCE").strValue());
			jkjl.setPurpose(gy.getStruct(i).getField("LOAN_PURPUSE_CODE").strValue());
			jkjl.setTotalAmount(gy.getStruct(i).getField("AMT").strValue());
			jkjl.setLoanDate(StringUtils.isEmpty(BODY.getField("DRAW_DOWN_DATE").strValue())?new Date():formatter10.parse(BODY.getField("DRAW_DOWN_DATE").strValue()));
			jkjl.setDeadline(gy.getStruct(i).getField("TERM").strValue());
			jkjl.setRates(gy.getStruct(i).getField("INT_RATE").strValue());
			jkjl.setRepayType(gy.getStruct(i).getField("REPAY_TYPE").strValue());
			jkjl.setGuaranteeMode(gy.getStruct(i).getField("GUARANTEE_MODE").strValue());
			jkjl.setRemainSum(gy.getStruct(i).getField("LOAN_BALANCE").strValue());
			jkjlList.add(jkjl);
		}
		
		qz.setHaveGunt(BODY.getField("PLEDGE_FLAG").strValue());
		qz.setThing_1(BODY.getField("PLEDGE_NAME1").strValue());
		qz.setThing_2(BODY.getField("PLEDGE_NAME2").strValue());
		qz.setThing_3(BODY.getField("PLEDGE_NAME3").strValue());
		qz.setThing_4(BODY.getField("PLEDGE_NAME4").strValue());
		qz.setLoanUser_1(BODY.getField("LOAN_USER1").strValue());
		qz.setLoanUser_2(BODY.getField("LOAN_USER2").strValue());
		qz.setLoanUser_3(BODY.getField("LOAN_USER3").strValue());
		qz.setLoanUser_4(BODY.getField("LOAN_USER4").strValue());
		qz.setGuntForOther(BODY.getField("GNT_OTHER_FLAG").strValue());
		qz.setGuntForOtherBankname(BODY.getField("GNT_OTHER_BANK").strValue());
		qz.setGuntForOtherClientname(BODY.getField("GNT_OTHERTNAME").strValue());
		qz.setHaveApplyLoan(BODY.getField("APPLY_LOAN_FLAG").strValue());
		qz.setHaveLoanTime(StringUtils.isEmpty(BODY.getField("DR_DATE").strValue())?new Date():formatter10.parse(BODY.getField("DR_DATE").strValue()));
		qz.setHaveElePro(BODY.getField("PRODUCT_FLAG").strValue());
		qz.setHaveEleProType(BODY.getField("PRODUCT_NO").strValue());
		qz.setHaveGotLoan(BODY.getField("RELATION_LOAN_FLAG").strValue());
		qz.setHaveGotLoanName(BODY.getField("DR_NAME").strValue());
		qz.setHaveGotLoanRelation(BODY.getField("RELATION_TYPE").strValue());
		qz.setSign(BODY.getField("SIGN").strValue());
		qz.setSignDate(StringUtils.isEmpty(BODY.getField("SIGN_DATE").strValue())?new Date():formatter10.parse(BODY.getField("SIGN_DATE").strValue()));
		qz.setInfoType(BODY.getField("INFO_SOURCE_TYPE").strValue());
		qz.setInfoTypeOther(BODY.getField("INFO_TYPE_OTHER").strValue());
		qz.setCommet(BODY.getField("COMMET").strValue());
		qz.setManagerId(BODY.getField("APP_NO").strValue());
		qz.setManagerName(BODY.getField("APP_NAME").strValue());
		qz.setManagerTime(StringUtils.isEmpty(BODY.getField("APP_DATE").strValue())?new Date():formatter10.parse(BODY.getField("APP_DATE").strValue()));
		
		
		//担保人信息数组
		Array dbrarray = BODY.getArray("GUARANTEE_INFO_ARRAY");
		List<QzApplnDbrxx> dbrList = new ArrayList<QzApplnDbrxx>();
		for(int i =0;i<dbrarray.size();i++){
			QzApplnDbrxx dbrxx = new QzApplnDbrxx();
			dbrxx.setId(BODY.getField("SEQU_NO").strValue());
			dbrxx.setCustomerId(BODY.getField("UESER_NO").strValue());
			dbrxx.setApplicationId(BODY.getField("INPUT_WARE_REG_NO").strValue());
			dbrxx.setName(BODY.getField("GUARANTEE_NAME").strValue());
			dbrxx.setSex(BODY.getField("SEX").strValue());
			dbrxx.setBirthday(StringUtils.isEmpty(BODY.getField("BIRTH_DATE").strValue())?new Date():formatter10.parse(BODY.getField("BIRTH_DATE").strValue()));
			dbrxx.setYeah(BODY.getField("RESIDENT_YEAR").strValue());
			dbrxx.setGlobalType(BODY.getField("GLOBAL_TYPE").strValue());
			dbrxx.setGlobalId(BODY.getField("GLOBAL_ID").strValue());
			dbrxx.setHaveGlobalIdCopy(BODY.getField("HARDCOY_FLAG").strValue());
			dbrxx.setPhone_1(BODY.getField("MOBILE").strValue());
			dbrxx.setPhone_2(BODY.getField("MOBILE2").strValue());
			dbrxx.setPhoneOther(BODY.getField("MOBILE3").strValue());
			dbrxx.setWorkUnitName(BODY.getField("COMMPANY_NAME").strValue());
			dbrxx.setWorkYear(BODY.getField("WORK_YEAR").strValue());
			dbrxx.setWorkUnitAddr(BODY.getField("COMMPANY_ADDR").strValue());
			dbrxx.setWorkUnitPhone(BODY.getField("COMMPANY_PHONE").strValue());
			dbrxx.setYeahIncome(BODY.getField("PERSON_ANNUAL_INCOME").strValue());
			dbrxx.setHaveProveFile(BODY.getField("FILE_FLAG").strValue());
			dbrxx.setFileName(BODY.getField("FILES_NAME").strValue());
			dbrxx.setMarriage(BODY.getField("MARITAL_STATUS").strValue());
			dbrxx.setHomePhone(BODY.getField("HOUSE_PHONE").strValue());
			dbrxx.setHomeAddr(BODY.getField("HOME_ADDR").strValue());
			dbrxx.setHomeType(BODY.getField("HOME_TYPE").strValue());
			dbrList.add(dbrxx);
		}
		//贷款信息数组
		Array loanArray = BODY.getArray("LOAN_MSG_ARRAY");
		List<QzApplnDbrxxDkjl> dkjList = new ArrayList<QzApplnDbrxxDkjl>();
		for(int i =0;i<loanArray.size();i++){
			QzApplnDbrxxDkjl dkjl = new QzApplnDbrxxDkjl();
			dkjl.setDbrxxId(BODY.getField("SEQU_NO").strValue());
			dkjl.setLoanType(BODY.getField("FUND_SOURCE").strValue());
			dkjl.setAmount(BODY.getField("LOAN_AMT").strValue());
			dkjl.setDeadline(BODY.getField("TERM").strValue());
			dkjl.setLoanDate(StringUtils.isEmpty(BODY.getField("DRAW_DOWN_DATE").strValue())?new Date():formatter10.parse(BODY.getField("DRAW_DOWN_DATE").strValue()));
			dkjl.setPurpose(BODY.getField("USAGE").strValue());
			dkjl.setRemainAmount(BODY.getField("LOAN_BALANCE").strValue());
			dkjl.setGuntForOther(BODY.getField("GNT_FLAG").strValue());
			dkjl.setGuntAmount(BODY.getField("GNT_AMT").strValue());
			dkjl.setGuntDeadline(BODY.getField("GNT_TERM").strValue());
			dkjList.add(dkjl);
		}
		
		//房产信息数组
		Array fc = BODY.getArray("HOUSE_PROPERTY_INFO_ARRAY");
		List<QzApplnDbrxxFc> fcList = new ArrayList<QzApplnDbrxxFc>();
		for(int i =0;i<fc.size();i++){
			QzApplnDbrxxFc fcobj = new QzApplnDbrxxFc();
			fcobj.setDbrxxId(BODY.getField("SEQU_NO").strValue());
			fcobj.setAddr(BODY.getField("ADDRESS").strValue());
			fcobj.setPrice(BODY.getField("COST").strValue());
			fcobj.setUsrSituation(BODY.getField("USER_INFO").strValue());
			fcobj.setHaveCopy(BODY.getField("HARDCOY_FLAG").strValue());
			fcList.add(fcobj);
		}
		//机动车信息数组
		Array jdcarray = BODY.getArray("VEHICLE_INFO_ARRAY");
		List<QzApplnDbrxxJdc> jdcList = new ArrayList<QzApplnDbrxxJdc>();
		for(int i =0;i<jdcarray.size();i++){
			QzApplnDbrxxJdc jdc = new QzApplnDbrxxJdc();
			jdc.setDbrxxId(BODY.getField("SEQU_NO").strValue());
			jdc.setCardNo(BODY.getField("VEH_NO").strValue());
			jdc.setPrice(BODY.getField("PRICE").strValue());
			jdc.setBuyDate(StringUtils.isEmpty(BODY.getField("BUY_DATE").strValue())?new Date():formatter10.parse(BODY.getField("BUY_DATE").strValue()));
			jdc.setHaveCopy_1(BODY.getField("VEHICLE_HARDCOY_FLAG").strValue());
			jdc.setHaveCopy_2(BODY.getField("HARDCOY_FLAG").strValue());
			jdcList.add(jdc);
		}
		
		qz.setEmail(BODY.getField("EMAIL").strValue());
		qz.setUnit(BODY.getField("COMPANY_NAME").strValue());
		qz.setProfession(BODY.getField("INDUSTRY").strValue());
		qz.setOccupation(BODY.getField("OCCUPATION").strValue());
		qz.setJobPost(BODY.getField("VOCATION_NAME").strValue());
		qz.setPositionTitle(BODY.getField("TITLE_CODE").strValue());
		qz.setUnitAddress(BODY.getField("COMMPANY_ADDR").strValue());
		qz.setWorkYear(BODY.getField("WORK_YEAR").strValue());
		qz.setUnitPhone(BODY.getField("COMMPANY_PHONE").strValue());
		qz.setCompanySize(BODY.getField("CORP_SCALE").strValue());
		qz.setUnitType(BODY.getField("CORP_ORG_FORM").strValue());
		qz.setBussdistrictAddress(BODY.getField("ADDRESS").strValue());
		
		//待上传资料
		QzApplnAttachmentList  attach = new  QzApplnAttachmentList();
		attach.setCustomerId(BODY.getField("CLIENT_CODE").strValue());
		attach.setApplicationId(BODY.getField("INPUT_WARE_REG_NO").strValue());
		attach.setShopName(BODY.getField("CMPNY_NAME").strValue());
		attach.setShopId(BODY.getField("LICENSE_NO").strValue());
		attach.setBussType(BODY.getField("BUSS_TYPE").strValue());
		attach.setChkValue(BODY.getField("INFO_TYPE").strValue());
		attach.setUser_1(BODY.getField("CUST_MANAGER_ID1").strValue());
		attach.setCreatedTime(StringUtils.isEmpty(BODY.getField("CREATION_TIME").strValue())?new Date():formatter10.parse(BODY.getField("CREATION_TIME").strValue()));
		attach.setDocid(BODY.getField("FLAG").strValue());
		attach.setUploadValue(BODY.getField("UPLOADED_INFO_TYPE").strValue());
			
		//贷款申请表 
		commonDao.insertObject(qz);
		//经营信息
		commonDao.insertObject(jyxx);
		//供应商数组
		for(QzApplnYwsqbZygys gys :gysList){
			commonDao.insertObject(gys);
		}
		//客户
		for(QzApplnYwsqbZykh khs :khList){
			commonDao.insertObject(khs);
		}
		//贷款信息明细
		for(QzApplnYwsqbJkjl jkjl:jkjlList){
			commonDao.insertObject(jkjl);
		}
		//担保人
		for(QzApplnDbrxx dbrxx :dbrList){
			commonDao.insertObject(dbrxx);
		}
		//贷款信息
		for(QzApplnDbrxxDkjl dkjl :dkjList){
			commonDao.insertObject(dkjl);
		}
		//房产
		for(QzApplnDbrxxFc fcobj :fcList){
			commonDao.insertObject(fcobj);
		}
		//机动车
		for(QzApplnDbrxxJdc jdc :jdcList){
			commonDao.insertObject(jdc);
		}
		//待上传资料
		commonDao.insertObject(attach);
		return null;
	}
	
	//ipad 微贷申请审批信息查询
	public CompositeData ipadWdAuditInfo(CompositeData BODY) throws ParseException, IpadException {
		String appId = BODY.getField("INPUT_WARE_REG_NO").strValue();
		//验证
		if(StringUtils.isEmpty(appId)){throw new IpadException("进件登记号必输!");}
		
		List<ApproveHistoryForm>  ls = customerInforDao.ipadWdAuditInfo(appId);
		//返回结果
		CompositeData respBody = new CompositeData();
		Array array = new Array();
		for(ApproveHistoryForm obj : ls ){
			CompositeData struct = new CompositeData();
			
			Field NODE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			NODE_NAME.setValue(obj.getStatusName());
			struct.addField("NODE_NAME", NODE_NAME);
			
			Field APPROVE_RESULT = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			APPROVE_RESULT.setValue(obj.getExamineResult());
			struct.addField("APPROVE_RESULT", APPROVE_RESULT);
			
			Field APPROVE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			APPROVE_NAME.setValue(obj.getDisplayName());
			struct.addField("APPROVE_NAME", APPROVE_NAME);
			
			Field APPROVE_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			APPROVE_DATE.setValue(sdf.format(obj.getStartExamineTime()));
			struct.addField("APPROVE_DATE", APPROVE_DATE);
			
			Field APPROVE_LIMIT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			APPROVE_LIMIT_AMT.setValue(obj.getExamineAmount()==null?Double.valueOf("0.0"):Double.valueOf(obj.getExamineAmount()));
			struct.addField("APPROVE_LIMIT_AMT", APPROVE_LIMIT_AMT);
			
			array.addStruct(struct);
		}
		respBody.addArray("APPROVE_INFO_ARRAY", array);
		return respBody;
	}
	
	//ipad 微贷申请审批流程查询
	public CompositeData ipadWdApplyProcess(CompositeData BODY) throws ParseException, IpadException {
		//产品id
		String productId = BODY.getField("PRODUCT_CODE").strValue();
		//验证
		if(StringUtils.isEmpty(productId)){throw new IpadException("产品代码必输!");}
		
		//查询产品 
		com.cardpay.pccredit.product.model.ProductAttribute  product = productService.findProductAttributeById(productId);
		
		if(product==null){
			throw new IpadException("未查询到该产品信息");
		}
		
		//查询审批信息
		List<NodeAuditForm> nodeList = new ArrayList<NodeAuditForm>();
		nodeList =  customerInforDao.ipadWdApplyProcess(productId);
		//返回结果
		CompositeData respBody = new CompositeData();
		Field PRODUCT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
		PRODUCT_CODE.setValue(product.getId());
		respBody.addField("PRODUCT_CODE", PRODUCT_CODE);
		
		Field PRODUCT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
		PRODUCT_NAME.setValue(product.getProductName());
		respBody.addField("PRODUCT_NAME", PRODUCT_NAME);
		
		Array array = new Array();
		for(NodeAuditForm node :nodeList){
			CompositeData struct = new CompositeData();
			
			Field NOTE_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			NOTE_CODE.setValue(node.getId());
			struct.addField("NOTE_CODE", NOTE_CODE);
			
			Field NODE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			NODE_NAME.setValue(node.getNodeName());
			struct.addField("NODE_NAME", NODE_NAME);
			
			Field START_NODE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			START_NODE_FLAG.setValue(node.getIsstart());
			struct.addField("START_NODE_FLAG", START_NODE_FLAG);
			
			Field END_NODE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			END_NODE_FLAG.setValue(node.getIsend());
			struct.addField("END_NODE_FLAG", END_NODE_FLAG);
			
			Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			SEQU_NO.setValue(node.getSeqNo()+"");
			struct.addField("SEQU_NO", SEQU_NO);
			
			array.addStruct(struct);
		}
		respBody.addArray("NOTE_INFO_ARRAY", array);
		return respBody;
	}
	
	//ipad 客户用信查询
    public CompositeData ipadKhyxInfo(CompositeData BODY) throws ParseException, IpadException {
    	//客户代码
        String clientCode = BODY.getField("CLIENT_CODE").strValue();
        
        //验证
		if(StringUtils.isEmpty(clientCode)){throw new IpadException("客户代码必输!");}
		
		List<Circle> circle_ls = circleService.findCircleApproved(clientCode);
        String clientNo = null;
		if(circle_ls != null && circle_ls.size() > 0){
			clientNo = circle_ls.get(0).getClientNo();
		}
		else{
			throw new IpadException("客户未授信!");
		}
		
        O_CLPM_CTR_LOAN_CONT cont = customerInforDao.ipadWdKuyx(clientNo);
        List<O_CLPM_ACC_LOAN> loanList = customerInforDao.ipadWdKuyxList(clientNo);
        //返回结果
        CompositeData respBody = new CompositeData();
        
      	double usedLimit = 0d;
      	double remainLimit = 0d;
      	
        if(cont != null){
        	Field CREIDT_LIMIT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
          	CREIDT_LIMIT.setValue(cont.getContAmt()==null?Double.valueOf("0.0"):Double.valueOf(cont.getContAmt()));
          	respBody.addField("CREIDT_LIMIT", CREIDT_LIMIT);
          	
          	remainLimit = cont.getContAmt()==null?Double.valueOf("0.0"):Double.valueOf(cont.getContAmt());
        }
        else{
        	Field CREIDT_LIMIT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
          	CREIDT_LIMIT.setValue(Double.valueOf("0.0"));
          	respBody.addField("CREIDT_LIMIT", CREIDT_LIMIT);
          	
          	remainLimit = 0d;
        }
      	
      	Array array = new Array();
      	for(O_CLPM_ACC_LOAN loan :loanList){
      		CompositeData struct = new CompositeData();
			
			Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			CLIENT_CODE.setValue(loan.getCusId());
			struct.addField("CLIENT_CODE", CLIENT_CODE);
			
			Field DUEBILL_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			DUEBILL_NO.setValue(loan.getBillNo());
			struct.addField("DUEBILL_NO", DUEBILL_NO);
			
			Field CONTRACT_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			CONTRACT_NO.setValue(loan.getContNo());
			struct.addField("CONTRACT_NO", CONTRACT_NO);
			
			Field LOAN_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOAN_AMT.setValue(loan.getLoanAmt());
			struct.addField("LOAN_AMT", LOAN_AMT);
			
			Field DRAWDOWN_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			DRAWDOWN_DATE.setValue(loan.getDistrDate());
			struct.addField("DRAWDOWN_DATE", DRAWDOWN_DATE);
			
			Field OVERDUE_TERMS = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			OVERDUE_TERMS.setValue(loan.getOverdue()+"");
			struct.addField("OVERDUE_TERMS", OVERDUE_TERMS);
			
			Field OVERDUE_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			OVERDUE_AMT.setValue(loan.getOverdueBalance());
			struct.addField("OVERDUE_AMT", OVERDUE_AMT);

			Field BOOK_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			BOOK_STATUS.setValue(loan.getAccStatus());
			struct.addField("BOOK_STATUS", BOOK_STATUS);
			
			array.addStruct(struct);
			
			usedLimit += Double.valueOf(loan.getLoanBalance());
      	}
      	
      	Field USED_LIMIT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));//???????????????????????????????有问题
      	USED_LIMIT.setValue(usedLimit);
      	respBody.addField("USED_LIMIT", USED_LIMIT);
      	
      	remainLimit -= usedLimit;
      	Field REMAIN_LIMIT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 16));
      	REMAIN_LIMIT.setValue(remainLimit);
      	respBody.addField("REMAIN_LIMIT", REMAIN_LIMIT);
      	
      	respBody.addArray("DUEBILL_INFO_ARRAY", array);
		return respBody;
    }
    
    //ipad 微贷贷后检查信息新增
    public CompositeData ipadDhJcAddInfo(CompositeData BODY) throws ParseException, IpadException {
    	String  taskId = BODY.getField("TASK_ID").strValue();
    	String  clientName = BODY.getField("CLIENT_NAME").strValue();
    	String  globalId = BODY.getField("GLOBAL_ID").strValue();
    	String  clientCode = BODY.getField("CLIENT_CODE").strValue();
    	String  taskType = BODY.getField("TASK_TYPE").strValue();
    	String  taskDate = BODY.getField("TASK_DATE").strValue();
    	String  loanAmt = BODY.getField("LOAN_AMT").strValue();
    	String  loanBalance = BODY.getField("LOAN_BALANCE").strValue();
    	String  cnt = BODY.getField("CNT").strValue();
    	String  chiCustManager = BODY.getField("CHI_CUST_MANAGER").strValue();
    	String  chiOrgNo = BODY.getField("CHI_ORG_NO").strValue();
    	String  checkDate = BODY.getField("CHECK_DATE").strValue();
    	String  checkPosition = BODY.getField("CHECK_POSITION").strValue();
    	String  helpers = BODY.getField("HELPERS").strValue();
    	String  contactMode = BODY.getField("CONTACT_MODE").strValue();
    	String  loanRepayMsg = BODY.getField("LOAN_REPAY_MSG").strValue();
    	String  remark = BODY.getField("REMARK").strValue();
    	String  checkType = BODY.getField("CHECK_TYPE").strValue();
    	String  riskLevel = BODY.getField("RISK_LEVEL").strValue();
    	String  checkResult = BODY.getField("CHECK_RESULT").strValue();
    	String  uploadFlag = BODY.getField("UPLOAD_FLAG").strValue();
    	
    	//贷后检查任务表
    	PspCheckTask  psptask = new PspCheckTask();
    	psptask.setTaskId(taskId);
    	psptask.setCusId(clientCode);
    	psptask.setTaskType(taskType);
    	psptask.setTaskCreateDate(taskDate);
    	psptask.setLoanTotlAmt(Double.parseDouble(loanAmt));
    	psptask.setLoanBalance(Double.parseDouble(loanBalance));
    	psptask.setQnt(Integer.parseInt(cnt));
    	psptask.setManagerId(chiCustManager);
        psptask.setManagerBrId(chiOrgNo);
        psptask.setCheckTime(checkDate);
        psptask.setCheckAddr(checkPosition);
        psptask.setAgreedPerson(helpers);
        psptask.setContactInformation(contactMode);
        psptask.setRepayment(loanRepayMsg);
        psptask.setRepaymentOther(remark);
        psptask.setReciprocalType(checkType);
        psptask.setCheckDescribe(riskLevel);
        psptask.setRemarks(checkResult);
        psptask.setUploadFlag(uploadFlag);
        
        commonDao.insertObject(psptask);
    	return null;
    }
    
    
    //ipad 微贷贷后检查信息查询
    public CompositeData ipadDhJcSearchInfo(CompositeData BODY) throws ParseException, IpadException {
    	String  taskId = BODY.getField("TASK_ID").strValue();
    	PspCheckTaskVo vo =  customerInforDao.ipadDhjcbrowse(taskId);
    	
    	//返回结果
        CompositeData respBody = new CompositeData();
        if(vo !=null){
	      	Field TASK_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	      	TASK_ID.setValue(vo.getTaskId());
	      	respBody.addField("TASK_ID", TASK_ID);
	      	
	      	Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	CLIENT_NAME.setValue(vo.getClientName());
	      	respBody.addField("CLIENT_NAME", CLIENT_NAME);
	      	
	      	
	      	Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	GLOBAL_ID.setValue(vo.getGlobalId());
	      	respBody.addField("GLOBAL_ID", GLOBAL_ID);
	      	
	      	Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
	      	CLIENT_CODE.setValue(vo.getGlobalId());
	      	respBody.addField("CLIENT_CODE",CLIENT_CODE);
	      	
	      	Field TASK_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	      	TASK_TYPE.setValue(vo.getTaskType());
	      	respBody.addField("TASK_TYPE",TASK_TYPE);
	      	
	      	Field TASK_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	      	TASK_DATE.setValue(vo.getTaskCreateDate());
	      	respBody.addField("TASK_DATE",TASK_DATE);
	      	
	      	Field LOAN_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	LOAN_AMT.setValue(vo.getLoanTotlAmt());
	      	respBody.addField("LOAN_AMT",LOAN_AMT);
	      	
	      	Field LOAN_BALANCE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	LOAN_BALANCE.setValue(vo.getLoanBalance());
	      	respBody.addField("LOAN_BALANCE",LOAN_BALANCE);
	      	
	      	Field CNT = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	      	CNT.setValue(vo.getQnt() + "");
	      	respBody.addField("CNT",CNT);
	      	
	    	Field CHI_CUST_MANAGER = new Field(new FieldAttr(FieldType.FIELD_STRING,30));
	    	CHI_CUST_MANAGER.setValue(vo.getManagerId());
	      	respBody.addField("CHI_CUST_MANAGER",CHI_CUST_MANAGER);
	      	
	      	Field CHI_ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,30));
	      	CHI_ORG_NO.setValue(vo.getManagerBrId());
	      	respBody.addField("CHI_ORG_NO",CHI_ORG_NO);
	      	
	      	Field CHECK_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING,8));
	      	CHECK_DATE.setValue(vo.getCheckTime());
	      	respBody.addField("CHECK_DATE",CHECK_DATE);
	      	
	    	Field CHECK_POSITION = new Field(new FieldAttr(FieldType.FIELD_STRING,1));
	    	CHECK_POSITION.setValue(vo.getCheckAddr());
	      	respBody.addField("CHECK_POSITION",CHECK_POSITION);
	      	
	      	Field HELPERS = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
	      	HELPERS.setValue(vo.getAgreedPerson());
	      	respBody.addField("HELPERS",HELPERS);
	      	
	      	Field CONTACT_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING,100));
	      	CONTACT_MODE.setValue(vo.getContactInformation());
	      	respBody.addField("CONTACT_MODE",CONTACT_MODE);
	      	
	      	Field LOAN_REPAY_MSG = new Field(new FieldAttr(FieldType.FIELD_STRING,100));
	      	LOAN_REPAY_MSG.setValue(vo.getRepayment());
	      	respBody.addField("LOAN_REPAY_MSG",LOAN_REPAY_MSG);
	      	
	      	Field REMARK = new Field(new FieldAttr(FieldType.FIELD_STRING,300));
	      	REMARK.setValue(vo.getRepaymentOther());
	      	respBody.addField("REMARK",REMARK);
	      	
	      	Field CHECK_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,1));
	      	CHECK_TYPE.setValue(vo.getReciprocalType());
	      	respBody.addField("CHECK_TYPE",CHECK_TYPE);
	      	
	      	Field RISK_LEVEL = new Field(new FieldAttr(FieldType.FIELD_STRING,6));
	      	RISK_LEVEL.setValue(vo.getCheckDescribe());
	      	respBody.addField("RISK_LEVEL",RISK_LEVEL);
	      	
	      	Field CHECK_RESULT = new Field(new FieldAttr(FieldType.FIELD_STRING,100));
	      	CHECK_RESULT.setValue(vo.getRemarks());
	      	respBody.addField("CHECK_RESULT",CHECK_RESULT);
	      	
	      	Field UPLOAD_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,1));
	      	UPLOAD_FLAG.setValue(vo.getUploadFlag());
	      	respBody.addField("UPLOAD_FLAG",UPLOAD_FLAG);
        }
    	return respBody;
    }
    
    //ipad 微贷数据字典查询
    public CompositeData ipadDictInfo(CompositeData BODY) throws ParseException, IpadException {
    	//英文字段名 以","隔开
    	String  EN_FIELD_NAME = BODY.getField("EN_FIELD_NAME").strValue();
    	if(StringUtils.isEmpty(EN_FIELD_NAME)){
    		//EN_FIELD_NAME =	"CERT_TYPE,CLIENT_NAME_TYPE,CLIENT_TYPE,CLIENT_STATUS,INDIV_SEX,CUS_COUNTRY,INDIV_NTN,MARITAL_STATUS,EDUCATION_LEVEL,CUS_BANK_REL,IDENTITY_TYPE,ADDRESS_TYPE,CONTACT_MODE_TYPE,OCCUPATION,LOAN_STATUS,GLOBAL_TYPE_WD,SEX_WD,HAVE_COPY,EDUCATION_LEVEL_WD,PROFIT_TYPE,MARITAL_STATUS_WD,GLOBAL_TYPE_WD,ADDRESS_TYPE_NEW,ADDRESS_TYPE_NEW,ADDRESS_TYPE_NEW,BUSS_TYPE_NEW,UNIT_TYPE,USR_SITUATION,REPAYMENT_TERM,INFO_TYPE,TASK_TYPE,reciprocalType,repayment_type,cusRisk";
    		EN_FIELD_NAME =	"CERT_TYPE,CLIENT_NAME_TYPE,CLIENT_TYPE,CLIENT_STATUS,"
    				+ "INDIV_SEX,MARITAL_STATUS,EDUCATION_LEVEL,CUS_BANK_REL,IDENTITY_TYPE,"
    				+ "ADDRESS_TYPE,CONTACT_MODE_TYPE,OCCUPATION,LOAN_STATUS,GLOBAL_TYPE_WD,"
    				+ "SEX_WD,HAVE_COPY,EDUCATION_LEVEL_WD,PROFIT_TYPE,MARITAL_STATUS_WD,"
    				+ "GLOBAL_TYPE_WD,ADDRESS_TYPE_NEW,ADDRESS_TYPE_NEW,ADDRESS_TYPE_NEW,"
    				+ "BUSS_TYPE_NEW,UNIT_TYPE,USR_SITUATION,REPAYMENT_TERM,INFO_TYPE,"
    				+ "TASK_TYPE,reciprocalType,repayment_type,cusRisk,BUSS_TYPE_WD";
    	}
    	//
    	String  FIELD_NAME[] = EN_FIELD_NAME.split(",");
    	
    	//返回结果
        CompositeData respBody = new CompositeData();
    	Array array = new Array();
		//读取数据字典
    	for(int i = 0;i < FIELD_NAME.length;i++){
			DictionaryManager dictMgr = Beans.get(DictionaryManager.class);
	        Dictionary dictionary = dictMgr.getDictionaryByName(FIELD_NAME[i]);//FIELD_NAME[i]具体数据字典字段
	        if(dictionary!= null){
	        List<DictionaryItem> dictItems = dictionary.getItems();
	        
				for(DictionaryItem item : dictItems){
					CompositeData struct = new CompositeData();
					
					Field enFileName = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
					enFileName.setValue(FIELD_NAME[i]);
					struct.addField("EN_FIELD_NAME", enFileName);
					
					Field FIELD_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
					FIELD_CODE.setValue(item.getName());
					struct.addField("FIELD_CODE", FIELD_CODE);
					
					Field CN_FIELD_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
					CN_FIELD_NAME.setValue(item.getTitle());
					struct.addField("CN_FIELD_NAME", CN_FIELD_NAME);
	
					array.addStruct(struct);
				}
	        }
    	}
      	respBody.addArray("FIELD_INFO_ARRAY", array);
      	return respBody;
    }
    
    //ipad 微贷专案信息查询
    public CompositeData ipadZaInfo(CompositeData BODY) throws ParseException, IpadException {
    	//返回结果
    	List<QzApplnZa> zaList  = customerInforDao.ipadApplyZaList();
    	CompositeData respBody = new CompositeData();
    	
     	Array array = new Array();
       	for(QzApplnZa za :zaList){
       		CompositeData struct = new CompositeData();
      		
      		Field PROJECT_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
      		PROJECT_NO.setValue(za.getId());
			struct.addField("PROJECT_NO", PROJECT_NO);
			
			Field PROJECT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			PROJECT_NAME.setValue(za.getName());
			struct.addField("PROJECT_NAME", PROJECT_NAME);
			
			Field PROJECT_ADDR = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
			PROJECT_ADDR.setValue(za.getAddress());
			struct.addField("PROJECT_ADDR", PROJECT_ADDR);
			
			Field CREATION_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			CREATION_DATE.setValue(sdf.format(za.getInitDate()));
			struct.addField("CREATION_DATE", CREATION_DATE);
			
			Field SPONSOR_ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			SPONSOR_ORG_NO.setValue(za.getOriginator());
			struct.addField("SPONSOR_ORG_NO", SPONSOR_ORG_NO);
			
			Field PRODUCT_SPONSOR = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			PRODUCT_SPONSOR.setValue(za.getInitiator());
			struct.addField("PRODUCT_SPONSOR", PRODUCT_SPONSOR);
					
			Field SUGGESTION = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
			SUGGESTION.setValue(za.getSug());
			struct.addField("SUGGESTION", SUGGESTION);
			
			array.addStruct(struct);
       	}
       	respBody.addArray("PROJECT_INFO_ARRAY", array);
    	return respBody;
    }
    
    
    //ipad 微贷申请审批查询
    public CompositeData ipadWdspInfoList(CompositeData BODY) throws ParseException, IpadException {
    	//进件登记号
    	String  INPUT_WARE_REG_NO = BODY.getField("INPUT_WARE_REG_NO").strValue();
    	//客户代码
    	String CLIENT_CODE = BODY.getField("CLIENT_CODE").strValue();
    	//验证
    	//if(StringUtils.isEmpty(INPUT_WARE_REG_NO)){throw new IpadException("进件登记号必输!");}
    	
    	//查询进件信息
    	CustomerApplyInfoIpad applyInfo =  customerInforDao.Wdsqspbrowse(INPUT_WARE_REG_NO,CLIENT_CODE);
    	
		if(applyInfo.getStatus().equals(Constant.SAVE_INTOPICES)){
			applyInfo.setProcess("未提交申请");
		} else if(applyInfo.getStatus().equals(Constant.APPROVE_INTOPICES)||applyInfo.getStatus().equals(Constant.TRTURN_INTOPICES)){
			String nodeName = intoPiecesComdao.findAprroveProgress(applyInfo.getInputWareRegNo());
			if(StringUtils.isNotEmpty(nodeName)){
				applyInfo.setProcess(nodeName);
			} else {
				applyInfo.setProcess("不在审批中");
			}
		} else if(applyInfo.getStatus().equals(Constant.RETURN_INTOPICES)){
			applyInfo.setProcess("退回");
		} else {
			applyInfo.setProcess("审批结束");
		}
    	
    	//返回结果
    	CompositeData respBody = new CompositeData();
    	
    	//进件信息
    	Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
    	CLIENT_NAME.setValue(applyInfo.getClientName());
      	respBody.addField("CLIENT_NAME", CLIENT_NAME);
      	
      	Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
      	GLOBAL_ID.setValue(applyInfo.getGlobalId());
      	respBody.addField("GLOBAL_ID", GLOBAL_ID);
      	
    	Field PRODUCT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
    	PRODUCT_CODE.setValue(applyInfo.getProductCode());
      	respBody.addField("PRODUCT_CODE", PRODUCT_CODE);
      	
      	Field PRODUCT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
      	PRODUCT_NAME.setValue(applyInfo.getProductName());
      	respBody.addField("PRODUCT_NAME",PRODUCT_NAME);
      	
      	Field APPLY_LIMIT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
      	APPLY_LIMIT_AMT.setValue(applyInfo.getApplyLimitAmt());
      	respBody.addField("APPLY_LIMIT_AMT",APPLY_LIMIT_AMT);
      	
    	Field STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
    	STATUS.setValue(applyInfo.getStatus());
      	respBody.addField("STATUS",STATUS);
      	
      	Field PROCESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
      	PROCESS.setValue(applyInfo.getProcess());
      	respBody.addField("PROCESS",PROCESS);
      	
        //审批信息数组
      	List<ApproveHistoryForm>  ls = customerInforDao.ipadWdAuditInfo(INPUT_WARE_REG_NO);
    	Array auditArray = new Array();
		for(ApproveHistoryForm obj : ls ){
			CompositeData struct = new CompositeData();
			
			Field NODE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			NODE_NAME.setValue(obj.getStatusName());
			struct.addField("NODE_NAME", NODE_NAME);
			
			Field APPROVE_RESULT = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			APPROVE_RESULT.setValue(obj.getExamineResult());
			struct.addField("APPROVE_RESULT", APPROVE_RESULT);
			
			Field APPROVE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			APPROVE_NAME.setValue(obj.getDisplayName());
			struct.addField("APPROVE_NAME", APPROVE_NAME);
			
			Field APPROVE_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			APPROVE_DATE.setValue(sdf.format(obj.getStartExamineTime()));
			struct.addField("APPROVE_DATE", APPROVE_DATE);
			
			Field APPROVE_LIMIT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			APPROVE_LIMIT_AMT.setValue(obj.getExamineAmount()==null?Double.valueOf("0.0"):Double.valueOf(obj.getExamineAmount()));
			struct.addField("APPROVE_LIMIT_AMT", APPROVE_LIMIT_AMT);
			
			auditArray.addStruct(struct);
		}
		respBody.addArray("APPROVE_INFO_ARRAY", auditArray);
    	
    	//节点信息数组
		List<NodeAuditForm> nodeList = customerInforDao.ipadWdApplyProcess(applyInfo.getProductCode());
    	Array nodeArray = new Array();
    	for(NodeAuditForm node :nodeList){
			CompositeData struct = new CompositeData();
			
			Field NOTE_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			NOTE_CODE.setValue(node.getId());
			struct.addField("NOTE_CODE", NOTE_CODE);
			
			Field NODE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			NODE_NAME.setValue(node.getNodeName());
			struct.addField("NODE_NAME", NODE_NAME);
			
			Field START_NODE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			START_NODE_FLAG.setValue(node.getIsstart());
			struct.addField("START_NODE_FLAG", START_NODE_FLAG);
			
			Field END_NODE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			END_NODE_FLAG.setValue(node.getIsend());
			struct.addField("END_NODE_FLAG", END_NODE_FLAG);
			
			Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			SEQU_NO.setValue(node.getSeqNo()+"");
			struct.addField("SEQU_NO", SEQU_NO);
			
			nodeArray.addStruct(struct);
		}
		respBody.addArray("NOTE_INFO_ARRAY", nodeArray);
    	
    	return respBody;
    }
    
    
    //ipad 微贷进件信息查询
    public CompositeData ipadWdApplyInfoList(CompositeData BODY) throws ParseException, IpadException {
    	//进件登记号
    	String  INPUT_WARE_REG_NO = BODY.getField("INPUT_WARE_REG_NO").strValue();
    	//验证
    	if(StringUtils.isEmpty(INPUT_WARE_REG_NO)){throw new IpadException("进件登记号必输!");}
    	//返回结果
    	CompositeData respBody = new CompositeData();
    	
    	//贷款申请表 
    	QzApplnYwsqb qz = customerInforDao.findQzApplnYwsqb(INPUT_WARE_REG_NO);
    	
    	if(qz!=null){
	    	Field APP_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	    	APP_ID.setValue(qz.getApplicationId());
	      	respBody.addField("INPUT_WARE_REG_NO", APP_ID);
	      	
	    	Field USER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
	    	USER_NO.setValue(qz.getCustomerId());
	      	respBody.addField("USER_NO", USER_NO);
	      	
	      	Field APP_BRANCH_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	APP_BRANCH_NAME.setValue(qz.getOrgName());
	      	respBody.addField("APP_BRANCH_NAME", APP_BRANCH_NAME);
	      	
	    	Field APP_BRANCH_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	    	APP_BRANCH_ID.setValue(qz.getOrgId());
	      	respBody.addField("APP_BRANCH_ID", APP_BRANCH_ID);
	      	
	      	Field APPLY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	      	APPLY_DATE.setValue(sdf.format(qz.getApplyTime()));
	      	respBody.addField("APPLY_DATE", APPLY_DATE);
	      	
	      	Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	CLIENT_NAME.setValue(qz.getName());
	      	respBody.addField("CLIENT_NAME", CLIENT_NAME);
	      	
	    	Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	    	SEX.setValue(qz.getSex());
	      	respBody.addField("SEX", SEX);
	      	
	      	Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	GLOBAL_ID.setValue(qz.getGlobalId());
	      	respBody.addField("GLOBAL_ID", GLOBAL_ID);
	      	
	      	Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
	      	GLOBAL_TYPE.setValue(qz.getGlobalType());
	      	respBody.addField("GLOBAL_TYPE", GLOBAL_TYPE);
	      	
	      	Field REMARK = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	      	REMARK.setValue(qz.getGlobalTypeOther());
	      	respBody.addField("REMARK", REMARK);
	      	
	      	Field EDUCATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	      	EDUCATION.setValue(qz.getEducationLevel());
	      	respBody.addField("EDUCATION",EDUCATION);
	      	
	      	Field REMARK1 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	      	REMARK1.setValue(qz.getEducationLevelOther());
	      	respBody.addField("REMARK1",REMARK1);
	      	
	      	Field MARRIAGE_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
	      	MARRIAGE_STATUS.setValue(qz.getMaritalStatus());
	      	respBody.addField("MARRIAGE_STATUS",MARRIAGE_STATUS);
	      	
	    	Field REMARK2 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	    	REMARK2.setValue(qz.getMaritalStatusOther());
	      	respBody.addField("REMARK2",REMARK2);
	      	
	      	Field MATE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	MATE_NAME.setValue(qz.getMaritalName());
	      	respBody.addField("MATE_NAME",MATE_NAME);
	      	
	      	Field MATE_GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
	      	MATE_GLOBAL_TYPE.setValue(qz.getMaritalGlobalType());
	      	respBody.addField("MATE_GLOBAL_TYPE",MATE_GLOBAL_TYPE);
	      	
	    	Field REMARK5 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	    	REMARK5.setValue(qz.getMaritalGlobalTypeOther());
	      	respBody.addField("REMARK5",REMARK5);
	      	
	      	Field MATE_GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 25));
	      	MATE_GLOBAL_ID.setValue(qz.getMaritalGlobalId());
	      	respBody.addField("MATE_GLOBAL_ID",MATE_GLOBAL_ID);
	      	
	      	Field MATE_COMPANY = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	MATE_COMPANY.setValue(qz.getMaritalWorkunit());
	      	respBody.addField("MATE_COMPANY",MATE_COMPANY);
	      	
	      	Field MATE_TELEPHONE_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	MATE_TELEPHONE_NO.setValue(qz.getMaritalPhone());
	      	respBody.addField("MATE_TELEPHONE_NO",MATE_TELEPHONE_NO);
	      	
	      	Field HOME_NUM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	      	HOME_NUM.setValue(qz.getFamilyNum());
	      	respBody.addField("HOME_NUM",HOME_NUM);
	      	
	      	Field HOME_ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	      	HOME_ADDRESS_TYPE.setValue(qz.getAddressType());
	      	respBody.addField("HOME_ADDRESS_TYPE",HOME_ADDRESS_TYPE);
	      	
	    	Field REMARK3 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	    	REMARK3.setValue(qz.getAddressTypeOther());
	      	respBody.addField("REMARK3",REMARK3);
	      	
	      	Field HOME_ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
	      	HOME_ADDRESS.setValue(qz.getAddress());
	      	respBody.addField("HOME_ADDRESS",HOME_ADDRESS);
	      	
	      	Field MOBILE = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	MOBILE.setValue(qz.getPhone_1());
	      	respBody.addField("MOBILE",MOBILE);
	      	
	    	Field MOBILE2 = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	MOBILE2.setValue(qz.getPhone_2());
	      	respBody.addField("MOBILE2",MOBILE2);
	      	
	      	Field PHONE_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	PHONE_NO.setValue(qz.getHomePhone());
	      	respBody.addField("PHONE_NO",PHONE_NO);
	      	
	      	
	       	Field LOAN_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	LOAN_AMT.setValue(qz.getApplyAmount()==null?Double.valueOf("0.0"):Double.valueOf(qz.getApplyAmount()));
	      	respBody.addField("LOAN_AMT",LOAN_AMT);
	      	
	      	Field LOAN_TERM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	      	LOAN_TERM.setValue(qz.getApplyDeadline());
	      	respBody.addField("LOAN_TERM",LOAN_TERM);
	      	
	    	Field LOAN_PURPUSE_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	    	LOAN_PURPUSE_CODE.setValue(qz.getApplyUse());
	      	respBody.addField("LOAN_PURPUSE_CODE",LOAN_PURPUSE_CODE);
	      	
	      	Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	      	EXPIRY_DATE.setValue(sdf.format(qz.getApplyEndTime()));
	      	respBody.addField("EXPIRY_DATE",EXPIRY_DATE);
	      	
	      	Field MONTH_REPAY = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	MONTH_REPAY.setValue(qz.getMonthRepay()==null?Double.valueOf("0.0"):Double.valueOf(qz.getMonthRepay()));
	      	respBody.addField("MONTH_REPAY",MONTH_REPAY);
	      	
	      	Field PLEDGE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	PLEDGE_NAME.setValue(qz.getGuntThing());
	      	respBody.addField("PLEDGE_NAME",PLEDGE_NAME);
	      	
	      	Field GURTER_ACCT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	      	GURTER_ACCT_NAME.setValue(qz.getGuntPeople());
	      	respBody.addField("GURTER_ACCT_NAME",GURTER_ACCT_NAME);
	      	
	      	Field LICENSE_INCOME = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	LICENSE_INCOME.setValue(qz.getYearIncome()==null?Double.valueOf("0.0"):Double.valueOf(qz.getYearIncome()));
	      	respBody.addField("LICENSE_INCOME",LICENSE_INCOME);
	      	
	      	Field PROFIT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
	      	PROFIT_TYPE.setValue(qz.getProfitType());
	      	respBody.addField("PROFIT_TYPE",PROFIT_TYPE);
	      	
	      	Field PROFIT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	PROFIT_AMT.setValue(qz.getProfit()==null?Double.valueOf("0.0"):Double.valueOf(qz.getProfit()));
	      	respBody.addField("PROFIT_AMT",PROFIT_AMT);
	      	
	      	Field ASSET_TOTAL_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	ASSET_TOTAL_AMT.setValue(qz.getTotalAssets()==null?Double.valueOf("0.0"):Double.valueOf(qz.getTotalAssets()));
	      	respBody.addField("ASSET_TOTAL_AMT",ASSET_TOTAL_AMT);
	      	
	      	Field OUGHT_ACCEPT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	OUGHT_ACCEPT_AMT.setValue(qz.getInMoney()==null?Double.valueOf("0.0"):Double.valueOf(qz.getInMoney()));
	      	respBody.addField("OUGHT_ACCEPT_AMT",OUGHT_ACCEPT_AMT);
	
	    	Field OUGHT_PAY_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	    	OUGHT_PAY_AMT.setValue(qz.getOutMoney()==null?Double.valueOf("0.0"):Double.valueOf(qz.getOutMoney()));
	      	respBody.addField("OUGHT_PAY_AMT",OUGHT_PAY_AMT);
	      	
	      	Field LIBILITY = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	LIBILITY.setValue(qz.getOtherOut()==null?Double.valueOf("0.0"):Double.valueOf(qz.getOtherOut()));
	      	respBody.addField("LIBILITY",LIBILITY);
	      	
	      	Field OTHER_INCOME = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
	      	OTHER_INCOME.setValue(qz.getMonthOtherIncome()==null?Double.valueOf("0.0"):Double.valueOf(qz.getMonthOtherIncome()));
	      	respBody.addField("OTHER_INCOME",OTHER_INCOME);
	      	
	      	Field OTHER_SOURCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	OTHER_SOURCE.setValue(qz.getOtherIncomeSrc());
	      	respBody.addField("OTHER_SOURCE",OTHER_SOURCE);
	      	
	      	
	      //贷款申请表
			Field PLEDGE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			PLEDGE_FLAG.setValue(qz.getHaveGunt());
	      	respBody.addField("PLEDGE_FLAG",PLEDGE_FLAG);
	      	
	      	Field PLEDGE_NAME1 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	PLEDGE_NAME1.setValue(qz.getThing_1());
	      	respBody.addField("PLEDGE_NAME1",PLEDGE_NAME1);
	      	
	      	Field PLEDGE_NAME2 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	PLEDGE_NAME2.setValue(qz.getThing_2());
	      	respBody.addField("PLEDGE_NAME2",PLEDGE_NAME2);
	      	
	      	Field PLEDGE_NAME3 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	PLEDGE_NAME3.setValue(qz.getThing_3());
	      	respBody.addField("PLEDGE_NAME3",PLEDGE_NAME3);
	      	
	      	Field PLEDGE_NAME4 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	PLEDGE_NAME4.setValue(qz.getThing_4());
	      	respBody.addField("PLEDGE_NAME4",PLEDGE_NAME4);
	      	
	      	Field LOAN_USER1 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	LOAN_USER1.setValue(qz.getLoanUser_1());
	      	respBody.addField("LOAN_USER1",LOAN_USER1);
	      	
	      	Field LOAN_USER2 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	LOAN_USER2.setValue(qz.getLoanUser_2());
	      	respBody.addField("LOAN_USER2",LOAN_USER2);
	      	
	      	Field LOAN_USER3 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	LOAN_USER3.setValue(qz.getLoanUser_3());
	      	respBody.addField("LOAN_USER3",LOAN_USER3);
	      	
	      	Field LOAN_USER4 = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	LOAN_USER4.setValue(qz.getLoanUser_4());
	      	respBody.addField("LOAN_USER42",LOAN_USER4);
	      	
	      	Field GNT_OTHER_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	      	GNT_OTHER_FLAG.setValue(qz.getGuntForOther());
	      	respBody.addField("GNT_OTHER_FLAG",GNT_OTHER_FLAG);
	      	
	    	Field GNT_OTHER_BANK = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
	    	GNT_OTHER_BANK.setValue(qz.getGuntForOtherBankname());
	      	respBody.addField("GNT_OTHER_BANK",GNT_OTHER_BANK);
	      	
	      	Field GNT_OTHERTNAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
	      	GNT_OTHERTNAME.setValue(qz.getGuntForOtherClientname());
	      	respBody.addField("GNT_OTHERTNAME",GNT_OTHERTNAME);
	      	
	      	Field APPLY_LOAN_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	      	APPLY_LOAN_FLAG.setValue(qz.getHaveApplyLoan());
	      	respBody.addField("APPLY_LOAN_FLAG",APPLY_LOAN_FLAG);
	      	
	      	Field DR_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	      	DR_DATE.setValue(qz.getHaveLoanTime()==null?null:sdf.format(qz.getHaveLoanTime()));
	      	respBody.addField("DR_DATE",DR_DATE);
	      	
	    	Field PRODUCT_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	    	PRODUCT_FLAG.setValue(qz.getHaveElePro());
	      	respBody.addField("PRODUCT_FLAG",PRODUCT_FLAG);
	      	
	      	Field PRODUCT_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	      	PRODUCT_NO.setValue(qz.getHaveEleProType());
	      	respBody.addField("PRODUCT_NO",PRODUCT_NO);
	      	
	      	Field RELATION_LOAN_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
	      	RELATION_LOAN_FLAG.setValue(qz.getHaveGotLoan());
	      	respBody.addField("RELATION_LOAN_FLAG",RELATION_LOAN_FLAG);
	      	
	    	Field DR_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
	    	DR_NAME.setValue(qz.getHaveGotLoanName());
	      	respBody.addField("DR_NAME",DR_NAME);
	      	
	      	Field RELATION_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	      	RELATION_TYPE.setValue(qz.getHaveGotLoanRelation());
	      	respBody.addField("RELATION_TYPE",RELATION_TYPE);
	      	
	      	Field SIGN = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
	      	SIGN.setValue(qz.getSign());
	      	respBody.addField("SIGN",SIGN);
	      	
	    	Field SIGN_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING,8));
	    	SIGN_DATE.setValue(sdf.format(qz.getSignDate()));
	      	respBody.addField("SIGN_DATE",SIGN_DATE);
	      	
	      	Field INFO_SOURCE_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,1));
	      	INFO_SOURCE_TYPE.setValue(qz.getInfoType());
	      	respBody.addField("INFO_SOURCE_TYPE",INFO_SOURCE_TYPE);
	      	
	      	Field INFO_TYPE_OTHER = new Field(new FieldAttr(FieldType.FIELD_STRING,100));
	      	INFO_TYPE_OTHER.setValue(qz.getInfoTypeOther());
	      	respBody.addField("INFO_TYPE_OTHER",INFO_TYPE_OTHER);
	      	
	      	Field COMMET = new Field(new FieldAttr(FieldType.FIELD_STRING,200));
	      	COMMET.setValue(qz.getCommet());
	      	respBody.addField("COMMET",COMMET);
	      	
	    	Field APP_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,20));
	    	APP_NO.setValue(qz.getManagerId());
	      	respBody.addField("APP_NO",APP_NO);
	      	
	      	Field APP_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING,150));
	      	APP_NAME.setValue(qz.getManagerName());
	      	respBody.addField("APP_NAME",APP_NAME);
	      	
	      	Field APP_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING,8));
	      	APP_DATE.setValue(sdf.format(qz.getManagerTime()));
	      	respBody.addField("APP_DATE",APP_DATE);
	      	
	        //贷款申请表
			
			Field EMAIL = new Field(new FieldAttr(FieldType.FIELD_STRING,150));
			EMAIL.setValue(qz.getEmail());
	      	respBody.addField("EMAIL",EMAIL);
	      	
	      	Field COMPANY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING,150));
	      	COMPANY_NAME.setValue(qz.getUnit());
	      	respBody.addField("COMPANY_NAME",COMPANY_NAME);
	      	
	      	Field INDUSTRY = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
	      	INDUSTRY.setValue(qz.getProfession());
	      	respBody.addField("INDUSTRY",INDUSTRY);
	      	
	      	Field OCCUPATION = new Field(new FieldAttr(FieldType.FIELD_STRING,30));
	      	OCCUPATION.setValue(qz.getOccupation());
	      	respBody.addField("OCCUPATION",OCCUPATION);
	      	

	      	Field VOCATION_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
	      	VOCATION_NAME.setValue(qz.getJobPost());
	      	respBody.addField("VOCATION_NAME",VOCATION_NAME);
	      	
	      	Field TITLE_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
	      	TITLE_CODE.setValue(qz.getPositionTitle());
	      	respBody.addField("TITLE_CODE",TITLE_CODE);
	      	
	      	Field COMMPANY_ADDR = new Field(new FieldAttr(FieldType.FIELD_STRING,200));
	      	COMMPANY_ADDR.setValue(qz.getUnitAddress());
	      	respBody.addField("COMMPANY_ADDR",COMMPANY_ADDR);
	      	
	    	Field WORK_YEAR = new Field(new FieldAttr(FieldType.FIELD_STRING,4));
	    	WORK_YEAR.setValue(qz.getWorkYear());
	      	respBody.addField("WORK_YEAR",WORK_YEAR);
	      	
	      	Field COMMPANY_PHONE = new Field(new FieldAttr(FieldType.FIELD_STRING,30));
	      	COMMPANY_PHONE.setValue(qz.getUnitPhone());
	      	respBody.addField("COMMPANY_PHONE",COMMPANY_PHONE);
	      	
	     	Field CORP_SCALE = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
	     	CORP_SCALE.setValue(qz.getCompanySize());
	      	respBody.addField("CORP_SCALE",CORP_SCALE);
	      	
	     	Field CORP_ORG_FORM = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
	     	CORP_ORG_FORM.setValue(qz.getUnitType());
	      	respBody.addField("CORP_ORG_FORM",CORP_ORG_FORM);
	      	
	     	Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING,300));
	     	ADDRESS.setValue(qz.getBussdistrictAddress());
	      	respBody.addField("ADDRESS",ADDRESS);
    	}
    	
      	//经营信息
      	QzApplnJyxx  jyxx = customerInforDao.findQzApplJyxx(INPUT_WARE_REG_NO);
      	if(jyxx!= null){
	      	Field BUSINESS_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	      	BUSINESS_NAME.setValue(jyxx.getBussName());
	      	respBody.addField("BUSINESS_NAME",BUSINESS_NAME);
	      	
	      	Field BUSINESS_SCOPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
	      	BUSINESS_SCOPE.setValue(jyxx.getBussScope());
	      	respBody.addField("BUSINESS_SCOPE",BUSINESS_SCOPE);
	      	
	      	Field OPEN_TIME = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
	      	OPEN_TIME.setValue(jyxx.getBussStartYear());
	      	respBody.addField("OPEN_TIME",OPEN_TIME);
	      	
	    	Field BSN_ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	    	BSN_ADDRESS_TYPE.setValue(jyxx.getBussAddrType());
	      	respBody.addField("BSN_ADDRESS_TYPE",BSN_ADDRESS_TYPE);
	      	
	      	Field REMARK6 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	      	REMARK6.setValue(jyxx.getBussAddrTypeOther());
	      	respBody.addField("REMARK6",REMARK6);
	      	
	      	Field BSN_ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
	      	BSN_ADDRESS.setValue(jyxx.getBussAddr());
	      	respBody.addField("BSN_ADDRESS",BSN_ADDRESS);
	      	
	      	Field WORK_PHONE = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	      	WORK_PHONE.setValue(jyxx.getBussPhone());
	      	respBody.addField("WORK_PHONE",WORK_PHONE);
	      	
	      	Field EMPLOYEE_NUMBER = new Field(new FieldAttr(FieldType.FIELD_INT, 10));
	      	EMPLOYEE_NUMBER.setValue(jyxx.getBussEmployeeNum());
	      	respBody.addField("EMPLOYEE_NUMBER",EMPLOYEE_NUMBER);
	      	
	      	Field ORG_FORM = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
	      	ORG_FORM.setValue(jyxx.getBussType());
	      	respBody.addField("ORG_FORM",ORG_FORM);
	      	
	      	Field REMARK4 = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	      	REMARK4.setValue(jyxx.getBussTypeOther());
	      	respBody.addField("REMARK4",REMARK4);
      	}
      	
   
      	

		//供应商数组
      	Array gylist = new Array();
		List<QzApplnYwsqbZygys> gysList =  customerInforDao.findGys(qz.getId());
		for(QzApplnYwsqbZygys gy :gysList){
			CompositeData struct = new CompositeData();
			
			Field SUPPLIER_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			SUPPLIER_NAME.setValue(gy.getName());
			struct.addField("SUPPLIER_NAME", SUPPLIER_NAME);
			
			Field LOT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOT.setValue(gy.getRate()==null?Double.valueOf("0.0"):Double.valueOf(gy.getRate()));
			struct.addField("LOT", LOT);
			
			Field DRAW_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			DRAW_TYPE.setValue(gy.getCondition());
			struct.addField("DRAW_TYPE", DRAW_TYPE);
			
			gylist.addStruct(struct);
		}
		respBody.addArray("SUPPLIER_INFO_ARRAY", gylist);
      	
		//客户
		Array khlist = new Array();
		List<QzApplnYwsqbZykh> KhsList = customerInforDao.findKh(qz.getId());
		for(QzApplnYwsqbZykh kh :KhsList){
			CompositeData struct = new CompositeData();
			
			Field clientName = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			clientName.setValue(kh.getName());
			struct.addField("CLIENT_NAME", clientName);
			
			Field LOT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOT.setValue(kh.getRate()==null?Double.valueOf("0.0"):Double.valueOf(kh.getRate()));
			struct.addField("LOT", LOT);
			
			Field DRAW_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			DRAW_TYPE.setValue(kh.getCondition());
			struct.addField("DRAW_TYPE", DRAW_TYPE);
			
			Field LOAN_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			DRAW_TYPE.setValue("");//TODO
			struct.addField("LOAN_FLAG", LOAN_FLAG);
			
			khlist.addStruct(struct);
		}
		respBody.addArray("CLIENT_ARRAY", khlist);
		
		//贷款信息明细
		Array mx = new Array();
		List<QzApplnYwsqbJkjl> jkjlList = customerInforDao.findDk(qz.getId());
		for(QzApplnYwsqbJkjl jkjl :jkjlList){
			CompositeData struct = new CompositeData();
			
			Field LOAN_SOURCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			LOAN_SOURCE.setValue(jkjl.getBankOrOtherType());
			struct.addField("LOAN_SOURCE", LOAN_SOURCE);
			
			Field loanPurpuseCode  = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			loanPurpuseCode.setValue(jkjl.getPurpose());
			struct.addField("LOAN_PURPUSE_CODE", loanPurpuseCode);
			
			Field AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			AMT.setValue(jkjl.getTotalAmount()==null?Double.valueOf("0.0"):Double.valueOf(jkjl.getTotalAmount()));
			struct.addField("AMT", AMT);
			
			Field DRAW_DOWN_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			DRAW_DOWN_DATE.setValue(sdf.format(jkjl.getLoanDate()));
			struct.addField("DRAW_DOWN_DATE", DRAW_DOWN_DATE);
			
			Field TERM = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			TERM.setValue(jkjl.getDeadline());
			struct.addField("TERM", TERM);
			
			Field INT_RATE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
			INT_RATE.setValue(jkjl.getRates()==null?Double.valueOf("0.0"):Double.valueOf(jkjl.getRates()));
			struct.addField("INT_RATE", INT_RATE);
			
			Field REPAY_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			REPAY_TYPE.setValue(jkjl.getRepayType());
			struct.addField("REPAY_TYPE", REPAY_TYPE);
			
			Field GUARANTEE_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			GUARANTEE_MODE.setValue(jkjl.getGuaranteeMode());
			struct.addField("GUARANTEE_MODE", GUARANTEE_MODE);
			
			Field LOAN_BALANCE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			LOAN_BALANCE.setValue(jkjl.getRemainSum()==null?Double.valueOf("0.0"):Double.valueOf(jkjl.getRemainSum()));
			struct.addField("LOAN_BALANCE", LOAN_BALANCE);
			
			mx.addStruct(struct);
		}
		respBody.addArray("LOAN_MSG_INFO_ARRAY", mx);
		
		


		//担保人
    	Array dbrarray = new Array();
		List<QzApplnDbrxx> dbrList = customerInforDao.findDbrxx(INPUT_WARE_REG_NO);
		for(QzApplnDbrxx dbrxx :dbrList){
			CompositeData struct = new CompositeData();
			
			Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			SEQU_NO.setValue(dbrxx.getSeqNo());
			struct.addField("SEQU_NO", SEQU_NO);
			
			Field GUARANTEE_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
			GUARANTEE_NAME.setValue(dbrxx.getName());
			struct.addField("GUARANTEE_NAME", GUARANTEE_NAME);
			
			Field sex = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
			sex.setValue(dbrxx.getSex());
			struct.addField("SEX",sex);
			
			Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			BIRTH_DATE.setValue(sdf.format(dbrxx.getBirthday()));
			struct.addField("BIRTH_DATE",BIRTH_DATE);

			Field RESIDENT_YEAR = new Field(new FieldAttr(FieldType.FIELD_STRING, 4));
			RESIDENT_YEAR.setValue(dbrxx.getYeah());
			struct.addField("RESIDENT_YEAR",RESIDENT_YEAR);
			
			Field globalType = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			globalType.setValue(dbrxx.getGlobalType());
			struct.addField("GLOBAL_TYPE",globalType);
			
			Field globalId = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			globalId.setValue(dbrxx.getGlobalId());
			struct.addField("GLOBAL_ID",globalId);
			
			Field HARDCOY_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			HARDCOY_FLAG.setValue(dbrxx.getHaveGlobalIdCopy());
			struct.addField("HARDCOY_FLAG",HARDCOY_FLAG);
			
			
			Field mobile = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			mobile.setValue(dbrxx.getPhone_1());
			struct.addField("MOBILE",mobile);
			
			Field mobile2 = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			mobile2.setValue(dbrxx.getPhone_2());
			struct.addField("MOBILE2",mobile2);
			
			Field MOBILE3 = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			MOBILE3.setValue(dbrxx.getPhoneOther());
			struct.addField("MOBILE3",MOBILE3);
			
			Field COMMPANY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			COMMPANY_NAME.setValue(dbrxx.getWorkUnitName());
			struct.addField("COMMPANY_NAME",COMMPANY_NAME);
			
			Field WORK_YEAR = new Field(new FieldAttr(FieldType.FIELD_STRING, 4));
			WORK_YEAR.setValue(dbrxx.getWorkYear());
			struct.addField("WORK_YEAR",WORK_YEAR);
			
			Field COMMPANY_ADDR = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
			COMMPANY_ADDR.setValue(dbrxx.getWorkUnitAddr());
			struct.addField("COMMPANY_ADDR",COMMPANY_ADDR);
			
			Field COMMPANY_PHONE = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			COMMPANY_PHONE.setValue(dbrxx.getWorkUnitPhone());
			struct.addField("COMMPANY_ADDR",COMMPANY_PHONE);
			
			Field PERSON_ANNUAL_INCOME = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20));
			PERSON_ANNUAL_INCOME.setValue(dbrxx.getYeahIncome()==null?Double.valueOf("0.0"):Double.valueOf(dbrxx.getYeahIncome()));
			struct.addField("PERSON_ANNUAL_INCOME",PERSON_ANNUAL_INCOME);
			
			Field FILE_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
			FILE_FLAG.setValue(dbrxx.getHaveProveFile());
			struct.addField("FILE_FLAG",FILE_FLAG);
			
			Field FILES_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			FILES_NAME.setValue(dbrxx.getFileName());
			struct.addField("FILES_NAME",FILES_NAME);
			
			
			Field MARITAL_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING,2));
			MARITAL_STATUS.setValue(dbrxx.getMarriage());
			struct.addField("MARITAL_STATUS",MARITAL_STATUS);
			
			Field HOUSE_PHONE = new Field(new FieldAttr(FieldType.FIELD_STRING,30));
			HOUSE_PHONE.setValue(dbrxx.getHomePhone());
			struct.addField("HOUSE_PHONE",HOUSE_PHONE);
			
			Field HOME_ADDR = new Field(new FieldAttr(FieldType.FIELD_STRING,150));
			HOME_ADDR.setValue(dbrxx.getHomeAddr());
			struct.addField("HOME_ADDR",HOME_ADDR);
			
			Field HOME_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,5));
			HOME_TYPE.setValue(dbrxx.getHomeType());
			struct.addField("HOME_TYPE",HOME_TYPE);
			
			dbrarray.addStruct(struct);
		}
		    respBody.addArray("GUARANTEE_INFO_ARRAY", dbrarray);
		
		//贷款信息
	    for(QzApplnDbrxx dbrxx :dbrList){
	    	Array loan = new Array();
			List<QzApplnDbrxxDkjl> dkjList = customerInforDao.findDbrxxDkjl(dbrxx.getId());
			for(QzApplnDbrxxDkjl dk:dkjList){
				CompositeData struct = new CompositeData();
				
				Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
				SEQU_NO.setValue(dk.getSeqNo());
				struct.addField("SEQU_NO",SEQU_NO);
				
				Field FUND_SOURCE = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
				FUND_SOURCE.setValue(dk.getLoanType());
				struct.addField("FUND_SOURCE",FUND_SOURCE);
				
				Field loanAmt = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				loanAmt.setValue(dk.getAmount()==null?Double.valueOf("0.0"):Double.valueOf(dk.getAmount()));
				struct.addField("LOAN_AMT",loanAmt);
				
				Field TERM = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
				TERM.setValue(dk.getDeadline());
				struct.addField("TERM",TERM);
				
				Field DRAW_DOWN_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING,8));
				DRAW_DOWN_DATE.setValue(sdf.format(dk.getLoanDate()));
				struct.addField("DRAW_DOWN_DATE",DRAW_DOWN_DATE);
				
				Field USAGE = new Field(new FieldAttr(FieldType.FIELD_STRING,200));
				USAGE.setValue(dk.getPurpose());
				struct.addField("USAGE",USAGE);
				
				Field LOAN_BALANCE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				LOAN_BALANCE.setValue(dk.getRemainAmount()==null?Double.valueOf("0.0"):Double.valueOf(dk.getRemainAmount()));
				struct.addField("LOAN_BALANCE",LOAN_BALANCE);
				
				Field GNT_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,3));
				GNT_FLAG.setValue(dk.getGuntForOther());
				struct.addField("GNT_FLAG",GNT_FLAG);
				
				Field GNT_AMT = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				GNT_AMT.setValue(dk.getGuntAmount()==null?Double.valueOf("0.0"):Double.valueOf(dk.getGuntAmount()));
				struct.addField("GNT_AMT",GNT_AMT);
				
				Field GNT_TERM = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				GNT_TERM.setValue(dk.getGuntDeadline()==null?Double.valueOf("0.0"):Double.valueOf(dk.getGuntDeadline()));
				struct.addField("GNT_TERM",GNT_TERM);
				
				loan.addStruct(struct);

			}
			respBody.addArray("LOAN_MSG_ARRAY", loan);
			
			//房产
			Array fc = new Array();
			List<QzApplnDbrxxFc> fcList = customerInforDao.findFcList(dbrxx.getId());
			for(QzApplnDbrxxFc f :fcList){
				CompositeData struct = new CompositeData();
				
				Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
				SEQU_NO.setValue(f.getSeqNo());
				struct.addField("SEQU_NO",SEQU_NO);
				
				Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING,300));
				ADDRESS.setValue(f.getAddr());
				struct.addField("ADDRESS",ADDRESS);
				
				Field COST = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				COST.setValue(f.getPrice()==null?Double.valueOf("0.0"):Double.valueOf(f.getPrice()));
				struct.addField("COST",COST);
				
				Field USER_INFO = new Field(new FieldAttr(FieldType.FIELD_STRING,300));
				USER_INFO.setValue(f.getUsrSituation());
				struct.addField("USER_INFO",USER_INFO);
				
				Field HARDCOY_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,3));
				HARDCOY_FLAG.setValue(f.getHaveCopy());
				struct.addField("HARDCOY_FLAG",HARDCOY_FLAG);
				
				fc.addStruct(struct);
			}
			respBody.addArray("HOUSE_PROPERTY_INFO_ARRAY", fc);
			
			//机动车
			Array jdcarray = new Array();
			List<QzApplnDbrxxJdc> jdcList = customerInforDao.findJdc(dbrxx.getId());
			for(QzApplnDbrxxJdc jdc :jdcList){
				CompositeData struct = new CompositeData();
				
				Field SEQU_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
				SEQU_NO.setValue(jdc.getSeqNo());
				struct.addField("SEQU_NO",SEQU_NO);
				
				Field VEH_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,20));
				VEH_NO.setValue(jdc.getCardNo());
				struct.addField("VEH_NO",VEH_NO);
				
				Field PRICE = new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20));
				PRICE.setValue(jdc.getPrice()==null?Double.valueOf("0.0"):Double.valueOf(jdc.getPrice()));
				struct.addField("PRICE",PRICE);
				
				Field BUY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING,8));
				BUY_DATE.setValue(sdf.format(jdc.getBuyDate()));
				struct.addField("BUY_DATE",BUY_DATE);
				
				Field VEHICLE_HARDCOY_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,3));
				VEHICLE_HARDCOY_FLAG.setValue(jdc.getHaveCopy_1());
				struct.addField("VEHICLE_HARDCOY_FLAG",VEHICLE_HARDCOY_FLAG);
				
				Field HARDCOY_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,3));
				HARDCOY_FLAG.setValue(jdc.getHaveCopy_2());
				struct.addField("HARDCOY_FLAG",HARDCOY_FLAG);
				
				jdcarray.addStruct(struct);
			}
			respBody.addArray("VEHICLE_INFO_ARRAY", jdcarray);
	    }
		
		//待上传资料
      	QzApplnAttachmentList  attach =  customerInforDao.findAttachList(INPUT_WARE_REG_NO);
      	
      	Field CLIENT_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING,16));
      	CLIENT_CODE.setValue(attach.getCustomerId());
      	respBody.addField("CLIENT_CODE",CLIENT_CODE);
      	
      	Field CMPNY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING,150));
      	CMPNY_NAME.setValue(attach.getShopName());
      	respBody.addField("CMPNY_NAME",CMPNY_NAME);
      	
      	Field LICENSE_NO = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
      	LICENSE_NO.setValue(attach.getShopId());
      	respBody.addField("LICENSE_NO",LICENSE_NO);
      	
    	Field BUSS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,50));
    	BUSS_TYPE.setValue(attach.getBussType());
      	respBody.addField("BUSS_TYPE",BUSS_TYPE);
      	
      	Field INFO_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
      	INFO_TYPE.setValue(attach.getChkValue());
      	respBody.addField("INFO_TYPE",INFO_TYPE);
      	
      	Field CUST_MANAGER_ID1 = new Field(new FieldAttr(FieldType.FIELD_STRING,16));
      	CUST_MANAGER_ID1.setValue(attach.getUser_1());
      	respBody.addField("CUST_MANAGER_ID1",CUST_MANAGER_ID1);
      	
      	Field CREATION_TIME = new Field(new FieldAttr(FieldType.FIELD_STRING,14));
      	CREATION_TIME.setValue(attach.getCreatedTime()==null?null:sdf.format(attach.getCreatedTime()));
      	respBody.addField("CREATION_TIME",CREATION_TIME);
      	
      	Field FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING,1));
      	FLAG.setValue(attach.getDocid());
      	respBody.addField("FLAG",FLAG);
      	
      	Field UPLOADED_INFO_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING,10));
      	UPLOADED_INFO_TYPE.setValue(attach.getUploadValue());
      	respBody.addField("UPLOADED_INFO_TYPE",UPLOADED_INFO_TYPE);
      	
    	return respBody;
    }
}
