/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.agencyuser.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.agencyuser.entity.AgencyUser;

/**
 * 账号管理DAO接口
 * @author zhaoliangdong
 * @version 2017-04-18
 */
@MyBatisDao
public interface AgencyUserDao extends CrudDao<AgencyUser> {

	public void deleteUserRole(AgencyUser user);

	public void insertUserRole(AgencyUser user);

	public int getAdminRole(AgencyUser auser);

	public List<AgencyUser> findListUndo(AgencyUser agencyUser);
	
}