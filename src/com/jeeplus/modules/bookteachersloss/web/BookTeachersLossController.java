package com.jeeplus.modules.bookteachersloss.web;

import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bookloss.service.BookLossService;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistoryPojo;
import com.jeeplus.modules.bookstudenthistory.service.BookStudentHistoryService;
import com.jeeplus.modules.bookteachersloss.service.BookTeachersLossService;

@RequestMapping(value="${adminPath}/bookteachersloss/bookteachersloss")
@Controller
public class BookTeachersLossController extends BaseController{
	
	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BooksService booksService;
	@Autowired
	private BookLossService bookLossService;
	@Autowired
	private BookStudentDao bookStudentDao;
	@Autowired
	private BookStudentHistoryService bookStudentHistoryService;
	@Autowired
	private BookTeachersLossService bookTeachersLossService;
	
	@ModelAttribute
	public BookStudentDetail get(@RequestParam(required=false) String id) {
		BookStudentDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookStudentDetailDao.get(id);
		}
		if (entity == null){
			entity = new BookStudentDetail();
		}
		return entity;
	}
	
	/**
	 * 遗失页面
	 * 加载所有book_student_detail表中的在借书籍，只有在借书籍可以做遗失处理
	 * @param bookStudentDetail
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value="bookteachersloss:bookteachersloss:list")
	@RequestMapping(value={"list", ""})
	public String list(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response){
//		List<BookStudentDetail> list = bookStudentDetailDao.findListByStudent(bookStudentDetail);
		Page<BookStudentDetail> page = new Page<BookStudentDetail>(request, response);
		bookStudentDetail.setPage(page);
		page = page.setList(bookStudentDetailDao.findListByTeacher(bookStudentDetail));
		model.addAttribute("page", page);
		return "modules/bookteachersloss/bookTeachersLossList";
	}
	
	/**
	 * 查看、编辑页面
	 * @param bookStudentDetail
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"bookteachersloss:bookteachersloss:add", "bookteachersloss:bookteachersloss:view", "bookteachersloss:bookteachersloss:edit"},logical=Logical.OR)
	@RequestMapping(value="form")
	public String form(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response){
//		bookStudentDetail.setStudentname(bookStudentDetail.getMainid().getStudent().getName());
//		bookStudentDetail.setStudentnum(bookStudentDetail.getMainid().getStudent().getXh());
		model.addAttribute("bookStudentDetail", bookStudentDetail);
		return "modules/bookteachersloss/bookTeachersLossForm";
	}
	
	
	/**
	 * 遗失处理
	 * 	图书状态更新为遗失
	 * 	历史借阅表更新，reamrks字段加：确认遗失，遗失时间为：xxxx
	 * @param bookStudentDetail
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"bookteachersloss:bookteachersloss:add", "bookteachersloss:bookteachersloss:view", "bookteachersloss:bookteachersloss:edit"},logical=Logical.OR)
	@RequestMapping(value="loss")
	public String loss(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes){
		
		String msg = bookLossService.loss(bookStudentDetail);
		
//		model.addAttribute("bookStudentDetail", bookStudentDetail);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookteachersloss/bookteachersloss/?repage";
	}
	
	
	/**
	 * 图书
	 */
	@RequiresPermissions(value={"bookteachersloss:bookteachersloss:add","bookteachersloss:bookteachersloss:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Books books, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "";
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
	 * 赔偿处理
	 * @param bookStudentDetail
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"bookteachersloss:bookteachersloss:add", "bookteachersloss:bookteachersloss:view", "bookteachersloss:bookteachersloss:edit"},logical=Logical.OR)
	@RequestMapping(value="payBook")
	public String payBook(BookStudentDetail bookStudentDetail, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){
		
		String msg = "";
		
		msg = bookTeachersLossService.payBook(bookStudentDetail);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookteachersloss/bookteachersloss/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bookstudenthistory:bookStudentHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookStudentHistory bookStudentHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生借阅历史记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<BookStudentHistory> page = bookStudentHistoryService.findPage(new Page<BookStudentHistory>(request, response, -1), bookStudentHistory);
            
            List<BookStudentHistoryPojo> list = bookStudentHistoryService.studentHistoryExport();
            
    		new ExportExcel("学生借阅历史记录", BookStudentHistoryPojo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生借阅历史记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bookstudenthistory/bookStudentHistory/?repage";
    }
}
