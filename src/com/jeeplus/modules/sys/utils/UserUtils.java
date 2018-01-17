/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Lists;
import com.jeeplus.common.service.BaseService;
import com.jeeplus.common.sms.SMSUtils;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.agencyuser.dao.AgencyUserDao;
import com.jeeplus.modules.agencyuser.entity.AgencyUser;
import com.jeeplus.modules.sys.dao.AreaDao;
import com.jeeplus.modules.sys.dao.MenuDao;
import com.jeeplus.modules.sys.dao.OfficeDao;
import com.jeeplus.modules.sys.dao.RoleDao;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.jeeplus.modules.sysaccesstoken.dao.SysaccesstokenDao;
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;
import com.jeeplus.modules.sysweixin.dao.SysweixinDao;
import com.jeeplus.modules.sysweixin.entity.Sysweixin;
import com.jeeplus.modules.sysweixintemplate.dao.SysweixintemplateDao;
import com.jeeplus.modules.sysweixintemplate.entity.Sysweixintemplate;

/**
 * 用户工具类
 * @author jeeplus
 * @version 2013-12-05
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static SysweixinDao sysweixinDao = SpringContextHolder.getBean(SysweixinDao.class);
	private static SysaccesstokenDao sysaccesstokenDao = SpringContextHolder.getBean(SysaccesstokenDao.class);
	private static SysweixintemplateDao sysweixintemplateDao = SpringContextHolder.getBean(SysweixintemplateDao.class);
	
	private static AgencyUserDao agencyUserDao = SpringContextHolder.getBean(AgencyUserDao.class);
	
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		/*
		  User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		  if (user == null){
			  user = userDao.getByLoginName(new User(null, loginName));
			  if (user == null){
				 return null;
			  }
			  user.setRoleList(roleDao.findList(new Role(user)));
			  CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			  CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		  }
		*/
		//校验用户的时候，不走服务端缓存，直接从数据库查询校验
		User user = userDao.getByLoginName(new User(null, loginName));
		
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		List<Role> roleList = Lists.newArrayList();
		roleList = roleDao.findAllList(new Role());
		return roleList;
	}
	
	/**
	 * 获取部门级角色，部门负责人和巡检人员
	 * @return
	 */
	public static List<Role> getDepartLayerRoleList(){
		List<Role> roleList = Lists.newArrayList();
		roleList = roleDao.findDeprtLayerRoleList(new Role());
		return roleList;
	}
	
	/**
	 * 获取巡检角色
	 * @return
	 */
	public static List<Role> getDepartCheckerLayerRoleList(){
		List<Role> roleList = Lists.newArrayList();
		roleList = roleDao.findDeprtCheckerLayerRoleList(new Role());
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static Menu getTopMenu(){
		Menu topMenu =  getMenuList().get(0);
		return topMenu;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}
	
	public static String getTime(Date date){
		StringBuffer time = new StringBuffer();
        Date date2 = new Date();
        long temp = date2.getTime() - date.getTime();    
        long days = temp / 1000 / 3600/24;                //相差小时数
        if(days>0){
        	time.append(days+"天");
        }
        long temp1 = temp % (1000 * 3600*24);
        long hours = temp1 / 1000 / 3600;                //相差小时数
        if(days>0 || hours>0){
        	time.append(hours+"小时");
        }
        long temp2 = temp1 % (1000 * 3600);
        long mins = temp2 / 1000 / 60;                    //相差分钟数
        time.append(mins + "分钟");
        return  time.toString();
	}


	//发送注册码
	public static String sendRandomCode(String uid, String pwd, String tel, String randomCode) throws IOException {
		//发送内容
		String content = "您的验证码是："+randomCode+"，有效期30分钟，请在有效期内使用。"; 
		
		return SMSUtils.send(uid, pwd, tel, content);

	}
	
	//注册用户重置密码
	public static String sendPass(String uid, String pwd, String tel, String password) throws IOException {
		//发送内容
		String content = "您的新密码是："+password+"，请登录系统，重新设置密码。"; 
		return SMSUtils.send(uid, pwd, tel, content);

	}
	
	/**
	 * 导出Excel调用,根据姓名转换为ID
	 */
	public static User getByUserName(String name){
		User u = new User();
		u.setName(name);
		List<User> list = userDao.findList(u);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new User();
		}
	}
	/**
	 * 导出Excel使用，根据名字转换为id
	 */
	public static Office getByOfficeName(String name){
		Office o = new Office();
		o.setName(name);
		List<Office> list = officeDao.findList(o);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new Office();
		}
	}
	/**
	 * 导出Excel使用，根据名字转换为id
	 */
	public static Area getByAreaName(String name){
		Area a = new Area();
		a.setName(name);
		List<Area> list = areaDao.findList(a);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new Area();
		}
	}
	

    /**
     * 获取所有的菜单
     * @return
     */
	public static List<Menu> getAllMenuList() {
		List<Menu> menuList = Lists.newArrayList();
		menuList = menuDao.findAllList(new Menu());
		return menuList;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取公司级别的角色列表
	 * @return
	 */
	public static List<Role> getCompanyLayerRoleList() {
		return roleDao.findCompanyLayerRoleList();
	}

	public static List<Role> getOrgLayerRoleList() {
		return roleDao.findOrgLayerRoleList();
	}

	/**
	 * 获取系统的微信公众号信息
	 * @return
	 */
	public static Sysweixin getSystemWeixin() {
		List<Sysweixin> list = sysweixinDao.findAllList();
		if(list == null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	/**
	 * 获取系统公众号的accessToken
	 * @return
	 */
	public static Sysaccesstoken getSystemWeixinToken() {
		List<Sysaccesstoken> list = sysaccesstokenDao.findAllList();
		if(list == null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	/**
	 * 根据编号获取微信消息模板
	 * @param string
	 * @return
	 */
	public static Sysweixintemplate getSystemTemplateByBh(String bh) {
		Sysweixintemplate tmp = new Sysweixintemplate();
		tmp.setBh(bh);
		return sysweixintemplateDao.getSystemTemplateByBh(tmp);
	}

	public static void deleteSystemAccesstoken(Sysaccesstoken token) {
		sysaccesstokenDao.deleteAll();
	}

	public static void insertSystemAccesstoken(Sysaccesstoken token) {
		token.preInsert();
		sysaccesstokenDao.insert(token);
	}

	public static void updateSystemAccesstoken(Sysaccesstoken token) {
		token.preUpdate();
		sysaccesstokenDao.update(token);
	}


	/**
	 * 获取场馆角色列表
	 * @return
	 */
	public static List<Role> getSiteRoleList() {
		return roleDao.getSiteRoleList(new Role());
	}

	public static List<Role> findAllRole() {
		List<Role> roleList = Lists.newArrayList();
		roleList = roleDao.findRoleList(new Role());
		return roleList;
	}

	public static AgencyUser getAgencyUser(String id) {
		User user = new User(id);
		AgencyUser auser = new AgencyUser();
		auser = agencyUserDao.get(id);
		if(auser==null){
			return null;
		}
		//获取用户角色
		auser.setRoleList(roleDao.findListByUser(new Role(user)));
		
		return auser;
	}
}
