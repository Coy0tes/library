package com.jeeplus.modules.teacherslossstatistic.web;

import java.math.BigDecimal;
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
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;
import com.jeeplus.modules.bookstudenthistory.entity.BookTeachersHistoryPojo;
import com.jeeplus.modules.bookstudenthistory.service.BookStudentHistoryService;

@Controller
@RequestMapping(value = "${adminPath}/teacherslossstatistic/teacherslossstatistic")
public class TeachersLossStatisticController extends BaseController{
	
	@Autowired
	private BookStudentHistoryService bookStudentHistoryService;
	
	@ModelAttribute
	public BookStudentHistory get(@RequestParam(required=false) String id) {
		BookStudentHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookStudentHistoryService.get(id);
		}
		if (entity == null){
			entity = new BookStudentHistory();
		}
		return entity;
	}
	
	@RequiresPermissions("teacherslossstatistic:teacherslossstatistic:list")
	@RequestMapping(value={"list",""})
	public String list(BookStudentHistory bookStudentHistory, HttpServletRequest request, HttpServletResponse response, Model model){
		Page<BookStudentHistory> page = new Page<BookStudentHistory>(request, response);
		
		bookStudentHistory.setPage(page);
		page = page.setList(bookStudentHistoryService.teachersLossStatisticList(bookStudentHistory));
		
		// 重新获取用于计算遗失总价格
		BookStudentHistory e = new BookStudentHistory();
		List<BookStudentHistory> list = bookStudentHistoryService.teachersLossStatisticList(e);
		
		BigDecimal money = new BigDecimal("0");
		for(BookStudentHistory l : list){
			BigDecimal d = new  BigDecimal(l.getPcje());
			money = money.add(d);
		}
		
		model.addAttribute("sum", money);
		model.addAttribute("page", page);
		return "modules/teacherslossstatistic/teachersLossStatisticList";
	}
	
	/**
	 * 查看，增加，编辑教师借阅历史记录表单页面
	 */
	@RequiresPermissions(value={"teacherslossstatistic:teacherslossstatistic:view","teacherslossstatistic:teacherslossstatistic:add","teacherslossstatistic:teacherslossstatistic:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookStudentHistory bookStudentHistory, Model model) {
		model.addAttribute("bookStudentHistory", bookStudentHistory);
		return "modules/teacherslossstatistic/teachersLossStatisticForm";
	}

	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teacherslossstatistic:teacherslossstatistic:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookStudentHistory bookStudentHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教师遗失记录统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<BookStudentHistory> page = bookStudentHistoryService.findPage(new Page<BookStudentHistory>(request, response, -1), bookStudentHistory);
            
            List<BookTeachersHistoryPojo> list = bookStudentHistoryService.teachersLossStatisticExport();
            
    		new ExportExcel("教师遗失记录统计", BookTeachersHistoryPojo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出教师遗失历史记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teacherslossstatistic/teachersLossStatisticList/?repage";
    }

}
