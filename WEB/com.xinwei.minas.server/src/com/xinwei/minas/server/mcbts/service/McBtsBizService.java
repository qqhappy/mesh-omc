/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * McBtsҵ�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizService {

	/**
	 * �����ܻ�ȡҵ������
	 * 
	 * @param moId
	 * @param bizName
	 * @return
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName)
			throws Exception;



	/**
	 * ����Ԫ��������ָ��(�����г־û�)
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void sendCommand(Long moId, GenericBizData genericBizData)
			throws Exception;

	
	/**
	 * ����Ԫ���ҵ������
	 * 
	 * @param moId
	 * @param genericBizData
	 * @return
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId, GenericBizData genericBizData)
			throws Exception;
	
	/**
	 * �־û���Ԫҵ������
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws Exception
	 */
	public void saveToDB(Long moId, GenericBizData genericBizData)
			throws Exception;


	
	
	/**
	 * ������Ԫҵ������
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData genericBizData)
			throws Exception;

	public Map<String, Map<Integer, Integer>> getStudyCache() throws Exception;

	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception;
}
