/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

/**
 * 数据库检查器接口
 * 
 * @author fanhaoyu
 * 
 */

public interface DataBaseChecker {

	/**
	 * 判断数据库表是否存在
	 * 
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	public boolean isTableExist(String dataBase, String tableName);

	/**
	 * 判断数据库是否存在
	 * 
	 * @param dataBase
	 * @return
	 */
	public boolean isDataBaseExist(String dataBase);
}
