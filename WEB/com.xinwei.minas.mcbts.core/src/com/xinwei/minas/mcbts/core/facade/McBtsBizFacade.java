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
 * McBts业务配置门面
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizFacade extends Remote {

	/**
	 * 根据MO ID和业务ID查询业务数据
	 * 
	 * @param moId
	 *            被管对象ID
	 * @param bizName
	 *            ` 业务名称
	 * @return 记录集
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName)
			throws RemoteException, Exception;

	/**
	 * 配置网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception;

	/**
	 * 向网元发送指令, 不需要进行持久化
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
	 * 从数据库获取配置信息
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
	 * 从网元获得配置信息
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
