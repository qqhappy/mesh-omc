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
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 通用服务实现
 * 
 * @author chenjunhua
 * 
 */

public class GenericServiceImpl<T, ID extends Serializable> implements
		GenericService<T,ID> {

	// 实体类
	private Class<T> entityClass;
	
	private GenericDAO<T, ID> genericDAO;

	public GenericServiceImpl() {
		// 利用反射获取实体类
		this.entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	
	
	public void setGenericDAO(GenericDAO<T,ID> genericDAO) {
		this.genericDAO = genericDAO;
	}

	public GenericDAO<T, ID> getGenericDAO() {
		return genericDAO;
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 *            实体主键
	 * @return
	 */
	public T queryById(ID id) {
		return genericDAO.queryById(id);
	}

	/**
	 * 查找所有实体
	 * 
	 * @return
	 */
	public List<T> queryAll() {
		return genericDAO.queryAll();
	}

	/**
	 * 增加实体
	 * 
	 * @param entity
	 */
	public void add(T entity) {		
		genericDAO.saveOrUpdate(entity);
	}
	
	
	/**
	 * 增加实体
	 * 
	 * @param entity
	 */
	public void modify(T entity) {
		genericDAO.saveOrUpdate(entity);
	}

	/**
	 * 删除指定ID的实体
	 * 
	 * @param id
	 */
	public void delete(ID id) {
		T entity = genericDAO.queryById(id);
		genericDAO.delete(entity);
	}




	
	
	

}
