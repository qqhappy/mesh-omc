/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbGlobalConfigFacade;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.minas.server.enb.service.EnbGlobalConfigService;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB全局配置数据服务器门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbGlobalConfigFacadeImpl extends UnicastRemoteObject implements
		EnbGlobalConfigFacade {

	protected EnbGlobalConfigFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public EnbGlobalConfig queryEnbGlobalConfig() throws Exception {
		return getService().queryEnbGlobalConfig();
	}

	@Override
	public Map<Object, String> configEnbGlobalConfig(OperObject operObject,
			EnbGlobalConfig config) throws Exception {
		return getService().configEnbGlobalConfig(config);
	}

	@Override
	public void addTaItem(OperObject operObject, TaModel taModel)
			throws Exception {
		getService().addTaItem(taModel);
	}

	@Override
	public void modifyTaItem(OperObject operObject, TaModel taModel)
			throws Exception {
		getService().modifyTaItem(taModel);
	}

	@Override
	public void deleteTaItem(OperObject operObject, int id) throws Exception {
		getService().deleteTaItem(id);
	}

	@Override
	public TaModel queryTaItemById(int id) throws Exception {
		return getService().queryTaItemById(id);
	}

	@Override
	public List<TaModel> queryAllTaItems() throws Exception {
		return getService().queryAllTaItems();
	}

	@Override
	public PagingData<TaModel> queryTaItems(PagingCondition condition)
			throws Exception {
		return getService().queryTaItems(condition);
	}

	private EnbGlobalConfigService getService() {
		return OmpAppContext.getCtx().getBean(EnbGlobalConfigService.class);
	}

}
