/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.books.dao.BooksDao;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudent.entity.BookStudentPojo;
import com.jeeplus.modules.bookstudenthistory.dao.BookStudentHistoryDao;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;

/**
 * bookstudentService
 * @author mxc
 * @version 2017-11-08
 */
@Service
@Transactional(readOnly = true)
public class BookStudentService extends CrudService<BookStudentDao, BookStudent> {

	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BooksDao booksDao;
	@Autowired
	private BookStudentHistoryDao bookStudentHistoryDao;
	
	public BookStudent get(String id) {
		BookStudent bookStudent = super.get(id);
		bookStudent.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(bookStudent)));
		return bookStudent;
	}
	
	public BookStudent getByTeachers(String id) {
		BookStudent bookStudent = dao.getByTeachers(id);
		bookStudent.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(bookStudent)));
		return bookStudent;
	}
	
	public List<BookStudent> findList(BookStudent bookStudent) {
		return super.findList(bookStudent);
	}
	
	public Page<BookStudent> findPage(Page<BookStudent> page, BookStudent bookStudent) {
		return super.findPage(page, bookStudent);
	}
	
	public Page<BookStudent> findTeacherPage(Page<BookStudent> page, BookStudent bookStudent) {
		bookStudent.setPage(page);
		return page.setList(dao.findTeachersList(bookStudent));
	}
	
	
	@Transactional(readOnly = false)
	public void delete(BookStudent bookStudent) {
		super.delete(bookStudent);
		Books books = new Books();
		for(BookStudentDetail book : bookStudent.getBookStudentDetailList()){
			books.setId(book.getBookid().getId());
			booksDao.bookByIn(books);
		}
		
		bookStudentDetailDao.delete(new BookStudentDetail(bookStudent));
	}

	@Transactional(readOnly = false)
	public BookStudent getXh(BookStudent bookStudent) {
		bookStudent = dao.getXh(bookStudent);
		// 如果没有，则该生没借过书，重新获取学生信息
		if(bookStudent!=null){
			bookStudent.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(bookStudent)));
		}
		
		return bookStudent;
	}


	// 学生信息查询
	@Transactional(readOnly = false)
	public List<BookStudent> getByXh(BookStudent bookStudent) {
		List<BookStudent> list = dao.getByXh(bookStudent);
		// 如果没有，则该生没借过书，重新获取学生信息
		for(BookStudent l:list){
			if(bookStudent!=null){
				l.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(l)));
			}
		}
		return list;
	}
	
	// 教师信息查询
	@Transactional(readOnly = false)
	public List<BookStudent> getByBh(BookStudent bookStudent) {
		List<BookStudent> list = dao.getByBh(bookStudent);
		// 如果没有，则该教师没借过书，重新获取学生信息
		for(BookStudent l:list){
			if(bookStudent!=null){
				l.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(l)));
			}
		}
		return list;
	}

	// 查询学生借书信息
	@Transactional(readOnly = false)
	public List<BookStudent> findListByStudent(BookStudent bookStudent) {
		List<BookStudent> list = dao.findList(bookStudent);
		for(BookStudent l:list){
			l.setBookStudentDetailList(bookStudentDetailDao.findList(new BookStudentDetail(l)));
		}
		return list;
	}

	// 学生借书明细
	@Transactional(readOnly = false)
	public List<BookStudentPojo> bookStudentExport(BookStudentPojo bookStudentPojo) {
		return dao.bookStudentExport();
	}

	@Transactional(readOnly = false)
	public BookStudent getStudentsById(BookStudent bookStudent) {
		return dao.getStudentsById(bookStudent);
	}

	@Transactional(readOnly = false)
	public BookStudent getTeachersByGh(BookStudent bookStudent) {
		return dao.getTeachersByGh(bookStudent);
	}

	/**
	 * 借书操作：
	 * 	detail表里新加数据，同时给history表里加数据，更新书（books）状态：借出
	 * @return 
	 */
	@Transactional(readOnly = false)
	public String saveInfo(BookStudent bookStudent) {
		String msg = "借书成功";
		Books books = new Books();
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		//借书数量和库存数量比较
		for (BookStudentDetail bookStudentDetail : bookStudent.getBookStudentDetailList()) {
			if (bookStudentDetail.getId() == null) {
				continue;
			}
			if (BookStudentDetail.DEL_FLAG_NORMAL.equals(bookStudentDetail.getDelFlag())) {
				if (StringUtils.isBlank(bookStudentDetail.getId())) {
					//获取isbn号对应的数量,存入map
					String isbn = bookStudentDetail.getIsbn();
					if(StringUtils.isNotEmpty(isbn)){
						Integer sl = map.get(isbn);
						if(sl==null){
							map.put(isbn, 1);
						}else{
							map.put(isbn, sl+1);
						}
					}
				} 
			}
		}
		
	    //遍历Map,查验在库数是否满足
		boolean isok = true;
		for (String isbn : map.keySet()) {
		    Integer sl = map.get(isbn);
		    books.setIsbn(isbn);
		    Books eachbook = booksDao.getByIsbn(books);
		    Integer zksl = Integer.parseInt(eachbook.getNumIn());
		    if(sl>zksl){
		    	isok = false;
		    	msg = "ISBN号["+isbn+"]的图书不足，库存数是"+zksl+"，借阅数量是"+sl;
		    	break;
		    }
		}
		
		if(isok){
			super.save(bookStudent);
			for (BookStudentDetail bookStudentDetail : bookStudent.getBookStudentDetailList()){
				
				if (bookStudentDetail.getId() == null){
					continue;
				}
				if (BookStudentDetail.DEL_FLAG_NORMAL.equals(bookStudentDetail.getDelFlag())){
					if (StringUtils.isBlank(bookStudentDetail.getId())){
						// 通过判断有没有图书的id来判断是不是空行
						if(!StringUtils.isEmpty(bookStudentDetail.getBookid().getId())){
							
							String id = bookStudentDetail.getBookid().getId();
							
							// 图书数量处理
							books.setId(id);
							booksDao.bookByOut(books);
							
							// 应还时间处理
							// 应还时间 默认一个月，在当前时间上增加
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String str = sdf.format(new Date());
							Date dt = new Date();
							try {
								dt=sdf.parse(str);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							Calendar calender = Calendar.getInstance();
							calender.setTime(dt);
							calender.add(Calendar.MONTH,1);	// 日期加一个月
							Date yhrqdate = calender.getTime();
							String yhrq = sdf.format(yhrqdate);
							
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String time = df.format(new Date());
							bookStudentDetail.setJcsj(time);
							bookStudentDetail.setMainid(bookStudent);
							bookStudentDetail.preInsert();
							bookStudentDetail.setYhrq(yhrq);		// 应还日期默认增加一个月
							bookStudentDetailDao.insert(bookStudentDetail);
							
							// 历史借阅表插入借阅记录
							try {
								BookStudentHistory history = new BookStudentHistory();
								
								// 历史表-图书信息插入
								history.preInsert();
								history.setMainid(bookStudent);
								history.setBookid(bookStudentDetail.getBookid());
								history.setIsbn(bookStudentDetail.getIsbn());
								history.setSsh(bookStudentDetail.getSsh());
								history.setBooktype(bookStudentDetail.getBooktype());
								history.setName(bookStudentDetail.getName());
								history.setAuthor(bookStudentDetail.getAuthor());
								history.setCbs(bookStudentDetail.getCbs());
								history.setPrice(bookStudentDetail.getPrice());
								history.setJcsj(bookStudentDetail.getJcsj());
								history.setGhsj(bookStudentDetail.getGhsj());
								history.setCqts(bookStudentDetail.getCqts());
								// 历史表-学生信息插入
								if(bookStudent.getStudent()!=null){
									history.setStudentId(bookStudent.getStudent().getId());
									history.setStudentName(bookStudent.getStudent().getName());
									history.setStudentXh(bookStudent.getStudent().getXh());
									history.setClassesId(bookStudent.getStudent().getClassid().getId());
									history.setClassesName(bookStudent.getStudent().getClassid().getName());
								}
								
								// 历史表-教师信息插入
								if(bookStudent.getTeachers()!=null){
									history.setTeachersId(bookStudent.getTeachers().getId());
									history.setTeachersName(bookStudent.getTeachers().getName());
									history.setTeachersBh(bookStudent.getTeachers().getBh());
								}
								
								// 应还时间处理，默认一个月
								history.setYhrq(yhrq);			// 应还日期增加一个月
								
								// 只更新历史记录表的id，插入到子表中，用于： 还书更新归还日期的时候根据此historyid寻找当前在借书本的历史记录
								bookStudentDetail.setHistoryid(history.getId());
								bookStudentDetailDao.updateHistoryid(bookStudentDetail); 
								
								bookStudentHistoryDao.insert(history);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}else{
						bookStudentDetail.preUpdate();
						bookStudentDetailDao.update(bookStudentDetail);
					}
				}else{
					bookStudentDetailDao.delete(bookStudentDetail);
				}
			}
		}
		
		return msg;
	}

	
}