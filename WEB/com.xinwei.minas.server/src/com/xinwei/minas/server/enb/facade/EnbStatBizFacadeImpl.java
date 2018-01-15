/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.enb.core.facade.EnbStatBizFacade;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.server.enb.xstat.service.EnbStatBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;

/**
 * 
 * 话务统计业务服务器门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatBizFacadeImpl extends UnicastRemoteObject implements
		EnbStatBizFacade {

	protected EnbStatBizFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public List<CounterItemConfig> queryCounterConfigs()
			throws RemoteException, Exception {
		return getService().queryCounterConfigs();
	}

	@Override
	public List<KpiItemConfig> queryKpiConfigs() throws RemoteException,
			Exception {
		return getService().queryKpiConfigs();
	}

	@Override
	public List<PreStatItem> queryStatData(StatDataQueryCondition condition)
			throws RemoteException, Exception {
		return getService().queryStatData(condition);
	}

	private EnbStatBizService getService() {
		return AppContext.getCtx().getBean(EnbStatBizService.class);
	}

}
