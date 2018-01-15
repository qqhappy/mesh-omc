/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 天线闭锁配置服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigService extends ICustomService {

	/**
	 * 查询天线闭锁配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsAntennaLock queryByMoId(Long moId) throws Exception;

	/**
	 * 配置天线闭锁
	 * 
	 * @param data
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws Exception;
}
