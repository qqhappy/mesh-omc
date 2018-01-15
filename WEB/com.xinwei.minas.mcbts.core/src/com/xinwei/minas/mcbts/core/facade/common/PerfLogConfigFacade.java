/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.common.PerfLogConfig;

/**
 * 
 * ������־��������
 * 
 * @author fanhaoyu
 * 
 */

public interface PerfLogConfigFacade extends MoBizFacade<PerfLogConfig> {

	/**
	 * ��moId�����ݿ��ѯ����������Ϣ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PerfLogConfig queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ���վ����
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, PerfLogConfig config) throws RemoteException,
			Exception;

	/**
	 * �ӻ�վ��ѯ����
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PerfLogConfig query(Long moId) throws RemoteException, Exception;

}
