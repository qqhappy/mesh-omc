/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * ����Facade�ӿڵĸ��ӿ�
 * ���е�ʵ��Facade���඼Ҫʵ�ִ˽ӿڶ������������
 * 
 * @author tiance
 * 
 */

public interface MoBizFacade<T> extends Remote{

	/**
	 * �����ݿ��ȡ������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public T queryFromEMS(Long moId) throws RemoteException, Exception;
	
	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public T queryFromNE(Long moId) throws RemoteException, Exception;
	
}
