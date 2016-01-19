package com.cardpay.pccredit.system.filter;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

public class LogFilter extends BaseQueryFilter{
	private String query_date;//yyyy-MM-dd

	public String getQuery_date() {
		return query_date;
	}

	public void setQuery_date(String query_date) {
		this.query_date = query_date;
	}
	
}
