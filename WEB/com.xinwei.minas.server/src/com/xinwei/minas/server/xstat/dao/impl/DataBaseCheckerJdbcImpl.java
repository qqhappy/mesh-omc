/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.xstat.dao.DataBaseChecker;
import com.xinwei.minas.server.xstat.dao.TableCache;

/**
 * 
 * 数据库检查器JDBC实现
 * 
 * @author fanhaoyu
 * 
 */

public class DataBaseCheckerJdbcImpl extends JdbcDaoSupport implements
		DataBaseChecker {

	@Override
	public boolean isDataBaseExist(String dataBase) {
		if (!TableCache.getInstance().isDatabaseExist(dataBase)) {
			// 如果缓存不存在，再判断数据库中是否存在
			int countDataBaseNum = getJdbcTemplate().queryForInt(
					"select count(*) from information_schema.schemata where schema_name='"
							+ dataBase + "'");
			// 判断数据库是否存在,如果不存在则新建数据库
			if (countDataBaseNum == 0) {
				return false;
			} else {
				// 如果数据库中存在，则添加到缓存中
				TableCache.getInstance().addDataBase(dataBase);
			}
		}
		return true;
	}

	@Override
	public boolean isTableExist(String dataBase, String tableName) {
		// 如果指定表不存在
		if (TableCache.getInstance().isTableNotExist(dataBase, tableName)) {
			return false;
		}
		if (!TableCache.getInstance().isTableExist(dataBase, tableName)) {
			// 数据库存在,判断指定的表是否存在,如果不存在则新建表
			String sql = "select count(*) from information_schema.tables where table_schema='"
					+ dataBase + "' and table_name='" + tableName + "'";
			int countTableNum = getJdbcTemplate().queryForInt(sql);
			if (countTableNum == 0) {
				// 第一次查询时不存在，则加入不存在的表缓存中，之后查询只需判断目标表是否在不存在的表缓存中就可以判断表是否存在
				TableCache.getInstance().addNotExistTable(dataBase, tableName);
				return false;
			} else {
				// 如果数据库中存在，则添加到缓存中
				TableCache.getInstance().addTable(dataBase, tableName);
			}
		}
		return true;
	}

}
