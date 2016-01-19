/**
 * 
 */
package com.cardpay.pccredit.ipad.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.taglib.TagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.ipad.dao.UserIpadDao;
import com.cardpay.pccredit.ipad.model.UserIpad;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.wicresoft.jrad.modules.dao.modulesComdao;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.web.RequestHelper;

/**
 * @author shaoming
 *
 * 2014年11月28日   下午2:11:47
 */
@Service
public class UserForIpadService {
	
	@Autowired
	private modulesComdao modulesCommondao;
	
	@Autowired
	private UserIpadDao userIpadDao;

	public UserIpad login(String login, String passwd) {
		// TODO Auto-generated method stub
		return null;
	}
}
