/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalVersionForceUpdateFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionForceUpdateService;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * �ն˰汾ǿ����������
 * 
 * 
 * @author tiance
 * 
 */

public class TerminalVersionForceUpdateFacadeImpl extends UnicastRemoteObject
		implements TerminalVersionForceUpdateFacade {

	private TerminalVersionForceUpdateService tvForceUpdateService;

	protected TerminalVersionForceUpdateFacadeImpl() throws RemoteException {
		super();
		tvForceUpdateService = AppContext.getCtx().getBean(
				TerminalVersionForceUpdateService.class);
	}

	/**
	 * �����ݿ��ѯ����ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<TerminalVersion> queryList() throws RemoteException,
			Exception {
		return tvForceUpdateService.queryList();
	}

	/**
	 * ���ǿ�ư汾�����Ŀ���
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public boolean getSwitchStatus() {
		return tvForceUpdateService.getSwitchStatus();
	}

	/**
	 * �����ն˰汾ǿ������
	 * 
	 * @param status
	 * @param ruleList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void config(OperObject operObject, boolean status, List<TerminalVersion> ruleList)
			throws RemoteException, Exception {
		tvForceUpdateService.config(status, ruleList);
	}
}
