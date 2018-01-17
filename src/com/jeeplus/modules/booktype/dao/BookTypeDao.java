/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.booktype.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.booktype.entity.BookType;

/**
 * 图书分类管理DAO接口
 * @author mxc
 * @version 2017-11-06
 */
@MyBatisDao
public interface BookTypeDao extends CrudDao<BookType> {

	List<BookType> findParentId(BookType bookType);

	
}