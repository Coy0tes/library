/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudenthistory.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistory;
import com.jeeplus.modules.bookstudenthistory.entity.BookStudentHistoryPojo;
import com.jeeplus.modules.bookstudenthistory.entity.BookTeachersHistoryPojo;

/**
 * bookStudentHistoryDAO接口
 * @author mxc
 * @version 2017-11-11
 */
@MyBatisDao
public interface BookStudentHistoryDao extends CrudDao<BookStudentHistory> {

	void updataHistory(BookStudentHistory bookStudentHistory);

	List<BookStudentHistory> findTeachersList(BookStudentHistory bookStudentHistory);

	void isLoss(BookStudentHistory bookStudentHistory);

	List<BookStudentHistoryPojo> studentHistoryExport();

	List<BookTeachersHistoryPojo> teachersHistoryExport();

	List<BookStudentHistory> studentLossStatistic(BookStudentHistory bookStudentHistory);

	List<BookStudentHistoryPojo> studentLossStatisticExport();

	List<BookStudentHistory> teachersLossStatisticList(BookStudentHistory teachersLossStatisticList);

	List<BookTeachersHistoryPojo> teachersLossStatisticExport();

}