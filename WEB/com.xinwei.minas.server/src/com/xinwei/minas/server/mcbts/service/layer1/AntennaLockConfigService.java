/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * ���߱������÷���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigService extends ICustomService {

	/**
	 * ��ѯ���߱���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsAntennaLock queryByMoId(Long moId) throws Exception;

	/**
	 * �������߱���
	 * 
	 * @param data
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws Exception;
}
