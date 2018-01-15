/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * ʵʱ���ܼ��ӷ���������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface RealTimePerfMonitorFacade extends Remote {

	/**
	 * ������ĳ������ļ���
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void start(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

	/**
	 * ֹͣ��ĳ������ļ���
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void stop(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

	/**
	 * �ͻ�����������������
	 * 
	 * @param sessionId
	 * @param item
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId, MonitorItem item)
			throws RemoteException, Exception;

}
