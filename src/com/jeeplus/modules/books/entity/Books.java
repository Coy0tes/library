/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.books.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * booksEntity
 * @author mxc
 * @version 2017-11-07
 */
public class Books extends DataEntity<Books> {
	
	private static final long serialVersionUID = 1L;
	private String isbn;		// ISBN号
	private String ssh;		// 索书号
	private String booktypeid;		// 图书分类id
	private String booktype;		// 图书分类
	private String name;		// 书名
	private String author;		// 作者
	private String cbs;		// 出版社
	private String status;	// 图书状态
	private Double price;		// 价格
	private String numIn;	// 在库数量
	private String numOut;	// 借出数量
	private String numLoss;	// 遗失数量
	private String numBroken;	// 损坏数量
	private String total;
	
	private String statusName;
	
	public Books() {
		super();
	}

	public Books(String id){
		super(id);
	}

	@ExcelField(title="ISBN号", align=2, sort=1)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@ExcelField(title="索书号", align=2, sort=2)
	public String getSsh() {
		return ssh;
	}

	public void setSsh(String ssh) {
		this.ssh = ssh;
	}
	
	@ExcelField(title="图书分类", align=2, sort=3)
	public String getBooktype() {
		return booktype;
	}

	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}
	
	public String getBooktypeid() {
		return booktypeid;
	}

	public void setBooktypeid(String booktypeid) {
		this.booktypeid = booktypeid;
	}

	@ExcelField(title="书名", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="作者", align=2, sort=6)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@ExcelField(title="出版社", align=2, sort=7)
	public String getCbs() {
		return cbs;
	}

	public void setCbs(String cbs) {
		this.cbs = cbs;
	}
	
	@ExcelField(title="图书状态", dictType="bookStatus", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ExcelField(title="价格", align=2, sort=9)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@ExcelField(title="在库数量", align=2, sort=10)
	public String getNumIn() {
		return numIn;
	}

	public void setNumIn(String numIn) {
		this.numIn = numIn;
	}

	@ExcelField(title="借出数量", align=2, sort=11)
	public String getNumOut() {
		return numOut;
	}

	public void setNumOut(String numOut) {
		this.numOut = numOut;
	}

	@ExcelField(title="遗失数量", align=2, sort=12)
	public String getNumLoss() {
		return numLoss;
	}

	public void setNumLoss(String numLoss) {
		this.numLoss = numLoss;
	}

	@ExcelField(title="损坏数量", align=2, sort=13)
	public String getNumBroken() {
		return numBroken;
	}

	public void setNumBroken(String numBroken) {
		this.numBroken = numBroken;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}