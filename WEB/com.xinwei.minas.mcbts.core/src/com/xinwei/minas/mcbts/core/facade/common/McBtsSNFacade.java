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
 * ��վ���к�����
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNFacade extends Remote {

	/**
	 * ���վ��ѯ���к�
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsSN querySNFromNE(Long moId) throws RemoteException,
			Exception;

	/**
	 * �����ݿ��ѯ��վ���к�
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsSN> querySNFromDB(long moId) throws RemoteException,
			Exception;

}
