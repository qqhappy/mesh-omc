/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform.generic.service;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 通用服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface GenericService <T, ID extends Serializable> {

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
	 * 增加实体
	 * 
	 * @param entity
	 */
	public void add(T entity);
	
	
	/**
	 * 修改实体
	 * 
	 * @param entity
	 */
	public void modify(T entity);
	

	/**
	 * 删除指定ID的实体
	 * 
	 * @param id
	 */
	public void delete(ID id);
}
