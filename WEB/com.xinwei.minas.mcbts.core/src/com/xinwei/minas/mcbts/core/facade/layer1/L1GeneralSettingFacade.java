/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;

/**
 * 
 * L1基本配置业务门面
 * 
 * @author chenjunhua
 * 
 */

public interface L1GeneralSettingFacade extends MoBizFacade<L1GeneralSetting> {

	/**
	 * 查询L1基本配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置L1配置基本信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, L1GeneralSetting data) throws RemoteException, Exception;

	/**
	 * 配置L1配置基本信息
	 * 
	 * @param data
	 * @param isSyncConfig
	 *            是否同步配置邻接表
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(L1GeneralSetting data, boolean isSyncConfig)
			throws RemoteException, Exception;

}
