/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.common.CommonChannelFacade;
import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;
import com.xinwei.minas.server.mcbts.service.common.CommonChannelService;

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

public class CommonChannelFacadeImpl extends UnicastRemoteObject implements CommonChannelFacade {

	private CommonChannelService commonChannelService;
	
	protected CommonChannelFacadeImpl() throws RemoteException {
		super();
	}

	public void setCommonChannelService(CommonChannelService commonChannelService) {
		this.commonChannelService = commonChannelService;
	}
	
	@Override
	public void saveCommonChannelSynInfo(List<CommonChannelSynInfo> synInfos) throws Exception {
		commonChannelService.saveCommonChannelSynInfos(synInfos);
	}

}
