/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysaccesstoken.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;
import com.jeeplus.modules.sysaccesstoken.dao.SysaccesstokenDao;

/**
 * accesstokenService
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Service
@Transactional(readOnly = true)
public class SysaccesstokenService extends CrudService<SysaccesstokenDao, Sysaccesstoken> {

	public Sysaccesstoken get(String id) {
		return super.get(id);
	}
	
	public List<Sysaccesstoken> findList(Sysaccesstoken sysaccesstoken) {
		return super.findList(sysaccesstoken);
	}
	
	public Page<Sysaccesstoken> findPage(Page<Sysaccesstoken> page, Sysaccesstoken sysaccesstoken) {
		return super.findPage(page, sysaccesstoken);
	}
	
	@Transactional(readOnly = false)
	public void save(Sysaccesstoken sysaccesstoken) {
		super.save(sysaccesstoken);
	}
	
	@Transactional(readOnly = false)
	public void delete(Sysaccesstoken sysaccesstoken) {
		super.delete(sysaccesstoken);
	}
	
	
	
	
}