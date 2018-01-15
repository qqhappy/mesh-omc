/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-11| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;

/**
 * 
 * eNB告警门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbAlarmFacade extends Remote {

	/**
	 * 对指定网元进行告警同步操作
	 * 
	 * @param moId
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void syncAlarm(OperObject operObject, long moId)
			throws RemoteException, Exception;

}
