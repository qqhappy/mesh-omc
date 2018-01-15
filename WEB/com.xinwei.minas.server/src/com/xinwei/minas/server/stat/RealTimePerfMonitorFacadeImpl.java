/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.stat.service.RealTimePerfMonitorService;
import com.xinwei.minas.stat.core.facade.RealTimePerfMonitorFacade;
import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * 实时性能监视服务器门面实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class RealTimePerfMonitorFacadeImpl extends UnicastRemoteObject
		implements RealTimePerfMonitorFacade {

	protected RealTimePerfMonitorFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void start(String sessionId, MonitorItem item)
			throws RemoteException, Exception {
		getService().start(sessionId, item);
	}

	@Override
	public void stop(String sessionId, MonitorItem item)
			throws RemoteException, Exception {
		getService().stop(sessionId, item);
	}

	@Override
	public void handshake(String sessionId, MonitorItem item)
			throws RemoteException, Exception {
		getService().handshake(sessionId, item);
	}

	private RealTimePerfMonitorService getService() {
		return AppContext.getCtx().getBean(RealTimePerfMonitorService.class);
	}

}
