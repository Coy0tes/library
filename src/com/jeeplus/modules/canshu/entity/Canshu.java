/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.canshu.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * canshuEntity
 * @author mxc
 * @version 2017-11-10
 */
public class Canshu extends DataEntity<Canshu> {
	
	private static final long serialVersionUID = 1L;
	private Double yajin;		// 借书押金
	private Integer booknum;		// 最多借书数量
	private Double csprice;		// 超时费用（天/元）
	
	public Canshu() {
		super();
	}

	public Canshu(String id){
		super(id);
	}

	@NotNull(message="借书押金不能为空")
	@ExcelField(title="借书押金", align=2, sort=1)
	public Double getYajin() {
		return yajin;
	}

	public void setYajin(Double yajin) {
		this.yajin = yajin;
	}
	
	@NotNull(message="最多借书数量不能为空")
	@ExcelField(title="最多借书数量", align=2, sort=2)
	public Integer getBooknum() {
		return booknum;
	}

	public void setBooknum(Integer booknum) {
		this.booknum = booknum;
	}
	
	@NotNull(message="超时费用（天/元）不能为空")
	@ExcelField(title="超时费用（天/元）", align=2, sort=3)
	public Double getCsprice() {
		return csprice;
	}

	public void setCsprice(Double csprice) {
		this.csprice = csprice;
	}
	
}