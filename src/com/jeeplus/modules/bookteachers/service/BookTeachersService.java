package com.jeeplus.modules.bookteachers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.entity.BookTeachersPojo;
@Service
@Transactional(readOnly = true)
public class BookTeachersService {
	@Autowired
	private BookStudentDao bookStudentDao;
	

	// 教师导出明细
	@Transactional(readOnly = false)
	public List<BookTeachersPojo> bookTeachersExport(BookTeachersPojo bookTeachersPojo) {
		return bookStudentDao.bookTeachersExport(bookTeachersPojo);
	}

	
}
