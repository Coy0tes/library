/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tree.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tree.entity.Testtree;

/**
 * treeDAO接口
 * @author tree
 * @version 2017-11-01
 */
@MyBatisDao
public interface TesttreeDao extends CrudDao<Testtree> {

	List<Testtree> getName(Testtree rept);

}