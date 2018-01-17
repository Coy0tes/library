/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.entity;

import com.jeeplus.modules.students.entity.Students;
import com.jeeplus.modules.teachers.entity.Teachers;

import javax.validation.constraints.NotNull;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * bookstudentEntity
 * @author mxc
 * @version 2017-11-10
 */
public class BookStudent extends DataEntity<BookStudent> {
	
	private static final long serialVersionUID = 1L;
	private Students student;		// 学生
	private Teachers teachers;		// 教师
	private List<BookStudentDetail> bookStudentDetailList = Lists.newArrayList();		// 子表列表
	
	public BookStudent() {
		super();
	}

	public BookStudent(String id){
		super(id);
	}

	@NotNull(message="学生不能为空")
	@ExcelField(title="学生", align=2, sort=1)
	public Students getStudent() {
		return student;
	}

	public void setStudent(Students student) {
		this.student = student;
	}
	
	public Teachers getTeachers() {
		return teachers;
	}

	public void setTeachers(Teachers teachers) {
		this.teachers = teachers;
	}

	public List<BookStudentDetail> getBookStudentDetailList() {
		return bookStudentDetailList;
	}

	public void setBookStudentDetailList(List<BookStudentDetail> bookStudentDetailList) {
		this.bookStudentDetailList = bookStudentDetailList;
	}
}