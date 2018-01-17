/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bookstudenthistory.entity;

import com.jeeplus.modules.books.entity.Books;
import com.jeeplus.modules.bookstudent.entity.BookStudent;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * bookstudentEntity
 * @author mxc
 * @version 2017-11-10
 */
public class BookStudentHistory extends DataEntity<BookStudentHistory> {
	
	private static final long serialVersionUID = 1L;
	private BookStudent mainid;		// 关联ID
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
	private String studentId;	// 学生id
	private String studentName;	// 学生姓名
	private String studentXh;	// 学生学号
	private String classesId;	// 班级id
	private String classesName;	// 班级
	private String teachersId;	// 教师id
	private String teachersName;// 教师名字
	private String teachersBh;// 教师手机号
	private String rem;		// 备注冗余
	private String pcsj;	// 赔偿时间
	private String pcje;	// 赔偿金额
	
	public BookStudentHistory() {
		super();
	}

	public BookStudentHistory(String id){
		super(id);
	}
	
	public BookStudentHistory(BookStudent mainid){
		this.mainid = mainid;
	}

	public BookStudent getMainid() {
		return mainid;
	}

	public void setMainid(BookStudent mainid) {
		this.mainid = mainid;
	}

	public Books getBookid() {
		return bookid;
	}

	public void setBookid(Books bookid) {
		this.bookid = bookid;
	}
	
	@ExcelField(title="ISBN编号", align=2, sort=7)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@ExcelField(title="索书号", align=2, sort=8)
	public String getSsh() {
		return ssh;
	}

	public void setSsh(String ssh) {
		this.ssh = ssh;
	}
	
	@ExcelField(title="图书分类", align=2, sort=9)
	public String getBooktype() {
		return booktype;
	}

	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}
	
	@ExcelField(title="书名", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="作者", align=2, sort=10)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@ExcelField(title="出版社", align=2, sort=11)
	public String getCbs() {
		return cbs;
	}

	public void setCbs(String cbs) {
		this.cbs = cbs;
	}
	
	@ExcelField(title="价格", align=2, sort=12)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@ExcelField(title="借出时间", align=2, sort=13)
	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}
	
	@ExcelField(title="应还日期", align=2, sort=14)
	public String getYhrq() {
		return yhrq;
	}

	public void setYhrq(String yhrq) {
		this.yhrq = yhrq;
	}
	
	@ExcelField(title="归还时间", align=2, sort=15)
	public String getGhsj() {
		return ghsj;
	}

	public void setGhsj(String ghsj) {
		this.ghsj = ghsj;
	}
	
	@ExcelField(title="超期天数", align=2, sort=16)
	public Integer getCqts() {
		return cqts;
	}

	public void setCqts(Integer cqts) {
		this.cqts = cqts;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@ExcelField(title="学生姓名", align=2, sort=1)
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@ExcelField(title="学生学号", align=2, sort=2)
	public String getStudentXh() {
		return studentXh;
	}

	public void setStudentXh(String studentXh) {
		this.studentXh = studentXh;
	}

	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}

	@ExcelField(title="学生班级", align=2, sort=3)
	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public String getTeachersId() {
		return teachersId;
	}

	public void setTeachersId(String teachersId) {
		this.teachersId = teachersId;
	}

	@ExcelField(title="教师姓名", align=2, sort=4)
	public String getTeachersName() {
		return teachersName;
	}

	public void setTeachersName(String teachersName) {
		this.teachersName = teachersName;
	}

	@ExcelField(title="教师工号", align=2, sort=5)
	public String getTeachersBh() {
		return teachersBh;
	}

	public void setTeachersBh(String teachersBh) {
		this.teachersBh = teachersBh;
	}

	@ExcelField(title="备注信息", align=2, sort=20)
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	@ExcelField(title="赔偿时间", align=2, sort=18)
	public String getPcsj() {
		return pcsj;
	}

	public void setPcsj(String pcsj) {
		this.pcsj = pcsj;
	}

	@ExcelField(title="赔偿金额", align=2, sort=19)
	public String getPcje() {
		return pcje;
	}

	public void setPcje(String pcje) {
		this.pcje = pcje;
	}
	
	

}