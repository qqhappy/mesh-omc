/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao;

import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 被管对象基本DAO
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicDAO extends GenericDAO<Mo, Long> {

	/**
	 * 查询所有Mo
	 * 
	 * @return
	 */
	public List<Mo> queryAllMo();

	/**
	 * 获取制定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId);

	/**
	 * 增加被管对象
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId);

}
