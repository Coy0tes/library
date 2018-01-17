package com.jeeplus.common.utils.excel.fieldtype;

import com.jeeplus.modules.bookstudent.entity.BookStudent;

public class BookStudentType {

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((BookStudent)val).getStudent().getName() != null){
			return ((BookStudent)val).getStudent().getName();
		}
		return "";
	}
}
