/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.ut.service.UTPrefDataService;
import com.xinwei.minas.ut.core.facade.UTPrefDataFacade;
import com.xinwei.minas.ut.core.model.UTPerfData;

/**
 * 
 * 查看终端性能数据门面
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTPrefDataFacadeImpl extends UnicastRemoteObject implements UTPrefDataFacade {

	private UTPrefDataService service;
	
	protected UTPrefDataFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(UTPrefDataService.class);
	}

	@Override
	public UTPerfData query(OperObject operObject, Long moId, Long pid) throws Exception {
		return service.query(moId, pid);
	}

}
