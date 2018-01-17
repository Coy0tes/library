package com.jeeplus.modules.bookteachers.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudent.entity.BookTeachersPojo;
import com.jeeplus.modules.bookstudent.service.BookStudentService;
import com.jeeplus.modules.bookteachers.service.BookTeachersService;
import com.jeeplus.modules.canshu.entity.Canshu;
import com.jeeplus.modules.canshu.service.CanshuService;
import com.jeeplus.modules.students.entity.Students;
import com.jeeplus.modules.students.service.StudentsService;
import com.jeeplus.modules.teachers.entity.Teachers;
import com.jeeplus.modules.teachers.service.TeachersService;

@Controller
@RequestMapping(value = "${adminPath}/bookteachers/bookteachers")
public class BookTeachersController extends BaseController{
	
	/**
	 * 数据库中表为bookstudent，实体类也是引用bookstudent
	 * 
	 */
	
	@Autowired
	private BookStudentService bookStudentService;
	@Autowired
	private CanshuService canshuService;
	@Autowired
	private StudentsService studentsService;
	@Autowired
	private TeachersService teachersService;
	@Autowired
	private BooksService booksService;
	@Autowired
	private BookTeachersService bookTeachersService;
	
	@ModelAttribute
	public BookStudent getByTeachers(@RequestParam(required=false) String id) {
		BookStudent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookStudentService.getByTeachers(id);
		}
		if (entity == null){
			entity = new BookStudent();
		}
		return entity;
	}
	
	/**
	 * 查询是否有该教师
	 * @param bookStudent
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getByTeacher")
	@ResponseBody
	public Map<String, Object> getByTeacher(BookStudent bookStudent, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String gh = request.getParameter("studentNum");
		Teachers teachers = new Teachers();
		
		// 根据工号查询教师信息
		teachers.setBh(gh);
		teachers = teachersService.getByBh(teachers);
		
		// 判断借书表有没有该教师信息
		if(teachers != null){
			bookStudent.setTeachers(teachers);
			bookStudent = bookStudentService.getTeachersByGh(bookStudent);
			
			// 如果有教师，返回00
			// 00 借书表有教师
			// 99 借书表没有教师
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
	 * 教师借书管理列表页面
	 */
	@RequiresPermissions("bookteachers:bookteachers:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BookStudent> page = bookStudentService.findTeacherPage(new Page<BookStudent>(request, response), bookStudent); 
//		Page<BookStudent> page = bookStudentService.findPage(new Page<BookStudent>(request, response), bookStudent); 
		model.addAttribute("page", page);
		return "modules/bookteachers/bookTeachersList";
	}
	
	@RequiresPermissions("bookstudent:bookStudent:list")
	@RequestMapping(value = "getByBh")
	@ResponseBody
	public Map<Object, Object> getByBh(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String bh = bookStudent.getTeachers().getBh();
		
		// 查询该教师是否借过书
		List<BookStudent> list = bookStudentService.getByBh(bookStudent);
		

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
		// 查询教师基本信息
		Teachers teachers = new Teachers();
		teachers.setBh(bh);
		teachers = teachersService.getByBh(teachers);
		
		// 如果教师没有在借书籍，则不返回在借信息
		if(list.size()>0){
			map.put("bookStudent", list.get(0));	// 教师借书历史
			map.put("teachersBooksShuliang", num);	// 教师借书数量
		}else{
			map.put("bookStudent", "0");	// 教师借书历史
			map.put("teachersBooksShuliang", "0");	// 没有借书时，数量为0
		}
		map.put("canshu", canshu);			// 参数配置信息
		map.put("teachers", teachers);		// 教师信息
		return map;
	}
	
	/**
	 * 查看，增加，编辑教师借书管理表单页面
	 */
	@RequiresPermissions(value={"bookstudent:bookStudent:view","bookstudent:bookStudent:add","bookstudent:bookStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookStudent bookStudent, Model model) {
		// 获取押金等参数信息
		Canshu canshu = new Canshu();
		List<Canshu> list = canshuService.findList(canshu);
		model.addAttribute("canshu", list.get(0));
		model.addAttribute("bookStudent", bookStudent);
		return "modules/bookteachers/bookTeachersForm";
	}

	/**
	 * 保存教师借书管理
	 */
	@RequiresPermissions(value={"bookteachers:bookteachers:add","bookteachers:bookteachers:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookStudent bookStudent, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "教师借书保存成功";
		if (!beanValidator(model, bookStudent)){
			return form(bookStudent, model);
		}
		if(!bookStudent.getIsNewRecord()){//编辑表单保存
			BookStudent t = bookStudentService.get(bookStudent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(bookStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			msg = bookStudentService.saveInfo(t);//保存
		}else{//新增表单保存
			msg = bookStudentService.saveInfo(bookStudent);//保存
		}
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookteachers/bookteachers/?repage";
	}

	/**
	 * 删除教师借书管理
	 */
	@RequiresPermissions("bookstudent:bookStudent:del")
	@RequestMapping(value = "delete")
	public String delete(BookStudent bookStudent, RedirectAttributes redirectAttributes) {
		bookStudentService.delete(bookStudent);
		addMessage(redirectAttributes, "删除教师借书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookteachers/bookteachers/?repage";
	}
	
	/**
	 * 批量删除教师借书管理
	 */
	@RequiresPermissions("bookstudent:bookStudent:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookStudentService.delete(bookStudentService.get(id));
		}
		addMessage(redirectAttributes, "删除教师借书管理成功");
		return "redirect:"+Global.getAdminPath()+"/bookteachers/bookteachers/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bookteachers:bookteachers:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookStudent bookStudent, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教师借书明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            BookTeachersPojo bookTeachersPojo = new BookTeachersPojo();
            // Page<BookStudent> page = bookStudentService.findPage(new Page<BookStudent>(request, response, -1), bookStudent);
//            List<BookTeachersPojo> list = bookTeachersService.bookTeachersExport(bookTeachersPojo);
            List<BookTeachersPojo> list = bookTeachersService.bookTeachersExport(bookTeachersPojo);
    		new ExportExcel("教师借书明细", BookTeachersPojo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出教师借书记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookteachers/bookteachers/?repage";
    }

}
