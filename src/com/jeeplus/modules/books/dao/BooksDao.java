/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.books.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.books.entity.Books;

/**
 * booksDAO接口
 * @author mxc
 * @version 2017-11-07
 */
@MyBatisDao
public interface BooksDao extends CrudDao<Books> {

	int findRepart(Books books);

	Books getByIsbn(Books books);

	void updateByBookStatus(Books books);

	// 图书统计管理
	Integer getAll(Books books);
	Integer getByIn(Books books);
	Integer getByOut(Books books);
	Integer getByLoss(Books books);
	Integer getByBreak(Books books);

	void updateSsh(Books bs);

	void updateBySsh(Books books);

	void bookByOut(Books books);

	void bookByIn(Books books);

	List<Books> getByIsbnList(Books books);

	void bookByLoss(Books books);

	void addNumin(Books books);
}