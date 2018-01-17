/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.school.entity.School;

/**
 * schoolDAO接口
 * @author mxc
 * @version 2017-11-01
 */
@MyBatisDao
public interface SchoolDao extends CrudDao<School> {

	
}