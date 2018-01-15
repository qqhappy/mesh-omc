/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.McBtsUserInfoBizFacade;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;
import com.xinwei.minas.server.mcbts.service.McBtsUserInfoBizService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站用户数据业务门面
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsUserInfoBizFacadeImpl extends UnicastRemoteObject implements
		McBtsUserInfoBizFacade {

	private McBtsUserInfoBizService service;

	protected McBtsUserInfoBizFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsUserInfoBizService.class);
	}

	@Override
	public void add(Long btsId, McBtsUserAllInfo info) throws RemoteException,
			Exception {
		service.add(btsId, info);
	}

	@Override
	public void modify(Long btsId, McBtsUserAllInfo info)
			throws RemoteException, Exception {
		service.modify(btsId, info);
	}

	@Override
	public void delete(Long btsId, Long pid) throws RemoteException, Exception {
		service.delete(btsId, pid);
	}

	@Override
	public List<McBtsUserAllInfo> queryAllInfoFromCache(Long btsId)
			throws RemoteException, Exception {
		return service.queryAllInfoFromCache(btsId);
	}

	@Override
	public List<McBtsUserAllInfo> queryUserInfo(Long btsId)
			throws RemoteException, Exception {
		return service.queryUserInfo(btsId);
	}

	@Override
	public void configUserInfo(Long btsId) throws RemoteException, Exception {
		service.configUserInfo(btsId);
	}

	@Override
	public void importUserAllInfoList(Long btsId,
			List<McBtsUserAllInfo> infoList) throws RemoteException, Exception {
		service.importUserAllInfoList(btsId, infoList);
	}

	@Override
	public List<McBtsUserGroup> queryAllGroups(Long btsId)
			throws RemoteException, Exception {
		return service.queryAllGroups(btsId);
	}
}