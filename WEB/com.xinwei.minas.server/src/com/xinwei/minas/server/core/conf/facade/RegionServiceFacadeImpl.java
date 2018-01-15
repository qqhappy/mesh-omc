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
 * ������������ ����̳�UnicastRemoteObject���ҹ��캯����Ҫ���岢��RemoteException
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
	 * ���ӵ���
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
	 * ��ѯ����
	 * 
	 */
	public List<Region> queryAll() throws RemoteException, Exception {
		RegionService regionService = AppContext.getCtx().getBean(
				RegionService.class);
		List<Region> regionList = regionService.queryAll();
		return regionList;
	}
}
