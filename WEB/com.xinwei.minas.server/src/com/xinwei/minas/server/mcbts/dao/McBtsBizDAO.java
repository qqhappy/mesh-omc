/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.conf.MoBizData;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.meta.XMetaBiz;

/**
 * 
 * McBtsҵ��DAO�ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizDAO {

	/**
	 * ����moId��ҵ�����ơ���ѯ������ѯҵ������
	 * 
	 * @param moId
	 *            ���ܶ���ID
	 * @param bizName
	 *            ҵ������
	 * @return ��¼��
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName,
			String condition, Object[] conditionValue) throws Exception;

	/**
	 * ��ѯĳ��ҵ�����������
	 * 
	 * @param bizName
	 * @return
	 * @throws Exception
	 */
	List<GenericBizData> queryExportList(String bizName) throws Exception;

	/**
	 * ������Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void addOrUpdate(Long moId, GenericBizData genericBizData)
			throws Exception;

	/**
	 * ɾ����Ԫҵ������
	 * 
	 * @param bizName
	 * @param moId
	 * @throws Exception
	 */
	public void delete(String bizName, Long moId) throws Exception;

	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception;

}