/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbAlarmFacade;
import com.xinwei.minas.server.enb.service.EnbAlarmService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * eNB告警门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbAlarmFacadeImpl extends UnicastRemoteObject implements
		EnbAlarmFacade {

	protected EnbAlarmFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void syncAlarm(OperObject operObject, long moId)
			throws RemoteException, Exception {
		getService().syncAlarm(moId);
	}

	private EnbAlarmService getService() {
		return AppContext.getCtx().getBean(EnbAlarmService.class);
	}

}
