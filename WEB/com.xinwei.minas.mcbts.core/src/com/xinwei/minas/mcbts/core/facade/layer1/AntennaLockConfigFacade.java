/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;

/**
 * ���߱�����������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigFacade extends Remote {

	/**
	 * ��ѯ���߱���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsAntennaLock queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * �������߱���
	 * 
	 * @param data
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws RemoteException,
			Exception;

}