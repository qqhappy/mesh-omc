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
 * ͨ�÷���ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class GenericServiceImpl<T, ID extends Serializable> implements
		GenericService<T,ID> {

	// ʵ����
	private Class<T> entityClass;
	
	private GenericDAO<T, ID> genericDAO;

	public GenericServiceImpl() {
		// ���÷����ȡʵ����
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
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 *            ʵ������
	 * @return
	 */
	public T queryById(ID id) {
		return genericDAO.queryById(id);
	}

	/**
	 * ��������ʵ��
	 * 
	 * @return
	 */
	public List<T> queryAll() {
		return genericDAO.queryAll();
	}

	/**
	 * ����ʵ��
	 * 
	 * @param entity
	 */
	public void add(T entity) {		
		genericDAO.saveOrUpdate(entity);
	}
	
	
	/**
	 * ����ʵ��
	 * 
	 * @param entity
	 */
	public void modify(T entity) {
		genericDAO.saveOrUpdate(entity);
	}

	/**
	 * ɾ��ָ��ID��ʵ��
	 * 
	 * @param id
	 */
	public void delete(ID id) {
		T entity = genericDAO.queryById(id);
		genericDAO.delete(entity);
	}




	
	
	

}
