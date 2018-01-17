/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysaccesstoken.web;

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
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;
import com.jeeplus.modules.sysaccesstoken.service.SysaccesstokenService;

/**
 * accesstokenController
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sysaccesstoken/sysaccesstoken")
public class SysaccesstokenController extends BaseController {

	@Autowired
	private SysaccesstokenService sysaccesstokenService;
	
	@ModelAttribute
	public Sysaccesstoken get(@RequestParam(required=false) String id) {
		Sysaccesstoken entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysaccesstokenService.get(id);
		}
		if (entity == null){
			entity = new Sysaccesstoken();
		}
		return entity;
	}
	
	/**
	 * token信息列表页面
	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:list")
	@RequestMapping(value = {"list", ""})
	public String list(Sysaccesstoken sysaccesstoken, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sysaccesstoken> page = sysaccesstokenService.findPage(new Page<Sysaccesstoken>(request, response), sysaccesstoken); 
		model.addAttribute("page", page);
		return "modules/sysaccesstoken/sysaccesstokenList";
	}

	/**
	 * 查看，增加，编辑token信息表单页面
	 */
	@RequiresPermissions(value={"sysaccesstoken:sysaccesstoken:view","sysaccesstoken:sysaccesstoken:add","sysaccesstoken:sysaccesstoken:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Sysaccesstoken sysaccesstoken, Model model) {
		model.addAttribute("sysaccesstoken", sysaccesstoken);
		return "modules/sysaccesstoken/sysaccesstokenForm";
	}

	/**
	 * 保存token信息
	 */
	@RequiresPermissions(value={"sysaccesstoken:sysaccesstoken:add","sysaccesstoken:sysaccesstoken:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Sysaccesstoken sysaccesstoken, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysaccesstoken)){
			return form(sysaccesstoken, model);
		}
		if(!sysaccesstoken.getIsNewRecord()){//编辑表单保存
			Sysaccesstoken t = sysaccesstokenService.get(sysaccesstoken.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysaccesstoken, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysaccesstokenService.save(t);//保存
		}else{//新增表单保存
			sysaccesstokenService.save(sysaccesstoken);//保存
		}
		addMessage(redirectAttributes, "保存token信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
	}
	
	/**
	 * 删除token信息
	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:del")
	@RequestMapping(value = "delete")
	public String delete(Sysaccesstoken sysaccesstoken, RedirectAttributes redirectAttributes) {
		sysaccesstokenService.delete(sysaccesstoken);
		addMessage(redirectAttributes, "删除token信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
	}
	
	/**
	 * 批量删除token信息
	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysaccesstokenService.delete(sysaccesstokenService.get(id));
		}
		addMessage(redirectAttributes, "删除token信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Sysaccesstoken sysaccesstoken, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "token信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Sysaccesstoken> page = sysaccesstokenService.findPage(new Page<Sysaccesstoken>(request, response, -1), sysaccesstoken);
    		new ExportExcel("token信息", Sysaccesstoken.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出token信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Sysaccesstoken> list = ei.getDataList(Sysaccesstoken.class);
			for (Sysaccesstoken sysaccesstoken : list){
				try{
					sysaccesstokenService.save(sysaccesstoken);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条token信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条token信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入token信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
    }
	
	/**
	 * 下载导入token信息数据模板
	 */
	@RequiresPermissions("sysaccesstoken:sysaccesstoken:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "token信息数据导入模板.xlsx";
    		List<Sysaccesstoken> list = Lists.newArrayList(); 
    		new ExportExcel("token信息数据", Sysaccesstoken.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysaccesstoken/sysaccesstoken/?repage";
    }
	
	
	

}