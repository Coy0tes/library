/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudent.entity;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.bookstudent.entity.BookStudent;

/**
 * bookstudentEntity
 * @author mxc
 * @version 2017-11-08
 */
public class BookTeachersPojo extends DataEntity<BookStudent> {
	
	private static final long serialVersionUID = 1L;
	private BookStudent mainid;		// 关联ID 父类
	private String teachersname;	// 教师姓名
	private String teacherbh;	// 教师学号
	private Books bookid;		// 书籍ID
	private String isbn;		// ISBN编号
	private String ssh;		// 索书号
	private String booktype;		// 图书分类
	private String name;		// 书名
	private String author;		// 作者
	private String cbs;		// 出版社
	private Double price;		// 价格
	private String jcsj;		// 借出时间
	private String yhrq;		// 应还日期
	private String ghsj;		// 归还时间
	private Integer cqts;		// 超期天数
	private String admins;		// 操作员
	private String rem;			// 备注冗余字段

	public BookStudent getMainid() {
		return mainid;
	}

	public void setMainid(BookStudent mainid) {
		this.mainid = mainid;
	}

	@ExcelField(title="教师姓名", align=2, sort=1)
	public String getTeachersname() {
		return teachersname;
	}

	public void setTeachersname(String teachersname) {
		this.teachersname = teachersname;
	}
	
	@ExcelField(title="教师学号", align=2, sort=2)
	public String getTeacherbh() {
		return teacherbh;
	}

	public void setTeacherbh(String teacherbh) {
		this.teacherbh = teacherbh;
	}


	public Books getBookid() {
		return bookid;
	}

	public void setBookid(Books bookid) {
		this.bookid = bookid;
	}

	@ExcelField(title="ISBN", align=2, sort=4)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@ExcelField(title="索书号", align=2, sort=5)
	public String getSsh() {
		return ssh;
	}

	public void setSsh(String ssh) {
		this.ssh = ssh;
	}

	@ExcelField(title="图书类型", align=2, sort=6)
	public String getBooktype() {
		return booktype;
	}

	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}

	@ExcelField(title="书名", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="作者", align=2, sort=8)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@ExcelField(title="出版社", align=2, sort=9)
	public String getCbs() {
		return cbs;
	}

	public void setCbs(String cbs) {
		this.cbs = cbs;
	}
	
	@ExcelField(title="价格", align=2, sort=10)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@ExcelField(title="借出日期", align=2, sort=11)
	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

	@ExcelField(title="应还日期", align=2, sort=12)
	public String getYhrq() {
		return yhrq;
	}

	public void setYhrq(String yhrq) {
		this.yhrq = yhrq;
	}

	@ExcelField(title="归还时间", align=2, sort=13)
	public String getGhsj() {
		return ghsj;
	}

	public void setGhsj(String ghsj) {
		this.ghsj = ghsj;
	}

	@ExcelField(title="超期天数", align=2, sort=14)
	public Integer getCqts() {
		return cqts;
	}

	public void setCqts(Integer cqts) {
		this.cqts = cqts;
	}

	@ExcelField(title="操作员", align=2, sort=15)
	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	@ExcelField(title="备注", align=2, sort=15)
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}
	
}