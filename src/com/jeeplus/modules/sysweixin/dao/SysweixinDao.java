/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixin.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sysweixin.entity.Sysweixin;

/**
 * 系统公众号管理DAO接口
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@MyBatisDao
public interface SysweixinDao extends CrudDao<Sysweixin> {

	Sysweixin getSystemWeixin();
	
}