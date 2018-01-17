/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.booktype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.booktype.entity.BookType;
import com.jeeplus.modules.booktype.dao.BookTypeDao;

/**
 * 图书分类管理Service
 * @author mxc
 * @version 2017-11-06
 */
@Service
@Transactional(readOnly = true)
public class BookTypeService extends CrudService<BookTypeDao, BookType> {

	public BookType get(String id) {
		return super.get(id);
	}
	
	public List<BookType> findList(BookType bookType) {
		return super.findList(bookType);
	}
	
	public Page<BookType> findPage(Page<BookType> page, BookType bookType) {
		return super.findPage(page, bookType);
	}
	
	@Transactional(readOnly = false)
	public void save(BookType bookType) {
		super.save(bookType);
	}
	
	@Transactional(readOnly = false)
	public void delete(BookType bookType) {
		super.delete(bookType);
	}

	@Transactional(readOnly = false)
	public List<BookType> findParentsId(BookType bookType) {
		return dao.findParentId(bookType);
	}
	
	
	
	
}