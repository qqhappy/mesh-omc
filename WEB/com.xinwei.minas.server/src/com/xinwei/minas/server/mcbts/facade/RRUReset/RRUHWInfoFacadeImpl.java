/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.RRUReset;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.mcbts.core.facade.rruManage.RRUHWInfoFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.service.rruManage.RRUHWInfoService;

/**
 * RRUӲ����Ϣ��ѯ����ʵ����
 * 
 * @author chenshaohua
 * 
 */

// TODO:����
public class RRUHWInfoFacadeImpl extends UnicastRemoteObject implements
		RRUHWInfoFacade {

	private RRUHWInfoService rruHWInfoService;

	protected RRUHWInfoFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public McBtsSN queryHWInfoFromNE(Long moId) throws RemoteException,
			Exception {
		return rruHWInfoService.queryHWInfoFromNE(moId);
	}

	public RRUHWInfoService getRruHWInfoService() {
		return rruHWInfoService;
	}

	public void setRruHWInfoService(RRUHWInfoService rruHWInfoService) {
		this.rruHWInfoService = rruHWInfoService;
	}

}
