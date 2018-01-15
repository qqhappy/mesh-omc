/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationTypeFacade;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationTypeService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 校准类型配置基本业务处理
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationTypeFacadeImpl extends UnicastRemoteObject implements
		CalibrationTypeFacade {

	private CalibrationTypeService service;
	private SequenceService sequenceService;

	public CalibrationTypeFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(CalibrationTypeService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

    @Override
	public CalibrationType queryByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, CalibrationType type) throws Exception {
		if (type.getIdx() == null) {
			type.setIdx(sequenceService.getNext());
		}
		service.config(type);
	}

	@Override
	public CalibrationType queryFromEMS(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}


	@Override
	public CalibrationType queryFromNE(Long moId) throws RemoteException,
			Exception {
		return service.queryFromNE(moId);
	}

}
