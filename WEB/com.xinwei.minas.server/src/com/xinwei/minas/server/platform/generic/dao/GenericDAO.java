/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform.generic.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 通用的DAO
 * 
 * @author chenjunhua
 * 
 */

public interface GenericDAO<T, ID extends Serializable> {

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 *            实体主键
	 * @return
	 */
	public T queryById(ID id);

	/**
	 * 查找所有实体
	 * 
	 * @return
	 */
	public List<T> queryAll();

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 删除实体
	 * 
	 * @param id
	 */
	public void delete(ID id);
	
	/**
	 * 删除实体
	 * 
	 * @param entity
	 */
	public void delete(T entity);

}
