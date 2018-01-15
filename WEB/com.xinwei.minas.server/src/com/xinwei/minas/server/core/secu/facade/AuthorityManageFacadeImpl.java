/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.facade.secu.AuthorityManageFacade;
import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;
import com.xinwei.minas.server.core.secu.service.AuthorityManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 权限管理门面实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class AuthorityManageFacadeImpl extends UnicastRemoteObject implements
		AuthorityManageFacade {

	protected AuthorityManageFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public List<OperAction> queryAuthority(String username)
			throws RemoteException, Exception {
		return getService().queryAuthority(username);
	}

	@Override
	public OperAction queryOperAction(OperSignature signature)
			throws RemoteException, Exception {
		return getService().queryOperAction(signature);
	}

	private AuthorityManageService getService() {
		return AppContext.getCtx().getBean(AuthorityManageService.class);
	}

	@Override
	public boolean checkPrivilege(String sessionId, OperAction operAction)
			throws RemoteException, Exception {
		return getService().checkPrivilege(sessionId, operAction);
	}

	@Override
	public boolean checkPrivilege(String sessionId, OperSignature signature)
			throws RemoteException, Exception {
		return getService().checkPrivilege(sessionId, signature);
	}

	@Override
	public void addLog(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) throws RemoteException,
			Exception {
		getService().addLog(sessionId, signature, operObject, params);
	}

	@Override
	public SystemLogQueryResult queryLog(LogQueryCondition condition)
			throws RemoteException, Exception {
		return getService().queryLog(condition);
	}

}