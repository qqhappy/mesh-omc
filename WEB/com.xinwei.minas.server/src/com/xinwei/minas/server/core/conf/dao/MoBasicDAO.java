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
 * ���ܶ������DAO
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicDAO extends GenericDAO<Mo, Long> {

	/**
	 * ��ѯ����Mo
	 * 
	 * @return
	 */
	public List<Mo> queryAllMo();

	/**
	 * ��ȡ�ƶ����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId);

	/**
	 * ���ӱ��ܶ���
	 * 
	 * @param mo
	 *            ���ܶ���
	 * @param regionId
	 *            �����������
	 */
	public void add(Mo mo, long regionId);

}
