/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-18	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.sxc.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.sxc.service.SxcBasicService;
import com.xinwei.minas.sxc.core.facade.SxcBasicFacade;
import com.xinwei.minas.sxc.core.model.SxcBasic;

/**
 * 
 * SXC基本信息服务 
 * 
 * @author chenjunhua
 * 
 */

public class SxcBasicFacadeImpl extends UnicastRemoteObject implements SxcBasicFacade {

	private SxcBasicService service;
	
	protected SxcBasicFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(SxcBasicService.class);
	}

	@Override
	public List<SxcBasic> queryAll() throws Exception {
		return service.queryAll();
	}

	@Override
	public Long addSxc(SxcBasic sxcBasic) throws Exception {
		return service.addSxc(sxcBasic);
	}

	@Override
	public void modifySxc(SxcBasic oldSxc, SxcBasic newSxc) throws Exception {
		service.modifySxc(oldSxc, newSxc);
	}

	@Override
	public void deleteSxc(SxcBasic sxcBasic) throws Exception {
		service.deleteSxc(sxcBasic);
	}

}
