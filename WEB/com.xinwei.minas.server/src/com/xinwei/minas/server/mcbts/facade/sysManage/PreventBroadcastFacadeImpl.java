/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.PreventBroadcastFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.sysManage.PreventBroadcastService;
import com.xinwei.minas.server.mcbts.service.sysManage.SwitchOptimizeService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p> 
 * 
 * @author zhaolingling
 * 
 */

public class PreventBroadcastFacadeImpl extends UnicastRemoteObject implements PreventBroadcastFacade {
	
	private PreventBroadcastService service;
	
	
	protected PreventBroadcastFacadeImpl() throws RemoteException {
		super();
	}
	
	@Override
	public void configAll(OperObject operObject, GenericBizData config)
			throws RemoteException, Exception {

		service.configAll(config);
	
	}

	@Override
	public GenericBizData queryFromEMS() throws RemoteException, Exception {
		service = AppContext.getCtx().getBean(PreventBroadcastService.class);
		List<GenericBizData> configList = service.queryAll();
		if (configList == null || configList.isEmpty()) {
			return null;
		}
		return configList.get(0);
	}

	@Override
	public GenericBizData queryFromNE(Long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(PreventBroadcastService.class);
		return service.queryFromNE(moId);
	}

	
	

}
