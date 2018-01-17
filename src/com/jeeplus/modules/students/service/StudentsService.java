/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.students.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.students.entity.Students;
import com.jeeplus.modules.students.dao.StudentsDao;

/**
 * studentsService
 * @author mxc
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class StudentsService extends CrudService<StudentsDao, Students> {

	@Autowired
	private BookStudentDao bookstudentDao;
	
	public Students get(String id) {
		return super.get(id);
	}
	
	public List<Students> findList(Students students) {
		return super.findList(students);
	}
	
	public Page<Students> findPage(Page<Students> page, Students students) {
		return super.findPage(page, students);
	}
	
	@Transactional(readOnly = false)
	public void save(Students students) {
		super.save(students);
	}
	
	@Transactional(readOnly = false)
	public void delete(Students students) {
		super.delete(students);
	}

	@Transactional(readOnly = false)
	public Students getByXh(Students students) {
		return dao.getByXh(students);
	}

}