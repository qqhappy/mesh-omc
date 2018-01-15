/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.io.FileUtils;

import com.xinwei.minas.core.facade.secu.MinasServerVersionFacade;
import com.xinwei.minas.core.model.secu.MinasServerVersion;
import com.xinwei.minas.server.core.secu.service.MinasServerVersionService;
import com.xinwei.minas.server.mcbts.service.oamManage.BtsRfPanelStatusService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 获取网管平台服务器版本模型的门面类
 * 
 * 
 * @author fangping
 * 
 */

public class MinasServerVersionFacadeImpl extends UnicastRemoteObject implements
		MinasServerVersionFacade {
	private static final long serialVersionUID = -3998091207270193180L;
	private MinasServerVersionService service;

	protected MinasServerVersionFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(MinasServerVersionService.class);
	}

	@Override
	public MinasServerVersion getMinasServerVersion() throws RemoteException,
			Exception {
		return service.getMinasServerVersion();
	}

}
