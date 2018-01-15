/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationResultFacade;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationResultService;
import com.xinwei.minas.server.mcbts.service.layer1.L1GeneralSettingService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 校准结果门面类
 * 
 * @author zhaolingling
 * 
 */

public class CalibrationResultFacadeImpl extends UnicastRemoteObject implements
		CalibrationResultFacade {

	private CalibrationResultService service;

	protected CalibrationResultFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(CalibrationResultService.class);
	}

	@Override
	public CalibrationResult queryFromEMS(Long moId) throws RemoteException,
			Exception {
		return null;
	}

	@Override
	public CalibrationResult queryFromNE(Long moId) throws RemoteException,
			Exception {
		return null;
	}

	@Override
	public Object[] queryByMoId(Long moId) throws RemoteException, Exception {
		return service.queryByMoId(moId);
	}

}
