package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.LoadBalanceFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.mcbts.service.layer3.LoadBalanceService;
import com.xinwei.minas.server.platform.AppContext;
/**
 * 负载均衡基本业务门面实现
 * @author yinbinqiang
 *
 */
public class LoadBalanceFacadeImpl  extends UnicastRemoteObject implements LoadBalanceFacade {

	private LoadBalanceService service;
	private SequenceService sequenceService;
	
	protected LoadBalanceFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(LoadBalanceService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfLoadBalance queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfLoadBalance loadBalance) throws RemoteException,
			Exception {
		Long idx = sequenceService.getNext();
		if(loadBalance.getIdx() == null) {
			loadBalance.setIdx(idx);
		}
		service.config(loadBalance);
	}

}
