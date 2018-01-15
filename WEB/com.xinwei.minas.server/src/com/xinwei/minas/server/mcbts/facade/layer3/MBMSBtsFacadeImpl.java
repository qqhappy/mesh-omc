package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.MBMSBtsFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.platform.AppContext;

public class MBMSBtsFacadeImpl extends UnicastRemoteObject implements MBMSBtsFacade {

	private MBMSBtsService service;
	private SequenceService sequenceService;
	
	protected MBMSBtsFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(MBMSBtsService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfMBMSBts queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfMBMSBts memsBts) throws RemoteException,
			Exception {
		Long idx = sequenceService.getNext();
		if(memsBts.getIdx() == null) {
			memsBts.setIdx(idx);
		}
		service.config(memsBts);
	}
}
