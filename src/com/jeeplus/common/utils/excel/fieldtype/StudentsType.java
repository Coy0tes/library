package com.jeeplus.common.utils.excel.fieldtype;

import com.jeeplus.modules.students.entity.Students;

public class StudentsType {
	
	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Students)val).getName() != null){
			return ((Students)val).getName();
		}
		return "";
	}
}
