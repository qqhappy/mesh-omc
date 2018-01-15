/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-20	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.enb.core.facade.EnbAssetFacade;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.minas.server.enb.dao.EnbAssetDAO;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * @author chenlong
 * 
 */

public class EnbAssetFacadeImpl extends UnicastRemoteObject implements
		EnbAssetFacade {
	
	protected EnbAssetFacadeImpl() throws RemoteException {
		super();
	}
	
	private EnbAssetDAO enbAssetDAO;
	
	public void setEnbAssetDAO(EnbAssetDAO enbAssetDAO) {
		this.enbAssetDAO = enbAssetDAO;
	}
	
	@Override
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition)
			throws Exception {
		return enbAssetDAO.queryByCondition(condition);
	}
	
	@Override
	public PagingData<EnbAssetHistory> queryHistoryByCondition(
			EnbAssetCondition condition) throws Exception {
		return enbAssetDAO.queryHistoryByCondition(condition);
	}
	
	@Override
	public void confirmStop(EnbAssetHistory assetHistory) throws Exception {
		EnbAsset asset = enbAssetDAO.queryById(assetHistory.getId());
		if (null != asset) {
			// 向历史表中增加一条新纪录
			enbAssetDAO.addHistory(assetHistory);
			// 删除资产表中纪录
			enbAssetDAO.delete(asset);
			// 删除缓存纪录
			EnbCache.getInstance().deleteAsset(asset.getEnbId(),
					asset.getProductionSN());
		}
	}

	@Override
	public void update(EnbAsset enbAsset) throws Exception {
		enbAssetDAO.update(enbAsset);
		EnbCache.getInstance().addOrUpdateAsset(enbAsset.getEnbId(), enbAsset);
	}
	
}
