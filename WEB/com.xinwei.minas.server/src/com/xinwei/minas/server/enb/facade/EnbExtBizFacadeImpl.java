/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbExtBizFacade;
import com.xinwei.minas.server.enb.service.EnbExtBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * eNB扩展业务门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbExtBizFacadeImpl extends UnicastRemoteObject implements
		EnbExtBizFacade {

	protected EnbExtBizFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void reset(OperObject operObject, long moId) throws Exception,
			RemoteException {
		getService().reset(moId);
	}

	@Override
	public void reset(OperObject operObject, long moId, int rackId,
			int shelfId, int boardId) throws Exception, RemoteException {
		getService().reset(moId, rackId, shelfId, boardId);
	}

	@Override
	public String exportActiveData(OperObject operObject, long moId)
			throws Exception, RemoteException {
		return getService().exportActiveData(moId);
	}

	@Override
	public Map<String, Map<String, XList>> queryStudyDataConfig()
			throws Exception, RemoteException {
		return getService().queryStudyDataConfig();
	}

	@Override
	public void studyEnbDataConfig(long moId, boolean reStudy)
			throws Exception, RemoteException {
		getService().studyEnbDataConfig(moId, reStudy);
	}

	@Override
	public void recoverDefaultData(long moId) throws Exception, RemoteException {
		getService().recoverDefaultData(moId);
	}

	private EnbExtBizService getService() {
		return AppContext.getCtx().getBean(EnbExtBizService.class);
	}

}
