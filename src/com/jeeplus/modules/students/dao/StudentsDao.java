/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.students.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.students.entity.Students;

/**
 * studentsDAO接口
 * @author mxc
 * @version 2017-11-01
 */
@MyBatisDao
public interface StudentsDao extends CrudDao<Students> {

	Students getXh(Students students);

	Students getByXh(Students students);

}