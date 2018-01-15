/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.TosConfFacade;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.server.mcbts.service.layer3.TosConfService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */

@SuppressWarnings("serial")
public class TosConfFacadeImpl extends UnicastRemoteObject implements
		TosConfFacade {

	private TosConfService service;

	protected TosConfFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(TosConfService.class);
	}

	@Override
	public List<McBtsTos> queryAllTos() throws RemoteException, Exception {
		return service.queryAllTos();
	}

	@Override
	public void config(OperObject operObject, List<McBtsTos> mcBtsTosList) throws RemoteException,
			Exception {
		service.config(mcBtsTosList);
	}

}
