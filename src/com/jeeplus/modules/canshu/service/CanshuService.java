/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.canshu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.canshu.entity.Canshu;
import com.jeeplus.modules.canshu.dao.CanshuDao;

/**
 * canshuService
 * @author mxc
 * @version 2017-11-10
 */
@Service
@Transactional(readOnly = true)
public class CanshuService extends CrudService<CanshuDao, Canshu> {

	public Canshu get(String id) {
		return super.get(id);
	}
	
	public List<Canshu> findList(Canshu canshu) {
		return super.findList(canshu);
	}
	
	public Page<Canshu> findPage(Page<Canshu> page, Canshu canshu) {
		return super.findPage(page, canshu);
	}
	
	@Transactional(readOnly = false)
	public void save(Canshu canshu) {
		super.save(canshu);
	}
	
	@Transactional(readOnly = false)
	public void delete(Canshu canshu) {
		super.delete(canshu);
	}

	@Transactional(readOnly = false)
	public Canshu find(Canshu canshu) {
		return dao.find(canshu);
	}

	@Transactional(readOnly = false)
	public Canshu getList(Canshu canshu) {
		return dao.getList(canshu);
	}
	
	
	
	
}