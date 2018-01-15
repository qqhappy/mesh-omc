/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.common.McBtsSNFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 基站序列号门面实现
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSNFacadeImpl extends UnicastRemoteObject implements
		McBtsSNFacade {

	private McBtsSNService service;

	public McBtsSNFacadeImpl() throws RemoteException {
		super();

	}

	@Override
	public McBtsSN querySNFromNE(Long moId) throws RemoteException, Exception {
		service = AppContext.getCtx().getBean(McBtsSNService.class);
		return service.querySNFromNE(moId);
	}

	@Override
	public List<McBtsSN> querySNFromDB(long moId) throws RemoteException,
			Exception {
		service = AppContext.getCtx().getBean(McBtsSNService.class);
		return service.querySNFromDB(moId);
	}

}
