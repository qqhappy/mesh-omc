package com.xinwei.minas.server.mcbts.facade.layer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.SDMAConfigFacade;
import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.mcbts.service.layer2.SDMAConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * SDMAConfig基本业务门面实现
 * 
 * @author fangping
 * 
 */
public class SDMAConfigFacadeImpl extends UnicastRemoteObject implements
		SDMAConfigFacade {

	private SDMAConfigService service;
	private SequenceService sequenceService;

	protected SDMAConfigFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(SDMAConfigService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public SDMAConfig queryByMoId(Long moId) throws RemoteException, Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, SDMAConfig sdmaConfig) throws RemoteException, Exception {
		if (sdmaConfig.getIdx() == null) {
			sdmaConfig.setIdx(sequenceService.getNext());
		}
		service.config(sdmaConfig);
	}

}
