/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tree.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.tree.entity.Testtree;
import com.jeeplus.modules.tree.dao.TesttreeDao;

/**
 * treeService
 * @author tree
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class TesttreeService extends CrudService<TesttreeDao, Testtree> {

	public Testtree get(String id) {
		return super.get(id);
	}
	
	public List<Testtree> findList(Testtree testtree) {
		return super.findList(testtree);
	}
	
	public Page<Testtree> findPage(Page<Testtree> page, Testtree testtree) {
		return super.findPage(page, testtree);
	}
	
	@Transactional(readOnly = false)
	public void save(Testtree testtree) {
		super.save(testtree);
	}
	
	@Transactional(readOnly = false)
	public void delete(Testtree testtree) {
		super.delete(testtree);
	}

	@Transactional(readOnly = false)
	public List<Testtree> getName(Testtree rept) {
		return dao.getName(rept);
	}
	
	
}