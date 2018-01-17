package cn.jpush.api.examples;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.jeeplus.common.config.Global;

public class PushUtils {
	private String appKey;
	private String masterSecret;
	private String title;
	private String content;
	private String alias;
	
	public PushUtils(String title,String content, String alias) {
		this.appKey = Global.getConfig("appKey");
		this.masterSecret = Global.getConfig("masterSecret");
		this.title = title;
		this.content = content;
		this.alias = alias;
	}
	
	public PushUtils(String title,String content){
		this.appKey = Global.getConfig("appKey");
		this.masterSecret = Global.getConfig("masterSecret");
		this.title = title;
		this.content = content;
	}
	
	/**
	 * 发送给指定的用户
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void sendNoticeToAlias() throws Exception {
		JPushClient jpushclient = new JPushClient(this.masterSecret, this.appKey, 3);
		PushPayload ppl = buildPayload(alias, this.content, this.title);
		try {
			PushResult pr = jpushclient.sendPush(ppl);
			System.out.println("alias:"+alias+",content:"+content+",title:"+title);
			System.out.println(pr);
		} catch (APIConnectionException e) {
			e.printStackTrace();
			throw e;
		} catch (APIRequestException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 发送消息给所有人
	 * @throws Exception
	 */
	public void sendNoticeToAll() throws Exception {
		JPushClient jpushclient = new JPushClient(this.masterSecret, this.appKey, 3);
		PushPayload ppl = buildPayloadAll(this.content, this.title);
		try {
			PushResult pr = jpushclient.sendPush(ppl);
			System.out.println(pr);
		} catch (APIConnectionException e) {
			e.printStackTrace();
			throw e;
		} catch (APIRequestException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 构造消息发送body,发送给个人，平台：ios和android
	 * @param alias
	 * @param alert
	 * @param title
	 * @return
	 */
    public static PushPayload buildPayload(String alias,String alert,String title) {
    	return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(alert))
                .build();
    }
    
    /**
     * 构造消息发送body,发送给所有人，平台：ios和android
     * @param alert
     * @param title
     * @return
     */
    public static PushPayload buildPayloadAll(String alert,String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.alert(alert))
                .build();
    }
    
}
