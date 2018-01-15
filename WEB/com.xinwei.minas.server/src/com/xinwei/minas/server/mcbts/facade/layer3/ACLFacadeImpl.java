/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.ACLFacade;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.server.mcbts.service.layer3.ACLService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class ACLFacadeImpl extends UnicastRemoteObject implements ACLFacade {

	private ACLService service;
	private SequenceService sequenceService;

	protected ACLFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(ACLService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}
	
	@Override
	public List<McBtsACL> queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, Long moId, List<McBtsACL> mcBtsACLList)
			throws RemoteException, Exception {

		if (mcBtsACLList != null) {
			for (McBtsACL obj : mcBtsACLList) {
				if (obj != null && obj.getIdx() == null) {
					obj.setIdx(sequenceService.getNext());
				}
			}
		}
		service.config(moId, mcBtsACLList);
	}

	@Override
	public void delete(McBtsACL temp) throws RemoteException, Exception {
		service.delete(temp);
	}

}
