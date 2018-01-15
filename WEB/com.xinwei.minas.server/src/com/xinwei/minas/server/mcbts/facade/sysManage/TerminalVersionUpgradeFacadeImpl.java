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
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalVersionUpgradeFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionForceUpdateService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * @author zhuxiaozhan
 * 
 */
public class TerminalVersionUpgradeFacadeImpl extends UnicastRemoteObject
		implements TerminalVersionUpgradeFacade {

	private TerminalVersionForceUpdateService tUpgradeService;

	protected TerminalVersionUpgradeFacadeImpl() throws RemoteException {
		super();
		tUpgradeService = AppContext.getCtx().getBean(
				TerminalVersionForceUpdateService.class);
	}

	@Override
	public void upgradeConfig(OperObject operObject, Long moId, String eid, TerminalVersion tv) throws Exception{
		tUpgradeService.upgradeConfig(moId, eid, tv);
	}


	@Override
	public Map<String, String> getUTProgress(List<UserTerminal> utList) {
		return tUpgradeService.getUTProgress(utList);
	}

	@Override
	public void bootloaderUpgrade(OperObject operObject, Long moId, String eid, TerminalVersion tv)
			throws RemoteException, Exception {
		tUpgradeService.bootloaderUpgrade(moId, eid, tv);
	}

}
