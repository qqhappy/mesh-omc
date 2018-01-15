/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.LocAreaFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.server.mcbts.service.sysManage.LocAreaService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 位置区门面类
 * 
 * 
 * @author chenshaohua
 * 
 */

public class LocAreaFacadeImpl extends UnicastRemoteObject implements
		LocAreaFacade {

	private LocAreaService service;

	private SequenceService sequenceService;

	public LocAreaFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(LocAreaService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	/**
	 * 查询所有位置区信息
	 */
	@Override
	public List<LocationArea> queryAll() throws RemoteException, Exception {
		return service.queryAll();
	}

	/**
	 * 新增或更新位置区信息
	 * 
	 * @param la
	 */
	@Override
	public void saveOrUpdate(OperObject operObject, LocationArea la) throws RemoteException, Exception {
		service.saveOrUpdate(la);
	}

	@Override
	public void delete(OperObject operObject, LocationArea la) {
		service.delete(la);
	}

	@Override
	public void config(List<LocationArea> locationAreaList)
			throws RemoteException, Exception {
		String locationId = null;
		for (LocationArea o : locationAreaList) {
			if (o.getIdx() == null) {
				o.setIdx(sequenceService.getNext());
			}
		}

		service.config(locationAreaList);
	}
}
