package com.cardpay.pccredit.intopieces.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
		
	/* 操作失败 */
	public static String FAIL_MESSAGE = "操作失败";
	
	/* 操作成功 */
	public static String SUCCESS_MESSAGE = "操作成功";
	
	/* 操作成功 */
	public static String UPLOAD_SUCCESS_MESSAGE = "导出并上传成功";
	
	/* 文件为空 */
	public static String FILE_EMPTY = "文件不可为空";
	
	/* 结果集 */
	public static String RESULT_LIST1 = "resultList1";
	
	/* 结果集 */
	public static String RESULT_LIST2 = "resultList2";
	
	/* 影像资料上传路径 */
	public static String FILE_PATH = "/home/pccredit/pccredit_upload/";
	
	/* 保存进件*/
	public static String SAVE_INTOPICES = "save";//泉州用到(进件)
	public static String SAVE_INTOPICES_CN = "暂存";//泉州用到(进件)
	
	/* 申请进件*/
	public static String APPROVE_INTOPICES = "audit";  //泉州用到(进件/冻结)
	public static String APPROVE_INTOPICES_CN = "已申请";
	/* 拒绝进件*/
	public static String REFUSE_INTOPICES = "refuse";//泉州用到(进件/冻结)
	public static String REFUSE_INTOPICES_CN = "拒绝";
	/* 成功进件*/
	public static String SUCCESS_INTOPICES = "success";
	
	/* 退件*/
	public static String NOPASS_INTOPICES = "nopass";
	/* 审核退回*/
	public static String TRTURN_INTOPICES = "RETURNAPPROVE";//泉州用到(进件/冻结)
	public static String TRTURN_INTOPICES_CN = "审核退回";
	/* 退件到申请状态*/
	public static String RETURN_INTOPICES = "returnedToFirst";//泉州用到(进件/冻结)
	public static String RETURN_INTOPICES_CN = "退回客户经理";
	/* 申请已通过*/
	public static String APPROVED_INTOPICES = "approved";//泉州用到(进件/冻结)
	public static String APPROVED_INTOPICES_CN = "审批通过";
	/*以下是上传状态*/
	public static String INITIAL_INTOPICES="initial";
	
	public static String  EXPORT_INTOPICES="export";
	
	public static String  UPLOAD_INTOPICES="upload";
	
	
	
	
	/*联系人*/
	public static String CONTACTID = "contactId";
	
	/*担保人*/
	public static String GUARANTORID = "guarantorId";
	
    /*推荐人*/
	public static String RECOMMENDID = "recommendId";
	
	/**
	 * 定时生成 默认用户
	 */
	public static String SCHEDULES_SYSTEM_USER = "system";
	
	
	/*FTP链接配置*/
	public static String FTPIP = "11.23.11.43";
	
	public static String FTPPORT = "21";
	
	public static String FTPUSERNAME = "root";
	
	public static String FTPPASSWORD = "abc,123";
	
	public static String FTPPATH = "qiankang";
	/*进件信息*/
	public static String APP_STATE_1="已申请";
	public static String APP_STATE_2="未申请";
	public static String APP_STATE_3="退回";
	public static String APP_STATE_5="拒件";
	public static String APP_STATE_4="审批完成";
	
	//泉州银行根机构id
	public static String QZ_ORG_ROOT_ID = "9350000000";
	
	/*默认产品*/
	public static String DEFAULT_TYPE="1";

	/*进件状态*/
	public static String status_shenqing="申请";
	public static String status_chushen="初审";
	public static String status_xingzheng1="内部审查";
	public static String status_shouxin="授信审批";
	public static String status_zhongxin="中心复核";
	public static String status_xinshen="填写合同信息";
	public static String status_xingzheng2="中心终审";
	public static String jyd_id="00001";//决议单默认ID
	public static String htd_id="00002";//合同单默认ID
	public static String status_buchong="补充上会";
	public static String status_scan="影像补扫";
	public static String status_onelevel="行长";
	public static String status_twolevel="团队长";
	public static String status_cardquery="卡中心进件查询";
	public static String status_anjudai="安居贷进件查询";
	public static String status_query="进件查询";
	/*审批结果*/
	public static String APPLN_TYPE_1="审批通过";
	public static String APPLN_TYPE_2="退件";
	public static String APPLN_TYPE_3="拒件";
	
	/*审批特殊标志*/
	public static String APPROVE_EDIT_1="1";//初审退回--直接删除申请件
	public static String APPROVE_EDIT_2="3";//初审拒件--授信岗审批
	public static String APPROVE_EDIT_3="4";//授信岗补充上会--退回客户经理
	
	/*待决策资料清单列表*/
	//<option value="1">工薪类</option>
	public static Map<Integer,String> ATT_BATCH_1 = new HashMap<Integer,String>(){{
		put(1,"合同扫描件");
		
		put(2,"贷款申请表");put(4,"调查报告");put(8,"征信查询授权书");
		put(16,"工作底稿");put(32,"信用报告及联网核查");put(64,"贷审小组决议表");
		put(128,"规范操作承诺书");put(256,"收入证明文件");put(512,"借款人资产文件、住址证明");
		put(1024,"借款人及共同借款人身份证复印件");put(2048,"借款人及共同借款人婚姻状况证明");
		put(4096,"担保人及配偶身份证明复印件");put(8192,"担保人及配偶婚姻状况说明");
		put(16384,"担保人收入证明");
		
		put(1073741824,"其他");
	}};
	
	//<option value="2">经营类</option>
	public static Map<Integer,String> ATT_BATCH_2 = new HashMap<Integer,String>(){{
		put(1,"合同扫描件");
		
		put(4,"调查报告");put(8,"征信查询授权书");
		put(16,"工作底稿");put(32,"信用报告及联网核查");put(64,"贷审小组决议表");
		put(128,"规范操作承诺书");
		
		put(2,"申请表");put(256,"营业执照（副本）复印件");put(512,"税务登记证（副本）复印件");
		put(1024,"组织机构代码证（副本）复印件");put(2048,"贷款卡及年检报告复印件");
		put(4096,"公司章程（包括验资报告复印件）");put(8192,"股东会或董事会决议原件");
		put(16384,"法定代表人身份证明文件复印件");put(32768,"股东身份证明复印件");
		put(65536,"担保人身份证明复印件");put(131072,"担保人贷款卡复印件");
		put(262144,"担保人收入证明");put(524288,"信用等级证复印件（如有）");
		
		put(1073741824,"其他");
	}};
	
	//影像空间放置路劲
	public static String OCX_FILEPATH="/home/pccredit/sunds_ocx.exe";//生产环境
	
	//其他流程配置
	public static String QUOTAFREEZEORTHAW_ID = "100000";
	public static String HETONG_ID = "200000";
}
