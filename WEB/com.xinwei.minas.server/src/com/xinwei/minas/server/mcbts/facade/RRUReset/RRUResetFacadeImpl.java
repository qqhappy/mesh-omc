/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.RRUReset;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.rruManage.RRUResetFacade;
import com.xinwei.minas.server.mcbts.service.rruManage.RRUResetService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * @author chenshaohua
 * 
 */

public class RRUResetFacadeImpl extends UnicastRemoteObject implements
		RRUResetFacade {

	private RRUResetService service;
	
	protected RRUResetFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(RRUResetService.class);
	}

	@Override
	public void config(OperObject operObject, Mo mo) throws RemoteException, Exception {
		service.config(mo);
	}

}
