/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.classes.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * classesEntity
 * @author mxc
 * @version 2017-11-01
 */
public class Classes extends DataEntity<Classes> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 班级名称
	
	public Classes() {
		super();
	}

	public Classes(String id){
		super(id);
	}

	@ExcelField(title="班级名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}