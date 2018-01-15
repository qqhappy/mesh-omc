/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.Map;

/**
 * 
 * eNB自学习数据配置DAO
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStudyDataConfigDAO {

	/**
	 * 查询所有数据配置
	 * 
	 * @return key=version, value=config
	 * @throws Exception
	 */
	public Map<String, String> queryAll() throws Exception;

	/**
	 * 按照版本查询数据配置
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public String queryByVersion(String version) throws Exception;

	/**
	 * 保存自学习数据
	 * 
	 * @param version
	 * @param dataConfig
	 * @throws Exception
	 */
	public void saveDataConfig(String version, String dataConfig)
			throws Exception;

	/**
	 * 删除指定版本的配置数据
	 * 
	 * @param version
	 * @throws Exception
	 */
	public void delete(String version) throws Exception;

}
