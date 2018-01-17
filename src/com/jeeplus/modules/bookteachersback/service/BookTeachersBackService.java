package com.jeeplus.modules.bookteachersback.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.books.dao.BooksDao;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudentback.dao.BookStudentBackDao;
import com.jeeplus.modules.bookstudentback.entity.BookStudentBack;
import com.jeeplus.modules.bookstudenthistory.dao.BookStudentHistoryDao;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;

@Service
@Transactional(readOnly = true)
public class BookTeachersBackService extends CrudService<BookStudentBackDao, BookStudentBack>{

	@Autowired
	private BooksDao booksDao;
	@Autowired
	private BookStudentHistoryDao bookStudentHistoryDao;
	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BookStudentDao bookStudentDao;
	
	// 教师还书
		@Transactional(readOnly = false)
		public String bookTeachersBack(BookStudentBack bookStudentBack) {
			String msgs = "还书成功！";
			
			
			// 根据isbn获取在借书本的信息
			BookStudentDetail bookStudentDetail  = new BookStudentDetail();
			bookStudentDetail.setId(bookStudentBack.getId());
			bookStudentDetail = bookStudentDetailDao.get(bookStudentDetail);
			
			Books books = new Books();
			
			// 图书在借数量跟借出数量更新
			books.setId(bookStudentDetail.getBookid().getId());
			booksDao.bookByIn(books);
				
				//更新历史表还书信息 归还时间ghsj
				BookStudentHistory bookStudentHistory = new BookStudentHistory();
				bookStudentHistory.setId(bookStudentDetail.getHistoryid());		// 历史表id
				bookStudentHistory.setIsbn(bookStudentBack.getIsbn());			// 历史表isbn
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		// 获取当前时间，格式：yyyy-MM-dd
				bookStudentHistory.setGhsj(sdf.format(new Date()).toString());	// 当前时间赋值
				bookStudentHistory.setCqts(bookStudentBack.getCqts());
				bookStudentHistoryDao.updataHistory(bookStudentHistory);
				
				String mainid = bookStudentDetail.getMainid().getId();
				
				// 借书子表删除
				bookStudentDetail.setId(bookStudentBack.getId());
				bookStudentDetailDao.delete(bookStudentDetail);
				
				BookStudentDetail bsd = new BookStudentDetail();
				bsd.setMainid(new BookStudent(mainid));
				
				// 判断父表是否可删除
				Integer detailNum = bookStudentDetailDao.getByMainid(bsd);
				// 如果detail表数量少于1，则可以删除父表
				if(detailNum==0){
					BookStudent bookStudent = new BookStudent();
					bookStudent.setId(mainid);	// 将子表的mainid附给父表的id
					bookStudentDao.delete(bookStudent);
				}
				
				
			return msgs;
		}
}
