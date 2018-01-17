/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.classes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.classes.entity.Classes;
import com.jeeplus.modules.classes.dao.ClassesDao;

/**
 * classesService
 * @author mxc
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class ClassesService extends CrudService<ClassesDao, Classes> {

	public Classes get(String id) {
		return super.get(id);
	}
	
	public List<Classes> findList(Classes classes) {
		return super.findList(classes);
	}
	
	public Page<Classes> findPage(Page<Classes> page, Classes classes) {
		return super.findPage(page, classes);
	}
	
	@Transactional(readOnly = false)
	public void save(Classes classes) {
		super.save(classes);
	}
	
	@Transactional(readOnly = false)
	public void delete(Classes classes) {
		super.delete(classes);
	}
	
	
	
	
}