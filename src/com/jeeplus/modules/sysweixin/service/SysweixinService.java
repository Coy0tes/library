/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sysweixin.entity.Sysweixin;
import com.jeeplus.modules.sysweixin.dao.SysweixinDao;

/**
 * 系统公众号管理Service
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Service
@Transactional(readOnly = true)
public class SysweixinService extends CrudService<SysweixinDao, Sysweixin> {

	public Sysweixin get(String id) {
		return super.get(id);
	}
	
	public List<Sysweixin> findList(Sysweixin sysweixin) {
		return super.findList(sysweixin);
	}
	
	public Page<Sysweixin> findPage(Page<Sysweixin> page, Sysweixin sysweixin) {
		return super.findPage(page, sysweixin);
	}
	
	@Transactional(readOnly = false)
	public void save(Sysweixin sysweixin) {
		super.save(sysweixin);
	}
	
	@Transactional(readOnly = false)
	public void delete(Sysweixin sysweixin) {
		super.delete(sysweixin);
	}
	
	
	
	
}