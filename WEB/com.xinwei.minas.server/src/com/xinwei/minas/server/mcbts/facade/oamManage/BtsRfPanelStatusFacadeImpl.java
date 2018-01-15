/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.oamManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer2.ResManagementFacade;
import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsRfPanelStatusFacade;
import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsStateQueryFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.mcbts.service.oamManage.BtsRfPanelStatusService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsStateQueryService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author fangping
 * 
 */
@SuppressWarnings("serial")
public class BtsRfPanelStatusFacadeImpl extends UnicastRemoteObject implements  McBtsRfPanelStatusFacade {

	private BtsRfPanelStatusService service;

	protected BtsRfPanelStatusFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(BtsRfPanelStatusService.class);
	}
	/**
	 * 从基站查信息
	 */
	@Override  
	public McBtsRfPanelStatus queryInfoFromNE(long moId) throws RemoteException,
			Exception {
		return service.queryInfoFromNE(moId);
	}

	@Override
	public TConfResmanagement queryFromEMS(Long moId) throws RemoteException,
			Exception {
		
		return null;
	}

	@Override
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception {
		
		return null;
	}
}
