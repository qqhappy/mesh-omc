/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.oamManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer2.ResManagementFacade;
import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsUserCountsQueryFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsUserCountsQueryService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author fangping
 * 
 */
@SuppressWarnings("serial")
public class BtsUserCountsQueryFacadeImpl extends UnicastRemoteObject implements
		McBtsUserCountsQueryFacade {

	private McBtsUserCountsQueryService service;

	protected BtsUserCountsQueryFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsUserCountsQueryService.class);
	}

	// public McBtsUserCountsQueryService getService() {
	// return service;
	// }
	//
	// public void setService(McBtsUserCountsQueryService service) {
	// this.service = service;
	// }

	public TConfResmanagement queryFromEMS(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	public void config(TConfResmanagement resManagement)
			throws RemoteException, Exception {

	}

	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception {

		return null;
	}

	@Override
	public List<McBtsUserCountsQuery> queryInfoFromDB(long moId)
			throws RemoteException, Exception {
		// 
		return null;
	}

	@Override
	public McBtsUserCountsQuery queryInfoFromNE(long moId)
			throws RemoteException, Exception {
		// 
		return service.queryInfoFromNE(moId);
	}
}
