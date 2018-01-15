/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbDynamicInfoFacade;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.server.enb.service.EnbStatusService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNodeB动态信息门面实现
 * 
 * @author chenjunhua
 * 
 */

public class EnbDynamicInfoFacadeImpl extends UnicastRemoteObject implements
		EnbDynamicInfoFacade {

	protected EnbDynamicInfoFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception {
		EnbStatusService service = getService();
		return service.queryEnbDynamicInfo(condition);
	}

	private EnbStatusService getService() {
		return OmpAppContext.getCtx().getBean(EnbStatusService.class);
	}

	@Override
	public void changeEnbTime(OperObject operObject, long moId, Long enbTime) throws Exception {
		getService().configEnbTime(moId, enbTime);
	}

	@Override
	public void configRfSwitch(OperObject operObject, long moId, int rackNo,
			int shelfNo, int slotNo, Integer rfSwitch) throws Exception {
		getService().configRfSwitch(moId, rackNo, shelfNo, slotNo, rfSwitch);
		
	}

}
