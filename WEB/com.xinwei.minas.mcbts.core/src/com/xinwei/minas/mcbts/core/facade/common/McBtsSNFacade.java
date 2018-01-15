/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * 基站序列号门面
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNFacade extends Remote {

	/**
	 * 向基站查询序列号
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsSN querySNFromNE(Long moId) throws RemoteException,
			Exception;

	/**
	 * 向数据库查询基站序列号
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsSN> querySNFromDB(long moId) throws RemoteException,
			Exception;

}
