/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;

/**
 * 
 * 基站兼容模式配置门面接口
 * 
 * @author jiayi
 * 
 */

public interface ChannelComparableModeFacade extends Remote {

	/**
	 * 配置指定基站的基站兼容模式
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws RemoteException, Exception
	 */
	@Loggable
	public void config(OperObject operObject, long moId, ChannelComparableMode config)
			throws RemoteException, Exception;

	/**
	 * 配置系统的基站兼容模式
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws RemoteException, Exception
	 */
	@Loggable
	public void configAll(OperObject operObject, ChannelComparableMode config) throws RemoteException,
			Exception;

	/**
	 * 从网管数据库查询兼容模式配置信息
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws RemoteException, Exception
	 */
	public ChannelComparableMode queryFromEMS() throws RemoteException,
			Exception;

	/**
	 * 从基站查询兼容模式配置信息
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws RemoteException, Exception
	 */
	public ChannelComparableMode queryFromNE(long moId) throws RemoteException,
			Exception;

}