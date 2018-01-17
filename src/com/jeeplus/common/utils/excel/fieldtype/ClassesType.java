package com.jeeplus.common.utils.excel.fieldtype;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.classes.entity.Classes;
import com.jeeplus.modules.classes.service.ClassesService;

public class ClassesType {
	@Autowired
	private static ClassesService classesService = SpringContextHolder.getBean(ClassesService.class);

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		Classes classes = new Classes();
		List<Classes> list = classesService.findList(classes);
		for (Classes e : list){
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
