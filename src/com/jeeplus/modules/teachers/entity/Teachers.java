/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachers.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * teachersEntity
 * @author mxc
 * @version 2017-11-01
 */
public class Teachers extends DataEntity<Teachers> {
	
	private static final long serialVersionUID = 1L;
	private String bh;		// 编号
	private String name;		// 姓名
	private String lxdh;		// 联系电话
	
	public Teachers() {
		super();
	}

	public Teachers(String id){
		super(id);
	}

	@ExcelField(title="编号", align=2, sort=1)
	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}
	
	@ExcelField(title="姓名", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="联系电话", align=2, sort=3)
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
}