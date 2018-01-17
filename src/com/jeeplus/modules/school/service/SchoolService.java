/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.entity.School;
import com.jeeplus.modules.school.dao.SchoolDao;

/**
 * schoolService
 * @author mxc
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class SchoolService extends CrudService<SchoolDao, School> {

	public School get(String id) {
		return super.get(id);
	}
	
	public List<School> findList(School school) {
		return super.findList(school);
	}
	
	public Page<School> findPage(Page<School> page, School school) {
		return super.findPage(page, school);
	}
	
	@Transactional(readOnly = false)
	public void save(School school) {
		super.save(school);
	}
	
	@Transactional(readOnly = false)
	public void delete(School school) {
		super.delete(school);
	}
	
	
	
	
}