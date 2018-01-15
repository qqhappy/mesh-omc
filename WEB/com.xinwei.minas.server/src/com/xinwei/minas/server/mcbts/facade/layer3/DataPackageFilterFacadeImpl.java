package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.DataPackageFilterFacade;
import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.server.mcbts.service.layer3.DataPackageFilterService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * DataPackageFilterFacadeImpl实现方法
 * 
 * @author fangping
 * 
 */
public class DataPackageFilterFacadeImpl extends UnicastRemoteObject implements
		DataPackageFilterFacade {
	private DataPackageFilterService service;

	protected DataPackageFilterFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(DataPackageFilterService.class);
	}

	@Override
	public int queryFilterType() throws RemoteException, Exception {
		return service.queryFilterType();
	}

	@Override
	public List<DataPackageFilter> queryAllFormEMS() throws RemoteException,
			Exception {
		return service.queryAllFromEMS();
	}

	@Override
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception {
		return service.queryFromNE(moId);
	}

	// forDB
	@Override
	public void config(OperObject operObject, int filterType, List<DataPackageFilter> filterList)
			throws RemoteException, Exception {
		service.config(filterType, filterList);

	}

	// forNE
	@Override
	public void config(OperObject operObject, Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception {
		service.config(moId, filterType, filterList);
	}
}
