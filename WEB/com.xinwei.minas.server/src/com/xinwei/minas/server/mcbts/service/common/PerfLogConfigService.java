/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.common.PerfLogConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 性能日志配置服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface PerfLogConfigService extends ICustomService {

	/**
	 * 按moId从数据库查询性能配置信息
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PerfLogConfig queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * 向基站配置
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, PerfLogConfig config) throws RemoteException,
			Exception;

	/**
	 * 从基站查询配置
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PerfLogConfig query(Long moId) throws RemoteException, Exception;
}
