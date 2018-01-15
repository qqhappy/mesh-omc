/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.common.GPSData;

/**
 * 
 * GPS信息的门面
 * 
 * 
 * @author tiance
 * 
 */

public interface GPSDataFacade extends MoBizFacade<GPSData> {

	/**
	 * 配置GPS信息
	 * @param data
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, GPSData data) throws RemoteException, Exception;

}
