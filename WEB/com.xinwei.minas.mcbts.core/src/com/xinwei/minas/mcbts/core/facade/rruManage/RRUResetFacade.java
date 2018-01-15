/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.rruManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;

/**
 * 
 * @author chenshaohua
 * 
 */

public interface RRUResetFacade extends Remote {

	/**
	 * RRU÷ÿ∆Ù
	 * @param mo
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Mo mo) throws RemoteException, Exception; 
}
