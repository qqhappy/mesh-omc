/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 切换优化开关配置服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeService extends ICustomService{

	/**
	 * 查询全部配置
	 * 
	 * @return
	 */
	public List<SwitchOptimizeConfig> queryAll();

	/**
	 * 向基站配置
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception;

	/**
	 * 向所有基站配置
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configAll(SwitchOptimizeConfig config) throws RemoteException,
			Exception;

	/**
	 * 向基站查询
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception;

}
