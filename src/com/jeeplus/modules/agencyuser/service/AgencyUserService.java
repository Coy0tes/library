/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.agencyuser.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.agencyuser.entity.AgencyUser;
import com.jeeplus.modules.agencyuser.dao.AgencyUserDao;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 账号管理Service
 * @author zhaoliangdong
 * @version 2017-04-18
 */
@Service
@Transactional(readOnly = true)
public class AgencyUserService extends CrudService<AgencyUserDao, AgencyUser> {

	public AgencyUser get(String id) {
		return UserUtils.getAgencyUser(id);
	}
	
	public List<AgencyUser> findList(AgencyUser agencyUser) {
		return super.findList(agencyUser);
	}
	
	public Page<AgencyUser> findPage(Page<AgencyUser> page, AgencyUser agencyUser) {
		return super.findPage(page, agencyUser);
	}
	
	@Transactional(readOnly = false)
	public void save(AgencyUser agencyUser) {
		super.save(agencyUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(AgencyUser agencyUser) {
		super.delete(agencyUser);
	}
	
	@Transactional(readOnly = false)
	public String saveUser(AgencyUser user) {
		String msg = "保存账号成功";
		if (StringUtils.isBlank(user.getId())){
			user.preInsert();
			dao.insert(user);
		}else{
			// 更新用户数据
			user.preUpdate();
			dao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			dao.deleteUserRole(user);
			dao.insertUserRole(user);
		}
		
		return msg;
	}

	//查询所有未被指派的用户
	public List<AgencyUser> findListUndo(AgencyUser agencyUser) {
		return dao.findListUndo(agencyUser);
	}
	
	
	
}