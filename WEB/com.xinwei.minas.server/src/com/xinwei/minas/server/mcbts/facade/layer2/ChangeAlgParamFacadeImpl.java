/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.ChangeAlgParamFacade;
import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.server.mcbts.service.layer2.ChangeAlgParamService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class ChangeAlgParamFacadeImpl extends UnicastRemoteObject implements
		ChangeAlgParamFacade {

	private ChangeAlgParamService service;
	private SequenceService sequenceService;

	public ChangeAlgParamFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(ChangeAlgParamService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public void config(OperObject operObject, TConfChangeAlgParam changeAlgParam)
			throws RemoteException, Exception {
		Long idx = sequenceService.getNext();
		if (changeAlgParam.getIdx() == null) {
			changeAlgParam.setIdx(idx);
		}
		service.config(changeAlgParam);
	}

	@Override
	public TConfChangeAlgParam queryByMoId(Long moId) throws RemoteException,
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
	public TConfChangeAlgParam queryFromEMS(Long moId) throws RemoteException,
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
	public TConfChangeAlgParam queryFromNE(Long moId) throws RemoteException,
			Exception {
		return service.query(moId);

	}

	public String getRestrictAreaFlag() throws RemoteException, Exception {
		return service.getRestrictAreaFlag();
	}

}
