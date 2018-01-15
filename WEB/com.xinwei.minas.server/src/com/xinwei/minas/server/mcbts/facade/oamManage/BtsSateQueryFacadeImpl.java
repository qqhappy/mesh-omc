/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.oamManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer2.ResManagementFacade;
import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsStateQueryFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsStateQueryService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author fangping
 * 
 */
@SuppressWarnings("serial")
public class BtsSateQueryFacadeImpl extends UnicastRemoteObject implements
		McBtsStateQueryFacade {

	private McBtsStateQueryService service;

	protected BtsSateQueryFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsStateQueryService.class);
	}

	// public McBtsStateQueryService getService() {
	// return service;
	// }
	//
	// public void setService(McBtsStateQueryService service) {
	// this.service = service;
	// }

	@Override
	public TConfResmanagement queryFromEMS(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	@Override
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	@Override
	public void config(TConfResmanagement resManagement)
			throws RemoteException, Exception {

	}

	@Override
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	@Override
	public List<McBtsSateQuery> queryInfoFromDB(long moId)
			throws RemoteException, Exception {
		return service.queryInfoFromDB(moId);
	}

	@Override
	public McBtsSateQuery queryInfoFromNE(long moId) throws RemoteException,
			Exception {
		return service.queryInfoFromNE(moId);
	}

}
