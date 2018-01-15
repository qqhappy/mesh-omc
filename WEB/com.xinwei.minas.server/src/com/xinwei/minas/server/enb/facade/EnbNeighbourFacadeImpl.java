/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbNeighbourFacade;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB邻区关系配置门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbNeighbourFacadeImpl extends UnicastRemoteObject implements
		EnbNeighbourFacade {

	protected EnbNeighbourFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public List<EnbNeighbourRecord> queryNeighbourRecords(long moId)
			throws Exception {
		return getService().queryNeighbourRecords(moId);
	}

	@Override
	public EnbNeighbourRecord queryNeighbourRecord(long moId,
			XBizRecord condition) throws Exception {
		return getService().queryNeighbourRecord(moId, condition);
	}

	@Override
	public void addNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception {
		getService().addNeighbour(moId, record);
	}

	@Override
	public void updateNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception {
		getService().updateNeighbour(moId, record);
	}

	@Override
	public void deleteNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception {
		getService().deleteNeighbour(moId, record);
	}

	private EnbNeighbourService getService() {
		return OmpAppContext.getCtx().getBean(EnbNeighbourService.class);
	}

}
