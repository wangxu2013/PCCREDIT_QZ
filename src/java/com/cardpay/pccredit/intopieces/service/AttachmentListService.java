package com.cardpay.pccredit.intopieces.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.divisional.constant.DivisionalProgressEnum;
import com.cardpay.pccredit.divisional.constant.DivisionalTypeEnum;
import com.cardpay.pccredit.divisional.service.DivisionalService;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.IntoPiecesException;
import com.cardpay.pccredit.intopieces.dao.AttachmentListDao;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationIntopieceWaitDao;
import com.cardpay.pccredit.intopieces.dao.DbrxxDao;
import com.cardpay.pccredit.intopieces.dao.IntoPiecesDao;
import com.cardpay.pccredit.intopieces.dao.comdao.IntoPiecesComdao;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.filter.MakeCardFilter;
import com.cardpay.pccredit.intopieces.model.ApplicationDataImport;
import com.cardpay.pccredit.intopieces.model.BasicCustomerInformationS;
import com.cardpay.pccredit.intopieces.model.CustomerAccountData;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationCom;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.CustomerCareersInformationS;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.MakeCard;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentListAdd;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.QzApplnProcessResult;
import com.cardpay.pccredit.intopieces.model.QzApplnSxjc;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.cardpay.pccredit.intopieces.model.VideoAccessories;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.intopieces.web.QzApplnSxjcForm;
import com.cardpay.pccredit.intopieces.web.QzDcnrUploadForm;
import com.cardpay.pccredit.intopieces.web.QzSdhjydForm;
import com.cardpay.pccredit.intopieces.web.QzShouxinForm;
import com.cardpay.pccredit.product.model.AddressAccessories;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.workflow.dao.WfStatusResultDao;
import com.cardpay.workflow.models.WfProcessRecord;
import com.cardpay.workflow.models.WfStatusQueueRecord;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.modules.privilege.model.TreeNode;
import com.wicresoft.util.spring.Beans;

@Service
public class AttachmentListService {

	// TODO 路径使用相对路径，首先获得应用所在路径，之后建立上传文件目录，图片类型使用IMG，文件使用DOC

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IntoPiecesDao intoPiecesDao;

	@Autowired
	private IntoPiecesComdao intoPiecesComdao;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private DivisionalService divisionalService;
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;

	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;
	
	@Autowired
	private WfStatusResultDao wfStatusResultDao;
	
	@Autowired
	private AttachmentListDao attachmentListDao;
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
	/* 查询附件列表 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public QzApplnAttachmentList findAttachmentList(String customerId,String applicationId) {
		return attachmentListDao.findAttachmentList(customerId, applicationId);
	}

	public QzApplnAttachmentList findAttachmentListByAppId(String applicationId) {
		return attachmentListDao.findAttachmentListByAppId(applicationId);
	}
	
	public TreeNode findAttachmentListJsonByAppId(String applicationId) {
		QzApplnAttachmentList qzApplnAttachmentList = attachmentListDao.findAttachmentListByAppId(applicationId);
		TreeNode rootNode = new TreeNode("root", null,"资料清单", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeGx = new TreeNode("gx", null,"工薪类", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeJy = new TreeNode("jy", null,"经营类", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeHt = new TreeNode("ht", null,"合同扫描件", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeYw = new TreeNode("yw", null,"业务信息", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeKh = new TreeNode("kh", null,"客户信息", "", "", "", "folderopen.gif",false, true, true, true, false);
		TreeNode rootNodeQt = new TreeNode("qt", null,"其他", "", "", "", "folderopen.gif",false, true, true, true, false);
		if(qzApplnAttachmentList != null && qzApplnAttachmentList.getChkValue() != null && !qzApplnAttachmentList.getChkValue().equals("0")){
			String chkValue = qzApplnAttachmentList.getChkValue();
			String uploadValue = qzApplnAttachmentList.getUploadValue();
			if(qzApplnAttachmentList.getBussType().equals("1")){//工薪类
				if((Integer.parseInt(chkValue)&1) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1", null,Constant.ATTACH_LIST0, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1",Constant.ATTACH_LIST0);
					rootNodeHt.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&2) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"2", null,Constant.ATTACH_LIST1, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"2",Constant.ATTACH_LIST1);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&4) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"4", null,Constant.ATTACH_LIST2, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"4",Constant.ATTACH_LIST2);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&8) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"8", null,Constant.ATTACH_LIST3, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"8",Constant.ATTACH_LIST3);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&16) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"16", null,Constant.ATTACH_LIST4, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"16",Constant.ATTACH_LIST4);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&32) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"32", null,Constant.ATTACH_LIST5, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"32",Constant.ATTACH_LIST5);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&64) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"64", null,Constant.ATTACH_LIST6, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"64",Constant.ATTACH_LIST6);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&128) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"128", null,Constant.ATTACH_LIST7, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"128",Constant.ATTACH_LIST7);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&256) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"256", null,Constant.ATTACH_LIST8, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"256",Constant.ATTACH_LIST8);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&512) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"512", null,Constant.ATTACH_LIST9, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"512",Constant.ATTACH_LIST9);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&1024) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1024", null,Constant.ATTACH_LIST10, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1024",Constant.ATTACH_LIST10);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&2048) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"2048", null,Constant.ATTACH_LIST11, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"2048",Constant.ATTACH_LIST11);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&4096) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"4096", null,Constant.ATTACH_LIST12, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"4096",Constant.ATTACH_LIST12);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&8192) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"8192", null,Constant.ATTACH_LIST13, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1",Constant.ATTACH_LIST13);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&16384) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"16384", null,Constant.ATTACH_LIST14, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"8192",Constant.ATTACH_LIST14);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&1073741824) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1073741824", null,Constant.ATTACH_LIST99, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1073741824",Constant.ATTACH_LIST99);
					rootNodeQt.addChildren(tmp);
				}
				rootNodeGx.addChildren(rootNodeHt);
				rootNodeGx.addChildren(rootNodeYw);
				rootNodeGx.addChildren(rootNodeKh);
				rootNodeGx.addChildren(rootNodeQt);
				rootNode.addChildren(rootNodeGx);
			}
			if(qzApplnAttachmentList.getBussType().equals("2")){
				if((Integer.parseInt(chkValue)&1) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1", null,Constant.ATTACH_LIST0, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1",Constant.ATTACH_LIST0);
					rootNodeHt.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&2) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"2", null,Constant.ATTACH_LIST15, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"2",Constant.ATTACH_LIST15);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&4) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"4", null,Constant.ATTACH_LIST2, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"4",Constant.ATTACH_LIST2);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&8) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"8", null,Constant.ATTACH_LIST3, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"8",Constant.ATTACH_LIST3);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&16) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"16", null,Constant.ATTACH_LIST4, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"16",Constant.ATTACH_LIST4);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&32) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"32", null,Constant.ATTACH_LIST5, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"32",Constant.ATTACH_LIST5);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&64) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"64", null,Constant.ATTACH_LIST6, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"64",Constant.ATTACH_LIST6);
					rootNodeYw.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&128) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"128", null,Constant.ATTACH_LIST7, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"128",Constant.ATTACH_LIST7);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&256) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"256", null,Constant.ATTACH_LIST16, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"256",Constant.ATTACH_LIST16);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&512) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"512", null,Constant.ATTACH_LIST17, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"512",Constant.ATTACH_LIST17);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&1024) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1024", null,Constant.ATTACH_LIST18, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1024",Constant.ATTACH_LIST18);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&2048) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"2048", null,Constant.ATTACH_LIST19, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"2048",Constant.ATTACH_LIST19);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&4096) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"4096", null,Constant.ATTACH_LIST20, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"4096",Constant.ATTACH_LIST20);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&8192) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"8192", null,Constant.ATTACH_LIST21, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"8192",Constant.ATTACH_LIST21);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&16384) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"16384", null,Constant.ATTACH_LIST22, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"16384",Constant.ATTACH_LIST22);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&32768) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"32768", null,Constant.ATTACH_LIST23, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"32768",Constant.ATTACH_LIST23);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&65536) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"65536", null,Constant.ATTACH_LIST24, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"65536",Constant.ATTACH_LIST24);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&131072) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"131072", null,Constant.ATTACH_LIST25, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"131072",Constant.ATTACH_LIST25);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&262144) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"262144", null,Constant.ATTACH_LIST26, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"262144",Constant.ATTACH_LIST26);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&524288) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"524288", null,Constant.ATTACH_LIST27, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"524288",Constant.ATTACH_LIST27);
					rootNodeKh.addChildren(tmp);
				}
				if((Integer.parseInt(chkValue)&1073741824) != 0){
					TreeNode tmp = new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"1073741824", null,Constant.ATTACH_LIST99, "", "", "", "page.gif",false, true, false, false, false);
					addAttNode(tmp,qzApplnAttachmentList,"1073741824",Constant.ATTACH_LIST99);
					rootNodeQt.addChildren(tmp);
				}
				rootNodeJy.addChildren(rootNodeHt);
				rootNodeJy.addChildren(rootNodeYw);
				rootNodeJy.addChildren(rootNodeKh);
				rootNodeJy.addChildren(rootNodeQt);
				rootNode.addChildren(rootNodeJy);
			}
		}
		return rootNode;
	}
	
	public void insert_page5(QzApplnAttachmentList qzApplnAttachmentList,HttpServletRequest request){
		//保存清单至调查内容表
		intoPiecesService.addAttachList(qzApplnAttachmentList);
		commonDao.insertObject(qzApplnAttachmentList);
		
		//每一项都增加10个附加节点
		String chkValue = qzApplnAttachmentList.getChkValue();
		if(qzApplnAttachmentList.getBussType().equals("1")){
			
			if((Integer.parseInt(chkValue)&1) != 0){
				addAttAdd(qzApplnAttachmentList,"1");
			}
			if((Integer.parseInt(chkValue)&2) != 0){
				addAttAdd(qzApplnAttachmentList,"2");
			}
			if((Integer.parseInt(chkValue)&4) != 0){
				addAttAdd(qzApplnAttachmentList,"4");
			}
			if((Integer.parseInt(chkValue)&8) != 0){
				addAttAdd(qzApplnAttachmentList,"8");
			}
			if((Integer.parseInt(chkValue)&16) != 0){
				addAttAdd(qzApplnAttachmentList,"16");
			}
			if((Integer.parseInt(chkValue)&32) != 0){
				addAttAdd(qzApplnAttachmentList,"32");
			}
			if((Integer.parseInt(chkValue)&64) != 0){
				addAttAdd(qzApplnAttachmentList,"64");
			}
			if((Integer.parseInt(chkValue)&128) != 0){
				addAttAdd(qzApplnAttachmentList,"128");
			}
			if((Integer.parseInt(chkValue)&256) != 0){
				addAttAdd(qzApplnAttachmentList,"256");
			}
			if((Integer.parseInt(chkValue)&512) != 0){
				addAttAdd(qzApplnAttachmentList,"512");
			}
			if((Integer.parseInt(chkValue)&1024) != 0){
				addAttAdd(qzApplnAttachmentList,"1024");
			}
			if((Integer.parseInt(chkValue)&2048) != 0){
				addAttAdd(qzApplnAttachmentList,"2048");
			}
			if((Integer.parseInt(chkValue)&4096) != 0){
				addAttAdd(qzApplnAttachmentList,"4096");
			}
			if((Integer.parseInt(chkValue)&8192) != 0){
				addAttAdd(qzApplnAttachmentList,"8192");
			}
			if((Integer.parseInt(chkValue)&16384) != 0){
				addAttAdd(qzApplnAttachmentList,"16384");
			}
			if((Integer.parseInt(chkValue)&1073741824) != 0){
				addAttAdd(qzApplnAttachmentList,"1073741824");
			}
		}
		else{
			if((Integer.parseInt(chkValue)&1) != 0){
				addAttAdd(qzApplnAttachmentList,"1");
			}
			if((Integer.parseInt(chkValue)&2) != 0){
				addAttAdd(qzApplnAttachmentList,"2");
			}
			if((Integer.parseInt(chkValue)&4) != 0){
				addAttAdd(qzApplnAttachmentList,"4");
			}
			if((Integer.parseInt(chkValue)&8) != 0){
				addAttAdd(qzApplnAttachmentList,"8");
			}
			if((Integer.parseInt(chkValue)&16) != 0){
				addAttAdd(qzApplnAttachmentList,"16");
			}
			if((Integer.parseInt(chkValue)&32) != 0){
				addAttAdd(qzApplnAttachmentList,"32");
			}
			if((Integer.parseInt(chkValue)&64) != 0){
				addAttAdd(qzApplnAttachmentList,"64");
			}
			if((Integer.parseInt(chkValue)&128) != 0){
				addAttAdd(qzApplnAttachmentList,"128");
			}
			if((Integer.parseInt(chkValue)&256) != 0){
				addAttAdd(qzApplnAttachmentList,"256");
			}
			if((Integer.parseInt(chkValue)&512) != 0){
				addAttAdd(qzApplnAttachmentList,"512");
			}
			if((Integer.parseInt(chkValue)&1024) != 0){
				addAttAdd(qzApplnAttachmentList,"1024");
			}
			if((Integer.parseInt(chkValue)&2048) != 0){
				addAttAdd(qzApplnAttachmentList,"2048");
			}
			if((Integer.parseInt(chkValue)&4096) != 0){
				addAttAdd(qzApplnAttachmentList,"4096");
			}
			if((Integer.parseInt(chkValue)&8192) != 0){
				addAttAdd(qzApplnAttachmentList,"8192");
			}
			if((Integer.parseInt(chkValue)&16384) != 0){
				addAttAdd(qzApplnAttachmentList,"16384");
			}
			if((Integer.parseInt(chkValue)&32768) != 0){
				addAttAdd(qzApplnAttachmentList,"32768");
			}
			if((Integer.parseInt(chkValue)&65536) != 0){
				addAttAdd(qzApplnAttachmentList,"65536");
			}
			if((Integer.parseInt(chkValue)&131072) != 0){
				addAttAdd(qzApplnAttachmentList,"131072");
			}
			if((Integer.parseInt(chkValue)&262144) != 0){
				addAttAdd(qzApplnAttachmentList,"262144");
			}
			if((Integer.parseInt(chkValue)&524288) != 0){
				addAttAdd(qzApplnAttachmentList,"524288");
			}
			if((Integer.parseInt(chkValue)&1073741824) != 0){
				addAttAdd(qzApplnAttachmentList,"1073741824");
			}
		}
		
	}

	public void update_page5(QzApplnAttachmentList qzApplnAttachmentList,HttpServletRequest request){
		//保存清单至调查内容表
		intoPiecesService.addAttachList(qzApplnAttachmentList);
		commonDao.updateObject(qzApplnAttachmentList);
		
		//每一项都增加10个附加节点
		String chkValue = qzApplnAttachmentList.getChkValue();
		if(qzApplnAttachmentList.getBussType().equals("1")){
			
			if((Integer.parseInt(chkValue)&1) != 0){
				addAttAdd(qzApplnAttachmentList,"1");
			}
			
			if((Integer.parseInt(chkValue)&2) != 0){
				addAttAdd(qzApplnAttachmentList,"2");
			}
			
			if((Integer.parseInt(chkValue)&4) != 0){
				addAttAdd(qzApplnAttachmentList,"4");
			}
			
			if((Integer.parseInt(chkValue)&8) != 0){
				addAttAdd(qzApplnAttachmentList,"8");
			}
			
			if((Integer.parseInt(chkValue)&16) != 0){
				addAttAdd(qzApplnAttachmentList,"16");
			}
			
			if((Integer.parseInt(chkValue)&32) != 0){
				addAttAdd(qzApplnAttachmentList,"32");
			}
			
			if((Integer.parseInt(chkValue)&64) != 0){
				addAttAdd(qzApplnAttachmentList,"64");
			}
			
			if((Integer.parseInt(chkValue)&128) != 0){
				addAttAdd(qzApplnAttachmentList,"128");
			}
			
			if((Integer.parseInt(chkValue)&256) != 0){
				addAttAdd(qzApplnAttachmentList,"256");
			}
			
			if((Integer.parseInt(chkValue)&512) != 0){
				addAttAdd(qzApplnAttachmentList,"512");
			}
			
			if((Integer.parseInt(chkValue)&1024) != 0){
				addAttAdd(qzApplnAttachmentList,"1024");
			}
			
			if((Integer.parseInt(chkValue)&2048) != 0){
				addAttAdd(qzApplnAttachmentList,"2048");
			}
			
			if((Integer.parseInt(chkValue)&4096) != 0){
				addAttAdd(qzApplnAttachmentList,"4096");
			}
			
			if((Integer.parseInt(chkValue)&8192) != 0){
				addAttAdd(qzApplnAttachmentList,"8192");
			}
			
			if((Integer.parseInt(chkValue)&16384) != 0){
				addAttAdd(qzApplnAttachmentList,"16384");
			}
			
			if((Integer.parseInt(chkValue)&1073741824) != 0){
				addAttAdd(qzApplnAttachmentList,"1073741824");
			}
		}
		else{
			if((Integer.parseInt(chkValue)&1) != 0){
				addAttAdd(qzApplnAttachmentList,"1");
			}
			
			if((Integer.parseInt(chkValue)&2) != 0){
				addAttAdd(qzApplnAttachmentList,"2");
			}
			
			if((Integer.parseInt(chkValue)&4) != 0){
				addAttAdd(qzApplnAttachmentList,"4");
			}
			
			if((Integer.parseInt(chkValue)&8) != 0){
				addAttAdd(qzApplnAttachmentList,"8");
			}
			
			if((Integer.parseInt(chkValue)&16) != 0){
				addAttAdd(qzApplnAttachmentList,"16");
			}
			
			if((Integer.parseInt(chkValue)&32) != 0){
				addAttAdd(qzApplnAttachmentList,"32");
			}
			
			if((Integer.parseInt(chkValue)&64) != 0){
				addAttAdd(qzApplnAttachmentList,"64");
			}
			
			if((Integer.parseInt(chkValue)&128) != 0){
				addAttAdd(qzApplnAttachmentList,"128");
			}
			
			if((Integer.parseInt(chkValue)&256) != 0){
				addAttAdd(qzApplnAttachmentList,"256");
			}
			
			if((Integer.parseInt(chkValue)&512) != 0){
				addAttAdd(qzApplnAttachmentList,"512");
			}
			
			if((Integer.parseInt(chkValue)&1024) != 0){
				addAttAdd(qzApplnAttachmentList,"1024");
			}
			
			if((Integer.parseInt(chkValue)&2048) != 0){
				addAttAdd(qzApplnAttachmentList,"2048");
			}
			
			if((Integer.parseInt(chkValue)&4096) != 0){
				addAttAdd(qzApplnAttachmentList,"4096");
			}
			
			if((Integer.parseInt(chkValue)&8192) != 0){
				addAttAdd(qzApplnAttachmentList,"8192");
			}
			
			if((Integer.parseInt(chkValue)&16384) != 0){
				addAttAdd(qzApplnAttachmentList,"16384");
			}
			
			if((Integer.parseInt(chkValue)&32768) != 0){
				addAttAdd(qzApplnAttachmentList,"32768");
			}
			
			if((Integer.parseInt(chkValue)&65536) != 0){
				addAttAdd(qzApplnAttachmentList,"65536");
			}
			
			if((Integer.parseInt(chkValue)&131072) != 0){
				addAttAdd(qzApplnAttachmentList,"131072");
			}
			
			if((Integer.parseInt(chkValue)&262144) != 0){
				addAttAdd(qzApplnAttachmentList,"262144");
			}
			
			if((Integer.parseInt(chkValue)&524288) != 0){
				addAttAdd(qzApplnAttachmentList,"524288");
			}
			
			if((Integer.parseInt(chkValue)&1073741824) != 0){
				addAttAdd(qzApplnAttachmentList,"1073741824");
			}
		}
	}
	
	public void addAttAdd(QzApplnAttachmentList qzApplnAttachmentList,String pValue){
		QzApplnAttachmentListAdd qzApplnAttachmentListAdd = attachmentListDao.findAttachmentListAddByAttId(qzApplnAttachmentList.getId(), pValue);
		if(qzApplnAttachmentListAdd == null){
			qzApplnAttachmentListAdd = new QzApplnAttachmentListAdd();
			qzApplnAttachmentListAdd.setAttId(qzApplnAttachmentList.getId());
			qzApplnAttachmentListAdd.setParentValue(pValue);
			int v = 1+2+4+8+16+32+64+128+256+512+1024+2048+4096+8192+16384;
			qzApplnAttachmentListAdd.setChkValue(v+"");
			qzApplnAttachmentListAdd.setUploadValue("0");
			commonDao.insertObject(qzApplnAttachmentListAdd);
		}
	}
	
	public void addAttNode(TreeNode rootNode,QzApplnAttachmentList qzApplnAttachmentList,String pValue,String name){
		for(int i = 0;i<15;i++){
			rootNode.addChildren(new TreeNode("id_"+qzApplnAttachmentList.getDocid()+"_"+pValue+"_"+new Double(Math.pow(2, i)).intValue(), null,name+":"+(i+1), "", "", "", "page.gif",false, true, false, true, false));
		}
	}

	public QzApplnAttachmentListAdd findAttachmentListAddByAttId(String attId,String parentValue) {
		return attachmentListDao.findAttachmentListAddByAttId(attId,parentValue);
	}
}
