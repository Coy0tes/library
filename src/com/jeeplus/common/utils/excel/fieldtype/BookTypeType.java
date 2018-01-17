package com.jeeplus.common.utils.excel.fieldtype;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.booktype.entity.BookType;
import com.jeeplus.modules.booktype.service.BookTypeService;
import com.jeeplus.modules.classes.entity.Classes;

public class BookTypeType {
	@Autowired
	private static BookTypeService bookTypeService = SpringContextHolder.getBean(BookTypeService.class);

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		BookType bookType = new BookType();
		List<BookType> list = bookTypeService.findList(bookType);
		for (BookType e : list){
			if (StringUtils.trimToEmpty(val).equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Classes)val).getName() != null){
			return ((Classes)val).getName();
		}
		return "";
	}
}
