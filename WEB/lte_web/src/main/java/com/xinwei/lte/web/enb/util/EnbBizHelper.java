/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-9	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * eNB业务配置助手
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizHelper {

	private static final Log log = LogFactory.getLog(EnbBizHelper.class);
	/**
	 * 易用性不需要显示的�?
	 */
	public static String[] FILTER_TABLES = { "T_RACK", "T_SHELF", "T_TOPO",
			"T_SNTP" };

	/**
	 * 获取eNB所有业务名称列�?
	 * 
	 * @return
	 */
	public static List<String> getAllTableNames(int enbTypeId, String protocolVersion) {
		// 按照版本前三位获取配�?
		XCollection xCollection = EnbVersionBizConfigCache.getInstance()
				.getVersionConfig(enbTypeId, protocolVersion);
		if (xCollection == null)
			return null;
		XList[] tableConfigs = xCollection.getList();
		List<String> tableNames = new LinkedList<String>();
		for (XList tableConfig : tableConfigs) {
			tableNames.add(tableConfig.getName());
		}
		return tableNames;
	}

	/**
	 * 获取经过TOPO排序的业务表名列�?
	 * 
	 * @return
	 */
	public static List<String> getTopoTableNames(int enbTypeId, String protocolVersion)
			throws Exception {
		List<String> topoTableNames = new LinkedList<String>();
		// 生成表依赖关系Map
		Map<String, Set<String>> dependentMap = generateDependentMap(enbTypeId, protocolVersion);
		// 计算表的入度
		Map<String, Integer> inDegrees = computeInDegrees(dependentMap);
		// TOPO排序
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		Iterator<String> keyItr = dependentMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String table = keyItr.next();
			int inDegree = inDegrees.get(table);
			if (inDegree == 0) {
				queue.add(table);
			}
		}
		while (queue.size() > 0) {
			String table = queue.take();
			if (!topoTableNames.contains(table)) {
				topoTableNames.add(table);
			}
			Set<String> dependentTables = dependentMap.get(table);
			for (String dependentTable : dependentTables) {
				int inDegree = inDegrees.get(dependentTable) - 1;
				inDegrees.put(dependentTable, inDegree);
				if (inDegree == 0) {
					queue.offer(dependentTable);
				}
			}
		}

		return topoTableNames;
	}
	
	

	/**
	 * 获取易用性不需要显示的�?
	 * 
	 * @return
	 */
	public static List<String> getSimplifyFilterTables() {
		List<String> tables = new LinkedList<String>();
		Collections.addAll(tables, FILTER_TABLES);
		return tables;
	}

	/**
	 * 生成表的依赖关系�?
	 * 
	 * @return key-表名 value-该表依赖的表列表
	 */
	private static Map<String, Set<String>> generateDependentMap(int enbTypeId,
			String protocolVersion) {
		List<String> tableNames = getAllTableNames(enbTypeId, protocolVersion);
		Map<String, Set<String>> dependentMap = new LinkedHashMap<String, Set<String>>();
		for (String tableName : tableNames) {
			Set<String> dependentTables = new HashSet<String>();
			XList bizMeta = getBizMetaBy(enbTypeId, protocolVersion, tableName);
			List<XList> fieldMetas = bizMeta.getAllFields();
			for (XList fieldMeta : fieldMetas) {
				List<String> refTables = fieldMeta.getFieldRefTables();
				for (String refTable : refTables) {
					if (tableNames.contains(refTable)) {
						dependentTables.add(refTable);
					} else {
						log.error("error ref table definition: " + refTable);
					}
				}
			}
			dependentMap.put(tableName, dependentTables);
		}
		return dependentMap;
	}

	/**
	 * 根据表依赖关系计算表的入�?
	 * 
	 * @param dependentMap
	 * @return
	 */
	private static Map<String, Integer> computeInDegrees(
			Map<String, Set<String>> dependentMap) {
		Map<String, Integer> inDegrees = new LinkedHashMap<String, Integer>();
		Iterator<String> keyItr = dependentMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String tableName = keyItr.next();
			if (!inDegrees.containsKey(tableName)) {
				inDegrees.put(tableName, 0);
			}
			Set<String> dependentTables = dependentMap.get(tableName);
			for (String table : dependentTables) {
				if (!inDegrees.containsKey(table)) {
					inDegrees.put(table, 0);
				}
				inDegrees.put(table, inDegrees.get(table) + 1);
			}
		}

		return inDegrees;
	}

	/**
	 * 获取经过TOPO排序的业务表名列�?
	 * 
	 * @return
	 */
	public static List<String> getReverseTopoTableNames(int enbTypeId, String protocolVersion)
			throws Exception {
		List<String> topoTableNames = getTopoTableNames(enbTypeId, protocolVersion);
		Collections.reverse(topoTableNames);
		return topoTableNames;
	}

	/**
	 * 获取动态表�?
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static List<String> getDynamicTables(int enbTypeId, String protocolVersion) {
		List<String> tableNames = new ArrayList<String>();
		XCollection xCollection = EnbVersionBizConfigCache.getInstance()
				.getVersionConfig(enbTypeId, protocolVersion);
		if (xCollection != null) {
			Map<String, XList> bizMap = xCollection.getBizMap();
			if (bizMap != null) {
				for (String tableName : bizMap.keySet()) {
					XList xList = bizMap.get(tableName);
					if (xList.isReadonly()) {
						tableNames.add(tableName);
					}
				}
			}
		}
		return tableNames;
	}

	/**
	 * 返回某个版本的所有元数据配置
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static XCollection getBizMetaBy(int enbTypeId, String protocolVersion) {
		return EnbVersionBizConfigCache.getInstance().getVersionConfig(enbTypeId,
				protocolVersion);
	}

	/**
	 * 获取指定表的元数据定�?
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static XList getBizMetaBy(int enbTypeId, String protocolVersion, String tableName) {
		XList bizMeta = EnbVersionBizConfigCache.getInstance().getTableConfig(enbTypeId,
				protocolVersion, tableName);
		return bizMeta;
	}

	/**
	 * 是否有指定业务的配置数据
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static boolean hasBizMeta(int enbTypeId, String protocolVersion, String tableName) {
		return getBizMetaBy(enbTypeId, protocolVersion, tableName) != null;
	}

	/**
	 * 获取指定表字段的元数据定�?
	 * 
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static XList getFieldMetaBy(int enbTypeId, String protocolVersion,
			String tableName, String fieldName) {
		XList tableConfig = EnbVersionBizConfigCache.getInstance()
				.getTableConfig(enbTypeId, protocolVersion, tableName);
		if (tableConfig == null)
			return null;
		return tableConfig.getFieldMeta(fieldName);
	}


	/**
	 * 获取表中所有静态字段配�?
	 * 
	 * @param tableConfig
	 * @return
	 */
	public static List<XList> getStaticFields(XList tableConfig) {
		List<XList> staticFields = new LinkedList<XList>();
		for (XList field : tableConfig.getAllFields()) {
			if (field.isReadonly())
				continue;
			staticFields.add(field);
		}
		return staticFields;
	}

	/**
	 * 是否bizTable有数�?
	 * 
	 * @param bizTable
	 * @return
	 */
	public static boolean hasRecord(XBizTable bizTable) {
		if (bizTable == null)
			return false;
		List<XBizRecord> records = bizTable.getRecords();
		if (records == null || records.isEmpty()) {
			return false;
		}
		return true;
	}


}
