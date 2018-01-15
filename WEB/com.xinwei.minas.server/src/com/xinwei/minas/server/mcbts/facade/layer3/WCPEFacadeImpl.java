package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.WCPEFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.mcbts.service.layer3.WCPEService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * WCPE基本业务门面实现
 * 
 * @author yinbinqiang
 * 
 */
public class WCPEFacadeImpl extends UnicastRemoteObject implements WCPEFacade {

	private WCPEService service;
	private SequenceService sequenceService;

	protected WCPEFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(WCPEService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfWCPE queryByMoId(Long moId) throws RemoteException, Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfWCPE wcpe) throws RemoteException, Exception {
		Long idx = sequenceService.getNext();
		if (wcpe.getIdx() == null) {
			wcpe.setIdx(idx);
		}
		service.config(wcpe);
	}

	@Override
	public TConfWCPE query(Long moId) throws RemoteException, Exception {
		return service.query(moId);
	}

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public TConfWCPE queryFromEMS(Long moId) throws RemoteException, Exception {
		return service.queryByMoId(moId);
	}

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public TConfWCPE queryFromNE(Long moId) throws RemoteException, Exception {
		return service.query(moId);
	}

}
