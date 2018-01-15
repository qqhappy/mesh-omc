/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao;

import java.util.List;

/**
 * 
 * 分表策略接口
 * 
 * @author fanhaoyu
 * 
 */

public interface TableStrategy {

	/**
	 * 根据条件获取数据应该存储到的数据库表的表名
	 * 
	 * @param condition
	 * @return
	 */
	public String getTableName(int timeType, long btsId);

	/**
	 * 获取用于分表的所有表
	 * 
	 * @return
	 */
	public List<String> getAllTables();
	
	/**
	 * 根据条件获取数据库表的表名列表
	 * 
	 * @return
	 */
	public List<String> getTables(int condition);
}
