/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.conf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.Region;

/**
 * 
 * ������������
 * 
 * @author chenjunhua
 * 
 */

public interface RegionServiceFacade extends Remote{
	/**
	 * ���ӵ���
	 * @param region
	 * @throws Exception
	 */
	public void addRegoin(Region region) throws RemoteException, Exception;
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Region> queryAll() throws RemoteException,Exception;
}
