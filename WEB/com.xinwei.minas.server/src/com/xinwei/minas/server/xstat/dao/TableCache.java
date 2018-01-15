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
 * ���ݿ����
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
	 * ��ָ������뵽�����ڵı�����
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
	 * ��ָ��������Ѵ��������ݿ�����У������ñ�Ӳ����ڵĻ������Ƴ�
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
	 * ��ָ����Ӳ����ڵĻ������Ƴ�
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
	 * �ж�ָ�����Ƿ����
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
	 * �ж�ָ�����ݿ��Ƿ����
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
	 * �ж�ָ�����ڲ����ڵı������Ƿ���ڣ�������ڣ���ñ������ݿ��в����ڣ����������ݿ��д���
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
