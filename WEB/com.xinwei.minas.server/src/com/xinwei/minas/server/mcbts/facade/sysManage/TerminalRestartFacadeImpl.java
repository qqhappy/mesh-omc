/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalRestartFacade;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalRestartService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * ÷’∂À÷ÿ∆Ù√≈√Ê
 * @author zhuxiaozhan
 *
 */
public class TerminalRestartFacadeImpl extends UnicastRemoteObject implements TerminalRestartFacade {


	private TerminalRestartService utRestartService;
	
	public TerminalRestartFacadeImpl() throws RemoteException {
		super();
		utRestartService = AppContext.getCtx().getBean(TerminalRestartService.class);
	}

	@Override
	public void restartConfig(OperObject operObject, Long moId, String eid) throws Exception {
		utRestartService.restartConfig(moId, eid);
	}

}
