/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsConfigFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsConfig;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站配置导入导出的门面类
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsConfigFacadeImpl extends UnicastRemoteObject implements
		McBtsConfigFacade {

	private McBtsConfigService mcBtsConfigService;

	public McBtsConfigFacadeImpl() throws RemoteException {
		super();
		mcBtsConfigService = AppContext.getCtx().getBean(
				McBtsConfigService.class);
	}

	@Override
	public String[] export() throws RemoteException, Exception {
		return mcBtsConfigService.export();
	}

	@Override
	public McBtsConfig getMcBtsConfig(boolean withData) throws RemoteException,
			Exception {
		return mcBtsConfigService.getMcBtsConfig(withData);
	}

	@Override
	public void importScript(String str) throws RemoteException, Exception {
		mcBtsConfigService.importScript(str);
	}

	@Override
	public void importMcBtsConfig(List<String> idList, McBtsConfig mcBtsConfig)
			throws RemoteException, Exception {
		mcBtsConfigService.importMcBtsConfig(idList, mcBtsConfig);

	}

}
