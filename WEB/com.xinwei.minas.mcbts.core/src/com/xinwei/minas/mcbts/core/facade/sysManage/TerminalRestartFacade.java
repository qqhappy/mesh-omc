/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;

/**
 * 终端重启门面接口
 * @author zhuxiaozhan
 *
 */
public interface TerminalRestartFacade extends Remote {
	/**
	 * 重启终端配置请求
	 * @param modId
	 * @param eid
	 */
	@Loggable
	public void restartConfig(OperObject operObject, Long moId, String eid) throws RemoteException, Exception;
}
