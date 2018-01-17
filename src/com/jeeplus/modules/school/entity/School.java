/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * schoolEntity
 * @author mxc
 * @version 2017-11-01
 */
public class School extends DataEntity<School> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 学校名称
	private String contents;		// 详细信息
	
	public School() {
		super();
	}

	public School(String id){
		super(id);
	}

	@ExcelField(title="学校名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="详细信息", align=2, sort=2)
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
}