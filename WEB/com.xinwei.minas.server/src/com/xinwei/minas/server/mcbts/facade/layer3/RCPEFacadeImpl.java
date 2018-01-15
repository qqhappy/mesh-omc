package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.RCPEFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.server.mcbts.service.layer3.RCPEService;
import com.xinwei.minas.server.platform.AppContext;
/**
 * RCPE基本业务门面实现
 * @author yinbinqiang
 *
 */
public class RCPEFacadeImpl extends UnicastRemoteObject implements RCPEFacade {

	private RCPEService service;
	private SequenceService sequenceService;
	
	protected RCPEFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(RCPEService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfRCPE queryByMoId(Long moId) throws RemoteException, Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfRCPE rcpe) throws RemoteException, Exception {
		Long idx = sequenceService.getNext();
		if(rcpe.getIdx() == null) {
			rcpe.setIdx(idx);
		}
		service.config(rcpe);
	}
	
	/**
	 * 删除指定RCPE信息
	 * @param rcpeItem
	 * @throws Exception
	 */
	@Override
	public void deleteRcpe(OperObject operObject, TConfRCPEItem rcpeItem) throws Exception {
		service.deleteRcpe(rcpeItem);
	}
}
