/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-19	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.omp.core.model.biz.PagingData;


/**
 * 
 * 资产管理
 * 
 * @author chenlong
 * 
 */

public interface EnbAssetFacade extends Remote {
	
	/**
	 * 按条件查询资产信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition) throws Exception;
	
	
	/**
	 * 按条件查询历史资产信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public PagingData<EnbAssetHistory> queryHistoryByCondition(EnbAssetCondition condition) throws Exception;
	
	
	/**
	 * 确认停用资产
	 * @param assetHistory
	 * @throws BizException
	 */
	@Loggable
	public void confirmStop(EnbAssetHistory assetHistory) throws Exception;
	
	
	/**
	 * 修改
	 * @param enbAsset
	 * @throws Exception
	 */
	public void update(EnbAsset enbAsset) throws Exception;
	
	
}
