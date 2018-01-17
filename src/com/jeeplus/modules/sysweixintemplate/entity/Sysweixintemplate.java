/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixintemplate.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统微信模板管理Entity
 * @author zhaoliangdong
 * @version 2017-03-13
 */
public class Sysweixintemplate extends DataEntity<Sysweixintemplate> {
	
	private static final long serialVersionUID = 1L;
	private String bh;		// 编号
	private String templateid;		// 模板ID
	private String templatetitle;		// 模板标题
	private String templatecontent;		// 模板内容
	
	public Sysweixintemplate() {
		super();
	}

	public Sysweixintemplate(String id){
		super(id);
	}

	@ExcelField(title="编号", align=2, sort=7)
	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}
	
	@ExcelField(title="模板ID", align=2, sort=8)
	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	
	@ExcelField(title="模板标题", align=2, sort=9)
	public String getTemplatetitle() {
		return templatetitle;
	}

	public void setTemplatetitle(String templatetitle) {
		this.templatetitle = templatetitle;
	}
	
	@ExcelField(title="模板内容", align=2, sort=10)
	public String getTemplatecontent() {
		return templatecontent;
	}

	public void setTemplatecontent(String templatecontent) {
		this.templatecontent = templatecontent;
	}
	
}