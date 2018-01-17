/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudenthistory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistoryPojo;
import com.jeeplus.modules.bookstudenthistory.entity.BookTeachersHistoryPojo;
import com.jeeplus.modules.bookstudenthistory.dao.BookStudentHistoryDao;

/**
 * bookStudentHistoryService
 * @author mxc
 * @version 2017-11-11
 */
@Service
@Transactional(readOnly = true)
public class BookStudentHistoryService extends CrudService<BookStudentHistoryDao, BookStudentHistory> {

	public BookStudentHistory get(String id) {
		return super.get(id);
	}
	
	public List<BookStudentHistory> findList(BookStudentHistory bookStudentHistory) {
		return super.findList(bookStudentHistory);
	}
	
	public Page<BookStudentHistory> findPage(Page<BookStudentHistory> page, BookStudentHistory bookStudentHistory) {
		return super.findPage(page, bookStudentHistory);
	}
	
	public Page<BookStudentHistory> findTeachersPage(Page<BookStudentHistory> page, BookStudentHistory bookStudentHistory) {
		bookStudentHistory.setPage(page);
		page.setList(dao.findTeachersList(bookStudentHistory));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(BookStudentHistory bookStudentHistory) {
		super.save(bookStudentHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(BookStudentHistory bookStudentHistory) {
		super.delete(bookStudentHistory);
	}

	// 学生历史记录导出
	@Transactional(readOnly = false)
	public List<BookStudentHistoryPojo> studentHistoryExport() {
		return dao.studentHistoryExport();
	}

	// 教师历史记录导出
	@Transactional(readOnly = false)
	public List<BookTeachersHistoryPojo> teachersHistoryExport() {
		return dao.teachersHistoryExport();
	}

	// 学生遗失统计
	@Transactional(readOnly = false)
	public List<BookStudentHistory> studentLossStatistic(BookStudentHistory bookStudentHistory) {
		return dao.studentLossStatistic(bookStudentHistory);
	}

	// 学生遗失统计导出
	@Transactional(readOnly = false)
	public List<BookStudentHistoryPojo> studentLossStatisticExport() {
		return dao.studentLossStatisticExport();
	}

	// 教师遗失统计
	@Transactional(readOnly = false)
	public List<BookStudentHistory> teachersLossStatisticList(BookStudentHistory teachersLossStatisticList) {
		return dao.teachersLossStatisticList(teachersLossStatisticList);
	}

	// 教师遗失统计导出
	@Transactional(readOnly = false)
	public List<BookTeachersHistoryPojo> teachersLossStatisticExport() {
		return dao.teachersLossStatisticExport();
	}

	
}