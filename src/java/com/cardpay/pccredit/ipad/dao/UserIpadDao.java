package com.cardpay.pccredit.ipad.dao;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.ipad.model.UserIpad;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface UserIpadDao {

	public User findByLogin(@Param("LOGIN_ID") String LOGIN_ID);
	public UserIpad findInfoByLogin(@Param("LOGIN_ID") String LOGIN_ID);

}
