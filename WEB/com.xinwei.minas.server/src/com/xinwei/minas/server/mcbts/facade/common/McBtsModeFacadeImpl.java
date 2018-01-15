/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.mcbts.core.facade.common.McBtsModeFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.service.common.GPSDataService;
import com.xinwei.minas.server.mcbts.service.common.McBtsModeService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站工作模式门面接口类
 * 
 * @author tiance
 * 
 */

public class McBtsModeFacadeImpl extends UnicastRemoteObject implements
		McBtsModeFacade {

	private McBtsModeService mcbtsModeService;

	protected McBtsModeFacadeImpl() throws RemoteException {
		super();
		mcbtsModeService = AppContext.getCtx().getBean(McBtsModeService.class);
	}

	@Override
	public McBts queryMcBtsMode(McBts mcbts) throws RemoteException, Exception {
		return mcbtsModeService.queryMcBtsMode(mcbts);
	}

}
