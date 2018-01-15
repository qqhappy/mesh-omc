/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-30	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.enb.core.facade.EnbFullTableConfigFacade;
import com.xinwei.minas.enb.core.facade.EnbFullTableReverseFacade;
import com.xinwei.minas.server.enb.service.EnbFullTableReverseService;

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

public class EnbFullTableReverseFacadeImpl extends UnicastRemoteObject implements EnbFullTableReverseFacade {

	private EnbFullTableReverseService service;
	
	protected EnbFullTableReverseFacadeImpl() throws RemoteException {
		super();
		
	}

	@Override
	public void config(Long moId) throws Exception {
		service.config(moId);
	}

	public void setService(EnbFullTableReverseService service) {
		this.service = service;
	}

}
