/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.web;

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
import com.jeeplus.modules.school.entity.School;
import com.jeeplus.modules.school.service.SchoolService;

/**
 * schoolController
 * @author mxc
 * @version 2017-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/school/school")
public class SchoolController extends BaseController {

	@Autowired
	private SchoolService schoolService;
	
	@ModelAttribute
	public School get(@RequestParam(required=false) String id) {
		School entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolService.get(id);
		}
		if (entity == null){
			entity = new School();
		}
		return entity;
	}
	
	/**
	 * 学校信息列表页面
	 */
	@RequiresPermissions("school:school:list")
	@RequestMapping(value = {"list", ""})
	public String list(School school, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<School> page = schoolService.findPage(new Page<School>(request, response), school); 
		model.addAttribute("page", page);
		return "modules/school/schoolList";
	}

	/**
	 * 查看，增加，编辑学校信息表单页面
	 */
	@RequiresPermissions(value={"school:school:view","school:school:add","school:school:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(School school, Model model) {
		model.addAttribute("school", school);
		return "modules/school/schoolForm";
	}

	/**
	 * 保存学校信息
	 */
	@RequiresPermissions(value={"school:school:add","school:school:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(School school, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, school)){
			return form(school, model);
		}
		if(!school.getIsNewRecord()){//编辑表单保存
			School t = schoolService.get(school.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(school, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			schoolService.save(t);//保存
		}else{//新增表单保存
			schoolService.save(school);//保存
		}
		addMessage(redirectAttributes, "保存学校信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
	}
	
	/**
	 * 删除学校信息
	 */
	@RequiresPermissions("school:school:del")
	@RequestMapping(value = "delete")
	public String delete(School school, RedirectAttributes redirectAttributes) {
		schoolService.delete(school);
		addMessage(redirectAttributes, "删除学校信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
	}
	
	/**
	 * 批量删除学校信息
	 */
	@RequiresPermissions("school:school:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			schoolService.delete(schoolService.get(id));
		}
		addMessage(redirectAttributes, "删除学校信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:school:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(School school, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学校信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<School> page = schoolService.findPage(new Page<School>(request, response, -1), school);
    		new ExportExcel("学校信息", School.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学校信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:school:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<School> list = ei.getDataList(School.class);
			for (School school : list){
				try{
					schoolService.save(school);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学校信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学校信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学校信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
    }
	
	/**
	 * 下载导入学校信息数据模板
	 */
	@RequiresPermissions("school:school:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学校信息数据导入模板.xlsx";
    		List<School> list = Lists.newArrayList(); 
    		new ExportExcel("学校信息数据", School.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/school/?repage";
    }
	
	
	

}