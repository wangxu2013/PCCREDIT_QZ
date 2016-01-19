/**
 * 
 */
package com.cardpay.pccredit.ipad.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.model.CustomerAccountData;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationCom;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.product.model.AddressAccessories;
import com.cardpay.pccredit.product.model.AppendixDict;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.service.ProductService;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.i18n.I18nHelper;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

/**
 * @author shaoming
 *
 * 2014年12月8日   上午9:24:47
 */
@Controller
public class IntopiecesIpadController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
	/**
	 * 提供userId得到该客户经理名下所有客户接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/getIntopieces.json")
	public String getCustomerInforByUserId(HttpServletRequest request) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		String userId = request.getParameter("userId");
		if(StringUtils.isNotEmpty(userId)){
			IntoPiecesFilter filter = new IntoPiecesFilter();
			filter.setUserId(userId);
			String currentPage=request.getParameter("currentPage");
			String pageSize=request.getParameter("pageSize");
			int page = 1;
			int limit = 10;
			if(StringUtils.isNotEmpty(currentPage)){
				page = Integer.parseInt(currentPage);
			}
			if(StringUtils.isNotEmpty(pageSize)){
				limit = Integer.parseInt(pageSize);
			}
			filter.setPage(page - 1);
			filter.setLimit(limit);
			
			QueryResult<IntoPieces> result = intoPiecesService.findintoPiecesByFilter(filter);
			for(IntoPieces intoPieces : result.getItems()){
				//当前申请状态 未申请save  已申请audit 申请未通过nopass  被拒接refuse 审批结束 approved  申请成功 succeed
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.SAVE_INTOPICES)){
					intoPieces.setStatusName("暂存");
				}
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.APPROVE_INTOPICES)){
					intoPieces.setStatusName("已申请");
				}
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.NOPASS_INTOPICES)){
					intoPieces.setStatusName("申请退回");
				}
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.REFUSE_INTOPICES)){
					intoPieces.setStatusName("拒绝");
				}
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.APPROVED_INTOPICES)){
					intoPieces.setStatusName("审批结束");
				}
				if(intoPieces.getStatus().equalsIgnoreCase(Constant.SUCCESS_INTOPICES)){
					intoPieces.setStatusName("申请成功");
				}
			}
			
			map.put("totalCount", result.getTotalCount());
			map.put("result",result.getItems());			
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 根据客户信息新增进件信息，将客户相关的信息带到后面进件新增页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/getIntoCustomerInfo.json")
	public String getIntoCustomerInfo(HttpServletRequest request) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		/* 客户信息 */
		CustomerInfor customerInfor = null;
		/* 职业信息 */
		CustomerCareersInformation customerCareersInformation = null;
		
		String customerId = request.getParameter("customerId");
		if (StringUtils.trimToNull(customerId) != null) {
			customerInfor = customerInforService.findCustomerInforById(customerId);
		}
		if (customerInfor != null) {
			customerCareersInformation = intoPiecesService.findCustomerCareersInformationByCustomerId(customerInfor.getId());
		}
		
		map.put("customer", customerInfor);
		map.put("customerCareers", customerCareersInformation);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 根据进件id获取进件相关信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/getIntopiecesById.json")
	public String input(HttpServletRequest request) {
		/* 客户信息 */
		CustomerInfor customerInfor = null;
		/* 职业信息 */
		CustomerCareersInformation customerCareersInformation = null;
		/* 客户联系人 */
		List<CustomerApplicationContact> customerApplicationContactList = null;
		/* 客户担保人 */
		List<CustomerApplicationGuarantor> customerApplicationGuarantorList = null;
		/* 客户推荐人 */
		List<CustomerApplicationRecom> customerApplicationRecomList = null;
		/* 客户申请主表信息 */
		CustomerApplicationInfo customerApplicationInfo = null;
		/* 其他资料 */
		CustomerApplicationOther customerApplicationOther = null;
		/* 行社专栏 */
		CustomerApplicationCom customerApplicationCom = null;
		/* 账户资料 */
		CustomerAccountData customerAccountData = null;
		/* 产品要上传的附件资料 */
		List<AppendixDict> appendixDictList = null;
		/* 实际上传的附件资料 */
		List<AddressAccessories> addressAccessoriesList = null;
		String intopiecesId = request.getParameter("intopiecesId");
		String userId = request.getParameter("userId");
		if (StringUtils.trimToNull(intopiecesId) != null) {// 修改进件首先查询出进件信息
			customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(intopiecesId);
			customerInfor = customerInforService.findCustomerInforById(customerApplicationInfo.getCustomerId());
			customerCareersInformation = intoPiecesService.findCustomerCareersInformationByCustomerId(customerInfor.getId());
			customerApplicationContactList = intoPiecesService.findCustomerApplicationContactsByApplicationId(intopiecesId);
			customerApplicationGuarantorList = intoPiecesService.findCustomerApplicationGuarantorsByApplicationId(intopiecesId);
			customerApplicationRecomList = intoPiecesService.findCustomerApplicationRecomsByApplicationId(intopiecesId);
			customerApplicationOther = intoPiecesService.findCustomerApplicationOtherByApplicationId(intopiecesId);
			customerApplicationCom = intoPiecesService.findCustomerApplicationComByApplicationId(intopiecesId);
			customerAccountData = intoPiecesService.findCustomerAccountDataByApplicationId(intopiecesId);
			if (customerApplicationInfo.getProductId() != null) {
				appendixDictList = productService.findAppendixByProductId(customerApplicationInfo.getProductId());
				addressAccessoriesList = intoPiecesService.findAddressAccessories(intopiecesId, customerApplicationInfo.getProductId());
			}
		}
		
		Map<String, String> productAttributeMap = new HashMap<String, String>();
		List<ProductAttribute> list = productService.findProductAttributeByuserId(userId);
		for (ProductAttribute productAttribute : list) {
			if (productAttribute != null) {
				productAttributeMap.put(productAttribute.getId(),productAttribute.getProductName());
			}
		}
	
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		map.put("customer", customerInfor);
		map.put("customerCareers", customerCareersInformation);
		map.put("customerApplicationContactList",customerApplicationContactList);
		map.put("customerApplicationGuarantorList",customerApplicationGuarantorList);
		map.put("customerApplicationRecomList",customerApplicationRecomList);
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("customerApplicationOther", customerApplicationOther);
		map.put("customerApplicationCom", customerApplicationCom);
		map.put("appendixDictList", appendixDictList);
		map.put("addressAccessoriesList", addressAccessoriesList);
		map.put("productAttributeMap", productAttributeMap);
		map.put("customerAccountData", customerAccountData);
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 添加进件信息(保存，修改)
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/saveIntopieces.json")
	public String save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String customerJosnStr = request.getParameter("customer");
			String customerCareersJosnStr = request.getParameter("customerCareers");
			String customerApplicationContactListJosnStr = request.getParameter("customerApplicationContactList");
			String customerApplicationGuarantorListJosnStr = request.getParameter("customerApplicationGuarantorList");
			String customerApplicationRecomListJosnStr = request.getParameter("customerApplicationRecomList");
			String customerApplicationInfoJosnStr = request.getParameter("customerApplicationInfo");
			String customerApplicationOtherJosnStr = request.getParameter("customerApplicationOther");
			String customerApplicationComJosnStr = request.getParameter("customerApplicationCom");
			String customerAccountDataJosnStr = request.getParameter("customerAccountData");
			// 0 - 保存  1 - 提交申请
			String operationButtonValue = request.getParameter("operationButtonValue"); 
			//客户经理id
			String userId = request.getParameter("userId");
			
			CustomerInfor customerInfor = null;
			if(StringUtils.isNotEmpty(customerJosnStr)){
				JSONObject customerJosnObj = JSONObject.fromObject(customerJosnStr);
				customerInfor = (CustomerInfor) JSONObject.toBean(customerJosnObj, CustomerInfor.class);
			}
			CustomerCareersInformation customerCareersInformation = new CustomerCareersInformation();
			if(StringUtils.isNotEmpty(customerCareersJosnStr)){
				JSONObject customerCareersJosnObj = JSONObject.fromObject(customerCareersJosnStr);
				customerCareersInformation = (CustomerCareersInformation) JSONObject.toBean(customerCareersJosnObj, CustomerCareersInformation.class);
			}
			CustomerApplicationContact customerApplicationContact = new CustomerApplicationContact();
			if(StringUtils.isNotEmpty(customerApplicationContactListJosnStr)){
				JSONObject customerApplicationContactListJosnObj = JSONObject.fromObject(customerApplicationContactListJosnStr);
				customerApplicationContact = (CustomerApplicationContact) JSONObject.toBean(customerApplicationContactListJosnObj, CustomerApplicationContact.class);
			}
			CustomerApplicationGuarantor customerApplicationGuarantor = new CustomerApplicationGuarantor();
			if(StringUtils.isNotEmpty(customerApplicationGuarantorListJosnStr)){
				JSONObject customerApplicationGuarantorListJosnObj = JSONObject.fromObject(customerApplicationGuarantorListJosnStr);
				customerApplicationGuarantor = (CustomerApplicationGuarantor) JSONObject.toBean(customerApplicationGuarantorListJosnObj, CustomerApplicationGuarantor.class);
			}
			CustomerApplicationRecom customerApplicationRecom = new CustomerApplicationRecom();
			if(StringUtils.isNotEmpty(customerApplicationRecomListJosnStr)){
				JSONObject customerApplicationRecomListJosnObj = JSONObject.fromObject(customerApplicationRecomListJosnStr);
				customerApplicationRecom = (CustomerApplicationRecom) JSONObject.toBean(customerApplicationRecomListJosnObj, CustomerApplicationRecom.class);
			}
			CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
			if(StringUtils.isNotEmpty(customerApplicationInfoJosnStr)){
				JSONObject customerApplicationInfoJosnObj = JSONObject.fromObject(customerApplicationInfoJosnStr);
				customerApplicationInfo = (CustomerApplicationInfo) JSONObject.toBean(customerApplicationInfoJosnObj, CustomerApplicationInfo.class);
			}
			CustomerApplicationOther customerApplicationOther = new CustomerApplicationOther();
			if(StringUtils.isNotEmpty(customerApplicationOtherJosnStr)){
				JSONObject customerApplicationOtherJosnObj = JSONObject.fromObject(customerApplicationOtherJosnStr);
				customerApplicationOther = (CustomerApplicationOther) JSONObject.toBean(customerApplicationOtherJosnObj, CustomerApplicationOther.class);
			}
			CustomerApplicationCom customerApplicationCom = new CustomerApplicationCom();
			if(StringUtils.isNotEmpty(customerApplicationComJosnStr)){
				JSONObject customerApplicationComJosnObj = JSONObject.fromObject(customerApplicationComJosnStr);
				customerApplicationCom = (CustomerApplicationCom) JSONObject.toBean(customerApplicationComJosnObj, CustomerApplicationCom.class);
			}
			CustomerAccountData customerAccountData = new CustomerAccountData();
			if(StringUtils.isNotEmpty(customerAccountDataJosnStr)){
				JSONObject customerAccountDataJosnObj = JSONObject.fromObject(customerAccountDataJosnStr);
				customerAccountData = (CustomerAccountData) JSONObject.toBean(customerAccountDataJosnObj, CustomerAccountData.class);
			}
			
			// 保存或者是申请 
			if("0".equals(operationButtonValue)){
				customerApplicationInfo.setStatus(Constant.SAVE_INTOPICES);
			} else{
				customerApplicationInfo.setStatus(Constant.APPROVE_INTOPICES);
			}
			//是否移交
			boolean checkFlag = Boolean.valueOf(request.getParameter("checkFlag"));
			if(!checkFlag&&StringUtils.trimToNull(customerInfor.getCustomerBusinessAptitude())!=null){
				customerApplicationInfo.setStatus(Constant.SAVE_INTOPICES);
			}
			/* 如果各部分信息都是新增的这里就插入进数据库 */
			List<BusinessModel> list = new ArrayList<BusinessModel>();
			/* 如果各部分信息已经存在就更新数据 */
			List<BusinessModel> updateList = new ArrayList<BusinessModel>();
			//申请件级别信息
			if (StringUtils.trimToNull(customerApplicationInfo.getId()) == null) {
				customerApplicationInfo.setId(IDGenerator.generateID());
				list.add(customerApplicationInfo);
			} else {
				updateList.add(customerApplicationInfo);
			}
			//申请表行社专栏
			if (StringUtils.trimToNull(customerApplicationCom.getId()) != null) {
				updateList.add(customerApplicationCom);
			} else {
				list.add(customerApplicationCom);
			}
			//其它信息
			if (StringUtils.trimToNull(customerApplicationOther.getId()) != null) {
				updateList.add(customerApplicationOther);
			} else {
				list.add(customerApplicationOther);
			}
			//客户基本资料
			if (StringUtils.trimToNull(customerInfor.getId()) == null) {
				customerInfor.setId(IDGenerator.generateID());
				customerCareersInformation.setCustomerId(customerInfor.getId());
				list.add(customerInfor);
			} else {
				customerCareersInformation.setCustomerId(customerInfor.getId());
				updateList.add(customerInfor);
			}
			//客户职业信息
			if (StringUtils.trimToNull(customerCareersInformation.getId()) != null) {
				updateList.add(customerCareersInformation);
			} else {
				list.add(customerCareersInformation);
			}
			//客户账户资料
			if(StringUtils.trimToNull(customerAccountData.getId()) != null){
				updateList.add(customerAccountData);
			}else{
				list.add(customerAccountData);
			}
			//添加联系人
			if(StringUtils.trimToNull(customerApplicationContact.getId()) != null){
				updateList.add(customerApplicationContact);
			}else{
				list.add(customerApplicationContact);
			}
			//添加担保人
			if(StringUtils.trimToNull(customerApplicationGuarantor.getId()) != null){
				updateList.add(customerApplicationGuarantor);
			}else{
				list.add(customerApplicationGuarantor);
			}
			//添加推荐人
			if(StringUtils.trimToNull(customerApplicationRecom.getId()) != null){
				updateList.add(customerApplicationRecom);
			}else{
				list.add(customerApplicationRecom);
			}
			
			customerApplicationGuarantor.setMainApplicationFormId(customerApplicationInfo.getId());
			customerApplicationContact.setMainApplicationFormId(customerApplicationInfo.getId());
			customerAccountData.setMainApplicationFormId(customerApplicationInfo.getId());
			customerApplicationCom.setMainApplicationFormId(customerApplicationInfo.getId());
			customerApplicationOther.setMainApplicationFormId(customerApplicationInfo.getId());
		
			customerInfor.setUserId(userId);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put("customerId", customerInfor.getId());
			paramMap.put("applicationId", customerApplicationInfo.getId());
			paramMap.put("productId", customerApplicationInfo.getProductId());
			paramMap.put("flag", true);
			paramMap.put("customerBusinessAptitude", customerInfor.getCustomerBusinessAptitude());
			paramMap.put("userId", userId);
			paramMap.put("checkFlag", checkFlag);
			if(!list.isEmpty()){
				/* 第一次保存时做插入操作 */
				intoPiecesService.saveAllInfo(list, request,userId,customerApplicationInfo.getStatus(),paramMap);
			}
			if(!updateList.isEmpty()){
				/* 第二次保存时做更新操作 */
				intoPiecesService.updateAllInfo(updateList, request,userId,customerApplicationInfo.getStatus(),paramMap);
			}
			
			map = new HashMap<String, Object>();
			map.put(JRadConstants.SUCCESS, true);
			map.put(JRadConstants.MESSAGE, Beans.get(I18nHelper.class).getMessageNotNull(JRadConstants.CREATE_SUCCESS));
			JSONObject obj = JSONObject.fromObject(map);
			return obj.toString();
		}catch(Exception e){
			map = new HashMap<String, Object>();
			map.put(JRadConstants.SUCCESS, false);
			map.put(JRadConstants.MESSAGE, "保存失败");
			JSONObject obj = JSONObject.fromObject(map);
			return obj.toString();
		}
	}
	
	/**
	 * 验证客户商业类型
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/checkValue.json")
	public String checkValue(HttpServletRequest request) {
		Map<String, Object> map = null;
		String currentUserId = request.getParameter("userId");
		boolean flag = false;
		try {
			String value = request.getParameter("customerBusinessAptitude");
			int i= intoPiecesService.checkValue(currentUserId,value);
			if(i>0){
				flag = true;
			}
			map = new HashMap<String, Object>();
			if(!flag){
				map.put(JRadConstants.SUCCESS, false);
				map.put(JRadConstants.MESSAGE, "由于没有该客户的商业类型选择权,你是否需要移交客户?");
			}else{
				map.put(JRadConstants.SUCCESS, true);
			}
		} catch (Exception e) {
			map.put(JRadConstants.SUCCESS, false);
			map.put(JRadConstants.MESSAGE, "验证客户商业类型异常出错");
		}
		JSONObject obj = JSONObject.fromObject(map);
		return obj.toString();
	}
}
