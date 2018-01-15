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
 * ͨ�÷���ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface GenericService <T, ID extends Serializable> {

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
	 * ����ʵ��
	 * 
	 * @param entity
	 */
	public void add(T entity);
	
	
	/**
	 * �޸�ʵ��
	 * 
	 * @param entity
	 */
	public void modify(T entity);
	

	/**
	 * ɾ��ָ��ID��ʵ��
	 * 
	 * @param id
	 */
	public void delete(ID id);
}
