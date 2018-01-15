/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer2.AntijammingParamFacade;
import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.minas.server.mcbts.service.layer2.AntijammingParamService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 *
 */
public class AntijammingParamFacadeImpl extends UnicastRemoteObject implements
		AntijammingParamFacade {

	private AntijammingParamService service;
	private SequenceService sequenceService;
	
	public AntijammingParamFacadeImpl() throws RemoteException{
		super();
		service = AppContext.getCtx().getBean(AntijammingParamService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}
	/* (non-Javadoc)
	 * @see com.xinwei.minas.mcbts.core.facade.layer2.AntijammingParamFacade#config(com.xinwei.minas.mcbts.core.model.layer2.TConfAntijamming)
	 */
	public void config(TConfAntijammingParam antijamming) throws RemoteException,
			Exception {
		// TODO Auto-generated method stub
		Long idx = sequenceService.getNext();
		if(antijamming.getIdx() == null){
			antijamming.setIdx(idx);
		}
		service.config(antijamming);
	}

	/* (non-Javadoc)
	 * @see com.xinwei.minas.mcbts.core.facade.layer2.AntijammingParamFacade#queryByMoId(java.lang.Long)
	 */
	public TConfAntijammingParam queryByMoId(Long moId) throws RemoteException,
			Exception {
		// TODO Auto-generated method stub
		return service.queryByMoId(moId);
	}

	public void config(TConfFreqSet freqSetEntity) throws RemoteException, Exception{
		Long idx = sequenceService.getNext();
		if(freqSetEntity.getIdx() == null){
			freqSetEntity.setIdx(idx);
		}
		service.config(freqSetEntity);
	}
}
