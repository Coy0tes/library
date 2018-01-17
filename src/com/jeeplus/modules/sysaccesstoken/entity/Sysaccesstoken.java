/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysaccesstoken.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * accesstokenEntity
 * @author zhaoliangdong
 * @version 2017-03-13
 */
public class Sysaccesstoken extends DataEntity<Sysaccesstoken> {
	
	private static final long serialVersionUID = 1L;
	private String accesstoken;		// accesstoken
	private Long expiresin;		// expiresin
	private String tokentime;		// tokentime
	
	public Sysaccesstoken() {
		super();
	}

	public Sysaccesstoken(String id){
		super(id);
	}

	@ExcelField(title="accesstoken", align=2, sort=7)
	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	
	@ExcelField(title="expiresin", align=2, sort=8)
	public Long getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(Long expiresin) {
		this.expiresin = expiresin;
	}
	
	@ExcelField(title="tokentime", align=2, sort=9)
	public String getTokentime() {
		return tokentime;
	}

	public void setTokentime(String tokentime) {
		this.tokentime = tokentime;
	}
	
}