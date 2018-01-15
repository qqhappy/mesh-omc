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
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 
 * ͨ�õ�Hibernate DAO
 * 
 * @author chenjunhua
 * 
 */

public class GenericHibernateDAO<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDAO<T, ID> {

	// ʵ����
	private Class<T> entityClass;

	public GenericHibernateDAO() {
		// ���÷����ȡʵ����
		this.entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 *            ʵ������
	 * @return
	 */
	public T queryById(ID id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * ��������ʵ��
	 * 
	 * @return
	 */
	public List<T> queryAll() {
		return getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * ���ӻ����ʵ��
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * ɾ��ʵ��
	 * 
	 * @param id
	 */
	public void delete(ID id) {
		T entity = this.queryById(id);
		getHibernateTemplate().delete(entity);
	}

	/**
	 * ɾ��ʵ��
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		if (entity != null) {
			getHibernateTemplate().delete(entity);
		}
	}

}
