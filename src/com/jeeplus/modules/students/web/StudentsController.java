/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.students.web;

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
import com.jeeplus.modules.classes.entity.Classes;
import com.jeeplus.modules.classes.service.ClassesService;
import com.jeeplus.modules.students.entity.Students;
import com.jeeplus.modules.students.service.StudentsService;

/**
 * studentsController
 * @author mxc
 * @version 2017-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/students/students")
public class StudentsController extends BaseController {

	@Autowired
	private StudentsService studentsService;
	@Autowired
	private ClassesService classesService;
	
	@ModelAttribute
	public Students get(@RequestParam(required=false) String id) {
		Students entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = studentsService.get(id);
		}
		if (entity == null){
			entity = new Students();
		}
		return entity;
	}
	
	/**
	 * 学生管理列表页面
	 */
	@RequiresPermissions("students:students:list")
	@RequestMapping(value = {"list", ""})
	public String list(Students students, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 获取班级信息
		Classes classes = new Classes();
		List<Classes> list = classesService.findList(classes);
		Page<Students> page = studentsService.findPage(new Page<Students>(request, response), students);
		model.addAttribute("classes", list);
		model.addAttribute("page", page);
		return "modules/students/studentsList";
	}

	/**
	 * 查看，增加，编辑学生管理表单页面
	 */
	@RequiresPermissions(value={"students:students:view","students:students:add","students:students:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Students students, Model model) {
		// 获取班级信息
		Classes classes = new Classes();
		List<Classes> list = classesService.findList(classes);
		model.addAttribute("classes", list);
		model.addAttribute("students", students);
		return "modules/students/studentsForm";
	}

	/**
	 * 保存学生管理
	 */
	@RequiresPermissions(value={"students:students:add","students:students:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Students students, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, students)){
			return form(students, model);
		}
		if(!students.getIsNewRecord()){//编辑表单保存
			Students t = studentsService.get(students.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(students, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			studentsService.save(t);//保存
		}else{//新增表单保存
			studentsService.save(students);//保存
		}
		addMessage(redirectAttributes, "保存学生管理成功");
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
	}
	
	/**
	 * 删除学生管理
	 */
	@RequiresPermissions("students:students:del")
	@RequestMapping(value = "delete")
	public String delete(Students students, RedirectAttributes redirectAttributes) {
		studentsService.delete(students);
		addMessage(redirectAttributes, "删除学生管理成功");
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
	}
	
	/**
	 * 批量删除学生管理
	 */
	@RequiresPermissions("students:students:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			studentsService.delete(studentsService.get(id));
		}
		addMessage(redirectAttributes, "删除学生管理成功");
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("students:students:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Students students, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Students> page = studentsService.findPage(new Page<Students>(request, response, -1), students);
    		new ExportExcel("学生管理", Students.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("students:students:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Students> list = ei.getDataList(Students.class);
			for (Students students : list){
				try{
					studentsService.save(students);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
    }
	
	/**
	 * 下载导入学生管理数据模板
	 */
	@RequiresPermissions("students:students:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生管理数据导入模板.xlsx";
    		List<Students> list = Lists.newArrayList(); 
    		new ExportExcel("学生管理数据", Students.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/students/students/?repage";
    }
	
	
	

}