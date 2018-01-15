/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.enb.core.facade.EnbRealtimeMonitorFacade;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeMonitorService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB实时性能监控服务器门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbRealtimeMonitorFacadeImpl extends UnicastRemoteObject implements
		EnbRealtimeMonitorFacade {

	protected EnbRealtimeMonitorFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception {
		return getService().queryItemConfig();
	}

	@Override
	public void startMonitor(String sessionId, long moId) throws Exception {
		getService().startMonitor(sessionId, moId);
	}

	@Override
	public void stopMonitor(String sessionId, long moId) throws Exception {
		getService().stopMonitor(sessionId, moId);
	}

	private EnbRealtimeMonitorService getService() {
		return OmpAppContext.getCtx().getBean(EnbRealtimeMonitorService.class);
	}

}
