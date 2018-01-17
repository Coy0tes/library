/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.agencyuser.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.agencyuser.entity.AgencyUser;
import com.jeeplus.modules.agencyuser.service.AgencyUserService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 账号管理Controller
 * @author zhaoliangdong
 * @version 2017-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/agencyuser/agencyUser")
public class AgencyUserController extends BaseController {

	@Autowired
	private AgencyUserService agencyUserService;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public AgencyUser get(@RequestParam(required=false) String id) {
		AgencyUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = agencyUserService.get(id);
		}
		if (entity == null){
			entity = new AgencyUser();
		}
		return entity;
	}
	
	/**
	 * 账号信息列表页面
	 */
	@RequiresPermissions("agencyuser:agencyUser:list")
	@RequestMapping(value = {"list", ""})
	public String list(AgencyUser agencyUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AgencyUser> page = agencyUserService.findPage(new Page<AgencyUser>(request, response), agencyUser); 
		model.addAttribute("page", page);
		return "modules/agencyuser/agencyUserList";
	}

	/**
	 * 查看，增加，编辑账号信息表单页面
	 */
	@RequiresPermissions(value={"agencyuser:agencyUser:view","agencyuser:agencyUser:add","agencyuser:agencyUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AgencyUser agencyUser, Model model) {
		model.addAttribute("allRoles", UserUtils.findAllRole());
		model.addAttribute("agencyUser", agencyUser);
		return "modules/agencyuser/agencyUserForm";
	}

	/**
	 * 保存账号信息
	 */
	@RequiresPermissions(value={"agencyuser:agencyUser:add","agencyuser:agencyUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AgencyUser agencyUser, Model model, RedirectAttributes redirectAttributes) throws Exception{
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(agencyUser.getNewPassword())) {
			agencyUser.setPassword(SystemService.entryptPassword(agencyUser.getNewPassword()));
		}
		// 保存用户信息
	    String msg = this.agencyUserService.saveUser(agencyUser);
				
	    //如果修改当前用户信息，则清空该用户的缓存
		if (agencyUser.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
		}
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
	}
	
	/**
	 * 删除账号信息
	 */
	@RequiresPermissions("agencyuser:agencyUser:del")
	@RequestMapping(value = "delete")
	public String delete(AgencyUser agencyUser, RedirectAttributes redirectAttributes) {
		agencyUserService.delete(agencyUser);
		addMessage(redirectAttributes, "删除账号信息成功");
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
	}
	
	/**
	 * 批量删除账号信息
	 */
	@RequiresPermissions("agencyuser:agencyUser:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			agencyUserService.delete(agencyUserService.get(id));
		}
		addMessage(redirectAttributes, "删除账号信息成功");
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("agencyuser:agencyUser:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(AgencyUser agencyUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "账号信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AgencyUser> page = agencyUserService.findPage(new Page<AgencyUser>(request, response, -1), agencyUser);
    		new ExportExcel("账号信息", AgencyUser.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出账号信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("agencyuser:agencyUser:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AgencyUser> list = ei.getDataList(AgencyUser.class);
			for (AgencyUser agencyUser : list){
				try{
					agencyUserService.save(agencyUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条账号信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条账号信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入账号信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
    }
	
	/**
	 * 下载导入账号信息数据模板
	 */
	@RequiresPermissions("agencyuser:agencyUser:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "账号信息数据导入模板.xlsx";
    		List<AgencyUser> list = Lists.newArrayList(); 
    		new ExportExcel("账号信息数据", AgencyUser.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/agencyuser/agencyUser/?repage";
    }

}