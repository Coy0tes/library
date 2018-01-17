/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixintemplate.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sysweixintemplate.entity.Sysweixintemplate;

/**
 * 系统微信模板管理DAO接口
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@MyBatisDao
public interface SysweixintemplateDao extends CrudDao<Sysweixintemplate> {

	Sysweixintemplate getSystemTemplateByBh(Sysweixintemplate tmp);

	
}