package com.cardpay.pccredit.QZBankInterface.service;

import com.cardpay.pccredit.QZBankInterface.client.Client;
import com.cardpay.pccredit.QZBankInterface.constant.Constant;
import com.cardpay.pccredit.QZBankInterface.dao.CircleDao;
import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.Circle_ACCT_INFO;
import com.cardpay.pccredit.QZBankInterface.model.Credit;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.dc.eai.data.CompositeData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CircleService {
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
	private Client client;
    
    @Autowired
	private IESBForCircleCredit iesbForCircleCredit;
    
    @Autowired
	private IESBForCore iesbForCore;
    
    @Autowired
	private ECIFService ecifService;
    
    @Autowired
	private CircleDao circleDao;
    
    /**
     * 插入数据
     * @param circle
     * @return
     */
    public void insertCustomerInforCircle(Circle circle) {
    	String id = IDGenerator.generateID();
		circle.setId(id);
		circle.setCreatedTime(new Date());
		commonDao.insertObject(circle);
		
	}

    /**
     * 更新数据
     * @param circle
     * @return
     */
    public void updateCustomerInforCircle(Circle circle) {
    	commonDao.updateObject(circle);
	}
    
    //对接并存db
    public String updateCustomerInforCircle_ESB(Circle circle) {
    	String returnMessage = "";
    	//先开户
//    	boolean rtn = ecifService.updateCustomerInfor(circle,ecifService.findEcifByCustomerId(circle.getCustomerId()));
//    	if(rtn == false){
//    		returnMessage = "开户失败";
//    		return returnMessage;
//    	}
    	
		//先查询核心
		List<Circle_ACCT_INFO> acct_info_ls = new ArrayList<Circle_ACCT_INFO>();
		//收息收款账号
		CompositeData req1 = iesbForCore.createCoreRequest(circle.getAcctNo1());
		CompositeData resp1 = client.sendMess(req1);
		if(resp1 == null){
			returnMessage = "收息账号不存在";
			return returnMessage;
		}
		Circle_ACCT_INFO acct_Info1 = iesbForCore.parseCoreResponse(resp1,"03");
		if(acct_Info1 == null){
			returnMessage = "解析ecif返回信息失败";
			return returnMessage;
		}
		acct_Info1.setCircleId(circle.getId());
		acct_info_ls.add(acct_Info1);
		//放款账号
		CompositeData req2 = iesbForCore.createCoreRequest(circle.getAcctNo2());
		CompositeData resp2 = client.sendMess(req2);
		if(resp2 == null){
			returnMessage = "放款账号不存在";
			return returnMessage;
		}
		Circle_ACCT_INFO acct_Info2 = iesbForCore.parseCoreResponse(resp2,"01");
		if(acct_Info2 == null){
			returnMessage = "解析ecif返回信息失败";
			return returnMessage;
		}
		acct_Info2.setCircleId(circle.getId());
		acct_info_ls.add(acct_Info2);
		//费用账号，由于费用屏蔽后不用填写费用账号，所以现在默认费用账号是放款账号
		CompositeData req3 = iesbForCore.createCoreRequest(circle.getAcctNo2());
		CompositeData resp3 = client.sendMess(req3);
		if(resp3 == null){
			returnMessage = "费用账号不存在";
			return returnMessage;
		}
		Circle_ACCT_INFO fee_Acct_Info = iesbForCore.parseCoreResponse(resp3,"07");
		if(fee_Acct_Info == null){
			returnMessage = "解析ecif返回信息失败";
			return returnMessage;
		}
		fee_Acct_Info.setCircleId(circle.getId());
		acct_info_ls.add(fee_Acct_Info);
		
		//组包
		CompositeData req = iesbForCircleCredit.createCircleCreditRequest(circle,acct_info_ls);
		//发送
		CompositeData resp = client.sendMess(req);
		if(resp == null){
			returnMessage = "放款交易发送失败";
			return returnMessage;
		}
		//解析，存db
		String res = iesbForCircleCredit.parseEcifResponse(resp,circle);
		
		//如果成功，保存circle_acct_info，并置ecif状态
		if("放款成功".equals(res)){
			commonDao.insertObject(acct_Info1);
			commonDao.insertObject(acct_Info2);
			//commonDao.insertObject(fee_Acct_Info);
			
		}
		return res;
	}
    
	/**
	 * 查询新开户
	 * @param filter
	 * @return
	 */
	public QueryResult<Circle> findCircleByFilter(EcifFilter filter) {
		List<Circle> ls = circleDao.findCircleByFilter(filter);
		int size = circleDao.findCircleCountByFilter(filter);
		QueryResult<Circle> qs = new QueryResult<Circle>(size, ls);
		return qs;
	}

	//按客户号查询circle
	public Circle findCircleByClientNo(String clientNo) {
		return circleDao.findCircleByClientNo(clientNo);
	}
	
	//查找circle
	public Circle findCircle(String customerId,String applicationId) {
		return circleDao.findCircle(customerId,applicationId);
	}
	
	public Circle findCircleByAppId(String applicationId) {
		return circleDao.findCircleByAppId(applicationId);
	}
	
	//按客户号查询circle
	public List<Circle> findCircleByCardNo(String cardno) {
		return circleDao.findCircleByCardNo(cardno);
	}
	
	//按照客户号号和合同号查询贷款记录
	public Circle findCircleByClientNoAndContNo(String clientNo,String retContNo){
		return circleDao.findCircleByClientNoAndContNo(clientNo, retContNo);
	}
}
