/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.SimulcastManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 同播资源门面类
 * 
 * 
 * @author tiance
 * 
 */

public class SimulcastManageFacadeImpl extends UnicastRemoteObject implements
		SimulcastManageFacade {
	SimulcastManageService simulcastManageService;

	protected SimulcastManageFacadeImpl() throws RemoteException {
		simulcastManageService = AppContext.getCtx().getBean(
				SimulcastManageService.class);
	}

	@Override
	public List<Simulcast> queryAll() throws RemoteException, Exception {
		return simulcastManageService.queryAll();
	}

	@Override
	public List<Simulcast> queryByDistrictId(long districtId)
			throws RemoteException, Exception {
		return simulcastManageService.queryByDistrictId(districtId);
	}

	@Override
	public void saveOrUpdate(OperObject operObject, Simulcast simulcast) throws RemoteException,
			Exception {
		simulcastManageService.saveOrUpdate(simulcast);
	}

	@Override
	public void delete(OperObject operObject, Simulcast simulcast) throws RemoteException, Exception {
		simulcastManageService.delete(simulcast);

	}

	@Override
	public boolean[] querySyncStatus() throws RemoteException, Exception {
		return simulcastManageService.querySyncStatus();
	}

	public void sync(OperObject operObject, boolean[] toSync) throws RemoteException, Exception {
		simulcastManageService.sync(toSync);
	}

}
