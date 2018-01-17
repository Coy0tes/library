/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixintemplate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sysweixintemplate.entity.Sysweixintemplate;
import com.jeeplus.modules.sysweixintemplate.dao.SysweixintemplateDao;

/**
 * 系统微信模板管理Service
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Service
@Transactional(readOnly = true)
public class SysweixintemplateService extends CrudService<SysweixintemplateDao, Sysweixintemplate> {

	public Sysweixintemplate get(String id) {
		return super.get(id);
	}
	
	public List<Sysweixintemplate> findList(Sysweixintemplate sysweixintemplate) {
		return super.findList(sysweixintemplate);
	}
	
	public Page<Sysweixintemplate> findPage(Page<Sysweixintemplate> page, Sysweixintemplate sysweixintemplate) {
		return super.findPage(page, sysweixintemplate);
	}
	
	@Transactional(readOnly = false)
	public void save(Sysweixintemplate sysweixintemplate) {
		super.save(sysweixintemplate);
	}
	
	@Transactional(readOnly = false)
	public void delete(Sysweixintemplate sysweixintemplate) {
		super.delete(sysweixintemplate);
	}
	
	
	
	
}