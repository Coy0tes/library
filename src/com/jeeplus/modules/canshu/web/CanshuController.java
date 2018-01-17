/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.canshu.web;

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
import com.jeeplus.modules.canshu.entity.Canshu;
import com.jeeplus.modules.canshu.service.CanshuService;

/**
 * canshuController
 * @author mxc
 * @version 2017-11-10
 */
@Controller
@RequestMapping(value = "${adminPath}/canshu/canshu")
public class CanshuController extends BaseController {

	@Autowired
	private CanshuService canshuService;
	
	@ModelAttribute
	public Canshu get(@RequestParam(required=false) String id) {
		Canshu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = canshuService.get(id);
		}
		if (entity == null){
			entity = new Canshu();
		}
		return entity;
	}
	
	/**
	 * 参数配置列表页面
	 */
	@RequiresPermissions("canshu:canshu:list")
	@RequestMapping(value = {"list", ""})
	public String list(Canshu canshu, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		canshu = canshuService.find(canshu);
		if(canshu==null){
			canshu = new Canshu();
		}
		
		model.addAttribute("canshu", canshu);
		return "modules/canshu/canshuForm";
	}

	/**
	 * 查看，增加，编辑参数配置表单页面
	 */
	@RequiresPermissions(value={"canshu:canshu:view","canshu:canshu:add","canshu:canshu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Canshu canshu, Model model) {
		model.addAttribute("canshu", canshu);
		return "modules/canshu/canshuForm";
	}

	/**
	 * 保存参数配置
	 */
	@RequiresPermissions(value={"canshu:canshu:add","canshu:canshu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Canshu canshu, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, canshu)){
			return form(canshu, model);
		}
		if(!canshu.getIsNewRecord()){//编辑表单保存
			Canshu t = canshuService.get(canshu.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(canshu, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			canshuService.save(t);//保存
		}else{//新增表单保存
			canshuService.save(canshu);//保存
		}
		addMessage(redirectAttributes, "保存参数配置成功");
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
	}
	
	/**
	 * 删除参数配置
	 */
	@RequiresPermissions("canshu:canshu:del")
	@RequestMapping(value = "delete")
	public String delete(Canshu canshu, RedirectAttributes redirectAttributes) {
		canshuService.delete(canshu);
		addMessage(redirectAttributes, "删除参数配置成功");
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
	}
	
	/**
	 * 批量删除参数配置
	 */
	@RequiresPermissions("canshu:canshu:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			canshuService.delete(canshuService.get(id));
		}
		addMessage(redirectAttributes, "删除参数配置成功");
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("canshu:canshu:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Canshu canshu, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "参数配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Canshu> page = canshuService.findPage(new Page<Canshu>(request, response, -1), canshu);
    		new ExportExcel("参数配置", Canshu.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出参数配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("canshu:canshu:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Canshu> list = ei.getDataList(Canshu.class);
			for (Canshu canshu : list){
				try{
					canshuService.save(canshu);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条参数配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条参数配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入参数配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
    }
	
	/**
	 * 下载导入参数配置数据模板
	 */
	@RequiresPermissions("canshu:canshu:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "参数配置数据导入模板.xlsx";
    		List<Canshu> list = Lists.newArrayList(); 
    		new ExportExcel("参数配置数据", Canshu.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/canshu/canshu/?repage";
    }
	
	
	

}