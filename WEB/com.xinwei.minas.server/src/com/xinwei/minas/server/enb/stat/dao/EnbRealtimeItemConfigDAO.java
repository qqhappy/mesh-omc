/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;

/**
 * 
 * eNB实时性能统计项DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeItemConfigDAO {

	/**
	 * 查询统计项配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception;

	/**
	 * 保存统计项配置
	 * 
	 * @param configList
	 * @throws Exception
	 */
	public void saveItemConfig(List<EnbRealtimeItemConfig> configList)
			throws Exception;

}
