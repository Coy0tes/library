package com.jeeplus.common.utils.excel.fieldtype;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.service.BooksService;

public class BooksType {

	@Autowired
	private static BooksService booksService = SpringContextHolder.getBean(BooksService.class);
	
	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Books)val).getName() != null){
			return ((Books)val).getName();
		}
		return "";
	}
	
}
