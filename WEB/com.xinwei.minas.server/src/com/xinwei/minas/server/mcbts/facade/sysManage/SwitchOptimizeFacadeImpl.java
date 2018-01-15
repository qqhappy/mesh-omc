/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.SwitchOptimizeFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.mcbts.service.sysManage.SwitchOptimizeService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SwitchOptimizeFacadeImpl extends UnicastRemoteObject implements
		SwitchOptimizeFacade {

	private SwitchOptimizeService service;

	protected SwitchOptimizeFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public SwitchOptimizeConfig queryFromEMS() throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(SwitchOptimizeService.class);
		List<SwitchOptimizeConfig> configList = service.queryAll();
		if (configList == null || configList.isEmpty()) {
			return null;
		}
		return configList.get(0);
	}

	@Override
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(SwitchOptimizeService.class);
		return service.queryFromNE(moId);
	}

	@Override
	public void config(OperObject operObject, Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception {
		service.config(moId, config);
	}

	@Override
	public void configAll(OperObject operObject, SwitchOptimizeConfig config) throws RemoteException,
			Exception {
		service.configAll(config);
	}

}
