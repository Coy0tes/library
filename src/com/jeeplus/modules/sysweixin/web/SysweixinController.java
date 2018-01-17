/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixin.web;

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
import com.jeeplus.modules.sysweixin.entity.Sysweixin;
import com.jeeplus.modules.sysweixin.service.SysweixinService;

/**
 * 系统公众号管理Controller
 * @author zhaoliangdong
 * @version 2017-03-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sysweixin/sysweixin")
public class SysweixinController extends BaseController {

	@Autowired
	private SysweixinService sysweixinService;
	
	@ModelAttribute
	public Sysweixin get(@RequestParam(required=false) String id) {
		Sysweixin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysweixinService.get(id);
		}
		if (entity == null){
			entity = new Sysweixin();
		}
		return entity;
	}
	
	/**
	 * 公众号信息列表页面
	 */
	@RequiresPermissions("sysweixin:sysweixin:list")
	@RequestMapping(value = {"list", ""})
	public String list(Sysweixin sysweixin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sysweixin> page = sysweixinService.findPage(new Page<Sysweixin>(request, response), sysweixin); 
		model.addAttribute("page", page);
		return "modules/sysweixin/sysweixinList";
	}

	/**
	 * 查看，增加，编辑公众号信息表单页面
	 */
	@RequiresPermissions(value={"sysweixin:sysweixin:view","sysweixin:sysweixin:add","sysweixin:sysweixin:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Sysweixin sysweixin, Model model) {
		model.addAttribute("sysweixin", sysweixin);
		return "modules/sysweixin/sysweixinForm";
	}

	/**
	 * 保存公众号信息
	 */
	@RequiresPermissions(value={"sysweixin:sysweixin:add","sysweixin:sysweixin:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Sysweixin sysweixin, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysweixin)){
			return form(sysweixin, model);
		}
		if(!sysweixin.getIsNewRecord()){//编辑表单保存
			Sysweixin t = sysweixinService.get(sysweixin.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysweixin, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysweixinService.save(t);//保存
		}else{//新增表单保存
			sysweixinService.save(sysweixin);//保存
		}
		addMessage(redirectAttributes, "保存公众号信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
	}
	
	/**
	 * 删除公众号信息
	 */
	@RequiresPermissions("sysweixin:sysweixin:del")
	@RequestMapping(value = "delete")
	public String delete(Sysweixin sysweixin, RedirectAttributes redirectAttributes) {
		sysweixinService.delete(sysweixin);
		addMessage(redirectAttributes, "删除公众号信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
	}
	
	/**
	 * 批量删除公众号信息
	 */
	@RequiresPermissions("sysweixin:sysweixin:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysweixinService.delete(sysweixinService.get(id));
		}
		addMessage(redirectAttributes, "删除公众号信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sysweixin:sysweixin:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Sysweixin sysweixin, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "公众号信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Sysweixin> page = sysweixinService.findPage(new Page<Sysweixin>(request, response, -1), sysweixin);
    		new ExportExcel("公众号信息", Sysweixin.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出公众号信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sysweixin:sysweixin:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Sysweixin> list = ei.getDataList(Sysweixin.class);
			for (Sysweixin sysweixin : list){
				try{
					sysweixinService.save(sysweixin);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条公众号信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条公众号信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公众号信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
    }
	
	/**
	 * 下载导入公众号信息数据模板
	 */
	@RequiresPermissions("sysweixin:sysweixin:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "公众号信息数据导入模板.xlsx";
    		List<Sysweixin> list = Lists.newArrayList(); 
    		new ExportExcel("公众号信息数据", Sysweixin.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysweixin/sysweixin/?repage";
    }
	
	
	

}