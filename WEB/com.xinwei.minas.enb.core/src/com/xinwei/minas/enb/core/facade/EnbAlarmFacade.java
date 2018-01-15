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
 * eNB�澯����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbAlarmFacade extends Remote {

	/**
	 * ��ָ����Ԫ���и澯ͬ������
	 * 
	 * @param moId
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void syncAlarm(OperObject operObject, long moId)
			throws RemoteException, Exception;

}
