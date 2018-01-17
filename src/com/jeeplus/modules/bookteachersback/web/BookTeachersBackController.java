package com.jeeplus.modules.bookteachersback.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudentback.entity.BookStudentBack;
import com.jeeplus.modules.bookstudentback.service.BookStudentBackService;
import com.jeeplus.modules.bookteachersback.service.BookTeachersBackService;

@Controller
@RequestMapping(value="${adminPath}/bookteachersback/bookteachersback")
public class BookTeachersBackController extends BaseController{

	@Autowired
	private BookStudentBackService bookStudentBackService;
	@Autowired
	private BooksService booksService;
	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BookTeachersBackService bookTeachersBackService;
	
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
	 * 教师还书页面：
	 * 	图书信息
	 * 	教师信息
	 * @param bookStudentDetail
	 * @return
	 */
	@RequiresPermissions("bookteachersback:bookteachersback:list")
	@RequestMapping(value = "getByIsbn")
	@ResponseBody
	public Map<Object, Object> getByIsbn(BookStudentDetail bookStudentDetail){
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		// 教师信息
		// 根据ISBN号查出当前教师
		 List<BookStudentDetail> l = bookStudentDetailDao.findTeachersList(bookStudentDetail);
		if(l.size()>0){
			bookStudentDetail = l.get(0);
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
			System.out.println("35131313");
			map.put("bookStudentDetail", bookStudentDetail);
		}else{
			map.put("bookStudentDetail", "00");
		}
		
//		map.put("books", list.get(0));
		return map;
	}
	
	/**
	 * 教师还书管理列表页面
	 */
	@RequiresPermissions("bookteachersback:bookteachersback:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookStudentBack bookStudentBack, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<BookStudentBack> page = bookStudentBackService.findPage(new Page<BookStudentBack>(request, response), bookStudentBack); 
//		model.addAttribute("page", page);
		System.out.println();
		return "modules/bookteachersback/bookTeachersBackForm";
	}

	/**
	 * 查看，增加，编辑教师还书管理表单页面
	 */
	@RequiresPermissions(value={"bookteachersback:bookteachersback:view","bookteachersback:bookteachersback:add","bookteachersback:bookteachersback:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookStudentBack bookStudentBack, Model model) {
		model.addAttribute("bookStudentBack", bookStudentBack);
		return "modules/bookteachersback/bookTeachersBackForm";
	}
	
	/**
	 * 教师还书：
	 * 	图书的状态
	 * 	历史表更新还书时间
	 * 	判断父表是否可删，如果detail子表少于一条，则删，否则不能删
	 * 	借书子表删除
	 * @param bookStudentDetail
	 * @return
	 */
	@RequiresPermissions(value={"bookteachersback:bookteachersback:add","bookteachersback:bookteachersback:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookStudentBack bookStudentBack, Model model,HttpServletRequest request , RedirectAttributes redirectAttributes) throws Exception{
		
		String msgs = bookTeachersBackService.bookTeachersBack(bookStudentBack);

		addMessage(redirectAttributes, msgs);
		return "redirect:"+Global.getAdminPath()+"/bookteachersback/bookteachersback/?repage";
	}
	
}
