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
 * McBts业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizService {

	/**
	 * 从网管获取业务数据
	 * 
	 * @param moId
	 * @param bizName
	 * @return
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName)
			throws Exception;



	/**
	 * 向网元发送配置指令(不进行持久化)
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void sendCommand(Long moId, GenericBizData genericBizData)
			throws Exception;

	
	/**
	 * 从网元获得业务数据
	 * 
	 * @param moId
	 * @param genericBizData
	 * @return
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId, GenericBizData genericBizData)
			throws Exception;
	
	/**
	 * 持久化网元业务数据
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws Exception
	 */
	public void saveToDB(Long moId, GenericBizData genericBizData)
			throws Exception;


	
	
	/**
	 * 配置网元业务数据
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
