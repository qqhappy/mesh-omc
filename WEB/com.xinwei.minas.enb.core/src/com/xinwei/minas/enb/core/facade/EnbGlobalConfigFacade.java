/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNB全局配置数据服务器门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbGlobalConfigFacade extends Remote {

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
	@Loggable
	public Map<Object, String> configEnbGlobalConfig(OperObject operObject,
			EnbGlobalConfig config) throws Exception;

	/**
	 * 添加跟踪区码
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	@Loggable
	public void addTaItem(OperObject operObject, TaModel taModel)
			throws Exception;

	/**
	 * 修改跟踪区码
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	@Loggable
	public void modifyTaItem(OperObject operObject, TaModel taModel)
			throws Exception;

	/**
	 * 删除跟踪区码
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Loggable
	public void deleteTaItem(OperObject operObject, int id) throws Exception;

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
