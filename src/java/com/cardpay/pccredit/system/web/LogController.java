package com.cardpay.pccredit.system.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.system.constants.DictTypeConstants;
import com.cardpay.pccredit.system.filter.DictFilter;
import com.cardpay.pccredit.system.filter.LogFilter;
import com.cardpay.pccredit.system.model.Dict;
import com.cardpay.pccredit.system.service.DictService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 查看生产环境的日志
 */
@Controller
@RequestMapping("/system/log/*")
@JRadModule("system.log")
public class LogController extends BaseController {
	
	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	
	//日志目录
	private String log_path = "/home/pccredit/pccredit_log/jradbaseweb.log";
	private String log_name = "jradbaseweb.log";
	
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(HttpServletRequest request) throws IOException {
		JRadModelAndView mv = new JRadModelAndView("/system/log/log_browse",request);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "download.json", method = { RequestMethod.GET })
	public AbstractModelAndView download(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String query_date = request.getParameter("query_date");
		String path = log_path;
		String fileName = log_name;
		
		if(StringUtils.isNotEmpty(query_date)){
			path = log_path + "." + query_date;
			fileName = log_name + "." + query_date;
		}
		
		File file = new File(path);
		if(file.exists()){
			byte[] buff = new byte[2048];
			int bytesRead;
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}
}
