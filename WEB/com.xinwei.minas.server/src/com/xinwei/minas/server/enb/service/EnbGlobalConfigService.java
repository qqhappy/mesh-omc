/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNB全局配置数据服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbGlobalConfigService {

	/**
	 * 查询eNB全局配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public EnbGlobalConfig queryEnbGlobalConfig() throws Exception;

	/**
	 * 配置eNB全局配置
	 * 
	 * @param config
	 * @return 配置返回消息
	 * @throws Exception
	 */
	public Map<Object, String> configEnbGlobalConfig(EnbGlobalConfig config)
			throws Exception;

	/**
	 * 添加跟踪区码
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	public void addTaItem(TaModel taModel) throws Exception;

	/**
	 * 修改跟踪区码
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	public void modifyTaItem(TaModel taModel) throws Exception;

	/**
	 * 删除跟踪区码
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteTaItem(int id) throws Exception;

	/**
	 * 按ID查询跟踪区码记录
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaModel queryTaItemById(int id) throws Exception;

	/**
	 * 查询所有跟踪区码
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaModel> queryAllTaItems() throws Exception;

	/**
	 * 按照分页条件查询跟踪区码数据
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<TaModel> queryTaItems(PagingCondition condition)
			throws Exception;

}
