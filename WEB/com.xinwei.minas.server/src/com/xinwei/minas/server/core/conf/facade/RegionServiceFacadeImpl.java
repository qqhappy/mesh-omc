/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.facade.conf.RegionServiceFacade;
import com.xinwei.minas.core.model.Region;
import com.xinwei.minas.server.core.conf.service.RegionService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 地区服务门面 必须继承UnicastRemoteObject，且构造函数需要定义并抛RemoteException
 * 
 * @author chenjunhua
 * 
 */

public class RegionServiceFacadeImpl extends UnicastRemoteObject implements
		RegionServiceFacade {

	public RegionServiceFacadeImpl() throws RemoteException {
		super();
	}

	/**
	 * 增加地区
	 * 
	 * @param region
	 * @throws Exception
	 */
	public void addRegoin(Region region) throws Exception {
		RegionService regionService = AppContext.getCtx().getBean(
				RegionService.class);
		regionService.add(region);
	}

	/**
	 * 查询地区
	 * 
	 */
	public List<Region> queryAll() throws RemoteException, Exception {
		RegionService regionService = AppContext.getCtx().getBean(
				RegionService.class);
		List<Region> regionList = regionService.queryAll();
		return regionList;
	}
}
