package com.cardpay.pccredit.intopieces.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentDetail;
import com.cardpay.pccredit.intopieces.web.Pic;
import com.cardpay.pccredit.intopieces.web.PicPojo;
import com.cardpay.pccredit.ipad.util.SundsException;
import com.sunyard.TransEngine.client.ClientApi;
import com.sunyard.TransEngine.doc.ECMDoc;
import com.sunyard.TransEngine.exception.SunTransEngineException;
import com.sunyard.TransEngine.util.OptionKey;
import com.sunyard.TransEngine.util.key.DocKey;
import com.sunyard.TransEngine.util.key.DocPartKey;

@Service
public class SundsHelper{
	public final Logger logger = Logger.getLogger(SundsHelper.class);
	
	public SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
	
	//测试环境
	private String ip = "168.168.241.75";
	private String port = "8023";
	private String username = "admin";
	private String password = "111";
	//生产环境
//	private String ip = "168.168.214.57";
//	private String port = "8023";
//	private String username = "admin";
//	private String password = "111";
		
	private String IMAGE_URL = "http://" + ip + ":9080/sunECM";
	
	//登录
	public void login() throws SunTransEngineException {
		ClientApi api = new ClientApi(ip, port, username, password);
		api.login();
		logger.info("影像平台登录成功");
	}
	
	//登出
	public void logOut() throws SunTransEngineException {
		ClientApi api = new ClientApi(ip, port, username);
		api.logout();
		logger.info("影像平台登出成功");
	}
	
	//检出
	public void checkIn(String batchId) throws SunTransEngineException {
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		// 必传信息， 设置检入批次号, 批次号长度小于48位
		doc.setBatchID(batchId);
		// 必传信息， 设置业务模型名
		doc.setObjName("WD_CASES");
		// 必传信息， 设置批次业务开始时间，时间格式为YYYYMMDD
		doc.setQueryTime(formatter8.format(new Date()));
		// 调用检入接口
		api.checkin(doc);
		
		logger.info("检入成功");
	}
	
	public void checkOut(String batchId) throws SunTransEngineException {
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		// 必选信息， 设置检出批次号, 批次号长度小于48位
		doc.setBatchID(batchId);
		// 必选信息， 设置业务模型英文名
		doc.setObjName("WD_CASES");
		// 必选信息， 设置批次业务开始时间，时间格式为YYYYMMDD
		doc.setQueryTime(formatter8.format(new Date()));
		// 调用检出接口
		String check_key = api.checkout(doc);
		
		logger.info("检出成功" + check_key);
	}
	
	//上传
	public void add(String batchId,String batchName,List<QzApplnAttachmentDetail> details_ls,String docId) throws SunTransEngineException {
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 开始上传!");
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		doc.setObjName("WD_CASES");
		doc.setBatchID(docId);
		doc.setBusiAttribute(DocKey.BUSI_START_TIME, formatter8.format(new Date()));
		doc.setBusiAttribute("AMOUNT", "1");
		doc.setBusiAttribute("CASEVER", "1");
		doc.setBusiAttribute("CASEUSERID", "1234567");
		doc.setBusiAttribute("CASEORGAN", "123456");
		doc.setBusiAttribute("CASEDATE", formatter8.format(new Date()));
		doc.setBusiAttribute("CASEMODF", "1");
		doc.setBusiAttribute("CASEID", docId);
		doc.setBusiAttribute("CASETYPE", "123456");
		doc.setBusiAttribute("APPID", "WD_CASES");
		// 必传信息， 设置文档部件英文名
		doc.beginFilePart("WD_IMAGES");

		for(int i=0;i<details_ls.size();i++){
			// 必传信息， 开始 设置图片信息
			doc.beginFile();
			doc.setFileAttribute("IMAGECONFKIND", "123456");
			doc.setFileAttribute("IMAGECOORD", "-1,-1");
			doc.setFileAttribute("IMAGENAME", details_ls.get(i).getId()+ "." +details_ls.get(i).getFileName().split("\\.")[1]);//用detail_id 做文件名来关联数据库
			doc.setFileAttribute("IMAGEDATE", formatter8.format(new Date()));
			doc.setFileAttribute("IMAGEFORMNO", "0");
			doc.setFileAttribute("IMAGENUMBER", Integer.toString(i + 1));
			doc.setFileAttribute("IMAGETYPE", "1234");
			doc.setFileAttribute("IMAGEVERFLG", "0");
			doc.setFileAttribute("IMAGEMODF", "1");
			doc.setFileAttribute("IMAGEMD5", "1");// 待定
			doc.setFileAttribute("IMAGEURL", IMAGE_URL);
			doc.setFileAttribute("IMAGEVER", "12345678");
			doc.setFileAttribute(DocPartKey.FILE_BISYTYPE, "1");
			doc.setFileAttribute(DocPartKey.VALID_PERIOD, "20991010121212");
			doc.setFileAttribute(DocPartKey.FILE_FORMAT, "jpg");
			doc.setFileAttribute(DocPartKey.UPLOAD_USER, "lch");
			doc.setFileAttribute(DocPartKey.PAGENUM, Integer.toString(i + 1));
			doc.setFileAttribute(DocPartKey.FILE_SIZE, details_ls.get(i).getPicSize());
			doc.setFileAttribute("FILE_NAME", details_ls.get(i).getId()+ "." +details_ls.get(i).getFileName().split("\\.")[1]);//用detail_id 做文件名来关联数据库
			doc.setFileAttribute("IMAGESIZE", details_ls.get(i).getPicSize());
			doc.setFileAttribute("IMAGESIDEFLAG", "0");
			doc.setFileAttribute("IMAGEDPI", "300");
			doc.setFileAttribute("FILE_PATH", Constant.FILE_PATH + batchId + "/" + details_ls.get(i).getFileName());
			// 必传信息， 设置文件路径和是否进行MD5校验
			doc.setFile(Constant.FILE_PATH + batchId + "/" + details_ls.get(i).getFileName(), false);
			doc.endFile();
		}
		
		// 必传信息， 结束设置文档部件
		doc.endFilePart();
		// 调用上传接口
		api.add(doc);
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 上传成功!");
	}

	//更新
	public void update(String batchId,String batchName,List<QzApplnAttachmentDetail> details_ls,String docId) throws SunTransEngineException {
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 开始单张更新!");
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		doc.setBreakPointResume(false);
		doc.setBatchID(docId);
		doc.setObjName("WD_CASES");
		doc.setQueryTime(formatter8.format(new Date()));

		doc.beginFilePart("WD_IMAGES");
		
		for(int i=0;i<details_ls.size();i++){
			doc.beginFile();
			doc.setFileAttribute(DocPartKey.FILE_NO, details_ls.get(i).getFileNo());
			doc.setFileAttribute("IMAGENUMBER", Integer.toString(i + 1));
			doc.setFile(Constant.FILE_PATH + batchId + "/" + details_ls.get(i).getFileName(), false);
			doc.setUpdateFlag(OptionKey.U_REPLACE);
			doc.endFile();
		}

		doc.endFilePart();
		// 可选信息，更新时自动检入、检出
		doc.setUpdateCheckOutAndIn(true, "admin");
		// 调用更新接口
		api.update(doc);
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 单张更新成功!");
	}
	
	//批量追加
	public void addMore(String batchId,String batchName,List<QzApplnAttachmentDetail> details_ls,String docId) throws SunTransEngineException {
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 开始批量更新!");
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		doc.setBreakPointResume(false);
		doc.setBatchID(docId);
		doc.setObjName("WD_CASES");
		doc.setQueryTime(formatter8.format(new Date()));

		doc.beginFilePart("WD_IMAGES");
		
		for(int i=0;i<details_ls.size();i++){
			doc.beginFile();
			// 必传信息
			doc.setFileAttribute("IMAGECONFKIND", "123456");
			doc.setFileAttribute("IMAGECOORD", "-1,-1");
			doc.setFileAttribute("IMAGENAME", details_ls.get(i).getId()+ "." +details_ls.get(i).getFileName().split("\\.")[1]);//用detail_id 做文件名来关联数据库
			doc.setFileAttribute("IMAGEDATE", formatter8.format(new Date()));
			doc.setFileAttribute("IMAGEFORMNO", "0");
			doc.setFileAttribute("IMAGENUMBER", Integer.toString(i + 1));
			doc.setFileAttribute("IMAGETYPE", "1234");
			doc.setFileAttribute("IMAGEVERFLG", "0");
			doc.setFileAttribute("IMAGEMODF", "1");
			doc.setFileAttribute("IMAGEMD5", "1");// 待定
			doc.setFileAttribute("IMAGEURL", IMAGE_URL);
			doc.setFileAttribute("IMAGEVER", "12345678");
			doc.setFileAttribute(DocPartKey.FILE_BISYTYPE, "1");
			doc.setFileAttribute(DocPartKey.VALID_PERIOD, "20991010121212");
			doc.setFileAttribute(DocPartKey.FILE_FORMAT, "jpg");
			doc.setFileAttribute(DocPartKey.UPLOAD_USER, "lch");
			doc.setFileAttribute(DocPartKey.PAGENUM, Integer.toString(i + 1));
			doc.setFileAttribute(DocPartKey.FILE_SIZE, details_ls.get(i).getPicSize());
			doc.setFileAttribute("FILE_NAME", details_ls.get(i).getId()+ "." +details_ls.get(i).getFileName().split("\\.")[1]);//用detail_id 做文件名来关联数据库
			doc.setFileAttribute("IMAGESIZE", details_ls.get(i).getPicSize());
			doc.setFileAttribute("IMAGESIDEFLAG", "0");
			doc.setFileAttribute("IMAGEDPI", "300");
			doc.setFileAttribute("FILE_PATH", Constant.FILE_PATH + batchId + "/" + details_ls.get(i).getFileName());
			// 必传信息， 设置文件路径和是否进行MD5校验
			doc.setFile(Constant.FILE_PATH + batchId + "/" + details_ls.get(i).getFileName(), false);
			// 必传信息， 设置更新标识， 以下标识为追加
			doc.setUpdateFlag(OptionKey.U_ADD);
			// 必传信息， 结束设置
			doc.endFile();
		}

		doc.endFilePart();
		// 可选信息，更新时自动检入、检出
		doc.setUpdateCheckOutAndIn(true, "admin");
		// 调用更新接口
		api.update(doc);
		logger.info("批次["+batchName+"] 批次号["+batchId+"] 批量更新成功!");
	}
	
	//搜索批次下的文件
	public String queryBatchFile(String batch_id,String docId) throws SunTransEngineException {
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		// 必选信息， 设置要查询的批注号
		doc.setBatchID(docId);
		// 必选信息， 设置业务模型英文名
		doc.setObjName("WD_CASES");
		// 必选信息， 设置查询标识，以下设置为查询批次文件信息
		doc.setOption(OptionKey.QUERY_BATCH_FILE);
		// 必选信息， 设置批次业务开始时间
		doc.setQueryTime(formatter8.format(new Date()));
		String xmlStr = api.queryFile(doc);
		logger.info("批次号["+batch_id+"] 查询文件成功:xml="+xmlStr);
		return xmlStr;
	}
	
	public Pic parseXml(String xmlStr,int page,int limit) throws DocumentException, SundsException{
		int start = page*limit;
		int end = (page+1)*limit;
		List<PicPojo> pics = new ArrayList<PicPojo>();
		Pic pic = new Pic();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element rootElt = doc.getRootElement();
		Element imagesElt = rootElt.element("IMAGES");
		if(imagesElt == null){
			throw new SundsException("无文件");
		}
		Iterator iter = imagesElt.elementIterator("IMAGE");
		int totalCount = 0;
		while(iter.hasNext()){
			Element imageElt = (Element)iter.next();
			if(totalCount>=start && totalCount<end){
				String url = imageElt.attributeValue("URL");
				String file_no = imageElt.attributeValue("FILE_NO");
				String file_name = imageElt.attributeValue("IMAGENAME");
				String doc_id = imageElt.attributeValue("BATCH_ID");
				String pic_size = imageElt.attributeValue("FILE_SIZE");
				PicPojo pojo = new PicPojo();
				pojo.setDoc_id(doc_id);
				pojo.setFile_name(file_name);
				pojo.setFile_no(file_no);
				pojo.setUrl(url);
				pojo.setPic_size(pic_size);
				pics.add(pojo);
			}
			totalCount ++;
		}
		pic.setTotalCount(totalCount);
		pic.setPics(pics);
		
		return pic;
	}
	
	public void del(String file_no,String doc_id,String batchId) throws SunTransEngineException {
		logger.info("批次号["+batchId+"] 文件号["+file_no+"] 开始单张删除!");
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		doc.setBreakPointResume(false);
		doc.setBatchID(doc_id);
		doc.setObjName("WD_CASES");
		doc.setQueryTime(formatter8.format(new Date()));

		doc.beginFilePart("WD_IMAGES");
		// 删除
		doc.beginFile();
		doc.setFileAttribute(DocPartKey.FILE_NO, file_no);
		//doc.setFileAttribute("IMAGENUMBER", "1");
		//doc.setFile("E:\\image\\1.jpg", true);
		doc.setUpdateFlag(OptionKey.U_DEL);
		doc.endFile();

		doc.endFilePart();
		// 可选信息，更新时自动检入、检出
		doc.setUpdateCheckOutAndIn(true, "admin");
		// 调用更新接口
		api.update(doc);
		logger.info("批次号["+batchId+"] 文件号["+file_no+"] 单张删除成功!");
	}
	
	public void delBatch(String batchId,String docId) throws SunTransEngineException {
		logger.info("批次号["+batchId+"] 开始批次删除!");
		ClientApi api = new ClientApi(ip, port, username, password);
		ECMDoc doc = new ECMDoc();
		// 必传信息， 设置批次号
		doc.setBatchID(docId);
		// 必传信息， 设置业务模型英文名
		doc.setObjName("WD_CASES");
		// 必传信息， 设置批次业务开始时间, 时间格式为YYYYMMDD
		doc.setQueryTime(formatter8.format(new Date()));
		doc.beginFilePart("WD_IMAGES");
		// 物理删除
		// doc.setOption(OptionKey.PHYSICAL_DEL);
		// 必传信息， 设置删除条件 物理删除：OptionKey.PHYSICAL_DEL；
		// OptionKey.LOGICAL_DEL：逻辑删除
		doc.setOption(OptionKey.PHYSICAL_DEL);
		// 调用删除接口
		api.del(doc);
		logger.info("批次号["+batchId+"] 批次删除成功!");
	}
}
