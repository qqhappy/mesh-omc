/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * McBtsҵ����������
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizFacade extends Remote {

	/**
	 * ����MO ID��ҵ��ID��ѯҵ������
	 * 
	 * @param moId
	 *            ���ܶ���ID
	 * @param bizName
	 *            ` ҵ������
	 * @return ��¼��
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName)
			throws RemoteException, Exception;

	/**
	 * ������Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception;

	/**
	 * ����Ԫ����ָ��, ����Ҫ���г־û�
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void sendCommand(OperObject operObject, Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception;

	/**
	 * �����ݿ��ȡ������Ϣ
	 * 
	 * @param moId
	 *            , genericBizData
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromEMS(Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception;

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 *            , genericBizData
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception;

	public Map<String, Map<Integer, Integer>> getStudyCache()
			throws RemoteException, Exception;

	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws RemoteException, Exception;
}
