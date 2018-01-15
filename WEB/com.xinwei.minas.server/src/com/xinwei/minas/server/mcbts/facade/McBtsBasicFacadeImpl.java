/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.server.mcbts.service.AutomaticFindMcBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts基本业务门面
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBasicFacadeImpl extends UnicastRemoteObject implements
		McBtsBasicFacade {

	private McBtsBasicService service;

	public McBtsBasicFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsBasicService.class);
	}

	@Override
	public void add(OperObject operObject, McBts mcBts) throws Exception {
		service.add(mcBts);
	}

	@Override
	public void modify(OperObject operObject, McBts mcBts) throws Exception {
		service.modify(mcBts);
	}

	@Override
	public void delete(OperObject operObject, Long moId) throws Exception {
		service.delete(moId);
	}

	@Override
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws Exception {
		service.changeManageState(moId, manageState);
	}

	@Override
	public McBts queryByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public McBts queryByBtsId(Long btsId) throws Exception {
		return service.queryByBtsId(btsId);
	}

	@Override
	public List<McBts> queryBySameFreq(int freq) throws Exception {
		return service.queryBySameFreq(freq);
	}

	@Override
	public List<McBts> queryAll() throws RemoteException, Exception {
		return service.queryAll();
	}

	@Override
	public List<McBts> queryByBtsType(int btsType) throws Exception {
		return service.queryByBtsType(btsType);
	}

	@Override
	public Map<Long, McBts> getMapByMoId() throws Exception {
		return service.getMapByMoId();
	}

	@Override
	public Map<Long, McBts> getMapByBtsId() throws Exception {
		return service.getMapByBtsId();
	}

	@Override
	public PagingData<McBts> queryAllByCondition(McBtsCondition mcBtsCondition)
			throws RemoteException, Exception {
		return service.queryAllByCondition(mcBtsCondition);
	}

	@Override
	public List<McBts> queryMcBtsBy(McBtsCondition condition)
			throws RemoteException, Exception {
		return service.queryMcBtsBy(condition);
	}

	@Override
	public List<McBts> queryByMoIdList(List<Long> moIds)
			throws RemoteException, Exception {
		return service.queryByMoIdList(moIds);
	}

	@Override
	public void updateSagInfo(SxcBasic oldSxc, SxcBasic newSxc)
			throws RemoteException, Exception {
		// TODO: updateSagInfo
	}

	@Override
	public List<McBts> queryAutomaticFindMcBts() throws RemoteException,
			Exception {
		List<McBts> mcBtsList = AutomaticFindMcBtsCache.getInstance()
				.queryAll();
		return mcBtsList;
	}

	@Override
	public void deleteAutomaticFindMcBts(Long btsId) throws RemoteException,
			Exception {
		AutomaticFindMcBtsCache.getInstance().delete(btsId);
	}

	@Override
	public void updateMcBtsCache(OperObject operObject, McBts mcBts)
			throws Exception {
		service.updateMcBtsCache(mcBts);
	}

}
