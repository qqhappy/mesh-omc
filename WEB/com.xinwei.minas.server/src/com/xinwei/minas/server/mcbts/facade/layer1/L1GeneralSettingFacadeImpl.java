/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.L1GeneralSettingFacade;
import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.server.mcbts.service.layer1.L1GeneralSettingService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * L1配置基本业务处理
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class L1GeneralSettingFacadeImpl extends UnicastRemoteObject implements
		L1GeneralSettingFacade {

	private L1GeneralSettingService service;
	private SequenceService sequenceService;
	L1GeneralSetting a = new L1GeneralSetting();

	public L1GeneralSettingFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(L1GeneralSettingService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	/**
	 * 查询L1配置基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	
	@Override
	public void config(OperObject operObject, L1GeneralSetting setting) throws Exception {
		if (setting.getIdx() == null) {
			setting.setIdx(sequenceService.getNext());
		}
		service.config(setting, false);
	}

	@Override
	public void config(L1GeneralSetting setting, boolean isSyncConfig)
			throws Exception {
		if (setting.getIdx() == null) {
			setting.setIdx(sequenceService.getNext());
		}
		service.config(setting, isSyncConfig);
	}

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public L1GeneralSetting queryFromEMS(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public L1GeneralSetting queryFromNE(Long moId) throws RemoteException,
			Exception {
		return service.queryFromNE(moId);
	}

}
