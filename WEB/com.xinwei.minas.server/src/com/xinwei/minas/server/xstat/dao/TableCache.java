/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 数据库表缓存
 * 
 * @author fanhaoyu
 * 
 */

public class TableCache {

	private static TableCache instance = new TableCache();

	// database - List[tableName]
	private Map<String, List<String>> tableCache = new HashMap<String, List<String>>();

	// database - List[tableName]
	private Map<String, List<String>> notExistTableCache = new HashMap<String, List<String>>();

	public static TableCache getInstance() {
		return instance;
	}

	public void addDataBase(String dataBase) {
		List<String> tableList = tableCache.get(dataBase);
		if (tableList == null) {
			tableList = new LinkedList<String>();
			tableCache.put(dataBase, tableList);
		}
	}

	/**
	 * 将指定表加入到不存在的表缓存中
	 * 
	 * @param dataBase
	 * @param tableName
	 */
	public void addNotExistTable(String dataBase, String tableName) {
		List<String> tableList = notExistTableCache.get(dataBase);
		if (tableList == null) {
			tableList = new LinkedList<String>();
			notExistTableCache.put(dataBase, tableList);
		}
		tableList.add(tableName);
	}

	/**
	 * 将指定表加入已存在是数据库表缓存中，并将该表从不存在的缓存中移除
	 * 
	 * @param dataBase
	 * @param tableName
	 */
	public void addTable(String dataBase, String tableName) {
		addDataBase(dataBase);
		List<String> tableList = tableCache.get(dataBase);
		tableList.add(tableName);
		removeInNotExistCache(dataBase, tableName);
	}

	/**
	 * 将指定表从不存在的缓存中移除
	 * 
	 * @param dataBase
	 * @param tableName
	 */
	private void removeInNotExistCache(String dataBase, String tableName) {
		List<String> tableList = notExistTableCache.get(dataBase);
		if (tableList != null) {
			tableList.remove(tableName);
		}
	}

	/**
	 * 判断指定表是否存在
	 * 
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	public boolean isTableExist(String dataBase, String tableName) {
		List<String> tableList = tableCache.get(dataBase);
		if (tableList == null || tableList.isEmpty())
			return false;
		for (String tableString : tableList) {
			if (tableString.equals(tableName))
				return true;
		}
		return false;
	}

	/**
	 * 判断指定数据库是否存在
	 * 
	 * @param dataBase
	 * @return
	 */
	public boolean isDatabaseExist(String dataBase) {
		List<String> tableList = tableCache.get(dataBase);
		if (tableList == null)
			return false;
		return true;
	}

	/**
	 * 判断指定表在不存在的表缓存里是否存在，如果存在，则该表在数据库中不存在，否则在数据库中存在
	 * 
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	public boolean isTableNotExist(String dataBase, String tableName) {
		List<String> tableList = notExistTableCache.get(dataBase);
		if (tableList == null)
			return false;
		if (tableList.contains(tableName))
			return true;
		return false;
	}

	public void remove(String dataBase) {
		tableCache.remove(dataBase);
	}

	public void remove(String dataBase, String tableName) {
		List<String> tableList = tableCache.get(dataBase);
		if (tableList == null || tableList.isEmpty())
			return;
		tableList.remove(tableName);
	}

}
