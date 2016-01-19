package com.cardpay.pccredit.divisional.dao.comdao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.divisional.filter.DivisionalFilter;
import com.cardpay.pccredit.divisional.model.DivisionalTransfer;
import com.cardpay.pccredit.divisional.model.DivisionalWeb;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;

/**
 * 
 * @author 姚绍明
 *
 * 
 */
@Service
public class DivisionalCommDao {
	@Autowired
	private CommonDao commonDao;
	/**
	 * 分案申请信息 主管
	 * @param filter
	 * @return
	 */
	public QueryResult<DivisionalWeb> findDivisional(DivisionalFilter filter){
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(); 
		sql.append("select d.id,chinese_name,");
		sql.append("card_type,card_id,residential_address,telephone,divisional_result ");
		sql.append("from basic_customer_information b,divisional_application d ");
		sql.append("where b.id=d.customer_id ");
		sql.append("and d.current_organization_id='"+filter.getCurrentOrganizationId()+"' ");
		sql.append("and d.divisional_progress='"+filter.getDivisionalProgress()+"'");
		return commonDao.queryBySqlInPagination(DivisionalWeb.class, sql.toString(), params,
				filter.getStart(), filter.getLimit());
	}
	
	/**
	 * 分案申请信息  卡中心
	 * @param filter
	 * @return
	 */
	public QueryResult<DivisionalWeb> findDivisionalByCardCenter(DivisionalFilter filter){
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(); 
		sql.append("select d.id,chinese_name,");
		sql.append("card_type,card_id,residential_address,telephone,divisional_result ");
		sql.append("from basic_customer_information b,divisional_application d ");
		sql.append("where b.id=d.customer_id ");
		sql.append("and d.divisional_progress='"+filter.getDivisionalProgress()+"'");
		return commonDao.queryBySqlInPagination(DivisionalWeb.class, sql.toString(), params,
				filter.getStart(), filter.getLimit());
	}

}
