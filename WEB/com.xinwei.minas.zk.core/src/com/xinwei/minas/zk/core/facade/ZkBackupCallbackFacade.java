/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.facade;

import java.rmi.Remote;

/**
 * 
 * 备份回调门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupCallbackFacade extends Remote {

	/**
	 * 通知客户端异常
	 * 
	 * @param exception
	 * @throws RuntimeException
	 * @throws Exception
	 */
	public void notifyException(Exception exception) throws RuntimeException, Exception;
}
