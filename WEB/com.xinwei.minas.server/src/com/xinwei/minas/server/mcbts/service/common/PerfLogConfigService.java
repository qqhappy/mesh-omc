/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.common.PerfLogConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * ������־���÷���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface PerfLogConfigService extends ICustomService {

	/**
	 * ��moId�����ݿ��ѯ����������Ϣ
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PerfLogConfig queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * ���վ����
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, PerfLogConfig config) throws RemoteException,
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
