/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentPojo;
import com.jeeplus.modules.bookstudent.entity.BookTeachersPojo;
import com.jeeplus.modules.teachers.entity.Teachers;

/**
 * bookstudentDAO接口
 * @author mxc
 * @version 2017-11-08
 */
@MyBatisDao
public interface BookStudentDao extends CrudDao<BookStudent> {

	BookStudent getXh(BookStudent bookStudent);

	List<BookStudentPojo> bookStudentExport();

	List<BookStudent> getByXh(BookStudent bookStudent);

	List<BookStudent> getByBh(BookStudent bookStudent);

	List<BookStudent> findTeachersList(BookStudent bookStudent);

	BookStudent getByTeachers(String id);

	BookStudent findListByStudent(BookStudent bookStudent);

	List<BookTeachersPojo> bookTeachersExport(BookTeachersPojo bookTeachersPojo);

	BookStudent getStudentsById(BookStudent bookStudent);

	BookStudent getTeachersByGh(BookStudent bookStudent);

}