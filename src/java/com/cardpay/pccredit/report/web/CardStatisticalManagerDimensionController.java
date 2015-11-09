package com.cardpay.pccredit.report.web;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.nextreports.jofc2.model.elements.BarChart;

import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.product.filter.ProductFilter;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.service.ProductService;
import com.cardpay.pccredit.report.filter.StatisticalFilter;
import com.cardpay.pccredit.report.model.CardStatistical;
import com.cardpay.pccredit.report.service.CardStatisticalService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * @author chenzhifang
 *
 * 2014-11-28上午10:09:21
 */
@Controller
@RequestMapping("/report/cardmanagerdimension/*")
@JRadModule("report.cardmanagerdimension")
public class CardStatisticalManagerDimensionController extends BaseController {
	@Autowired
	private CardStatisticalService cardStatisticalService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
	/**
	 * 浏览“灵活金”发卡进展情况统计表（客户经理/微贷经理、营销经理维度）
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute StatisticalFilter filter, HttpServletRequest request) {
		filter.setRequest(request);

		ProductFilter pFilter = new ProductFilter();
		pFilter.setLimit(Integer.MAX_VALUE);
		QueryResult<ProductAttribute> qs = productService.findProductsByFilter(pFilter);
		
		if(StringUtils.isEmpty(filter.getProductId())){
			filter.setProductId(qs.getItems().size() != 0 ? qs.getItems().get(0).getId() : "");
		}
		if(filter.getBasicDate() == null){
			filter.setBasicDate(DateHelper.getDateFormat("2013-08-01", "yyyy-MM-dd"));
		}
		if(filter.getReportDate() == null){
			filter.setReportDate(new Date());
		}
		List<CardStatistical> list = cardStatisticalService.getManagerCardStatistical(filter);

		JRadModelAndView mv = new JRadModelAndView("/report/cardstatistical/cardstatistical_manager_browse", request);
		mv.addObject("list", list);
		mv.addObject("filter", filter);
		
		mv.addObject(PAGED_RESULT, qs);
		return mv;
	}
	
	/**
	 * 进件流程状态柱状图
	 * 
	 * @param filter
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "report.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView report(@ModelAttribute StatisticalFilter filter, HttpServletRequest request) throws IOException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		HttpSession session = request.getSession(); //创建 
		
		//创建主题样式         
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");         
		//设置标题字体         
		standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));         
		//设置图例的字体         
		standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));        
		//设置轴向的字体         
		standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));         
		//应用主题样式         
		ChartFactory.setChartTheme(standardChartTheme);  
		int count1 = Integer.parseInt(intoPiecesService.getStatusCount(Constant.APPROVE_INTOPICES,user.getId()));
		int count2 = Integer.parseInt(intoPiecesService.getStatusCount(Constant.RETURN_INTOPICES,user.getId()));
		int count3 = Integer.parseInt(intoPiecesService.getStatusCount(Constant.REFUSE_INTOPICES,user.getId()));
		int count4 = Integer.parseInt(intoPiecesService.getStatusCount(Constant.APPROVED_INTOPICES,user.getId()));
		double[][] data = new double[][]{{count1},{count2},{count3},{count4}} ;
		String[] rowKeys = {"申请中", "退件", "拒件","审批通过"};
		String[] columnKeys = {""};
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
		 
		JFreeChart chart = ChartFactory.createBarChart3D("当前进件状态统计图", 
		                  "状态",
		                  "数量",
		                  dataset,
		                  PlotOrientation.VERTICAL,
		                  true,
		                  false,
		                  false);
		CategoryPlot plot = chart.getCategoryPlot();    
		//设置网格背景颜色  
		plot.setBackgroundPaint(Color.white);  
		//设置网格竖线颜色  
		plot.setDomainGridlinePaint(Color.pink);  
		//设置网格横线颜色  
		plot.setRangeGridlinePaint(Color.pink);  
		  
		//显示每个柱的数值，并修改该数值的字体属性  
		BarRenderer3D renderer = new BarRenderer3D();  
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
		renderer.setBaseItemLabelsVisible(true);  
		  
		//默认的数字显示在柱子中，通过如下两句可调整数字的显示  
		//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题  
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));  
		renderer.setItemLabelAnchorOffset(10D);  
		//设置每个地区所包含的平行柱的之间距离  
		renderer.setItemMargin(0.4);  
		plot.setRenderer(renderer);  
		
		String filename = ServletUtilities.saveChartAsPNG(chart, 800, 450, null, session);
		String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;


		JRadModelAndView mv = new JRadModelAndView("/report/cardstatistical/application_browse", request);
		mv.addObject("chart", graphURL);
		return mv;
	}

	/**
	 * 进件流程节点柱状图
	 * 
	 * @param filter
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "reportTip.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView reportTip(HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession(); //创建 
		
		//创建主题样式         
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");         
		//设置标题字体         
		standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));         
		//设置图例的字体         
		standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));        
		//设置轴向的字体         
		standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));         
		//应用主题样式         
		ChartFactory.setChartTheme(standardChartTheme);  
		List<HashMap<String, Object>> list = intoPiecesService.getTipCount();
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();  
		for(int j=0;j<list.size();j++){        
			categoryDataset.addValue(Integer.parseInt(list.get(j).get("COUNT").toString()), "", list.get(j).get("NAME").toString()); 
		 }
		JFreeChart chart = ChartFactory.createBarChart3D("当前进件流程节点统计图", 
		                  "流程节点",
		                  "数量",
		                  categoryDataset,
		                  PlotOrientation.VERTICAL,
		                  true,
		                  false,
		                  false);
		CategoryPlot plot = chart.getCategoryPlot();    
		//设置网格背景颜色  
		plot.setBackgroundPaint(Color.white);  
		//设置网格竖线颜色  
		plot.setDomainGridlinePaint(Color.pink);  
		//设置网格横线颜色  
		plot.setRangeGridlinePaint(Color.pink);  
		  
		//显示每个柱的数值，并修改该数值的字体属性  
		BarRenderer3D renderer = new BarRenderer3D();  
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
		renderer.setBaseItemLabelsVisible(true);  
		  
		//默认的数字显示在柱子中，通过如下两句可调整数字的显示  
		//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题  
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));  
		renderer.setItemLabelAnchorOffset(10D);  
		//设置每个地区所包含的平行柱的之间距离  
		renderer.setItemMargin(0.4);  
		plot.setRenderer(renderer);  
		
		String filename = ServletUtilities.saveChartAsPNG(chart, 800, 450, null, session);
		String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;


		JRadModelAndView mv = new JRadModelAndView("/report/cardstatistical/application_approve_browse", request);
		mv.addObject("chart", graphURL);
		return mv;
	}
}
