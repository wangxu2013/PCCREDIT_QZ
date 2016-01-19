package com.cardpay.pccredit.ipad.constant;

import java.text.SimpleDateFormat;

public class IpadConstant {
	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";
	public static final String LOGINNOTNULL = "账号或密码为空";
	public static final String LOGINSUCCESS = "登陆成功";
	public static final String LOGINFAIL = "登陆失败";
	public static final String CREATESUCCESS = "添加成功";
	public static final String CREATEFAIL = "添加失败"; 
	public static final String SYSTEMERROR = "系统异常";
	
	public static final String RET_CODE_SUCCESS = "000000";//成功时的返回值
	public static final String RET_CODE_EMPTY = "888888";//空时的返回值
	public static final String RET_CODE_ERROR = "9999";//失败时的返回值
	
	public static final String CODE1  = "1100300000709"; //账户信息查询(11003000007)	  微贷用户信息查询(09)
	public static final String CODE2  = "1100200001927"; //客户信息维护（11002000019）            微贷客户信息新增(27)
	public static final String CODE3  = "1100200001928"; //客户信息维护（11002000019）            微贷客户位置更新(28)
	public static final String CODE4  = "1100300000131"; //客户信息查询(11003000001)	  微贷客户信息查询(31)
	public static final String CODE5  = "1100300000132"; //客户信息查询(11003000001)	  微贷客户详细信息查询(32)
	public static final String CODE6  = "0200300001201"; //贷款申请查询(02003000012)	  微贷申请信息查询(01)
	public static final String CODE7  = "0200300001301"; //贷后检查信息查询(02003000013)  微贷贷后检查任务查询(01)
	public static final String CODE8  = "0200200001401"; //贷款任务提醒(02002000014)     微贷任务提醒(01)
	public static final String CODE9  = "0200300000264"; //贷款信息查询(02003000002)     到期/逾期微贷信息查询(64)
	public static final String CODE10 = "0200200001002"; //贷款信息维护(02002000010)     进件申请(02)
	public static final String CODE11 = "0200200001009"; //贷款信息维护(02002000010)	   微贷进件信息新增(09)
	public static final String CODE12 = "0200300001401"; //贷款审批信息查询(02003000014)	   微贷申请审批信息查询(01)
	public static final String CODE13 = "0200300001402"; //贷款审批信息查询(02003000014)	   微贷申请审批流程查询(02)
	public static final String CODE14 = "0200300000265"; //贷款信息查询(02003000002)  	   微贷客户用信查询(65)
	public static final String CODE15 = "0200200001010"; //贷款信息维护(02002000010)	   微贷贷后检查信息新增(10)
	public static final String CODE16 = "0200300001202"; //贷款申请查询(02003000012)	   微贷进件信息查询(02)
	public static final String CODE17 = "0200300001302"; //贷后检查信息查询(02003000013)	   微贷贷后检查信息查询(02)
	public static final String CODE18 = "0200300000266"; //贷款信息查询(02003000002)	   微贷数据字典查询(66)
	public static final String CODE19 = "0200300000267"; //贷款信息查询(02003000002)	   微贷专案信息查询(67)
	public static final String CODE20 = "0200300001403"; //贷款审批信息查询(02003000014)	   微贷申请审批查询(03)
	


	//CODE1
	public static final String QUERY_TYPE_ALL = "0";//全部
	public static final String QUERY_TYPE_WSX = "1";//未授信
	public static final String QUERY_TYPE_WJQ = "2";//未结清
	public static final String QUERY_TYPE_YJQ = "3";//已结清
	
	//微贷申请交易类型
	public static final String QUERY_TYPE_01 = "01";//01-查询已进件贷款申请列表
	public static final String QUERY_TYPE_02 = "02";//02-查询贷款申请待处理补退件列表
	public static final String QUERY_TYPE_03 = "03";//03-查询贷中客户列表
	
	
	public static final String CODE99 = "1100300000131";	
} 
