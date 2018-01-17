/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.books.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.books.dao.BooksDao;

/**
 * booksService
 * @author mxc
 * @version 2017-11-07
 */
@Service
@Transactional(readOnly = true)
public class BooksService extends CrudService<BooksDao, Books> {

	public Books get(String id) {
		return super.get(id);
	}
	
	public List<Books> findList(Books books) {
		return super.findList(books);
	}
	
	public Page<Books> findPage(Page<Books> page, Books books) {
		return super.findPage(page, books);
	}
	
	@Transactional(readOnly = false)
	public void save(Books books) {
		super.save(books);
	}
	
	@Transactional(readOnly = false)
	public void delete(Books books) {
		super.delete(books);
	}

	@Transactional(readOnly = false)
	public int findRepart(Books books) {
		// TODO Auto-generated method stub
		return dao.findRepart(books);
	}

	@Transactional(readOnly = false)
	public Books getByIsbn(Books books) {
		return dao.getByIsbn(books);
	}

	@Transactional(readOnly = false)
	public void updateByBookStatus(Books books) {
		dao.updateByBookStatus(books);
		
	}

	// 图书统计-所有图书数量
	@Transactional(readOnly = false)
	public Integer getAll(Books books) {
		return dao.getAll(books);
	}
	public Integer getByIn(Books books) {
		return dao.getByIn(books);
	}
	public Integer getByOut(Books books) {
		return dao.getByOut(books);
	}
	public Integer getByBreak(Books books) {
		return dao.getByBreak(books);
	}
	public Integer getByLoss(Books books) {
		return dao.getByLoss(books);
	}

	@Transactional(readOnly = false)
	public String updateSsh(String isbnlist,String ssh) {
		String rnt="00";
		String[] arr = isbnlist.split(",");
		for(String isbn : arr){
			Books bs = new Books();
			bs.setIsbn(isbn);
			bs.setSsh(ssh);
			dao.updateSsh(bs);
		}
		
		return rnt;
	}

	@Transactional(readOnly = false)
	public String addSsh(String ids, Books books) {
		String msg = "批量添加成！";
		try {
			String list[] = ids.split(",");
			for(String id:list){
				// 根据id获取书本信息
				books.setId(id);
				// 赋值索书号
				dao.updateBySsh(books);
			}
		} catch (Exception e) {
			msg = "批量添加失败！";
		}
		
		return msg;
	}

	@Transactional(readOnly = false)
	public String bookByOut(Books books) {
		String msg = "保存学生借书管理成功";
		dao.bookByOut(books);
		
		return msg;
	}

	@Transactional(readOnly = false)
	public List<Books> getByIsbnList(Books books) {
		return dao.getByIsbnList(books);
	}

	//图书入库
	@Transactional(readOnly = false)
	public String saveInfo(Books books) {
		String msg = "保存图书管理成功";
		String isbn = books.getIsbn();
		if(StringUtils.isNotEmpty(isbn)){
			Books book = dao.getByIsbn(books);
			if(book==null){
				super.save(books);
			}else{
				dao.addNumin(books);
			}
		}
		return msg;
	}
	
	
}