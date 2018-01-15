/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.cache.XUIMetaCache;

/**
 * 
 * EnbBizHelper
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizHelper {
	
	private static final Log log = LogFactory.getLog(EnbBizHelper.class);
	
	/**
	 * 获取eNB所有业务名称列表
	 * 
	 * @return
	 */
	public static Set<String> getAllTableNames(int enbTypeId,
			String protocolVersion) {
		// 按照版本前三位获取配置
		Set<String> tableNameSet = XUIMetaCache.getInstance().getAllBizName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion);
		return tableNameSet;
	}
	
	/**
	 * 获取元数据表中所有字段名
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<String> getAllFieldNames(int enbTypeId,
			String protocolVersion, String tableName) {
		XList tableConfig = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		List<String> fields = new ArrayList<String>();
		if (tableConfig != null) {
			for (XList field : tableConfig.getAllFields()) {
				fields.add(field.getName());
			}
		}
		return fields;
	}
	
	/**
	 * 获取经过TOPO排序的业务表名列表
	 * 
	 * @return
	 */
	public static List<String> getTopoTableNames(int enbTypeId,
			String protocolVersion) throws Exception {
		List<String> topoTableNames = new LinkedList<String>();
		// 生成表依赖关系Map
		Map<String, Set<String>> dependentMap = generateDependentMap(enbTypeId,
				protocolVersion);
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
	 * 获取经过TOPO排序的业务表名列表
	 * 
	 * @return
	 */
	public static List<String> getReverseTopoTableNames(int enbTypeId,
			String protocolVersion) throws Exception {
		List<String> topoTableNames = getTopoTableNames(enbTypeId,
				protocolVersion);
		Collections.reverse(topoTableNames);
		return topoTableNames;
	}
	
	/**
	 * 获取动态表名
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static List<String> getDynamicTables(int enbTypeId,
			String protocolVersion) {
		List<String> tableNames = new ArrayList<String>();
		XCollection xCollection = XUIMetaCache.getInstance().getMoBizMetas(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion);
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
	 * 生成表的依赖关系图
	 * 
	 * @return key-表名 value-该表依赖的表列表
	 */
	private static Map<String, Set<String>> generateDependentMap(int enbTypeId,
			String protocolVersion) {
		Set<String> tableNames = getAllTableNames(enbTypeId, protocolVersion);
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
					}
					else {
						log.error("error ref table definition: " + refTable);
					}
				}
			}
			dependentMap.put(tableName, dependentTables);
		}
		return dependentMap;
	}
	
	/**
	 * 根据表依赖关系计算表的入度
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
	 * 更新指定字段的数值
	 * 
	 * @param left
	 * @param right
	 */
	public static void updateField(XBizTable left, XBizTable right,
			List<XList> updateFields) {
		// 比较的数据不能带不支持的字段
		Map<String, XBizRecord> leftRecordMap = createRecordMap(left);
		Map<String, XBizRecord> rightRecordMap = createRecordMap(right);
		// 如果向基站查询的数据为空，则直接返回
		if (rightRecordMap == null || rightRecordMap.isEmpty())
			return;
		Iterator<String> keyItr = leftRecordMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			XBizRecord leftRecord = leftRecordMap.get(key);
			XBizRecord rightRecord = rightRecordMap.get(key);
			if (rightRecord == null) {
				continue;
			}
			for (XList field : updateFields) {
				String fieldName = field.getName();
				leftRecord.addField(rightRecord.getFieldBy(fieldName).clone());
			}
		}
	}
	
	/**
	 * 计算需要增加的数据
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<XBizRecord> computeAddRecords(XBizTable left,
			XBizTable right) {
		List<XBizRecord> records = new LinkedList<XBizRecord>();
		// 比较的数据不能带不支持的字段
		Map<String, XBizRecord> leftRecordMap = createRecordMap(left);
		Map<String, XBizRecord> rightRecordMap = createRecordMap(right);
		Iterator<String> keyItr = leftRecordMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			XBizRecord leftRecord = leftRecordMap.get(key);
			XBizRecord rightRecord = rightRecordMap.get(key);
			if (rightRecord == null) {
				records.add(leftRecord);
			}
		}
		return records;
	}
	
	/**
	 * 计算需要修改的数据
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<XBizRecord> computeUpdateRecords(XBizTable left,
			XBizTable right) {
		List<XBizRecord> records = new LinkedList<XBizRecord>();
		// 比较的数据不能带不支持的字段
		Map<String, XBizRecord> leftRecordMap = createRecordMap(left);
		Map<String, XBizRecord> rightRecordMap = createRecordMap(right);
		Iterator<String> keyItr = leftRecordMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			XBizRecord leftRecord = leftRecordMap.get(key);
			XBizRecord rightRecord = rightRecordMap.get(key);
			if (rightRecord == null) {
				continue;
			}
			if (!leftRecord.equals(rightRecord)) {
				records.add(leftRecord);
			}
		}
		return records;
	}
	
	/**
	 * 计算需要删除的数据
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<XBizRecord> computeDeleteRecords(XBizTable left,
			XBizTable right) {
		return computeAddRecords(right, left);
	}
	
	/**
	 * 创建记录Map，包含不支持的字段
	 * 
	 * @param bizTable
	 * @return
	 */
	public static Map<String, XBizRecord> createRecordMap(XBizTable bizTable) {
		Enb enb = EnbCache.getInstance().queryByMoId(bizTable.getMoId());
		XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), bizTable.getTableName());
		List<String> indexFields = bizMeta.getIndexList();
		Map<String, XBizRecord> recordMap = new LinkedHashMap<String, XBizRecord>();
		List<XBizRecord> records = bizTable.getRecords();
		for (XBizRecord record : records) {
			StringBuilder keyBuf = new StringBuilder();
			for (String indexField : indexFields) {
				XBizField bizField = record.getFieldBy(indexField);
				keyBuf.append(bizField.getValue().toString() + "*");
			}
			keyBuf.deleteCharAt(keyBuf.length() - 1);
			recordMap.put(keyBuf.toString(), record);
		}
		
		return recordMap;
		
	}
	
	/**
	 * 按元数据定义的顺序格式化Record
	 * 
	 * @param tableName
	 * @param record
	 * @return
	 */
	public static XBizRecord formatBizRecord(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord record) {
		XList bizMeta = XUIMetaCache.getInstance().getBizMetaByName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion, tableName);
		// 调整record中field的顺序
		XBizRecord newRecord = new XBizRecord();
		XList[] fieldMetas = bizMeta.getFieldMetaList();
		for (XList fieldMeta : fieldMetas) {
			String fieldName = fieldMeta.getName();
			XBizField field = record.getFieldBy(fieldName);
			// 元数据配置中存在该字段才加进数据中
			if (field != null) {
				newRecord.addField(field);
			}
			else {
				log.warn("field is not exist in this record. fieldName="
						+ fieldName);
			}
		}
		return newRecord;
	}
	
	/**
	 * 将记录中的hexArray类型字段的值转成小写
	 * 
	 * @param tableName
	 * @param bizRecord
	 */
	public static void changeHexArrayToLowerCase(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord bizRecord) {
		XList tableMeta = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		List<XList> fieldMetas = tableMeta.getAllFields();
		for (XList fieldMeta : fieldMetas) {
			if (fieldMeta.isHexArray()) {
				String fieldName = fieldMeta.getName();
				XBizField field = fieldMap.get(fieldName);
				XBizField newField = new XBizField(fieldName, field.getValue()
						.toLowerCase());
				fieldMap.put(fieldName, newField);
			}
		}
	}
	
	/**
	 * 返回某个版本的所有元数据配置
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static XCollection getBizMetaBy(int enbTypeId, String protocolVersion) {
		return XUIMetaCache.getInstance().getMoBizMetas(MoTypeDD.ENODEB,
				enbTypeId, protocolVersion);
	}
	
	/**
	 * 获取指定表的元数据定义
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static XList getBizMetaBy(int enbTypeId, String protocolVersion,
			String tableName) {
		XList bizMeta = XUIMetaCache.getInstance().getBizMetaByName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion, tableName);
		return bizMeta;
	}
	
	/**
	 * 获取指定表的元数据定义
	 * 
	 * @param moId
	 * @param tableName
	 * @return
	 */
	public static XList getBizMetaBy(long moId, String tableName) {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		return getBizMetaBy(enb.getEnbType(), enb.getProtocolVersion(),
				tableName);
	}
	
	/**
	 * 是否有指定业务的配置数据
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static boolean hasBizMeta(int enbTypeId, String protocolVersion,
			String tableName) {
		return getBizMetaBy(enbTypeId, protocolVersion, tableName) != null;
	}
	
	/**
	 * 是否有指定业务的配置数据
	 * 
	 * @param moId
	 * @param tableName
	 * @return
	 */
	public static boolean hasBizMeta(long moId, String tableName) {
		return getBizMetaBy(moId, tableName) != null;
	}
	
	/**
	 * 获取指定表字段的元数据定义
	 * 
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static XList getFieldMetaBy(int enbTypeId, String protocolVersion,
			String tableName, String fieldName) {
		XList fieldMeta = XUIMetaCache.getInstance().getFiledMetaByName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion, tableName,
				fieldName);
		return fieldMeta;
	}
	
	/**
	 * 判断表中字段是否存在
	 * @param enbTypeId
	 * @param protocolVersion
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static boolean fieldIsExist(int enbTypeId, String protocolVersion,
			String tableName, String fieldName) {
		
		XList fieldXList = getFieldMetaBy(enbTypeId, protocolVersion,
				tableName, fieldName);
		if(null == fieldXList) {
			return false;
		}
		return true;
	}
	
	public static XList getFieldMetaBy(long moId, String tableName,
			String fieldName) {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		return getFieldMetaBy(enb.getEnbType(), enb.getProtocolVersion(),
				tableName, fieldName);
	}
	
	/**
	 * 获取指定表记录的关键字字段
	 * 
	 * @param tableName
	 * @param bizRecord
	 * @return
	 */
	public static XBizRecord getKeyRecordBy(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord bizRecord) {
		XList bizMeta = XUIMetaCache.getInstance().getBizMetaByName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion, tableName);
		if (bizMeta == null)
			return null;
		XBizRecord bizKey = bizRecord.getKeyRecord(bizMeta);
		return bizKey;
	}
	
	public static XBizRecord getKeyRecordBy(long moId, String tableName,
			XBizRecord bizRecord) {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		return getKeyRecordBy(enb.getEnbType(), enb.getProtocolVersion(),
				tableName, bizRecord);
	}
	
	/**
	 * 过滤掉不支持的字段
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @param bizRecord
	 * @return
	 */
	public static XBizRecord filterUnsupportedFields(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord bizRecord) {
		List<String> fieldList = getAllFieldNames(enbTypeId, protocolVersion,
				tableName);
		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		XBizRecord record = new XBizRecord();
		for (String fieldName : fieldMap.keySet()) {
			if (fieldList.contains(fieldName)) {
				record.addField(fieldMap.get(fieldName));
			}
		}
		return record;
	}
	
	/**
	 * 获取开站参数相关的表
	 * 
	 * @return
	 */
	public static List<String> getActiveRelatedTables() {
		String[] array = new String[] { EnbConstantUtils.TABLE_NAME_T_ENB_PARA,
				EnbConstantUtils.TABLE_NAME_T_RACK,
				EnbConstantUtils.TABLE_NAME_T_SHELF,
				EnbConstantUtils.TABLE_NAME_T_BOARD,
				EnbConstantUtils.TABLE_NAME_T_ETHPARA,
				EnbConstantUtils.TABLE_NAME_T_IPV4,
				EnbConstantUtils.TABLE_NAME_T_OMC };
		List<String> tables = new ArrayList<String>();
		for (String table : array) {
			tables.add(table);
		}
		return tables;
	}
	
	/**
	 * 判断是否是开站参数相关表
	 * 
	 * @param tableName
	 * @return
	 */
	public static boolean isActiveRelatedTable(String tableName) {
		return getActiveRelatedTables().contains(tableName);
	}
	
	/**
	 * 获取指定版本的所有小区相关表
	 * 
	 * @param protocolVersion
	 * @return
	 * @throws Exception
	 */
	public static List<String> getCellRelatedTables(int enbTypeId,
			String protocolVersion) throws Exception {
		List<String> tables = new LinkedList<String>();
		List<String> tableNames = getTopoTableNames(enbTypeId, protocolVersion);
		for (String tableName : tableNames) {
			boolean ok = isCellRelatedTable(enbTypeId, protocolVersion,
					tableName);
			if (ok) {
				tables.add(tableName);
			}
		}
		return tables;
	}
	
	/**
	 * 是否是小区相关表，主键和外键都是小区ID
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static boolean isCellRelatedTable(int enbTypeId,
			String protocolVersion, String tableName) {
		XList xList = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		List<XList> fieldXLists = xList.getAllFields();
		for (XList fieldXList : fieldXLists) {
			List<XMetaRef> refList = fieldXList.getFieldRefs();
			for (XMetaRef xMetaRef : refList) {
				if (xMetaRef.getRefTable().equals(
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 在小区相关表中获取到小区cid关联的字段
	 * 
	 * @param enbTypeId
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static String getCidRelatedField(int enbTypeId,
			String protocolVersion, String tableName) {
		XList xList = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		List<XList> fieldXLists = xList.getAllFields();
		for (XList fieldXList : fieldXLists) {
			List<XMetaRef> refList = fieldXList.getFieldRefs();
			for (XMetaRef xMetaRef : refList) {
				if (xMetaRef.getRefTable().equals(
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA)
						|| xMetaRef.getKeyColumn().equals(
								EnbConstantUtils.FIELD_NAME_CELL_ID)) {
					return fieldXList.getName();
				}
			}
		}
		return "";
	}
	
	/**
	 * 版本是否支持
	 * 
	 * @param version
	 * @return
	 */
	public static boolean isVersionSupported(int enbTypeId, String version) {
		if (!isVersionLegal(version)) {
			return false;
		}
		version = getProtocolVersion(version);
		Set<String> supportedVersions = XUIMetaCache.getInstance()
				.getSupportedVersions(MoTypeDD.ENODEB, enbTypeId);
		return supportedVersions.contains(version);
	}
	
	/**
	 * 判断版本号是否合法
	 * 
	 * @param version
	 * @return
	 */
	public static boolean isVersionLegal(String version) {
		if (version == null)
			return false;
		String regex = "[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]*[PT]{0,1}[0-9]+";
		return Pattern.matches(regex, version);
	}
	
	/**
	 * 获取协议版本号(前三位)
	 * 
	 * @param softwareVersion
	 * @return
	 */
	public static String getProtocolVersion(String softwareVersion) {
		return softwareVersion.substring(0, softwareVersion.lastIndexOf("."));
	}
	
	/**
	 * 判断指定数据的格式与指定版本的元数据格式是否一致
	 * 
	 * @param protocolVersion
	 * @param bizTable
	 * @return
	 */
	public static boolean isDataMatchVersion(int enbTypeId,
			String protocolVersion, XBizTable bizTable) {
		boolean dataMatch = true;
		List<XBizRecord> records = bizTable.getRecords();
		if (records != null && !records.isEmpty()) {
			dataMatch = isRecordMatchVersion(enbTypeId, protocolVersion,
					bizTable.getTableName(), records.get(0));
		}
		return dataMatch;
	}
	
	/**
	 * 记录中的数据是否与当前版本的格式相匹配
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @param record
	 * @return
	 */
	public static boolean isRecordMatchVersion(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord record) {
		boolean dataMatch = true;
		List<String> fieldNames = getAllFieldNames(enbTypeId, protocolVersion,
				tableName);
		// 字段数是否一致
		if (record.getFieldMap().size() != fieldNames.size())
			return false;
		// 目标版本中的字段是否存在
		for (String fieldName : fieldNames) {
			if (record.getFieldBy(fieldName) == null) {
				dataMatch = false;
				break;
			}
		}
		return dataMatch;
		
	}
	
	/**
	 * 按照所有版本中字段的全集补充数据中缺失的字段值
	 * 
	 * @param bizTable
	 */
	public static void makeFullData(Long moId, XBizTable bizTable) {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			return;
		}
		List<XBizRecord> records = bizTable.getRecords();
		if (records == null || records.isEmpty())
			return;
		Map<String, String> fullFields = getFullFields(enb,
				bizTable.getTableName());
		for (XBizRecord bizRecord : records) {
			for (String fieldName : fullFields.keySet()) {
				XBizField bizField = bizRecord.getFieldBy(fieldName);
				if (bizField == null) {
					String value = fullFields.get(fieldName);
					bizRecord.addField(new XBizField(fieldName, value));
				}
			}
		}
		
	}
	
	/**
	 * 获取所有版本中字段的全集和默认值
	 * 
	 * @param tableName
	 * @return
	 */
	public static Map<String, String> getFullFields(Enb enb, String tableName) {
		
		Set<String> protocolVersions = getAllProtocolVersions(enb);
		Map<String, String> fullFields = new HashMap<String, String>();
		for (String protocolVersion : protocolVersions) {
			XUIMetaCache instance = XUIMetaCache.getInstance();
			XList list = instance.getBizMetaByName(MoTypeDD.ENODEB,
					enb.getEnbType(), protocolVersion, tableName);
			if (list != null) {
				List<XList> fieldMetas = list.getAllFields();
				for (XList fieldMeta : fieldMetas) {
					String defaultValue = fieldMeta
							.getPropertyValue(XList.P_DEFAULT);
					if (defaultValue == null)
						defaultValue = "";
					fullFields.put(fieldMeta.getName(), defaultValue);
				}
			}
		}
		return fullFields;
	}
	
	/**
	 * 获取当前支持的所有eNB协议版本
	 * 
	 * @return
	 */
	public static Set<String> getAllProtocolVersions(Mo mo) {
		Enb enb = (Enb) mo;
		int key = XUIMetaCache.getInstance().createKey(MoTypeDD.ENODEB,
				enb.getEnbType());
		return XUIMetaCache.getInstance().getUiMap(key).keySet();
	}
	
	/**
	 * 根据旧数据创建新版本格式的数据
	 * 
	 * @param tableName
	 * @param oldRecord
	 * @return
	 */
	public static XBizRecord convertDataByVersion(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord oldRecord) {
		if (oldRecord == null) {
			//oldRecord = new XBizRecord();
			return null;
		}
		
		XList bizMeta = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		XBizRecord newRecord = new XBizRecord();
		XList[] fieldMetas = bizMeta.getFieldMetaList();
		// 根据元数据配置生成新数据
		for (XList fieldMeta : fieldMetas) {
			String fieldName = fieldMeta.getName();
			XBizField field = oldRecord.getFieldBy(fieldName);
			if (field != null) {
				// 保留旧数据中的字段值
				newRecord.addField(field);
			}
			else {
				// 旧数据没有该字段，则取默认值
				String value = fieldMeta.getPropertyValue(XList.P_DEFAULT);
				if (value == null) {
					value = "";
					log.warn("protocolVersion=" + protocolVersion + ", table="
							+ tableName
							+ ", default value of field is null. fieldName="
							+ fieldName);
				}
				newRecord.addField(new XBizField(fieldName, value));
			}
		}
		return newRecord;
	}
	
	/**
	 * 创建小区相关表的数据，只适用于外键和主键都是小区ID的情况
	 * 
	 * @param cellTable
	 * @param tableConfig
	 * @return
	 */
	public static List<XBizRecord> createCellRelatedRecords(int enbTypeId,
			String targetVersion, String tableName, XBizTable cellTable) {
		// 如果有小区
		if (!hasRecord(cellTable))
			return null;
		XList tableConfig = getBizMetaBy(enbTypeId, targetVersion, tableName);
		// 获取主键字段名
		String keyField = tableConfig.getIndexList().get(0);
		List<XBizRecord> records = new ArrayList<XBizRecord>();
		for (XBizRecord record : cellTable.getRecords()) {
			XBizField cellId = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			XBizRecord newRecord = new XBizRecord();
			// 遍历字段，设置默认值
			for (XList fieldConfig : tableConfig.getAllFields()) {
				String defaultValue = fieldConfig
						.getPropertyValue(XList.P_DEFAULT);
				if (defaultValue == null)
					defaultValue = "";
				XBizField field = new XBizField(fieldConfig.getName(),
						defaultValue);
				newRecord.addField(field);
			}
			// 设置小区ID
			newRecord.addField(new XBizField(keyField, cellId.getValue()));
			records.add(newRecord);
		}
		return records;
	}
	
	/**
	 * 是否bizTable有数据
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
	
	/**
	 * 获取表中所有外键
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public static List<String> getForeignKeyFields(int enbTypeId,
			String protocolVersion, String tableName) {
		XList tableMeta = getBizMetaBy(enbTypeId, protocolVersion, tableName);
		if (tableMeta == null)
			return Collections.emptyList();
		List<String> foreignKeys = new LinkedList<String>();
		for (XList fieldMeta : tableMeta.getAllFields()) {
			if (fieldMeta.containsRef()) {
				foreignKeys.add(fieldMeta.getName());
			}
		}
		return foreignKeys;
	}
	
	/**
	 * 获取从旧版本到新版本新增的字段配置
	 * 
	 * @param oldVersion
	 * @param newVersion
	 * @return
	 */
	public static List<XList> getNewFields(int enbTypeId, String oldVersion,
			String newVersion, String tableName) {
		XList oldTableMeta = getBizMetaBy(enbTypeId, oldVersion, tableName);
		XList newTableMeta = getBizMetaBy(enbTypeId, newVersion, tableName);
		if (newTableMeta == null) {
			return Collections.emptyList();
		}
		if (oldTableMeta == null) {
			return newTableMeta.getAllFields();
		}
		
		List<XList> newFields = new LinkedList<XList>();
		// 新版本中有，旧版本中没有的字段即为新版本中新增的字段
		for (XList newFieldMeta : newTableMeta.getAllFields()) {
			XList fieldMeta = oldTableMeta.getFieldMeta(newFieldMeta.getName());
			if (fieldMeta == null) {
				newFields.add(newFieldMeta);
			}
		}
		return newFields;
		
	}
	
	/**
	 * 由于3.0.2及其以后版本将工作以太网参数表的工作模式拆分成了3个字段共同
	 * 表示,当旧的工作模式为电口模式或者光口模式时,需要特殊处理新版本的数据
	 * @param newRecord
	 * @param oldRecord
	 */
	public static void ethParaDataConvertor(XBizRecord newRecord, XBizRecord oldRecord) {
		// 旧数据的工作模式字段
		String u8WorkMode = oldRecord.getStringValue("u8WorkMode");
		// 新数据的光电模式
		String u8PortWorkType = "";
		// 新数据的电口配置
		String u8ElecWorkMode = "";
		
		// 如果工作模式是1(电口10M全双工)
		if("1".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "1";
		}
		// 如果工作模式是2(电口100M全双工)
		else if("2".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "2";
		}
		// 如果工作模式是3(电口1000M全双工)
		else if("3".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "3";
		}
		// 如果工作模式是4(光口100M全双工)
		else if("4".equals(u8WorkMode)) {
			u8PortWorkType = "2";
		}
		// 如果工作模式是5(光口1000M全双工)
		else if("5".equals(u8WorkMode)) {
			u8PortWorkType = "2";
		}
		// 如果工作模式是6(光口10000M全双工)
		else if("6".equals(u8WorkMode)) {
			u8PortWorkType = "2";
		}
		
		if(!"".equals(u8PortWorkType)) {
			newRecord.addField(new XBizField("u8PortWorkType", u8PortWorkType));
			if("1".equals(u8PortWorkType)) {
				newRecord.addField(new XBizField("u8ElecWorkMode", u8ElecWorkMode));
			}
		}
		
	}
	
	public static String getHexEnbId(long enbId) {
		String hexString = Long.toHexString(enbId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8 - hexString.length(); i++) {
			sb.append("0");
		}
		sb.append(hexString);
		return sb.toString();
	}
}
