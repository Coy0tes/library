package com.jeeplus.modules.wechat;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.sword.lang.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class WeixinMessageUtil {
	private static Logger logger = Logger.getLogger(WeixinMessageUtil.class);
	private String appid;
	private String appsecrete;
	private String departid;
	private String accesstoken;
	private long tokentime;
	private long expirein;
	private String toUserOpenId;
	private String templateId;
	private JSONObject datajson;
	private String msgBody;
	private String isRequest = "0"; //0,未请求微信服务器获取access_token;1,请求微信服务器获取access_token.
	
	/**
	 * 构造函数
	 * @param departid
	 * @param accesstoken
	 * @param parseLong
	 * @param parseLong2
	 * @param toUserOpenId2
	 * @param templateId2
	 * @param datajson2
	 */
	public WeixinMessageUtil(String appid,String appsecrete,String departid, String accesstoken,
			long tokentime, long expirein, String toUserOpenId,
			String templateId, JSONObject datajson) {
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.departid = departid;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
		this.toUserOpenId = toUserOpenId;
		this.templateId = templateId;
		this.datajson = datajson;
	}
	
	public WeixinMessageUtil(String appid,String appsecrete, String accesstoken,
			long tokentime, long expirein, String toUserOpenId,
			String templateId, JSONObject datajson) {
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
		this.toUserOpenId = toUserOpenId;
		this.templateId = templateId;
		this.datajson = datajson;
	}
	
	/**
	 * 构造函数
	 * @param appid
	 * @param appsecrete
	 * @param accesstoken
	 * @param tokentime
	 * @param expirein
	 */
	public WeixinMessageUtil(String appid,String appsecrete, String accesstoken,
			long tokentime, long expirein) {
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
	}
	
	/**
	 * 构造函数
	 * @param uid
	 * @param appid
	 * @param appsecrete
	 * @param accesstoken
	 * @param tokentime
	 * @param expirein
	 */
	public WeixinMessageUtil(String departid,String appid,String appsecrete, String accesstoken,
			long tokentime, long expirein) {
		this.departid = departid;
		this.appid = appid;
		this.appsecrete = appsecrete;
		this.accesstoken = accesstoken;
		this.tokentime = tokentime;
		this.expirein = expirein;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public String getAccessToken(){
		if(!isValid()){
			request();
		}
		return this.accesstoken;
	}
	
	/**
     * access_token是否合法
     * @return
     */
	public boolean isValid() {
		if (StringUtils.isBlank(this.accesstoken)) {
			return false;
		}
	    if (this.expirein <= 0L) {
	      return false;
	    }
	    if (isExpire())
	      return false;
	    return true;
	}
	
	/**
	 * access_token是否过期  
	 * @return
	 */
    private boolean isExpire()
    {
	    Date currentDate = new Date();
	    long currentTime = currentDate.getTime();
	    long expiresTime = this.expirein * 1000L - 600000; //延迟10分钟

	    if (this.tokentime*1000L + expiresTime > currentTime)
	      return false;
	    return true;
    }
    
    /**
	 * 请求access_token
	 * @return
	 */
	private boolean request(){
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.appid + "&secret=" + this.appsecrete;
	    String result = HttpUtils.get(url);
	    if (StringUtils.isBlank(result))
	      return false;
	    if (!parseData(result)) {
	      return false;
	    }
	    logger.info("token获取成功");
		this.isRequest = "1";
		return true;
	}
	
	/**
	 * 解析access_token请求返回数据
	 */
	private boolean parseData(String data){
	    JSONObject jsonObject = JSONObject.parseObject(data);
	    String tokenName = "access_token";
	    String expiresInName = "expires_in";
	    try {
	      String token = jsonObject.get(tokenName).toString();
	      if (StringUtils.isBlank(token)) {
	        logger.error("token获取失败,获取结果" + data);
	        return false;
	      }
	      this.accesstoken = token;
	      //this.tokentime = new Date().getTime();
	      this.tokentime = System.currentTimeMillis()/1000L;
	      String expiresIn = jsonObject.get(expiresInName).toString();
	      if (StringUtils.isBlank(expiresIn)) {
	        logger.error("token获取失败,获取结果" + expiresIn);
	        return false;
	      }
	      this.expirein = Long.valueOf(expiresIn).longValue();
	    }
	    catch (Exception e) {
	      logger.error("token 结果解析失败，token参数名称: " + tokenName + 
	        "有效期参数名称:" + expiresInName + 
	        "token请求结果:" + data);
	      e.printStackTrace();
	      return false;
	    }
	    return true;
    }
	
	/**
	 * 发送[消费]模板消息
	 * @return
	 */
	public String sendText() throws Exception {
		//获取access_token
		getAccessToken();
		
		//发送模板消息
		try {
			send();
		} catch (Exception e) {
			//throw e;
		}
		JSONObject jo = new JSONObject();
		jo.put("isRequest", this.isRequest);
		jo.put("departid", this.departid);
		jo.put("accesstoken", this.accesstoken);
		jo.put("expirein", this.expirein);
		jo.put("tokentime", this.tokentime);
		return jo.toJSONString();
	}
	
	/**
	 * 发送[消费]模板消息
	 * @return
	 * @throws Exception
	 */
	public String send() throws Exception
	  {
		String rtn = null;
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("touser", this.toUserOpenId);
		jsonMsg.put("template_id", this.templateId);
		jsonMsg.put("url", "");
		jsonMsg.put("topcolor", "#FF0000");
		jsonMsg.put("data", this.datajson);		

	    this.msgBody = jsonMsg.toJSONString();
	    
	    rtn = sendTemplateMsg();
	    JSONObject jo = JSON.parseObject(rtn);
		String errcode = jo.get("errcode").toString();
		String errmsg = jo.get("errmsg").toString();
		String msgid = jo.get("msgid").toString();
		if("0".equals(errcode)){
			logger.info("微信推送模板消息成功,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
		}else{
			logger.info("微信推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
			//throw new Exception("微信推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
		}
	    return rtn;
	}
	
	/**
	 * 发送[预订成功]消息
	 * @return
	 * @throws Exception
	 */
	public String sendText2() throws Exception {
		//获取access_token
		getAccessToken();
		
		//发送模板消息
		try {
			send2();
		} catch (Exception e) {
			//throw e;
		}
		JSONObject jo = new JSONObject();
		jo.put("isRequest", this.isRequest);
		jo.put("departid", this.departid);
		jo.put("accesstoken", this.accesstoken);
		jo.put("expirein", this.expirein);
		jo.put("tokentime", this.tokentime);
		return jo.toJSONString();
	}
	
	/**
	 * 发送[预订成功]消息
	 * @return
	 * @throws Exception
	 */
	public String send2() throws Exception
	  {
		//String weixin_interface_url = ResourceUtil.getSessionattachmenttitle("weixin_interface_url");
		
		String rtn = null;
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("touser", this.toUserOpenId);
		jsonMsg.put("template_id", this.templateId);
		//jsonMsg.put("url", weixin_interface_url+"/member/myorder2.aspx?uid="+this.departid);
		jsonMsg.put("url","");
		jsonMsg.put("topcolor", "#FF0000");
		jsonMsg.put("data", this.datajson);		

	    this.msgBody = jsonMsg.toJSONString();
	    
	    rtn = sendTemplateMsg();
	    JSONObject jo = JSON.parseObject(rtn);
		String errcode = jo.get("errcode").toString();
		String errmsg = jo.get("errmsg").toString();
		String msgid = jo.get("msgid").toString();
		if("0".equals(errcode)){
			logger.info("微信推送模板消息成功,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
		}else{
			logger.info("微信推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
			//throw new Exception("微信推送模板消息失败,errcode:"+errcode+",errmsg:"+errmsg+",msgid:"+msgid);
		}
	    return rtn;
	}
	
	/**
	 * 发送微信模板消息[微信接口]
	 * @return
	 */
	private String sendTemplateMsg(){
		String rtn=null;
		String accessToken = this.accesstoken;
		if (StringUtils.isBlank(this.toUserOpenId)) {
	      return rtn;
	    }
	    if (StringUtils.isBlank(accessToken)) {
	      logger.error("发送失败，无法得到accessToken");
	      return rtn;
	    }
	    if (StringUtils.isNotBlank(accessToken)) {
	        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
	        rtn = HttpUtils.post(url, this.msgBody);
	    }
	    return rtn;
	}
	
}
