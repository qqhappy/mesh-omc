package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.RemoteBtsFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.service.layer3.RemoteBtsService;
import com.xinwei.minas.server.platform.AppContext;

@SuppressWarnings("serial")
public class RemoteBtsFacadeImpl extends UnicastRemoteObject implements
		RemoteBtsFacade {

	private RemoteBtsService service;
	private SequenceService sequenceService;

	protected RemoteBtsFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(RemoteBtsService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfRemoteBts queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfRemoteBts remoteBts) throws RemoteException,
			Exception {
		Long idx = sequenceService.getNext();
		if (remoteBts.getIdx() == null) {
			remoteBts.setIdx(idx);
		}
		service.config(remoteBts, false);
	}

	@Override
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws RemoteException, Exception {
		Long idx = sequenceService.getNext();
		if (remoteBts.getIdx() == null) {
			remoteBts.setIdx(idx);
		}
		service.config(remoteBts, isSyncConfig);
	}

}
