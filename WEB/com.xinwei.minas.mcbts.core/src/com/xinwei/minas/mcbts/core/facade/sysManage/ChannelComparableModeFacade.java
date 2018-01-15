/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;

/**
 * 
 * ��վ����ģʽ��������ӿ�
 * 
 * @author jiayi
 * 
 */

public interface ChannelComparableModeFacade extends Remote {

	/**
	 * ����ָ����վ�Ļ�վ����ģʽ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws RemoteException, Exception
	 */
	@Loggable
	public void config(OperObject operObject, long moId, ChannelComparableMode config)
			throws RemoteException, Exception;

	/**
	 * ����ϵͳ�Ļ�վ����ģʽ
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws RemoteException, Exception
	 */
	@Loggable
	public void configAll(OperObject operObject, ChannelComparableMode config) throws RemoteException,
			Exception;

	/**
	 * ���������ݿ��ѯ����ģʽ������Ϣ
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
	 * 
	 * @throws RemoteException, Exception
	 */
	public ChannelComparableMode queryFromEMS() throws RemoteException,
			Exception;

	/**
	 * �ӻ�վ��ѯ����ģʽ������Ϣ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
	 * 
	 * @throws RemoteException, Exception
	 */
	public ChannelComparableMode queryFromNE(long moId) throws RemoteException,
			Exception;

}