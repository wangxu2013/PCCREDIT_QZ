package com.cardpay.pccredit.intopieces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.dao.CustomerApplicationInfoDao;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class CustomerApplicationInfoService {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private CustomerApplicationInfoDao customerApplicationInfoDao;

	/*
	 * 保存申请客户信息
	 */
	public void save(CustomerApplicationInfo customerApplicationInfo) {
		commonDao.insertObject(customerApplicationInfo);
	}

	/*
	 * 删除客户信息
	 */
	public void delete(String id) {
		commonDao.deleteObject(CustomerApplicationInfo.class, id);
	}
	/**
	 * 统计拒绝进件条数
	 * @return
	 */
	public int findCustomerApplicationInfoCount(String userId,String status1,String status2){
		return customerApplicationInfoDao.findCustomerApplicationInfoCount(userId,status1,status2);
	}
	
	public CustomerApplicationInfo findById(String id) {
		return commonDao.findObjectById(CustomerApplicationInfo.class, id);
	}
}
