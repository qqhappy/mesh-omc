/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.DistrictManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.District;
import com.xinwei.minas.server.mcbts.service.sysManage.DistrictManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 地域管理门面类
 * 
 * @author tiance
 * 
 */

public class DistrictManageFacadeImpl extends UnicastRemoteObject implements
		DistrictManageFacade {

	private DistrictManageService districtManageService;

	protected DistrictManageFacadeImpl() throws RemoteException {
		super();

		districtManageService = AppContext.getCtx().getBean(
				DistrictManageService.class);
	}

	@Override
	public List<District> queryAll() throws RemoteException, Exception {
		return districtManageService.queryAll();
	}

	@Override
	public void saveOrUpdate(OperObject operObject, District district) throws RemoteException,
			Exception {
		districtManageService.saveOrUpdate(district);
	}

	@Override
	public void delete(OperObject operObject, District district) throws RemoteException, Exception {
		districtManageService.delete(district);
	}

}
