/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * 实时性能监视服务器门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface RealTimePerfMonitorFacade extends Remote {

	/**
	 * 启动对某监视项的监视
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void start(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

	/**
	 * 停止对某监视项的监视
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void stop(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

	/**
	 * 客户端与服务器间的握手
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

}
