package com.jeeplus.modules.bookloss.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bookloss.service.BookLossService;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudent.service.BookStudentService;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;
import com.jeeplus.modules.bookstudenthistory.service.BookStudentHistoryService;
import com.jeeplus.modules.canshu.service.CanshuService;
import com.jeeplus.modules.students.service.StudentsService;

@RequestMapping(value="${adminPath}/bookloss/bookloss")
@Controller
public class BookLossController extends BaseController{
	
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
	@RequiresPermissions(value="bookloss:bookloss:list")
	@RequestMapping(value={"list", ""})
	public String list(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response){
//		List<BookStudentDetail> list = bookStudentDetailDao.findListByStudent(bookStudentDetail);
		Page<BookStudentDetail> page = new Page<BookStudentDetail>(request, response);
		bookStudentDetail.setPage(page);
		page = page.setList(bookStudentDetailDao.findListByStudent(bookStudentDetail));
		model.addAttribute("page", page);
		return "modules/bookloss/bookLossList";
	}
	
	/**
	 * 查看、编辑页面
	 * @param bookStudentDetail
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"bookloss:bookloss:add", "bookloss:bookloss:view", "bookloss:bookloss:edit"},logical=Logical.OR)
	@RequestMapping(value="form")
	public String form(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response){
//		bookStudentDetail.setStudentname(bookStudentDetail.getMainid().getStudent().getName());
//		bookStudentDetail.setStudentnum(bookStudentDetail.getMainid().getStudent().getXh());
		model.addAttribute("bookStudentDetail", bookStudentDetail);
		return "modules/bookloss/bookLossForm";
	}
	
	/**
	 * 赔偿遗失处理
	 * @param bookStudentDetail
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"bookloss:bookloss:add", "bookloss:bookloss:view", "bookloss:bookloss:edit"},logical=Logical.OR)
	@RequestMapping(value="payBook")
	public String payBook(BookStudentDetail bookStudentDetail, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){
		
		String msg = "";
		
		msg = bookLossService.bookLoss(bookStudentDetail);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookloss/bookloss/?repage";
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
	@RequiresPermissions(value={"bookloss:bookloss:add", "bookloss:bookloss:view", "bookloss:bookloss:edit"},logical=Logical.OR)
	@RequestMapping(value="loss")
	public String loss(BookStudentDetail bookStudentDetail, Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes){

		String msg = bookLossService.loss(bookStudentDetail);
		
		model.addAttribute("bookStudentDetail", bookStudentDetail);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/bookloss/bookloss/?repage";
	}
	
	
	/**
	 * 图书赔偿
	 */
	@RequiresPermissions(value={"bookloss:bookloss:add","bookloss:bookloss:edit"},logical=Logical.OR)
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
	
	
}
