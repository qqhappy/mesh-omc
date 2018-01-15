/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-20	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.omp.core.model.biz.PagingData;


/**
 * 
 * 资产信息DAO
 * 
 * @author chenlong
 * 
 */

public interface EnbAssetDAO {
	
	/**
	 * 新增
	 * @param enbAsset
	 * @throws Exception
	 */
	public void add(EnbAsset enbAsset) throws Exception;
	
	/**
	 * 修改
	 * @param enbAsset
	 * @throws Exception
	 */
	public void update(EnbAsset enbAsset) throws Exception;
	
	/**
	 * 删除
	 * @param productionSN
	 * @throws Exception
	 */
	public void delete(EnbAsset enbAsset) throws Exception;
	
	/**
	 * 根据主键ID查询资产纪录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EnbAsset queryById(long id) throws Exception;
	
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	public List<EnbAsset> queryAll() throws Exception;
	
	/**
	 * 增加一条资产历史记录
	 * @param assetHistory
	 * @throws Exception
	 */
	public void addHistory(EnbAssetHistory assetHistory) throws Exception; 
	
	/**
	 * 按条件查询资产信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition) throws Exception;
	
	/**
	 * 按条件查询历史资产信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<EnbAssetHistory> queryHistoryByCondition(EnbAssetCondition condition) throws Exception;

	/**
	 * 删除该基站下所有资产
	 * @param enbId
	 */
	public void deleteEnbAll(Long enbId);
	
	
	
	
}
