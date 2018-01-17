package com.jeeplus.modules.bookteachersloss.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.books.dao.BooksDao;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.bookstudent.dao.BookStudentDao;
import com.jeeplus.modules.bookstudent.dao.BookStudentDetailDao;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.modules.bookstudent.entity.BookStudentDetail;
import com.jeeplus.modules.bookstudenthistory.dao.BookStudentHistoryDao;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;

@Transactional(readOnly = true)
@Service
public class BookTeachersLossService {

	@Autowired
	private BookStudentDao bookStudentDao;
	@Autowired
	private BookStudentHistoryDao bookStudentHistoryDao;
	@Autowired
	private BookStudentDetailDao bookStudentDetailDao;
	@Autowired
	private BooksDao booksDao;
	
	/**
	 * 赔偿
	 * @param bookStudentDetail
	 * @return
	 */
	@Transactional(readOnly = false)
	public String payBook(BookStudentDetail bookStudentDetail) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String msg = "操作成功！";
		
		String mainid = bookStudentDetail.getMainid().getId();
		
		// 更新历史表remarks字段，原有的基础上，加上“已赔偿”
		BookStudentHistory bookStudentHistory = new BookStudentHistory();
		bookStudentHistory.setId(bookStudentDetail.getHistoryid());
		bookStudentHistory = bookStudentHistoryDao.get(bookStudentHistory);
		
		// 如果书本遗失，不点“图书遗失”按钮，直接点“赔偿图书” 处理
		if(StringUtils.isEmpty(bookStudentHistory.getRemarks())){
			
			Books books = new Books();
			// 更新图书数量，在库 -1 ，遗失 +1
			books.setId(bookStudentDetail.getBookid().getId());
			booksDao.bookByLoss(books);
			bookStudentDetail.setRemarks("1");
			bookStudentDetailDao.updateRemarks(bookStudentDetail);
			
			bookStudentHistory.setRemarks("已遗失,时间:"+sdf.format(new Date())+" 已赔偿");
		}else{
			bookStudentHistory.setRemarks(bookStudentHistory.getRemarks()+" "+"已赔偿");
		}
		// 添加赔偿时间、赔偿金额
		bookStudentHistory.setPcsj(sdf.format(new Date()));
		bookStudentHistory.setPcje(bookStudentDetail.getPcje());
		bookStudentHistoryDao.update (bookStudentHistory);
		
		
		// 赔偿后删除借书子表信息
		bookStudentDetailDao.delete(bookStudentDetail);
		BookStudentDetail bsd = new BookStudentDetail();
		bsd.setMainid(new BookStudent(mainid));
		
		// 根据mainid获取借书主表信息，如果返回0，则说明可以删
		Integer num = bookStudentDetailDao.getByMainid(bsd);
		
		if(num==0){
			BookStudent bookStudent = new BookStudent();
			bookStudent.setId(mainid);	// 将子表的mainid附给父表的id
			bookStudentDao.delete(bookStudent);
		}
		
		return msg;
	}
	
	

}
