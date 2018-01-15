/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.McBtsTypeDDFacade;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.service.McBtsTypeDDService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站类型数据字典门面实现
 * 
 * @author chenjunhua
 * 
 */

public class McBtsTypeDDFacadeImpl extends UnicastRemoteObject implements
		McBtsTypeDDFacade {

	private McBtsTypeDDService service;

	protected McBtsTypeDDFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsTypeDDService.class);
	}

	@Override
	public List<McBtsTypeDD> queryAll() throws RemoteException {
		return service.queryAll();
	}

	@Override
	public McBtsTypeDD queryByTypeId(int mcBtsType) throws RemoteException {
		return service.queryByTypeId(mcBtsType);
	}

}
