/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * �л��Ż��������÷���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeService extends ICustomService{

	/**
	 * ��ѯȫ������
	 * 
	 * @return
	 */
	public List<SwitchOptimizeConfig> queryAll();

	/**
	 * ���վ����
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception;

	/**
	 * �����л�վ����
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configAll(SwitchOptimizeConfig config) throws RemoteException,
			Exception;

	/**
	 * ���վ��ѯ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception;

}
