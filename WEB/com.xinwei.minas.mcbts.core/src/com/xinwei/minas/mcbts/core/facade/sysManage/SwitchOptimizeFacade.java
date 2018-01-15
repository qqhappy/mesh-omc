/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;

/**
 * 
 * 切换优化开关配置门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeFacade extends Remote {

	/**
	 * 向基站配置
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception;

	/**
	 * 向所有基站配置
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void configAll(OperObject operObject, SwitchOptimizeConfig config) throws RemoteException,
			Exception;

	/**
	 * 从数据库查询
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromEMS() throws RemoteException,
			Exception;

	/**
	 * 从基站查询
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception;

}