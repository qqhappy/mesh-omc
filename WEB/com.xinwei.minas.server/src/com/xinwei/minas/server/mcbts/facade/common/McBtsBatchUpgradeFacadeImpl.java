/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.common.McBtsBatchUpgradeFacade;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.server.mcbts.service.common.McBtsBatchUpgradeService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站批量升级门面的类
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeFacadeImpl extends UnicastRemoteObject implements
		McBtsBatchUpgradeFacade {

	private McBtsBatchUpgradeService service;

	public McBtsBatchUpgradeFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsBatchUpgradeService.class);
	}

	@Override
	public void addTask(OperObject operObject, List<UpgradeInfo> list)
			throws RemoteException, Exception {
		service.addTask(list);
	}

	@Override
	public Map<Integer, Map<Long, UpgradeInfo>> queryAll()
			throws RemoteException, Exception {
		return service.queryAll();
	}

	@Override
	public Map<Integer, List<UpgradeInfo>> queryAll2() throws RemoteException,
			Exception {
		return service.queryAll2();
	}

	@Override
	public List<UpgradeInfo> queryUpgrading() throws RemoteException, Exception {
		return service.queryUpgrading();
	}

	@Override
	public void terminate(OperObject operObject, List<UpgradeInfo> list)
			throws RemoteException, Exception {
		service.terminate(list);
	}

	@Override
	public void archive(OperObject operObject) throws RemoteException,
			Exception {
		service.archive();
	}

	@Override
	public Map<Integer, List<UpgradeInfoArchive>> queryLatestArchive()
			throws RemoteException, Exception {
		return service.queryLatestArchive();
	}

	@Override
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId)
			throws RemoteException, Exception {
		return service.queryArchiveByMoId(moId);
	}

	@Override
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version)
			throws RemoteException, Exception {
		return service.queryUpgradingInfoByVersion(version);
	}

}
