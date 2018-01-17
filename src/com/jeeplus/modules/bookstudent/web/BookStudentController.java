/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.web;

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
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudent.entity.BookStudentPojo;
import com.jeeplus.modules.bookstudent.service.BookStudentService;
import com.jeeplus.modules.canshu.entity.Canshu;
import com.jeeplus.modules.canshu.service.CanshuService;
import com.jeeplus.modules.students.entity.Students;
import com.jeeplus.modules.students.service.StudentsService;

/**
 * bookstudentController
 * @author mxc
 * @version 2017-11-08
 */
@Controller
@RequestMapping(value = "${adminPath}/bookstudent/bookStudent")
public class BookStudentController extends BaseController {

	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BookStudentService bookStudentService;
	@Autowired
	private StudentsService studentsService;
	@Autowired
	private BooksService booksService;
	@Autowired
	private CanshuService canshuService;
	
	@ModelAttribute
	public BookStudent get(@RequestParam(required=false) String id) {
		BookStudent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookStudentService.get(id);
		}
		if (entity == null){
			entity = new BookStudent();
		}
		return entity;
	}
	
	/**
	 * 查询是否有该学生
	 * @param bookStudent
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getByStudent")
	@ResponseBody
	public Map<String, Object> getByStudent(BookStudent bookStudent, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String studentNum = request.getParameter("studentNum");
		Students students = new Students();
		
		// 根据学号查询学生信息
		students.setXh(studentNum);
		students = studentsService.getByXh(students);
		
		// 判断借书表有没有该生信息
		if(students != null){
			bookStudent.setStudent(students);
			bookStudent = bookStudentService.getStudentsById(bookStudent);
			
			// 如果有学生，返回00
			// 00 借书表有学生
			// 99 借书表没有学生
			// 11 该生不存在
			if(bookStudent != null){
				map.put("code", "00");
			}else{
				map.put("code", "99");
			}
		}else{
			map.put("code", "11");
		}
		return map;
	}
	
	/**
	 * 学生借书
	 * 根据学号查询学生以及在借书籍信息
	 * 
	 * java事件：
	 * 	1、该生在借书籍、在借书籍数量
	 * 	2、基本参数信息（超期计费（天/元）、最大借书数量、借书押金）
	 *  3、学生基本信息
	 * 返回值：
	 * 	学生当前借书信息为空：
	 * 		map.put("bookStudent", "0");	// 学生借书历史
			map.put("studentBooksShuliang", "0");	// 没有借书时，数量为0
		学生当前借书信息不为空：
			map.put("bookStudent", list.get(0));	// 学生借书历史
			map.put("studentBooksShuliang", num);	// 学生借书数量
		map.put("canshu", canshu);			// 参数配置信息
		map.put("students", students);		// 学生信息
	 * 	
	 * 	
	 */
	@RequiresPermissions("bookstudent:bookStudent:list")
	@RequestMapping(value = "getXh")
	@ResponseBody
	public Map<Object, Object> getXh(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String xu = bookStudent.getStudent().getXh();
		
		// 查询该学生是否借过书
		List<BookStudent> list = bookStudentService.getByXh(bookStudent);
		

		// 如果有借书信息，获取该生的借书数量
		int num = 0;
		if(list.size()>0){
			// 查询每次借书借过多少本，然后相加
			for(BookStudent l:list){
				num = num + l.getBookStudentDetailList().size();
			}
			System.out.println(num);
		}
		
		// 查询参数配置，最多借阅多少本
		Canshu canshu = new Canshu();
		canshu = canshuService.getList(canshu);
		// 查询学生基本信息
		Students students = new Students();
		students.setXh(xu);
		students = studentsService.getByXh(students);
		
		// 如果学生没有在借书籍，则不返回在借信息
		if(list.size()>0){
			map.put("bookStudent", list.get(0));	// 学生借书历史
			map.put("studentBooksShuliang", num);	// 学生借书数量
		}else{
			map.put("bookStudent", "0");	// 学生借书历史
			map.put("studentBooksShuliang", "0");	// 没有借书时，数量为0
		}
		map.put("canshu", canshu);			// 参数配置信息
		map.put("students", students);		// 学生信息
		return map;
	}
	
	
	/**
	 * 学生借书管理列表页面
	 */
	@RequiresPermissions("bookstudent:bookStudent:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BookStudent> page = bookStudentService.findPage(new Page<BookStudent>(request, response), bookStudent); 
		model.addAttribute("page", page);
		return "modules/bookstudent/bookStudentList";
	}

	/**
	 * 查看，增加，编辑学生借书管理表单页面
	 */
	@RequiresPermissions(value={"bookstudent:bookStudent:view","bookstudent:bookStudent:add","bookstudent:bookStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookStudent bookStudent, Model model) {
		// 获取押金等参数信息
		Canshu canshu = new Canshu();
		List<Canshu> list = canshuService.findList(canshu);
		model.addAttribute("canshu", list.get(0));
		model.addAttribute("bookStudent", bookStudent);
		return "modules/bookstudent/bookStudentForm";
	}

	/**
	 * 保存学生借书管理
	 */
	@RequiresPermissions(value={"bookstudent:bookStudent:add","bookstudent:bookStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookStudent bookStudent, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception{
		String msg = "";
		Books books = new Books();
		if (!beanValidator(model, bookStudent)){
			return form(bookStudent, model);
		}
		if(!bookStudent.getIsNewRecord()){//编辑表单保存
			BookStudent t = bookStudentService.get(bookStudent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(bookStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			// 更新图书在库数量-1，外借图书数量+1
//			booksService.bookByOut(bookStudent.getBookStudentDetailList().get);
			msg = bookStudentService.saveInfo(t);//保存
		}else{//新增表单保存
			
			boolean b = true;
			
			
			for(BookStudentDetail bookStudentDetail : bookStudent.getBookStudentDetailList()){
				if(!StringUtils.isEmpty(bookStudentDetail.getIsbn())){
					books.setIsbn(bookStudentDetail.getIsbn());
					books = booksService.getByIsbn(books);
					// 判断图书数量，如果该图书数量（num）不为0，则可以借出,图书数量-1，外借图书（numout+1）
					if(books.getNumIn().equals("0")){
						msg = "该图书【" + books.getName() + "】已借完！";
						b = false;
					}
					
				}
			}
			
			if(b){

				msg = bookStudentService.saveInfo(bookStudent);//保存
			}
			
			
		}
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
	}
	
	
	/**
	 * 删除学生借书管理
	 */
	@RequiresPermissions("bookstudent:bookStudent:del")
	@RequestMapping(value = "delete")
	public String delete(BookStudent bookStudent, RedirectAttributes redirectAttributes) {
		bookStudentService.delete(bookStudent);
		addMessage(redirectAttributes, "删除学生借书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
	}
	
	/**
	 * 批量删除学生借书管理
	 */
	@RequiresPermissions("bookstudent:bookStudent:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookStudentService.delete(bookStudentService.get(id));
		}
		addMessage(redirectAttributes, "删除学生借书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bookstudent:bookStudent:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生借书明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            BookStudentPojo bookStudentPojo = new BookStudentPojo();
            // Page<BookStudent> page = bookStudentService.findPage(new Page<BookStudent>(request, response, -1), bookStudent);
            List<BookStudentPojo> list = bookStudentService.bookStudentExport(bookStudentPojo);
            
    		new ExportExcel("学生借书明细", BookStudentPojo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生借书管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bookstudent:bookStudent:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BookStudent> list = ei.getDataList(BookStudent.class);
			for (BookStudent bookStudent : list){
				try{
					bookStudentService.save(bookStudent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生借书管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生借书管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生借书管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
    }
	
	/**
	 * 下载导入学生借书管理数据模板
	 */
	@RequiresPermissions("bookstudent:bookStudent:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生借书管理数据导入模板.xlsx";
    		List<BookStudent> list = Lists.newArrayList(); 
    		new ExportExcel("学生借书管理数据", BookStudent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudent/bookStudent/?repage";
    }
	
	
}