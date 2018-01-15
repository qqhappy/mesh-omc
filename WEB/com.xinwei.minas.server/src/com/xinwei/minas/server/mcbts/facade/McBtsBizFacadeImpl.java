/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBizFacade;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * McBts业务门面 必须继承UnicastRemoteObject，且构造函数需要定义并抛RemoteException
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizFacadeImpl extends UnicastRemoteObject implements
		McBtsBizFacade {

	private McBtsBizService service;

	public McBtsBizFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsBizService.class);
	}

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
			throws Exception {
		return service.queryAllBy(moId, bizName);
	}

	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception {
		return service.queryEnumItem(tableName, sqlStatement);
	}

	/**
	 * 配置网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void config(OperObject operObject, Long moId, GenericBizData genericBizData)
			throws Exception {
		service.config(moId, genericBizData);
	}

	/**
	 * 向网元发送指令, 不需要进行持久化
	 * 
	 * @param moId
	 * @param genericBizData
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void sendCommand(OperObject operObject, Long moId, GenericBizData genericBizData)
			throws Exception {
		service.sendCommand(moId, genericBizData);
	}

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 *            , genericBizData
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public GenericBizData queryFromEMS(Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception {
		return service.queryAllBy(moId, genericBizData.getBizName());
	}

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 *            , genericBizData
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public GenericBizData queryFromNE(Long moId, GenericBizData genericBizData)
			throws RemoteException, Exception {
		return service.queryFromNE(moId, genericBizData);
	}

	@Override
	public Map<String, Map<Integer, Integer>> getStudyCache()
			throws RemoteException, Exception {
		return service.getStudyCache();
	}

}
