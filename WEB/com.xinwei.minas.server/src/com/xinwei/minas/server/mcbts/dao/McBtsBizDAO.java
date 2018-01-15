/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.conf.MoBizData;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.meta.XMetaBiz;

/**
 * 
 * McBts业务DAO接口
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizDAO {

	/**
	 * 根据moId、业务名称、查询条件查询业务数据
	 * 
	 * @param moId
	 *            被管对象ID
	 * @param bizName
	 *            业务名称
	 * @return 记录集
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryAllBy(Long moId, String bizName,
			String condition, Object[] conditionValue) throws Exception;

	/**
	 * 查询某个业务的所有数据
	 * 
	 * @param bizName
	 * @return
	 * @throws Exception
	 */
	List<GenericBizData> queryExportList(String bizName) throws Exception;

	/**
	 * 配置网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void addOrUpdate(Long moId, GenericBizData genericBizData)
			throws Exception;

	/**
	 * 删除网元业务数据
	 * 
	 * @param bizName
	 * @param moId
	 * @throws Exception
	 */
	public void delete(String bizName, Long moId) throws Exception;

	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception;

}