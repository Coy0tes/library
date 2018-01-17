/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixintemplate.web;

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
import com.jeeplus.modules.sysweixintemplate.entity.Sysweixintemplate;
import com.jeeplus.modules.sysweixintemplate.service.SysweixintemplateService;

/**
 * 系统微信模板管理Controller
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sysweixintemplate/sysweixintemplate")
public class SysweixintemplateController extends BaseController {

	@Autowired
	private SysweixintemplateService sysweixintemplateService;
	
	@ModelAttribute
	public Sysweixintemplate get(@RequestParam(required=false) String id) {
		Sysweixintemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysweixintemplateService.get(id);
		}
		if (entity == null){
			entity = new Sysweixintemplate();
		}
		return entity;
	}
	
	/**
	 * 模板信息列表页面
	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:list")
	@RequestMapping(value = {"list", ""})
	public String list(Sysweixintemplate sysweixintemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sysweixintemplate> page = sysweixintemplateService.findPage(new Page<Sysweixintemplate>(request, response), sysweixintemplate); 
		model.addAttribute("page", page);
		return "modules/sysweixintemplate/sysweixintemplateList";
	}

	/**
	 * 查看，增加，编辑模板信息表单页面
	 */
	@RequiresPermissions(value={"sysweixintemplate:sysweixintemplate:view","sysweixintemplate:sysweixintemplate:add","sysweixintemplate:sysweixintemplate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Sysweixintemplate sysweixintemplate, Model model) {
		model.addAttribute("sysweixintemplate", sysweixintemplate);
		return "modules/sysweixintemplate/sysweixintemplateForm";
	}

	/**
	 * 保存模板信息
	 */
	@RequiresPermissions(value={"sysweixintemplate:sysweixintemplate:add","sysweixintemplate:sysweixintemplate:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Sysweixintemplate sysweixintemplate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysweixintemplate)){
			return form(sysweixintemplate, model);
		}
		if(!sysweixintemplate.getIsNewRecord()){//编辑表单保存
			Sysweixintemplate t = sysweixintemplateService.get(sysweixintemplate.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysweixintemplate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysweixintemplateService.save(t);//保存
		}else{//新增表单保存
			sysweixintemplateService.save(sysweixintemplate);//保存
		}
		addMessage(redirectAttributes, "保存模板信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
	}
	
	/**
	 * 删除模板信息
	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:del")
	@RequestMapping(value = "delete")
	public String delete(Sysweixintemplate sysweixintemplate, RedirectAttributes redirectAttributes) {
		sysweixintemplateService.delete(sysweixintemplate);
		addMessage(redirectAttributes, "删除模板信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
	}
	
	/**
	 * 批量删除模板信息
	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysweixintemplateService.delete(sysweixintemplateService.get(id));
		}
		addMessage(redirectAttributes, "删除模板信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Sysweixintemplate sysweixintemplate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "模板信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Sysweixintemplate> page = sysweixintemplateService.findPage(new Page<Sysweixintemplate>(request, response, -1), sysweixintemplate);
    		new ExportExcel("模板信息", Sysweixintemplate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出模板信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Sysweixintemplate> list = ei.getDataList(Sysweixintemplate.class);
			for (Sysweixintemplate sysweixintemplate : list){
				try{
					sysweixintemplateService.save(sysweixintemplate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条模板信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条模板信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
    }
	
	/**
	 * 下载导入模板信息数据模板
	 */
	@RequiresPermissions("sysweixintemplate:sysweixintemplate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "模板信息数据导入模板.xlsx";
    		List<Sysweixintemplate> list = Lists.newArrayList(); 
    		new ExportExcel("模板信息数据", Sysweixintemplate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixintemplate/sysweixintemplate/?repage";
    }
	
	
	

}