package com.cardpay.pccredit.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.afterloan.model.O_CLPM_ACC_LOAN;
import com.cardpay.pccredit.afterloan.model.O_CLPM_CTR_LOAN_CONT;
import com.cardpay.pccredit.afterloan.model.PspCheckTaskVo;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.model.CustomerInforWeb;
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
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.ipad.model.CustomerApplyInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDhJcInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDhJcTxInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerDqwdtxInfoIpad;
import com.cardpay.pccredit.ipad.model.CustomerInfoIpad;
import com.cardpay.pccredit.system.model.Dict;
import com.cardpay.pccredit.system.web.NodeAuditForm;
import com.wicresoft.util.annotation.Mapper;
/**
 * 
 * @author shaoming
 *  
 */
@Mapper
public interface CustomerInforDao {
	/**
	 * 根据dict_type获取字典
	 * @return
	 */
	public List<Dict> findDict(@Param("dict_type") String dict_type);
	/**
	 * 获得国籍
	 * @return
	 */
	public List<Dict> findNationality();
	/**
	 * 获得证件类型
	 * @return
	 */
	public List<Dict> findCardType();
	/**
	 * 获得婚姻状况
	 * @return
	 */
    public List<Dict> findMaritalStatus();
    /** 
     * 获得住宅性质
     * @return
     */
    public List<Dict> findResidentialPropertie();
    
    /** 
     * 获得性别
     * @return
     */
    public List<Dict> findSex();
    
    /** 
     * 获得单位性质
     * @return
     */
    public List<Dict> findUnitPropertis();
    /**
     * 获得行业类型
     * @return
     */
    public List<Dict> findIndustryType();
    /**
     * 催收,维护方式
     * @return
     */
    public List<Dict> findCollectionMethod();
    /**
     * 职务
     * @return
     */
    public List<Dict> findPositio();
    /**
     * 职称
     * @return
     */
    public List<Dict> findTitle();
    /**
     * 通过证件类型代码得到证件类型名
     * @param typecode
     * @return
     */
    public String findTypeNameByTypeCode(String typecode);
    /**
     * 通过Name得到客户信息
     * @param name
     * @return
     */
    public List<CustomerInfor> findCustomerInforByName(@Param("userId") String userId,@Param("name") String name);
    /**
     * 修改客户信息的客户经理id和移交状态
     * @param id
     * @param customerManagerId
     * @return
     */
    public int updateCustomerInfor(@Param("id")String id,@Param("customerManagerId")String customerManagerId,@Param("status") String status);
    /**
     * 得到客户信息中的客户经理id
     * @param customerId
     * @return
     */
    public String findCustomerManagerIdById(@Param("customerId")String customerId);
    /**
     * 得到页面显示的客户信息
     * @param id
     * @return
     */
	public CustomerInforWeb findCustomerInforWebById(@Param("id") String id);
	
	/**
	 * 修改客户信息移交状态
	 * @param id
	 * @param status
	 */
	public int updateCustomerInforDivisionalStatus(@Param("id") String id,@Param("status") String status);
	/**
	 * 得到客户信息移交状态
	 * @param id
	 * @return
	 */
	public String getCustomerInforDivisionalStatus(@Param("id") String id);
	
	
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneBasicCustomerInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneCustomerCareersInf(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneCustomerMainManager(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneCustomerQuestionInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneCustomerWorkshipInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneDimensionalModelCredit(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 进件申请提交时 客户维护资料做快照
	 * @param customerId
	 * @param applicationId
	 */
	public void cloneCustomerVideoAccessories(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	/**
	 * 根据进件申请Id查询客户维护资料快照
	 * @param applicationId
	 */
	public CustomerInfor findCloneCustomerInforByAppId(@Param("applicationId") String applicationId);
	
	/**
	 * 根据客户Id查询客户职业资料维护资料快照
	 * @param customerId
	 */
	public CustomerCareersInformation findcloneCustomerCareersByCustomerId(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	/**
	 * 得到该客户经理下的客户数量
	 * @param userId
	 * @return
	 */
	public int findCustomerInforCountByUserId(@Param("userId") String userId);
	
	public void deleteCloneBasicCustomerInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneCustomerCareersInf(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneCustomerQuestionInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneCustomerMainManager(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneCustomerWorkshipInfo(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneDimensionalModelCredit(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public void deleteCloneCustomerVideoAccessories(@Param("customerId") String customerId, @Param("applicationId") String applicationId);
	
	public List<CustomerInfoIpad> ipadFindAllCustomerByUserId(@Param("user_no") String user_no,@Param("client_name")  String client_name,@Param("default_type") String default_type,@Param("global_id") String global_id
			,@Param("limit") String limit,@Param("page") String page);
	public List<CustomerInfoIpad> ipadFindWsxCustomerByUserId(@Param("user_no") String user_no,@Param("client_name")  String client_name,@Param("default_type") String default_type,@Param("global_id") String global_id
			,@Param("limit") String limit,@Param("page") String page);
	public List<CustomerInfoIpad> ipadFindWjqCustomerByUserId(@Param("user_no") String user_no,@Param("client_name")  String client_name,@Param("default_type") String default_type,@Param("global_id") String global_id
			,@Param("limit") String limit,@Param("page") String page);
	public List<CustomerInfoIpad> ipadFindYjqCustomerByUserId(@Param("user_no") String user_no,@Param("client_name")  String client_name,@Param("default_type") String default_type,@Param("global_id") String global_id
			,@Param("limit") String limit,@Param("page") String page);
	
	//01-查询已进件贷款申请列表
	public List<CustomerApplyInfoIpad> ipadApplyInfoJjByUserId(@Param("tranType") String tranType,
															   @Param("userNo")  String userNo,
															   @Param("clientName") String clientName,
															   @Param("globalId") String globalId,
															   @Param("chl") String chl
															   ,@Param("limit") String limit,@Param("page") String page);
	//02-查询贷款申请待处理补退件列表
	public List<CustomerApplyInfoIpad> ipadApplyInfoThByUserId(@Param("tranType") String tranType,
															   @Param("userNo")  String userNo,
															   @Param("clientName") String clientName,
															   @Param("globalId") String globalId,
															   @Param("chl") String chl
															   ,@Param("limit") String limit,@Param("page") String page);
	//03-查询贷中客户列表										
	public List<CustomerApplyInfoIpad> ipadApplyInfoDzByUserId(@Param("tranType") String tranType,
															   @Param("userNo")  String userNo,
															   @Param("clientName") String clientName,
															   @Param("globalId") String globalId
															   ,@Param("limit") String limit,@Param("page") String page);
   
	public List<CustomerDhJcInfoIpad> ipadDhJcByUserId(@Param("QUERY_TYPE") String tranType,
													   @Param("APPROVE_STATUS")  String userNo,
													   @Param("userId") String userId,
													   @Param("chineseName") String chineseName,
													   @Param("cardId") String cardId,
													   @Param("clientNo") String clientNo,
													   @Param("checkType") String checkType,
													   @Param("belongChildIds") String belongChildIds
													   ,@Param("limit") String limit,@Param("page") String page);
	
	public CustomerDhJcTxInfoIpad ipadWdtxByUserId(@Param("userId") String userId,@Param("loginId") String loginId); 
	
	public List<CustomerDqwdtxInfoIpad> ipadDqwdByUserId(@Param("CLIENT_NAME") String CLIENT_NAME,
														 @Param("GLOBAL_ID") String GLOBAL_ID,
														 @Param("BUSS_TYPE") String BUSS_TYPE,
														 @Param("userId") String userId
														 ,@Param("limit") String limit,@Param("page") String page);
	
	public List<ApproveHistoryForm> ipadWdAuditInfo(@Param("id") String id);
	
	public List<NodeAuditForm> ipadWdApplyProcess(@Param("productId") String productId);
	
	//取最新的合同
	public O_CLPM_CTR_LOAN_CONT ipadWdKuyx(@Param("clientCode") String clientCode);
	//取正常的借据
	public List<O_CLPM_ACC_LOAN> ipadWdKuyxList(@Param("clientCode") String clientCode);
	
	public  PspCheckTaskVo  ipadDhjcbrowse(@Param("taskId") String taskId);
	
	public List<Dict>  ipadDictList(@Param("dictType") String dictType);
	
	public List<QzApplnZa> ipadApplyZaList();
	
	public CustomerApplyInfoIpad Wdsqspbrowse(@Param("appId") String appId,@Param("customerId") String customerId);
	
	//贷款申请表信息
	public QzApplnYwsqb findQzApplnYwsqb(@Param("appId") String appId);
	
	//经营信息
	public QzApplnJyxx findQzApplJyxx(@Param("appId") String appId);
	
	//供应商信息
	public List<QzApplnYwsqbZygys> findGys(@Param("ywsqbId") String ywsqbId);
	
	//查询客户
	public List<QzApplnYwsqbZykh> findKh(@Param("ywsqbId") String ywsqbId);
	
	//查询贷款信息明细
	public List<QzApplnYwsqbJkjl> findDk(@Param("ywsqbId") String ywsqbId);
	
	//查询担保人信息
	public List<QzApplnDbrxx> findDbrxx(@Param("appId") String appId);
	
	//担保人贷款信息
	public List<QzApplnDbrxxDkjl> findDbrxxDkjl(@Param("dbrxxId") String dbrxxId);
	
	//房产
	public List<QzApplnDbrxxFc> findFcList(@Param("dbrxxId") String dbrxxId);
	
	//机动车
	public List<QzApplnDbrxxJdc> findJdc(@Param("dbrxxId") String dbrxxId);
	
	//待上传资料
	public QzApplnAttachmentList findAttachList(@Param("appId") String appId);
	
	//更新客户位置信息
	public void updateCustomerLocationMsg(@Param("COORDINATE") String COORDINATE, @Param("CLIENT_CODE") String CLIENT_CODE,@Param("REMARK") String REMARK);
	public int checkCanApplyOrNot(@Param("custId") String custId);
}
