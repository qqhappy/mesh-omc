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
 * 通用的Hibernate DAO
 * 
 * @author chenjunhua
 * 
 */

public class GenericHibernateDAO<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDAO<T, ID> {

	// 实体类
	private Class<T> entityClass;

	public GenericHibernateDAO() {
		// 利用反射获取实体类
		this.entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 *            实体主键
	 * @return
	 */
	public T queryById(ID id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * 查找所有实体
	 * 
	 * @return
	 */
	public List<T> queryAll() {
		return getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * 删除实体
	 * 
	 * @param id
	 */
	public void delete(ID id) {
		T entity = this.queryById(id);
		getHibernateTemplate().delete(entity);
	}

	/**
	 * 删除实体
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		if (entity != null) {
			getHibernateTemplate().delete(entity);
		}
	}

}
