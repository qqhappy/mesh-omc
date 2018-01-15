/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;

/**
 * 
 * 同步配置门面
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSynConfigFacade extends Remote {

	/**
	 * 同步EMS数据到基站
	 * 
	 * @param restudy
	 *            是否需要自学习
	 * @param moId
	 *            基站的网元ID
	 * @return 失败业务列表
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public List<String> config(OperObject operObject, Integer restudy, Long moId)
			throws RemoteException, Exception;

	/**
	 * 从基站同步数据到EMS
	 * 
	 * @param moId
	 *            基站的网元ID
	 * @return 失败的业务列表
	 * @throws Exception
	 */
	@Loggable
	public List<String> syncFromNEToEMS(OperObject operObject, Long moId) throws RemoteException,
			Exception;

}
