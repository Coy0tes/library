/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachers.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.teachers.entity.Teachers;
import com.jeeplus.modules.teachers.dao.TeachersDao;

/**
 * teachersService
 * @author mxc
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class TeachersService extends CrudService<TeachersDao, Teachers> {

	public Teachers get(String id) {
		return super.get(id);
	}
	
	public List<Teachers> findList(Teachers teachers) {
		return super.findList(teachers);
	}
	
	public Page<Teachers> findPage(Page<Teachers> page, Teachers teachers) {
		return super.findPage(page, teachers);
	}
	
	@Transactional(readOnly = false)
	public void save(Teachers teachers) {
		super.save(teachers);
	}
	
	@Transactional(readOnly = false)
	public void delete(Teachers teachers) {
		super.delete(teachers);
	}

	@Transactional(readOnly = false)
	public Teachers getByBh(Teachers teachers) {
		return dao.getByBh(teachers);
	}
	
	
	
	
}