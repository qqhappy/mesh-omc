/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.oamManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsSynConfigFacade;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsSynConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 同步配置门面实现
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSynConfigFacadeImpl extends UnicastRemoteObject implements
		McBtsSynConfigFacade {

	private McBtsSynConfigService service;

	protected McBtsSynConfigFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsSynConfigService.class);
	}

	@Override
	public List<String> config(OperObject operObject, Integer restudy, Long moId) throws RemoteException,
			Exception {
		return service.config(restudy, moId);
	}

	@Override
	public List<String> syncFromNEToEMS(OperObject operObject, Long moId) throws RemoteException,
			Exception {
		return service.syncFromNEToEMS(moId);
	}

}
