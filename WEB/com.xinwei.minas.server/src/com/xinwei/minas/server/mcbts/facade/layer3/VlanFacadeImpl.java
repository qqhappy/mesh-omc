/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.VlanFacade;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.server.mcbts.service.layer3.VlanService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class VlanFacadeImpl extends UnicastRemoteObject implements VlanFacade {

	private VlanService service;

	private SequenceService sequenceService;

	/**
	 * @throws RemoteException
	 */
	public VlanFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(VlanService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public List<McBtsVlan> queryAllByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryAllByMoId(moId);
	}

	public void config(OperObject operObject, List<McBtsVlan> mcBtsVlanList, Long moId)
			throws RemoteException, Exception {
		for (McBtsVlan o : mcBtsVlanList) {
			if (o.getIdx() == null) {
				o.setIdx(sequenceService.getNext());
			}
		}
		// if (attach.getIdx() == null) {
		// attach.setIdx(sequenceService.getNext());
		// }
		service.config(mcBtsVlanList, moId);
	}

	@Override
	public McBtsVlanAttach queryAttacheByMoId(long moId)
			throws RemoteException, Exception {
		return service.queryAttachByMoId(moId);
	}

}
