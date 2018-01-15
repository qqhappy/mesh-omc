/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.layer3.TerminalParamFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.server.mcbts.service.layer3.TerminalParamService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class TerminalParamFacadeImpl extends UnicastRemoteObject implements
		TerminalParamFacade {

	private TerminalParamService service;

	protected TerminalParamFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(TerminalParamService.class);
	}

	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception {
		return service.queryAllTerminalParam();
	}

	public void config(List<TerminalBizParam> terminalBizParamList)
			throws RemoteException, Exception {
		service.config(terminalBizParamList);
	}

	public TerminalParamService getService() {
		return service;
	}

	public void setService(TerminalParamService service) {
		this.service = service;
	}

}
