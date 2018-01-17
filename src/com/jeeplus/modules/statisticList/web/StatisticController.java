package com.jeeplus.modules.statisticList.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;
import com.jeeplus.modules.sys.entity.Dict;
import com.jeeplus.modules.sys.utils.DictUtils;

@Controller
@RequestMapping(value = "${adminPath}/statistic/statistic")
public class StatisticController {
	
	@Autowired
	private BooksService booksService;
	
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
	
	@RequiresPermissions("statistic:statistic:list")
	@RequestMapping(value="getBooks")
	@ResponseBody
	public Map<String, Integer> getBooks(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		Books books = new Books();
		
		
		// 统计所有图书信息
		Integer booksAll = booksService.getAll(books);
		
		// 统计在库
		Integer booksByIn = booksService.getByIn(books);
		
		// 统计借出
		Integer booksByOut = booksService.getByOut(books);
		
		// 统计损坏
		Integer booksByBreak = booksService.getByBreak(books);
		
		// 统计遗失
		Integer booksByLoss = booksService.getByLoss(books);
		
		map.put("booksAll", booksAll);
		map.put("booksByIn", booksByIn);
		map.put("booksByOut", booksByOut);
		map.put("booksByBreak", booksByBreak);
		map.put("booksByLoss", booksByLoss);
		
		return map;
	}
	
	@RequiresPermissions("statistic:statistic:list")
	@RequestMapping(value = {"list",""})
	public String list(Books books, Model model){
		
		
		return "modules/statisticList/agencyStatisticList";
	}

}
