/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.ResManagementFacade;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class ResManagementFacadeImpl extends UnicastRemoteObject implements
		ResManagementFacade {

	private ResManagementService service;
	private SequenceService sequenceService;

	protected ResManagementFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(ResManagementService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public void config(OperObject operObject, TConfResmanagement resManagement)
			throws RemoteException, Exception {
		Long idx = sequenceService.getNext();
		if (resManagement.getIdx() == null) {
			resManagement.setIdx(idx);
		}
		service.config(resManagement);
	}

	@Override
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
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
	public TConfResmanagement queryFromEMS(Long moId) throws RemoteException,
			Exception {
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
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception {
		return service.queryFromNE(moId);
	}

}
