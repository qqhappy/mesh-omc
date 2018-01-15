/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.server.enb.service.EnbBasicService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNB基站基本信息门面接口
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBasicFacadeImpl extends UnicastRemoteObject implements
		EnbBasicFacade {

	protected EnbBasicFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void add(OperObject operObject, Enb enb) throws Exception {
		getService().add(enb);
	}

	@Override
	public void modify(OperObject operObject, Enb enb) throws Exception {
		getService().modify(enb);
	}

	@Override
	public void delete(OperObject operObject, Long moId) throws Exception {
		getService().delete(moId);
	}

	@Override
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws Exception {
		getService().changeManageState(moId, manageState);
	}

	@Override
	public List<Enb> queryAllEnb() throws Exception {
		return getService().queryAllEnb();
	}

	@Override
	public Enb queryByMoId(Long moId) throws Exception {
		return getService().queryByMoId(moId);
	}

	@Override
	public Enb queryByEnbId(Long enbId) throws Exception {
		return getService().queryByEnbId(enbId);
	}

	@Override
	public PagingData<Enb> queryAllByCondition(EnbCondition condition)
			throws Exception {
		return getService().queryAllByCondition(condition);
	}

	@Override
	public List<Enb> queryByMoIdList(List<Long> moIds) throws RemoteException,
			Exception {
		return getService().queryByMoIdList(moIds);
	}

	private EnbBasicService getService() {
		return AppContext.getCtx().getBean(EnbBasicService.class);
	}

}
