package com.cardpay.pccredit.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.model.CustomerInforWeb;
import com.cardpay.pccredit.system.model.Dict;
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
}
