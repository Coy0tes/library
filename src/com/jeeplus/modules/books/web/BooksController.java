/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.books.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.booktype.service.BookTypeService;
import com.jeeplus.modules.canshu.entity.Canshu;
import com.jeeplus.modules.canshu.service.CanshuService;

/**
 * booksController
 * @author mxc
 * @version 2017-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/books/books")
public class BooksController extends BaseController {

	@Autowired
	private BooksService booksService;
	@Autowired
	private BookTypeService bookTypeService;
	@Autowired
	private CanshuService canshuService;
	
	@ModelAttribute
	public Books get(@RequestParam(required=false) String id) {
		Books entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = booksService.get(id);
		}
		if (entity == null){
			entity = new Books();
		}
		return entity;
	}
	
	/**
	 * 索书号页面
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value="ssh")
	public String ssh(Books books, String ids, Model model, HttpServletRequest request){
		// 取到选中的id
 		ids = request.getParameter("ids");
		
		model.addAttribute("books", books);
		model.addAttribute("ids", ids);
		return "modules/books/booksAddSsh";
	}
	
	/**
	 * 添加索书号
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addSsh")
	public String addSsh(String ids, Books books, RedirectAttributes redirectAttributes, HttpServletRequest request){
		String msg = "";
		ids = request.getParameter("ids");
		msg = booksService.addSsh(ids, books);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
	}
	
	/**
	 * 根据ISBN号查找图书
	 * @param books
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getByIsbn")
	@ResponseBody
	public Map<Object, Object> getByIsbn(Books books, HttpServletRequest request, HttpServletResponse response, Model model) {
 		Map<Object, Object> map = new HashMap<Object, Object>();
		books = booksService.getByIsbn(books);
		
		// 查询 参数设置并返回
		Canshu canshu = new Canshu();
		canshu = canshuService.getList(canshu);
		
		map.put("canshu", canshu);
		map.put("books", books);
		return map;
	}
	
	
	/**
	 * 图书管理列表页面
	 */
	@RequiresPermissions("books:books:list")
	@RequestMapping(value = {"list", ""})
	public String list(Books books, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Books> page = booksService.findPage(new Page<Books>(request, response), books); 
		model.addAttribute("page", page);
		return "modules/books/booksList";
	}

	/**
	 * 查看，增加，编辑图书管理表单页面
	 */
	@RequiresPermissions(value={"books:books:view","books:books:add","books:books:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Books books, Model model) {
		model.addAttribute("books", books);
		return "modules/books/booksForm";
	}
	

	/**
	 * 保存图书管理
	 */
	@RequiresPermissions(value={"books:books:add","books:books:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Books books, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "";
		if (!beanValidator(model, books)){
			return form(books, model);
		}
		if(!books.getIsNewRecord()){//编辑表单保存
			Books t = booksService.get(books.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(books, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			booksService.save(t);//保存
		}else{//新增表单保存
			
			// 去重判断
			int rep = booksService.findRepart(books);
			if(rep > 1){
				msg = "ISBN号【"+books.getIsbn()+"】已存在!";
			}
			
			booksService.save(books);//保存
			msg = "保存图书管理成功";
		}
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
	}
	
	/**
	 * 图书入库界面
	 */
	@RequiresPermissions(value={"books:books:view","books:books:add","books:books:edit"},logical=Logical.OR)
	@RequestMapping(value = "formAddBook")
	public String formAddBook(Books books, Model model) {
		System.out.println(books);
		books = new Books();
//		model.addAttribute("books", books);
		return "modules/books/booksAddForm";
	}
	
	/**
	 * 保存图书入库管理
	 */
	@RequiresPermissions(value={"books:books:add","books:books:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveAdd")
	public String saveAdd(Books books, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "保存图书管理成功";
		msg = booksService.saveInfo(books);//保存
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/books/books/formAddBook";
	}
	
	
	/**
	 * 提交时异步效验ISBN号是否重复
	 * @param books
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findRepart")
	@ResponseBody
	public Map<String, Object> findRepart(Books books, Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		int rep = booksService.findRepart(books);
		map.put("rep", rep);
		return map;
	}
	
	/**
	 * 删除图书管理
	 */
	@RequiresPermissions("books:books:del")
	@RequestMapping(value = "delete")
	public String delete(Books books, RedirectAttributes redirectAttributes) {
		booksService.delete(books);
		addMessage(redirectAttributes, "删除图书管理成功");
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
	}
	
	/**
	 * 批量删除图书管理
	 */
	@RequiresPermissions("books:books:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			booksService.delete(booksService.get(id));
		}
		addMessage(redirectAttributes, "删除图书管理成功");
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("books:books:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Books books, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "图书管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Books> page = booksService.findPage(new Page<Books>(request, response, -1), books);
    		new ExportExcel("图书管理", Books.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出图书管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("books:books:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Books> list = ei.getDataList(Books.class);
			for (Books books : list){
				try{
					booksService.save(books);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条图书管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条图书管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入图书管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
    }
	
	/**
	 * 下载导入图书管理数据模板
	 */
	@RequiresPermissions("books:books:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "图书管理数据导入模板.xlsx";
    		List<Books> list = Lists.newArrayList(); 
    		new ExportExcel("图书管理数据", Books.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/books/books/?repage";
    }
	
	
	/**
	 * 获取远程书籍列表
	 * @param request
	 * @param response
	 * @return
	 */
	 @RequestMapping(value = "getRemoteBookList")
	 @ResponseBody
	 public Map<String, Object> getRemoteBookList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Books e = new Books();
		 
		 //分页设置
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		
		String isbn = request.getParameter("isbn");
		String ssh = request.getParameter("ssh");
		String name = request.getParameter("name");
		String cbs = request.getParameter("cbs");
		String author = request.getParameter("author");
		String status = request.getParameter("status");
		
		if(StringUtils.isNotEmpty(isbn)){
			e.setIsbn(isbn);
		}
		if(StringUtils.isNotEmpty(ssh)){
			e.setSsh(ssh);
		}
		if(StringUtils.isNotEmpty(name)){
			e.setName(name);
		}
		if(StringUtils.isNotEmpty(cbs)){
			e.setCbs(cbs);
		}
		if(StringUtils.isNotEmpty(author)){
			e.setAuthor(author);
		}
		if(StringUtils.isNotEmpty(status)){
			e.setStatus(status);
		}
		if(pageNo == null){
			pageNo = "1";
		}
		if(pageSize == null){
			pageSize = "10";	
		}
		
		//分页对象
		Page<Books> page = new Page<Books>();
		page.setPageNo(Integer.valueOf(pageNo));
		page.setPageSize(Integer.valueOf(pageSize));
		e.setPage(page);
		page.setList(booksService.findList(e));
		
		//计算总页数
		Books ee = new Books();
		MyBeanUtils.copyBeanNotNull2Bean(e, ee);
		ee.setPage(null);
		int totalrows = booksService.findList(ee).size();
		int sizePerPage = Integer.valueOf(pageSize);
		int totalpages = 0;
		if(totalrows%sizePerPage == 0){
			totalpages = totalrows / sizePerPage;
		}else{
			totalpages = totalrows / sizePerPage + 1;
		}
			
		//组装返回数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", page.getList());
		map.put("firstpage", page.isFirstPage());
		map.put("lastpage", page.isLastPage());
		map.put("total", totalrows);
		map.put("pages", totalpages);
		
		return map;
	 }
	 
	 
	 @RequestMapping(value = "updateSsh")
	 @ResponseBody
	 public String updateSsh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String rtns = "";
		 String isbnlist = request.getParameter("isbnlist");
		 String ssh = request.getParameter("ssh");
		 rtns = this.booksService.updateSsh(isbnlist,ssh);
		 
		 return rtns;
	 }

}