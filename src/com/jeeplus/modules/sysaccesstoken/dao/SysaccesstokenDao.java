/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysaccesstoken.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;

/**
 * accesstokenDAO接口
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@MyBatisDao
public interface SysaccesstokenDao extends CrudDao<Sysaccesstoken> {

	void deleteAll();

	
}