/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.server.mcbts.service.ICustomService;
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

public interface PreventBroadcastService  extends ICustomService{
	/**
	 * ��ѯȫ������
	 * 
	 * @return
	 */
	public List<GenericBizData> queryAll();
	/**
	 * �����л�վ����
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configAll(GenericBizData config) throws RemoteException,
			Exception;
	
	/**
	 * ��ָ����վ��������
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData config) throws RemoteException, Exception;

	/**
	 * ���վ��ѯ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId) throws RemoteException,
			Exception;
}
