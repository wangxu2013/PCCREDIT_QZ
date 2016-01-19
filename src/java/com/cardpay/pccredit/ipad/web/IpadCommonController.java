/**
 * 
 */
package com.cardpay.pccredit.ipad.web;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.ipad.constant.IpadConstant;
import com.cardpay.pccredit.ipad.service.IpadCommonService;
import com.cardpay.pccredit.ipad.service.UserForIpadService;
import com.cardpay.pccredit.main.MainService;
import com.dc.eai.data.*;
import com.dcfs.esb.config.ServerConfig;
import com.dcfs.esb.server.service.Service;

@Controller
public class IpadCommonController implements Service {

	public static final Logger logger = Logger.getLogger(IpadCommonController.class);

	@Autowired
	private UserForIpadService userService;
	@Autowired
	private MainService mainService;
	@Autowired
	private CustomerInforService customerInforservice;
	@Autowired
	private IpadCommonService ipadCommonService;
	
	@Override
	public CompositeData exec(CompositeData cd) {
		// TODO Auto-generated method stub

		logger.info("服务端获取到的报文 [" + cd + "]");

		// 获取交易码
		CompositeData SYS_HEAD = cd.getStruct("SYS_HEAD");
		String SERVICE_CODE = SYS_HEAD.getField("SERVICE_CODE").strValue();
		String SERVICE_SCENE = SYS_HEAD.getField("SERVICE_SCENE").strValue();
		String SERVICE = SERVICE_CODE + SERVICE_SCENE;
		CompositeData BODY = cd.getStruct("BODY");
		// 根据交易码调用不同的service
		try {
			CompositeData respCd = null;
			//1100300000709  查询用户信息
			if (SERVICE.equals(IpadConstant.CODE1)) {
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadLogin(SYS_HEAD));
			}
			//1100200001927  微贷客户信息新增
			else if(SERVICE.equals(IpadConstant.CODE2)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadAddCustomerInfo(BODY));
			}
			//1100200001928 微贷客户位置更新
			else if(SERVICE.equals(IpadConstant.CODE3)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadUpdateCustomerPosition(BODY));
			}
			//1100300000131  微贷客户信息列表查询
			else if(SERVICE.equals(IpadConstant.CODE4)){
				CompositeData APP_HEAD = cd.getStruct("APP_HEAD");
				String limit = APP_HEAD.getField("TOTAL_NUM").strValue();
				String page = APP_HEAD.getField("CURRENT_NUM").strValue();
				if(StringUtils.isNotEmpty(limit) && StringUtils.isNotEmpty(page)){
					page = Integer.parseInt(page)-1 + "";
				}
				
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadQueryCustomerInfoList(BODY,limit,page));
			}
			//1100300000132 微贷客户信息查询
			else if(SERVICE.equals(IpadConstant.CODE5)){
				CompositeData tmp = ipadCommonService.ipadQueryCustomerInfo(BODY);
				if(tmp == null){
					respCd = this.getRespCdCommon("查无结果",IpadConstant.RET_CODE_EMPTY,null);
				}
				else{
					respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,tmp);
				}
			}
			//0200300001201 微贷申请信息查询
			else if(SERVICE.equals(IpadConstant.CODE6)){
				CompositeData APP_HEAD = cd.getStruct("APP_HEAD");
				String limit = APP_HEAD.getField("TOTAL_NUM").strValue();
				String page = APP_HEAD.getField("CURRENT_NUM").strValue();
				if(StringUtils.isNotEmpty(limit) && StringUtils.isNotEmpty(page)){
					page = Integer.parseInt(page)-1 + "";
				}
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadQueryApplyInfoList(BODY,limit,page));
			}
			//0200300001301微贷贷后检查任务查询
			else if(SERVICE.equals(IpadConstant.CODE7)){
				CompositeData APP_HEAD = cd.getStruct("APP_HEAD");
				String limit = APP_HEAD.getField("TOTAL_NUM").strValue();
				String page = APP_HEAD.getField("CURRENT_NUM").strValue();
				if(StringUtils.isNotEmpty(limit) && StringUtils.isNotEmpty(page)){
					page = Integer.parseInt(page)-1 + "";
				}
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadQueryDhjcist(BODY,limit,page));
			}
			//0200200001401微贷任务提醒
			else if(SERVICE.equals(IpadConstant.CODE8)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadWdtxList(BODY));
			}
			//0200300000264到期微贷信息查询
			else if(SERVICE.equals(IpadConstant.CODE9)){
				CompositeData APP_HEAD = cd.getStruct("APP_HEAD");
				String limit = APP_HEAD.getField("TOTAL_NUM").strValue();
				String page = APP_HEAD.getField("CURRENT_NUM").strValue();
				if(StringUtils.isNotEmpty(limit) && StringUtils.isNotEmpty(page)){
					page = Integer.parseInt(page)-1 + "";
				}
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadDqwdList(BODY,limit,page));
			}
			//0200200001002进件申请
			else if(SERVICE.equals(IpadConstant.CODE10)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadAddCustomerApplyInfo(BODY));
			}
			//0200200001009进件资料上传
			else if(SERVICE.equals(IpadConstant.CODE11)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadApplyUpload(BODY));
			}
			//0200300001401微贷申请审批信息查询
			else if(SERVICE.equals(IpadConstant.CODE12)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadWdAuditInfo(BODY));
			}
			//0200300001402微贷申请审批流程查询
			else if(SERVICE.equals(IpadConstant.CODE13)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadWdApplyProcess(BODY));
			}
			//0200300000265微贷客户用信查询
			else if(SERVICE.equals(IpadConstant.CODE14)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadKhyxInfo(BODY));
			}
			//0200200001010微贷贷后检查信息新增
			else if(SERVICE.equals(IpadConstant.CODE15)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadDhJcAddInfo(BODY));
			}
			//0200300001202微贷进件信息查询
			else if(SERVICE.equals(IpadConstant.CODE16)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadWdApplyInfoList(BODY));
			}
			//0200300001302微贷贷后检查信息查询
			else if(SERVICE.equals(IpadConstant.CODE17)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadDhJcSearchInfo(BODY));
			}
			//0200300000266微贷数据字典查询
			else if(SERVICE.equals(IpadConstant.CODE18)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadDictInfo(BODY));
			}
			//0200300000267微贷专案信息查询
			else if(SERVICE.equals(IpadConstant.CODE19)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadZaInfo(BODY));
			}
			//0200300001403微贷申请审批查询
			else if(SERVICE.equals(IpadConstant.CODE20)){
				respCd = this.getRespCdCommon("成功",IpadConstant.RET_CODE_SUCCESS,ipadCommonService.ipadWdspInfoList(BODY));
			}
			else{
				respCd = this.getRespCdCommon("无此交易",IpadConstant.RET_CODE_ERROR,null);
			}
			
			logger.info("服务端获返回的报文 [" + respCd + "]");
			return respCd;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("服务端交易处理失败:",e);
			return this.getRespCdCommon(e.getMessage(),IpadConstant.RET_CODE_ERROR,null);
		}
	}

	public CompositeData getRespCdCommon(String msg, String code,CompositeData body) {
		CompositeData resp = new CompositeData();
		CompositeData sys_head = new CompositeData();

		Field ret_status = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
		ret_status.setValue("S");
		sys_head.addField("RET_STATUS", ret_status);

		Array array = new Array();
		CompositeData struct1 = new CompositeData();
		Field ret_msg = new Field(new FieldAttr(FieldType.FIELD_STRING, 42));
		ret_msg.setValue(msg);
		Field ret_code = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
		ret_code.setValue(code);
		struct1.addField("RET_MSG", ret_msg);
		struct1.addField("RET_CODE", ret_code);

		array.addStruct(struct1);
		sys_head.addArray("RET", array);

		resp.addStruct("SYS_HEAD", sys_head);

		if(body == null){
			resp.addStruct("BODY", new CompositeData());	
		}else{
			resp.addStruct("BODY", body);
		}
		return resp;
	}
}
