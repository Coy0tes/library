/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachers.web;

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
import com.jeeplus.modules.teachers.entity.Teachers;
import com.jeeplus.modules.teachers.service.TeachersService;

/**
 * teachersController
 * @author mxc
 * @version 2017-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/teachers/teachers")
public class TeachersController extends BaseController {

	@Autowired
	private TeachersService teachersService;
	
	@ModelAttribute
	public Teachers get(@RequestParam(required=false) String id) {
		Teachers entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = teachersService.get(id);
		}
		if (entity == null){
			entity = new Teachers();
		}
		return entity;
	}
	
	/**
	 * 教师管理列表页面
	 */
	@RequiresPermissions("teachers:teachers:list")
	@RequestMapping(value = {"list", ""})
	public String list(Teachers teachers, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Teachers> page = teachersService.findPage(new Page<Teachers>(request, response), teachers); 
		model.addAttribute("page", page);
		return "modules/teachers/teachersList";
	}

	/**
	 * 查看，增加，编辑教师管理表单页面
	 */
	@RequiresPermissions(value={"teachers:teachers:view","teachers:teachers:add","teachers:teachers:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Teachers teachers, Model model) {
		model.addAttribute("teachers", teachers);
		return "modules/teachers/teachersForm";
	}

	/**
	 * 保存教师管理
	 */
	@RequiresPermissions(value={"teachers:teachers:add","teachers:teachers:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Teachers teachers, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, teachers)){
			return form(teachers, model);
		}
		if(!teachers.getIsNewRecord()){//编辑表单保存
			Teachers t = teachersService.get(teachers.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(teachers, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			teachersService.save(t);//保存
		}else{//新增表单保存
			teachersService.save(teachers);//保存
		}
		addMessage(redirectAttributes, "保存教师管理成功");
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
	}
	
	/**
	 * 删除教师管理
	 */
	@RequiresPermissions("teachers:teachers:del")
	@RequestMapping(value = "delete")
	public String delete(Teachers teachers, RedirectAttributes redirectAttributes) {
		teachersService.delete(teachers);
		addMessage(redirectAttributes, "删除教师管理成功");
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
	}
	
	/**
	 * 批量删除教师管理
	 */
	@RequiresPermissions("teachers:teachers:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			teachersService.delete(teachersService.get(id));
		}
		addMessage(redirectAttributes, "删除教师管理成功");
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teachers:teachers:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Teachers teachers, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教师管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Teachers> page = teachersService.findPage(new Page<Teachers>(request, response, -1), teachers);
    		new ExportExcel("教师管理", Teachers.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出教师管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("teachers:teachers:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Teachers> list = ei.getDataList(Teachers.class);
			for (Teachers teachers : list){
				try{
					teachersService.save(teachers);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条教师管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条教师管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入教师管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
    }
	
	/**
	 * 下载导入教师管理数据模板
	 */
	@RequiresPermissions("teachers:teachers:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教师管理数据导入模板.xlsx";
    		List<Teachers> list = Lists.newArrayList(); 
    		new ExportExcel("教师管理数据", Teachers.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teachers/teachers/?repage";
    }
	
	
	

}