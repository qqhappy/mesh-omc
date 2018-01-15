/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.facade.secu.RoleManageFacade;
import com.xinwei.minas.core.model.secu.Role;
import com.xinwei.minas.server.core.secu.service.RoleManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 角色管理门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class RoleManageFacadeImpl extends UnicastRemoteObject implements
		RoleManageFacade {

	protected RoleManageFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void addRole(Role role) throws RemoteException, Exception {
		getService().addRole(role);
	}

	@Override
	public void deleteRole(Role role) throws RemoteException, Exception {
		getService().deleteRole(role);
	}

	@Override
	public void modifyRole(Role role) throws RemoteException, Exception {
		getService().modifyRole(role);
	}

	@Override
	public List<Role> queryAllRoles() throws RemoteException, Exception {
		return getService().queryAllRoles();
	}

	private RoleManageService getService() {
		return AppContext.getCtx().getBean(RoleManageService.class);
	}

}
