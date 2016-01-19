package com.cardpay.pccredit.report.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.report.filter.OClpmAccLoanFilter;
import com.cardpay.pccredit.report.filter.StatisticalFilter;
import com.cardpay.pccredit.report.model.AccLoanInfo;
import com.cardpay.pccredit.report.model.PsNormIntAmt;
import com.cardpay.pccredit.report.service.AferAccLoanService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 贷款借据计息（客户经理）
 */
@Controller
@RequestMapping("/report/psNormIntAmt/*")
@JRadModule("report.psNormIntAmt")
public class PsNormIntAmtController extends BaseController{
	
	@Autowired
	private AferAccLoanService aferAccLoanService;
	
	private static final Logger logger = Logger.getLogger(PsNormIntAmtController.class);
	/**
	 * 贷款借据计息
	 * 还款计划表
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	public AbstractModelAndView browse(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		if(filter.getEndDate()==null){
			filter.setEndDate(new Date());
		}
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		
		QueryResult<PsNormIntAmt> accloanList = aferAccLoanService.getPsNormIntAmt(filter);
		JRadPagedQueryResult<PsNormIntAmt> pagedResult = new JRadPagedQueryResult<PsNormIntAmt>(filter, accloanList);
		JRadModelAndView mv = new JRadModelAndView("/report/psNormIntAmt/manager_browse", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
		
	}
	
	/**
	 * 贷款借据计息清单导出
	 * 
	 * @param filter
	 * @param request
	 * @param excel
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "export.page", method = { RequestMethod.GET })
	public void export(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request,HttpServletResponse response) {
		JRadModelAndView mv;
		filter.setRequest(request);
		if(filter.getEndDate()==null){
			filter.setEndDate(new Date());
		}
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		List<PsNormIntAmt> list = aferAccLoanService.getPsNormIntAmtList(filter);
		create(list,response,filter);
		//下载完成，返回页面
		
	}
	
	public void create(List<PsNormIntAmt> list,HttpServletResponse response,OClpmAccLoanFilter filter){
		boolean flag = false;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("计息");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("客户名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("正常利息");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("逾期利息");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("已还利息");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("已还逾期利息");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("应还利息");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("计算日期");
		cell.setCellStyle(style);
		
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < list.size(); i++){
			PsNormIntAmt ps = list.get(i);
			row = sheet.createRow((int) i+1);
			row.createCell((short) 0).setCellValue(ps.getRowIndex());
			row.createCell((short) 1).setCellValue(ps.getClient_name());
			row.createCell((short) 2).setCellValue(df.format(Double.valueOf(ps.getPs_norm_int_amt())));
			row.createCell((short) 3).setCellValue(df.format(Double.valueOf(ps.getPs_od_int_amt())));
			row.createCell((short) 4).setCellValue(df.format(Double.valueOf(ps.getSetl_norm_int())));
			row.createCell((short) 5).setCellValue(df.format(Double.valueOf(ps.getSetl_od_int_amt())));
			row.createCell((short) 6).setCellValue(df.format(Double.valueOf(ps.getPs_amt())));
			row.createCell((short) 7).setCellValue(ps.getPsTime());
		}
		String fileName = "利息计算";
		try{
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(fileName.getBytes("gbk"),"iso8859-1")+".xls");
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();

		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
