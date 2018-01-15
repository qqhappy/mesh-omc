/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbFullTableConfigFacade;
import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableConfigFacadeImpl extends UnicastRemoteObject implements
		EnbFullTableConfigFacade {

	private EnbFullTableConfigService service;

	protected EnbFullTableConfigFacadeImpl() throws RemoteException {
		super();
	}

	public void setService(EnbFullTableConfigService service) {
		this.service = service;
	}

	@Override
	public void config(OperObject operObject, Long moId) throws Exception {
		service.config(moId);
	}

	@Override
	public void delete(FullTableConfigInfo data) throws Exception {
		service.delete(data);
	}

	@Override
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public List<FullTableConfigInfo> queryAll() throws Exception {
		return service.queryAll();
	}

	@Override
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception {
		return service.queryByStatus(status);
	}

}
