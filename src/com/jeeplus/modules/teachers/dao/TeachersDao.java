/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachers.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.teachers.entity.Teachers;

/**
 * teachersDAO接口
 * @author mxc
 * @version 2017-11-01
 */
@MyBatisDao
public interface TeachersDao extends CrudDao<Teachers> {

	Teachers getByBh(Teachers teachers);

	
}