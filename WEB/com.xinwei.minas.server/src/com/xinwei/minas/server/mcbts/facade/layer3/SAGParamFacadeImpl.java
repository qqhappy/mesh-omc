package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.SAGParamFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.SAGParamService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 备份SAG参数配置基本业务信息门面实现
 * 
 * @author yinbinqiang
 * 
 */
public class SAGParamFacadeImpl extends UnicastRemoteObject implements
		SAGParamFacade {

	private SAGParamService sagParamService;
	private SequenceService sequenceService;

	public SAGParamFacadeImpl() throws RemoteException {
		super();
		sagParamService = AppContext.getCtx().getBean(SAGParamService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public List getAllSagInfo() throws RemoteException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllLocationArea() throws RemoteException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TConfBackupSag queryByMoId(Long moId) throws RemoteException,
			Exception {
		return sagParamService.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfBackupSag backupSag) throws RemoteException,
			Exception {
		if (backupSag.getIdx() == null) {
			backupSag.setIdx(sequenceService.getNext());
		}
		sagParamService.config(backupSag);
	}

	@Override
	public McBts getBTSInfo(Long moId) throws RemoteException, Exception {
		return McBtsCache.getInstance().queryByMoId(moId);
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
	public TConfBackupSag queryFromEMS(Long moId) throws RemoteException,
			Exception {
		return sagParamService.queryByMoId(moId);
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
	public TConfBackupSag queryFromNE(Long moId) throws RemoteException,
			Exception {
		return sagParamService.queryFromNE(moId);
	}

}
