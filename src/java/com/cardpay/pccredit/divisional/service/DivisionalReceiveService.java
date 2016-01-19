package com.cardpay.pccredit.divisional.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.customer.constant.CustomerInforDStatusEnum;
import com.cardpay.pccredit.customer.dao.CustomerInforDao;
import com.cardpay.pccredit.customer.service.CardInfomationService;
import com.cardpay.pccredit.customer.web.CardInfomationFrom;
import com.cardpay.pccredit.divisional.constant.DivisionalConstant;
import com.cardpay.pccredit.divisional.dao.DivisionalDao;
import com.cardpay.pccredit.divisional.dao.comdao.DivisionalCommDao;
import com.cardpay.pccredit.divisional.filter.DivisionalFilter;
import com.cardpay.pccredit.divisional.model.Divisional;
import com.cardpay.pccredit.divisional.model.DivisionalWeb;
import com.cardpay.pccredit.notification.constant.NotificationEnum;
import com.cardpay.pccredit.notification.service.NotificationService;
import com.cardpay.pccredit.riskControl.constant.RiskType;
import com.cardpay.pccredit.riskControl.service.AccountabilityService;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.modules.privilege.model.User;

/**
 * 
 * @author 姚绍明
 *
 * 
 */
@Service
public class DivisionalReceiveService {
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private DivisionalCommDao divisionalcommDao;
	
	@Autowired
	private DivisionalDao divisionalDao;
	
	@Autowired
	private CustomerInforDao customerinforDao;
	
	@Autowired
	private AccountabilityService accountabilityService;
	
	@Autowired
	private CardInfomationService cardInfomationService;
	
	@Autowired
	private DivisionalService divisionalService;
	
	@Autowired
	private NotificationService notificationService;
	
	/**
	 * 获得分案申请信息 
	 * @param filter
	 * @return
	 */
	public QueryResult<DivisionalWeb> findDivisionalByCustomerManager(DivisionalFilter filter){
		List<DivisionalWeb> pList = divisionalDao.findDivisionalByCustomerManager(filter);
		int size = divisionalDao.findDivisionalByCustomerManagerCount(filter);
		QueryResult<DivisionalWeb> queryResult = new QueryResult<DivisionalWeb>(size, pList);
		
		return queryResult;
	}
	/**
	 * 接受客户,修改客户基本信息表客户经理id与分案申请表的分案进度
	 * @param customerId
	 * @param userId
	 * @param id
	 * @param result 分案进度
	 * @param process 分案结果
	 * @return
	 */
	public boolean updateCustomerInforAndDivisional(String customerId,String userId,CustomerInforDStatusEnum status,String id,String result,String process){
		int i = customerinforDao.updateCustomerInfor(customerId, userId,status.toString());
		if(i>0){
			int j = divisionalDao.updateDivisionalResultAndProcess(id, result, process);
			if(j>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/**
	 * 拒绝客户,修改分案申请表分案结果为reject
	 * @param id
	 * @param result
	 * @return
	 */
	public boolean updateDivisionalResult(String id,String result){
		int i = divisionalDao.updateDivisionalResultAndProcess(id,result,null);
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 得到用户名
	 * @param id
	 * @return
	 */
	public String findUserNameByUserId(String id){
		return divisionalDao.getUserNameByUserId(id);
	}
	
	public void reveive(String customerId,String userId,String id){
		this.updateCustomerInforAndDivisional(customerId, userId, CustomerInforDStatusEnum.complete,id,DivisionalConstant.RECEIVED,DivisionalConstant.MANAGER);
		Divisional divisional = divisionalService.findDivisinalById(id);
		String oldManagerName = this.findUserNameByUserId(divisional.getOriginalManagerOld());
	    String ManagerName = this.findUserNameByUserId(divisional.getCustomerManagerId());
		notificationService.insertNotification(NotificationEnum.qita, divisional.getOriginalManagerOld(), DivisionalConstant.RECEIVESUCCESS, oldManagerName+"向"+ManagerName+DivisionalConstant.RECEIVESUCCESS, userId);
		
		User oldUser = commonDao.findObjectById(User.class, divisional.getOriginalManagerOld());
		User newUser = commonDao.findObjectById(User.class, divisional.getCustomerManagerId());
		String sql = "select * from qz_iesb_for_circle where customer_id = '"+customerId+"'";
		List<Circle> circle_ls = commonDao.queryBySql(Circle.class, sql, null);
		//更新用户相关的表
		//1.basic_customer_infomation -- user_id字段
		//2.qz_iesb_for_ecif -- user_id字段
		//3.qz_iesb_for_circle -- user_id字段
		//4.psp_check_task -- manager_id字段
		sql = "update basic_customer_information set user_id = '"+newUser.getId()+"' where id = '"+customerId+"'";
		commonDao.queryBySql(sql, null);
		sql = "update qz_iesb_for_ecif set user_id = '"+newUser.getId()+"' where customer_id = '"+customerId+"'";
		commonDao.queryBySql(sql, null);
		sql = "update qz_iesb_for_circle set user_id = '"+newUser.getId()+"' where customer_id = '"+customerId+"'";
		commonDao.queryBySql(sql, null);
		if(circle_ls != null && circle_ls.size() > 0){
			sql = "update psp_check_task set manager_id = '"+newUser.getId()+"' where cus_id = '"+circle_ls.get(0).getClientNo()+"'";
			commonDao.queryBySql(sql, null);
		}
		
	}
}
