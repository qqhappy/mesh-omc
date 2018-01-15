package com.xinwei.minas.server.mcbts.facade.oamManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.oamManage.McBtsOnlineTerminalListFacade;
import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsOnlineTerminalListService;
import com.xinwei.minas.server.platform.AppContext;
/**
 * BTS在线终端列表_门面实现
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalListFacadeImpl extends UnicastRemoteObject implements
McBtsOnlineTerminalListFacade{

	private McBtsOnlineTerminalListService service;

	public McBtsOnlineTerminalListFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsOnlineTerminalListService.class);
	}

	@Override
	public List<ActiveUserInfo> queryOnlineTerminalListFromNE(Long moId)
			throws RemoteException, Exception {
		return service.queryOnlineTerminalListFromNE(moId);
	}

	@Override
	public List<ActiveUserInfo> queryOnlineTerminalListFromDB(long moId)
			throws RemoteException, Exception {
		return null;
	}

}
