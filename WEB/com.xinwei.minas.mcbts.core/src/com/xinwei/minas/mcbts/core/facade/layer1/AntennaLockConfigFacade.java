/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;

/**
 * 天线闭锁配置门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigFacade extends Remote {

	/**
	 * 查询天线闭锁配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsAntennaLock queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置天线闭锁
	 * 
	 * @param data
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws RemoteException,
			Exception;

}