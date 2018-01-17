/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudentback.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudentback.entity.BookStudentBack;
import com.jeeplus.modules.bookstudentback.service.BookStudentBackService;

/**
 * bookStudentBackController
 * @author mxc
 * @version 2017-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/bookstudentback/bookStudentBack")
public class BookStudentBackController extends BaseController {

	@Autowired
	private BookStudentBackService bookStudentBackService;
	@Autowired
	private BooksService booksService;
	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	
	@ModelAttribute
	public BookStudentBack get(@RequestParam(required=false) String id) {
		BookStudentBack entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookStudentBackService.get(id);
		}
		if (entity == null){
			entity = new BookStudentBack();
		}
		return entity;
	}
	
	
	/**
	 * 学生还书页面：
	 * 	图书信息
	 * 	学生信息
	 * @param bookStudentDetail
	 * @return
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:list")
	@RequestMapping(value = "getByIsbn")
	@ResponseBody
	public Map<Object, Object> getByIsbn(BookStudentDetail bookStudentDetail){
		Map<Object, Object> map = new HashMap<Object, Object>();
		Books books = new Books();
		
		// 学生信息
		// 根据ISBN号查出当前学生跟班级
		List<BookStudentDetail> list = bookStudentDetailDao.findSdtCls(bookStudentDetail);
		
		if(list.size()>0){
			bookStudentDetail = list.get(0);
			
			// 逾期未还天数金额计算START----------------------------------
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//现在时间
				Date d = new Date();
				String yhrq = bookStudentDetail.getYhrq();
				Date yhrqDate = format.parse(yhrq);
				// 现在时间-应还时间
				long day=(d.getTime() - yhrqDate.getTime())/(24*60*60*1000);
				// 逾期天数赋值，如果没有逾期，则为0
				if(day>0){
					bookStudentDetail.setCqts((int)day);
				}else{
					bookStudentDetail.setCqts(0);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			// 逾期未还天数金额计算END----------------------------------
			
			map.put("bookStudentDetail", bookStudentDetail);
		}else{
			map.put("bookStudentDetail", "00");
		}
		
		map.put("books", books);
		return map;
	}
	
	/**
	 * 学生还书管理列表页面
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookStudentBack bookStudentBack, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<BookStudentBack> page = bookStudentBackService.findPage(new Page<BookStudentBack>(request, response), bookStudentBack); 
//		model.addAttribute("page", page);
		return "modules/bookstudentback/bookStudentBackForm";
	}

	/**
	 * 查看，增加，编辑学生还书管理表单页面
	 */
	@RequiresPermissions(value={"bookstudentback:bookStudentBack:view","bookstudentback:bookStudentBack:add","bookstudentback:bookStudentBack:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookStudentBack bookStudentBack, Model model) {
		model.addAttribute("bookStudentBack", bookStudentBack);
		return "modules/bookstudentback/bookStudentBackForm";
	}

	/**
	 * 学生还书：
	 * 	图书的状态
	 * 	历史表更新还书时间
	 * 	判断父表是否可删，如果detail子表少于一条，则删，否则不能删
	 * 	借书子表删除
	 * @param bookStudentDetail
	 * @return
	 */
	@RequiresPermissions(value={"bookstudentback:bookStudentBack:add","bookstudentback:bookStudentBack:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookStudentBack bookStudentBack, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		
		String msg = bookStudentBackService.bookBack(bookStudentBack);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
	}
	
	/**
	 * 删除学生还书管理
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:del")
	@RequestMapping(value = "delete")
	public String delete(BookStudentBack bookStudentBack, RedirectAttributes redirectAttributes) {
		bookStudentBackService.delete(bookStudentBack);
		addMessage(redirectAttributes, "删除学生还书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
	}
	
	/**
	 * 批量删除学生还书管理
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookStudentBackService.delete(bookStudentBackService.get(id));
		}
		addMessage(redirectAttributes, "删除学生还书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookStudentBack bookStudentBack, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生还书管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BookStudentBack> page = bookStudentBackService.findPage(new Page<BookStudentBack>(request, response, -1), bookStudentBack);
    		new ExportExcel("学生还书管理", BookStudentBack.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生还书管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BookStudentBack> list = ei.getDataList(BookStudentBack.class);
			for (BookStudentBack bookStudentBack : list){
				try{
					bookStudentBackService.save(bookStudentBack);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生还书管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生还书管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生还书管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
    }
	
	/**
	 * 下载导入学生还书管理数据模板
	 */
	@RequiresPermissions("bookstudentback:bookStudentBack:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生还书管理数据导入模板.xlsx";
    		List<BookStudentBack> list = Lists.newArrayList(); 
    		new ExportExcel("学生还书管理数据", BookStudentBack.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudentback/bookStudentBack/?repage";
    }
	
	
	

}