/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.centersource.dao;

import java.util.List;

import com.xinwei.minas.server.centersource.model.McBtsIpData;

/**
 * 
 * 基站IP数据持久化接口
 * 
 * @author fanhaoyu
 * 
 */

public interface McBtsIpDataDao {

	/**
	 * 保存或更新
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveOrUpdate(McBtsIpData data) throws Exception;

	/**
	 * 按btsId查询
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBtsIpData query(long btsId) throws Exception;

	/**
	 * 查询所有数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsIpData> queryAll() throws Exception;

	/**
	 * 删除
	 * 
	 * @param btsId
	 * @throws Exception
	 */
	public void delete(long btsId) throws Exception;

}
