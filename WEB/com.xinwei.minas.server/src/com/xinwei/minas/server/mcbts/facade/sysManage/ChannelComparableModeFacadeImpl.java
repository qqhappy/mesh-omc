/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.ChannelComparableModeFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.service.sysManage.ChannelComparableModeService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站兼容模式配置门面
 * 
 * @author jiayi
 * 
 */

@SuppressWarnings("serial")
public class ChannelComparableModeFacadeImpl extends UnicastRemoteObject
		implements ChannelComparableModeFacade {

	private ChannelComparableModeService service;

	protected ChannelComparableModeFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(ChannelComparableModeService.class);
	}

	/**
	 * 配置指定基站的基站兼容模式
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws RemoteException, Exception
	 */
	public void config(OperObject operObject, long moId, ChannelComparableMode config)
			throws RemoteException, Exception {
		service.config(moId, config);
	}

	@Override
	public void configAll(OperObject operObject, ChannelComparableMode config) throws RemoteException,
			Exception {
		service.config(config);
	}

	@Override
	public ChannelComparableMode queryFromEMS() throws RemoteException,
			Exception {
		return service.queryFromEMS();
	}

	@Override
	public ChannelComparableMode queryFromNE(long moId) throws RemoteException,
			Exception {
		return service.queryFromNE(moId);
	}

}
