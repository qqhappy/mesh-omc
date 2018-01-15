/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-5	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalDetectiveFacade;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalDetectiveService;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalRestartService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author zhuxiaozhan
 * 
 */
public class TerminalDetectiveFacadeImpl extends UnicastRemoteObject implements
		TerminalDetectiveFacade {

	private TerminalDetectiveService utDetectiveService;

	/**
	 * @throws RemoteException
	 */
	public TerminalDetectiveFacadeImpl() throws RemoteException {
		super();
		utDetectiveService = AppContext.getCtx().getBean(
				TerminalDetectiveService.class);
	}

	@Override
	public UserTerminal detectiveQuery(OperObject operObject, Long moId, String eid)
			throws RemoteException, Exception {
		return utDetectiveService.detectiveQuery(moId, eid);
	}

}
