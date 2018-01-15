/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.common.GPSDataFacade;
import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.mcbts.service.common.GPSDataService;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * GPS管理消息门面类
 * 
 * 
 * @author tiance
 * 
 */

public class GPSDataFacadeImpl extends UnicastRemoteObject implements
		GPSDataFacade {

	private GPSDataService service;
	private SequenceService sequenceService;

	protected GPSDataFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(GPSDataService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public GPSData queryFromEMS(Long moId) throws RemoteException, Exception {
		return service.queryFromEMS(moId);
	}

	@Override
	public GPSData queryFromNE(Long moId) throws RemoteException, Exception {
		return service.queryFromNE(moId);
	}

	@Override
	public void config(OperObject operObject, GPSData data) throws RemoteException, Exception {
		if (data.getIdx() == null) {
			data.setIdx(sequenceService.getNext());
		}

		service.config(data);

	}

}
