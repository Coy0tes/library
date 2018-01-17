/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixin.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统公众号管理Entity
 * @author zhaoliangdong
 * @version 2017-03-13
 */
public class Sysweixin extends DataEntity<Sysweixin> {
	
	private static final long serialVersionUID = 1L;
	private String weixinhao;		// 微信号原始ID
	private String weixinappid;		// 微信Appid
	private String weixinappsecret;		// 微信Appsecret
	private String weixintoken;		// 微信Token
	private String mchid;		// 支付商户ID
	private String apikey;		// 支付ApiKey
	
	public Sysweixin() {
		super();
	}

	public Sysweixin(String id){
		super(id);
	}

	@ExcelField(title="微信号原始ID", align=2, sort=7)
	public String getWeixinhao() {
		return weixinhao;
	}

	public void setWeixinhao(String weixinhao) {
		this.weixinhao = weixinhao;
	}
	
	@ExcelField(title="微信Appid", align=2, sort=8)
	public String getWeixinappid() {
		return weixinappid;
	}

	public void setWeixinappid(String weixinappid) {
		this.weixinappid = weixinappid;
	}
	
	@ExcelField(title="微信Appsecret", align=2, sort=9)
	public String getWeixinappsecret() {
		return weixinappsecret;
	}

	public void setWeixinappsecret(String weixinappsecret) {
		this.weixinappsecret = weixinappsecret;
	}
	
	@ExcelField(title="微信Token", align=2, sort=10)
	public String getWeixintoken() {
		return weixintoken;
	}

	public void setWeixintoken(String weixintoken) {
		this.weixintoken = weixintoken;
	}
	
	@ExcelField(title="支付商户ID", align=2, sort=11)
	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}
	
	@ExcelField(title="支付ApiKey", align=2, sort=12)
	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
}