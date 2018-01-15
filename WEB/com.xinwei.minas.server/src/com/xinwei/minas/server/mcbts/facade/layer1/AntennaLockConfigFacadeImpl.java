/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.facade.layer1.AntennaLockConfigFacade;
import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.mcbts.service.layer1.AntennaLockConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 天线闭锁配置门面实现类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class AntennaLockConfigFacadeImpl extends UnicastRemoteObject implements
		AntennaLockConfigFacade {

	private AntennaLockConfigService service;
	private SequenceService sequenceService;

	protected AntennaLockConfigFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(AntennaLockConfigService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public McBtsAntennaLock queryByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(McBtsAntennaLock lockConfig) throws Exception {
		if (lockConfig != null) {
			if (lockConfig.getIdx() == null) {
				lockConfig.setIdx(sequenceService.getNext());
			}
		}
		service.config(lockConfig);
	}
}
