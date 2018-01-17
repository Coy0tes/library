/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.canshu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.canshu.entity.Canshu;

/**
 * canshuDAO接口
 * @author mxc
 * @version 2017-11-10
 */
@MyBatisDao
public interface CanshuDao extends CrudDao<Canshu> {

	Canshu find(Canshu canshu);

	Canshu getList(Canshu canshu);
	
}