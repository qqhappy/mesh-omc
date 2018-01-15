/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.RepeaterFacade;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.server.mcbts.service.layer3.RepeaterService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
@SuppressWarnings("serial")
public class RepeaterFacadeImpl extends UnicastRemoteObject implements
		RepeaterFacade {

	private RepeaterService service;
	private SequenceService sequenceService;

	protected RepeaterFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(RepeaterService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public List<McBtsRepeater> queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, long moId,
			List<McBtsRepeater> mcBtsRepeaterList) throws RemoteException,
			Exception {
		for (McBtsRepeater obj : mcBtsRepeaterList) {
			if (obj.getIdx() == null) {
				obj.setIdx(sequenceService.getNext());
			}
		}
		service.config(moId, mcBtsRepeaterList);
	}

	@Override
	public void delete(McBtsRepeater temp) throws RemoteException, Exception {
		service.delete(temp);
	}

}
