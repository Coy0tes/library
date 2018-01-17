/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.students.entity;

import com.jeeplus.modules.classes.entity.Classes;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * studentsEntity
 * @author mxc
 * @version 2017-11-01
 */
public class Students extends DataEntity<Students> {
	
	private static final long serialVersionUID = 1L;
	private String xh;		// 学号
	private String name;		// 姓名
	private Classes classid;		// 所属班级
	
	public Students() {
		super();
	}

	public Students(String id){
		super(id);
	}

	@ExcelField(title="学号", align=2, sort=1)
	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}
	
	@ExcelField(title="姓名", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="所属班级不能为空")
	@ExcelField(title="所属班级", align=2, sort=3)
	public Classes getClassid() {
		return classid;
	}

	public void setClassid(Classes classid) {
		this.classid = classid;
	}

}