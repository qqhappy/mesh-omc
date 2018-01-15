/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbStatusFacade;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.server.enb.service.EnbStatusService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB状态信息门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatusFacadeImpl extends UnicastRemoteObject implements
		EnbStatusFacade {

	protected EnbStatusFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public List<Object> queryStatus(long moId, EnbStatusQueryCondition condition)
			throws Exception {
		return getService().queryStatus(moId, condition);
	}

	@Override
	public void configEnbTime(OperObject operObject, long moId, Long enbTime)
			throws Exception {
		getService().configEnbTime(moId, enbTime);
	}

	@Override
	public void configRfSwitch(OperObject operObject, long moId, int rackNo,
			int shelfNo, int slotNo, Integer rfSwitch) throws Exception {
		getService().configRfSwitch(moId, rackNo, shelfNo, slotNo, rfSwitch);
	}

	private EnbStatusService getService() {
		return OmpAppContext.getCtx().getBean(EnbStatusService.class);
	}

	@Override
	public List<Object> configAirFlowBegin(OperObject createEnbOperObject,
			long moId, String ipAddress) throws Exception {
		return getService().configAirFlowBegin(moId, ipAddress);

	}

	@Override
	public List<Object> configAirFlowEnd(OperObject createEnbOperObject,
			long moId) throws Exception {
		return getService().configAirFlowEnd(moId);
	}

	@Override
	public List<Object> configS1Package(OperObject createEnbOperObject,
			long moId) throws Exception {
		
		return getService().configS1Package(moId);
	}

	@Override
	public List<Object> configS1Time(OperObject createEnbOperObject, long moId) throws Exception {
		return getService().configS1Time(moId);
	}
	

}
