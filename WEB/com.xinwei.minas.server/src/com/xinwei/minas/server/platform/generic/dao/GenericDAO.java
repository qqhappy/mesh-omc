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
 * ͨ�õ�DAO
 * 
 * @author chenjunhua
 * 
 */

public interface GenericDAO<T, ID extends Serializable> {

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 *            ʵ������
	 * @return
	 */
	public T queryById(ID id);

	/**
	 * ��������ʵ��
	 * 
	 * @return
	 */
	public List<T> queryAll();

	/**
	 * ���ӻ����ʵ��
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity);

	/**
	 * ɾ��ʵ��
	 * 
	 * @param id
	 */
	public void delete(ID id);
	
	/**
	 * ɾ��ʵ��
	 * 
	 * @param entity
	 */
	public void delete(T entity);

}
