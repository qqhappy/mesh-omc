/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-10	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhaolingling
 * 
 */

public interface BtsReleaseVoiceTimerFacade  extends Remote {

	/**
	 * 
	 */
	/**
	 * �����л�վ����
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void configAll(OperObject operObject, GenericBizData config) throws RemoteException,
			Exception;

	/**
	 * �����ݿ��ѯ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromEMS() throws RemoteException,
			Exception;

	/**
	 * �ӻ�վ��ѯ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId) throws RemoteException,
			Exception;
}
