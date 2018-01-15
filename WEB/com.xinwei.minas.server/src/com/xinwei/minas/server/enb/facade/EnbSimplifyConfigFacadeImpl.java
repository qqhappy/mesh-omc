/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbSimplifyConfigFacade;
import com.xinwei.minas.server.enb.service.EnbSimplifyConfigService;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB简易配置接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbSimplifyConfigFacadeImpl extends UnicastRemoteObject implements
		EnbSimplifyConfigFacade {

	protected EnbSimplifyConfigFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public void addBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception {
		getService().addBoard(moId, boardRecord, fiberPort);
	}

	@Override
	public void updateBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception {
		getService().updateBoard(moId, boardRecord, fiberPort);
	}

	@Override
	public void deleteBoard(OperObject operObject, long moId,
			XBizRecord boardRecord) throws Exception {
		getService().deleteBoard(moId, boardRecord);
	}

	@Override
	public void updateCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception {
		getService().updateCellPara(moId, cellParaRecord);
	}

	@Override
	public void deleteCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception {
		getService().deleteCellPara(moId, cellParaRecord);
	}

	private EnbSimplifyConfigService getService() {
		return OmpAppContext.getCtx().getBean(EnbSimplifyConfigService.class);
	}

}
