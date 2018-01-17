/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;

/**
 * bookstudentDAO接口
 * @author mxc
 * @version 2017-11-08
 */
@MyBatisDao
public interface BookStudentDetailDao extends CrudDao<BookStudentDetail> {

	List<BookStudentDetail> findSdtCls(BookStudentDetail bookStudentDetail);

	void deleteByIsbn(BookStudentDetail bookStudentDetail);

	void updateHistoryid(BookStudentDetail bookStudentDetail);

	BookStudentDetail getByIsbn(BookStudentDetail bookStudentDetail);

	Integer getByMainid(BookStudentDetail bookStudentDetail);

	BookStudentDetail findTeachers(BookStudentDetail bookStudentDetail);

	List<BookStudentDetail> findListByStudent(BookStudentDetail bookStudentDetail);

	Page<BookStudentDetail> findListByStudent(Page<BookStudentDetail> page,BookStudentDetail bookStudentDetail);

	List<BookStudentDetail> findListByTeacher(BookStudentDetail bookStudentDetail);

	List<BookStudentDetail> getByIsbnList(BookStudentDetail bookStudentDetail);

	List<BookStudentDetail> findTeachersList(BookStudentDetail bookStudentDetail);

	void updateRemarks(BookStudentDetail bookStudentDetail);


}