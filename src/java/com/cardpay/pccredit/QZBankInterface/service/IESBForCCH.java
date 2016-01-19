package com.cardpay.pccredit.QZBankInterface.service;

/**
 * 核算接口
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.service.QuotaFreezeOrThawService;
import com.cardpay.pccredit.ipad.constant.IpadConstant;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class IESBForCCH {
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private QuotaFreezeOrThawService quotaFreezeOrThawService;
    /**
     * 组装CompositeData报文
     * @return
     */
    public CompositeData createCCHRequest(String clientNo){
    	SimpleDateFormat formatter2 = new SimpleDateFormat("dd");
    	SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter10 = new SimpleDateFormat("yyyy-MM-dd");
    	
        CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("02003000011");//核算服务服务码
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("01"); //核算对应服务场景01
        syaHead_struct.addField("SERVICE_SCENE", serviceSceneField);
        
       //在syaHead_struct中加TRAN_DATE
        Field tran_datefield = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        tran_datefield.setValue(formatter8.format(new Date())); //交易日期
        syaHead_struct.addField("TRAN_DATE", tran_datefield);
        
        //在syaHead_struct中加CONSUMER_ID
        Field consumer_idField = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        consumer_idField.setValue("300025"); //消费系统编号
        syaHead_struct.addField("CONSUMER_ID", consumer_idField);
        
        cd.addStruct("SYS_HEAD", syaHead_struct);

        //BODY
        CompositeData body_struct = new CompositeData();

        //客户号
        Field CLIENT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CLIENT_NO.setValue("00001");//todo:传入客户号
        CLIENT_NO.setValue(clientNo);//todo:传入客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);
        
        cd.addStruct("BODY",body_struct);

        return cd;
    }
    
    /**
     * 返回成功与否
     * @param resp
     * 获取 最新的一笔授信状态 ，如果是 10 20 的话就先终止 然后再授信；如果是30 40的话 直接授信！
     * @return
     * @throws Exception 
     */
	public String parseCCHResponse(CompositeData resp,String clientNo) throws Exception {
		if(resp == null){
			throw new Exception("核算授信查询接口调用失败");//esb接口调用失败
		}
		CompositeData SYS_HEAD = resp.getStruct("SYS_HEAD");

        //根据数组名称去获取数组
        Array RET = SYS_HEAD.getArray("RET");
        
        String RET_CODE = "";
        String RET_MSG = "";
        if(null != RET && RET.size() > 0){
            int m = RET.size();
            CompositeData array_element = null;
            for (int i = 0; i < m; i++) {
                //数组中的元素也是CompositeData，这是固定的写法。根据游标就可以获取到数组中的所有元素
                array_element = RET.getStruct(i);

                RET_CODE = array_element.getField("RET_CODE").strValue();
                RET_MSG = array_element.getField("RET_MSG").strValue();
            }
        }
        //存在前一笔授信协议
        if(IpadConstant.RET_CODE_SUCCESS.equals(RET_CODE)){
        	CompositeData BODY = resp.getStruct("BODY");
        	String CONTRACT_NO = BODY.getField("CONTRACT_NO").strValue();
        	String LOAN_CARD_NO = BODY.getField("LOAN_CARD_NO").strValue();
        	String CLIENT_NO = clientNo;//BODY.getField("CLIENT_NO").strValue();
        	String CREDIT_STATUS = BODY.getField("CREDIT_STATUS").strValue();
        	//先终止 然后再授信
        	if(CREDIT_STATUS.equals("10") || CREDIT_STATUS.equals("20")){
        		HashMap<String,String> map = new HashMap<String,String>();
				map.put("clientNo", CLIENT_NO);
				map.put("cardNo", LOAN_CARD_NO);
				map.put("operateType", "30");
				map.put("contNo", CONTRACT_NO);
				Map<String,String> ret_map = quotaFreezeOrThawService.getIesbForED(map);//终止的返回值
				if(ret_map == null || !IpadConstant.RET_CODE_SUCCESS.equals(ret_map.get("RET_CODE"))){
					throw new Exception("终止前一笔授信合同失败！");//终止失败
				}
				else{
					return ret_map.get("LOAN_STATUS");//终止成功
				}
        	}
        	else{
        		return CREDIT_STATUS;
        	}
        }else if(IpadConstant.RET_CODE_ERROR.equals(RET_CODE)){//此前无授信
        	return RET_CODE;
        }
        else{
        	throw new Exception("核算授信查询接口调用失败");//esb接口调用失败
        }
	}
	
	
	//如果有授信  获取合同号
	public String parseCCHResponse2(CompositeData resp) throws Exception {
		String retMsg = "";
		if(resp == null){
			throw new Exception("核算授信查询接口调用失败");//esb接口调用失败
		}
		CompositeData SYS_HEAD = resp.getStruct("SYS_HEAD");

        //根据数组名称去获取数组
        Array RET = SYS_HEAD.getArray("RET");
        
        String RET_CODE = "";
        String RET_MSG = "";
        if(null != RET && RET.size() > 0){
            int m = RET.size();
            CompositeData array_element = null;
            for (int i = 0; i < m; i++) {
                //数组中的元素也是CompositeData，这是固定的写法。根据游标就可以获取到数组中的所有元素
                array_element = RET.getStruct(i);

                RET_CODE = array_element.getField("RET_CODE").strValue();
                RET_MSG = array_element.getField("RET_MSG").strValue();
            }
        }
        CompositeData BODY = resp.getStruct("BODY");
    	String CONTRACT_NO = BODY.getField("CONTRACT_NO").strValue();
    	return CONTRACT_NO;
	}
}
