/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;

/**
 * 
 * �л��Ż�������������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeFacade extends Remote {

	/**
	 * ���վ����
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception;

	/**
	 * �����л�վ����
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void configAll(OperObject operObject, SwitchOptimizeConfig config) throws RemoteException,
			Exception;

	/**
	 * �����ݿ��ѯ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromEMS() throws RemoteException,
			Exception;

	/**
	 * �ӻ�վ��ѯ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception;

}