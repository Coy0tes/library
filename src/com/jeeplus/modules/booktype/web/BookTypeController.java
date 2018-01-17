/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.booktype.web;

import java.util.ArrayList;
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
import com.jeeplus.modules.booktype.entity.BookType;
import com.jeeplus.modules.booktype.service.BookTypeService;

/**
 * 图书分类管理Controller
 * @author mxc
 * @version 2017-11-06
 */
@Controller
@RequestMapping(value = "${adminPath}/booktype/bookType")
public class BookTypeController extends BaseController {

	@Autowired
	private BookTypeService bookTypeService;
	
	@ModelAttribute
	public BookType get(@RequestParam(required=false) String id) {
		BookType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookTypeService.get(id);
		}
		if (entity == null){
			entity = new BookType();
		}
		return entity;
	}
	
	/**
	 * 图书分类列表页面
	 */
	@RequiresPermissions("booktype:bookType:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookType bookType, HttpServletRequest request, HttpServletResponse response, Model model) {
		bookType.setParent("0");
		List<BookType> list = bookTypeService.findParentsId(bookType);
		List<BookType> topList = new ArrayList<BookType>();
		model.addAttribute("topList", topList);
		model.addAttribute("list", list);
		return "modules/booktype/bookTypeList";
	}
	
	/**
	 * 图书分类列表页面
	 */
	@RequiresPermissions("booktype:bookType:list")
	@RequestMapping(value = "nextList")
	public String nextList(BookType bookType, HttpServletRequest request, HttpServletResponse response, Model model) {
		BookType bt = new BookType();
		boolean a = true;
		String str = "";
		
		
		List<BookType> list = bookTypeService.findParentsId(bookType);
		
		// 如果查出为空，表示没有下级了
		if(list.size()==0){
			
			bookType.setId(bookType.getParent());
			list = bookTypeService.findList(bookType);
			String msg = "没有下级了！！";
			model.addAttribute("message", msg);
			
		}
		// 导航设置
		
		
		bookType.setId(bookType.getParent());
		
		do{
			
			bt = bookTypeService.get(bookType);		// 只要还能查到上级，就继续循环
			if(bt==null){
				a=false;
			}else{
				str = "<li><a href='/library/a/booktype/bookType/nextList?parent="+ bt.getId() +"' title="+ bt.getRemarks()+"><font style='color:blue'>"+ bt.getRemarks() +"</font></a></li>" + str;
				bookType.setId(bt.getParent());
			}
		}while(a);
		
		model.addAttribute("topList", str);
		model.addAttribute("list", list);
		return "modules/booktype/bookTypeList";
	}
	
	/**
	 * 图书选择分类界面
	 */
	@RequiresPermissions("booktype:bookType:list")
	@RequestMapping(value = "bookTypeList")
	public String bookTypeList(BookType bookType, HttpServletRequest request, HttpServletResponse response, Model model) {
		BookType bt = new BookType();
		boolean a = true;
		String str = "";
		int num = 0;	// 标记是否是最后一级，0不是最后一级，1是最后一级
		
		List<BookType> list = bookTypeService.findParentsId(bookType);
		
		// 如果查出为空，表示没有下级了
		if(list.size()==0){
			System.out.println(32);
			bookType.setId(bookType.getParent());
			list = bookTypeService.findList(bookType);
			String msg = "没有下级了！！";
			num = 1;
			model.addAttribute("message", msg);
			
		}
		// 导航设置
		bookType.setId(bookType.getParent());
		
		do{
			
			bt = bookTypeService.get(bookType);		// 只要还能查到上级，就继续循环
			if(bt==null){
				a=false;
			}else{
				str = "<li><a href='/library/a/booktype/bookType/bookTypeList?parent="+ bt.getId() +"' title="+ bt.getRemarks()+"><font style='color:blue'>"+ bt.getRemarks() +"</font></a></li>" + str;
				bookType.setId(bt.getParent());
			}
		}while(a);
		
		model.addAttribute("num", num);
		model.addAttribute("topList", str);
		model.addAttribute("list", list);
		return "modules/books/bookTypeList";
	}
	

	/**
	 * 查看，增加，编辑图书分类表单页面
	 */
	@RequiresPermissions(value={"booktype:bookType:view","booktype:bookType:add","booktype:bookType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookType bookType, Model model) {
		model.addAttribute("bookType", bookType);
		return "modules/booktype/bookTypeForm";
	}

	/**
	 * 保存图书分类
	 */
	@RequiresPermissions(value={"booktype:bookType:add","booktype:bookType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookType bookType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, bookType)){
			return form(bookType, model);
		}
		if(!bookType.getIsNewRecord()){//编辑表单保存
			BookType t = bookTypeService.get(bookType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(bookType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			bookTypeService.save(t);//保存
		}else{//新增表单保存
			bookTypeService.save(bookType);//保存
		}
		addMessage(redirectAttributes, "保存图书分类成功");
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
	}
	
	/**
	 * 删除图书分类
	 */
	@RequiresPermissions("booktype:bookType:del")
	@RequestMapping(value = "delete")
	public String delete(BookType bookType, RedirectAttributes redirectAttributes) {
		bookTypeService.delete(bookType);
		addMessage(redirectAttributes, "删除图书分类成功");
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
	}
	
	/**
	 * 批量删除图书分类
	 */
	@RequiresPermissions("booktype:bookType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookTypeService.delete(bookTypeService.get(id));
		}
		addMessage(redirectAttributes, "删除图书分类成功");
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("booktype:bookType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookType bookType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "图书分类"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BookType> page = bookTypeService.findPage(new Page<BookType>(request, response, -1), bookType);
    		new ExportExcel("图书分类", BookType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出图书分类记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("booktype:bookType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BookType> list = ei.getDataList(BookType.class);
			for (BookType bookType : list){
				try{
					bookTypeService.save(bookType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条图书分类记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条图书分类记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入图书分类失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
    }
	
	/**
	 * 下载导入图书分类数据模板
	 */
	@RequiresPermissions("booktype:bookType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "图书分类数据导入模板.xlsx";
    		List<BookType> list = Lists.newArrayList(); 
    		new ExportExcel("图书分类数据", BookType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/booktype/bookType/?repage";
    }
	
	
	

}