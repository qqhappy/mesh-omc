/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.common.PerfLogConfigFacade;
import com.xinwei.minas.mcbts.core.model.common.PerfLogConfig;
import com.xinwei.minas.server.mcbts.service.common.PerfLogConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 性能日志配置门面实现类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class PerfLogConfigFacadeImpl extends UnicastRemoteObject implements
		PerfLogConfigFacade {

	private PerfLogConfigService service;

	public PerfLogConfigFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public PerfLogConfig queryByMoId(Long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(PerfLogConfigService.class);
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, Long moId, PerfLogConfig config) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(PerfLogConfigService.class);
		service.config(moId, config);
	}

	@Override
	public PerfLogConfig query(Long moId) throws RemoteException, Exception {
		service = AppContext.getCtx().getBean(PerfLogConfigService.class);
		return service.query(moId);
	}

	@Override
	public PerfLogConfig queryFromEMS(Long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(PerfLogConfigService.class);
		return service.queryByMoId(moId);
	}

	@Override
	public PerfLogConfig queryFromNE(Long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(PerfLogConfigService.class);
		return service.query(moId);
	}

}
