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
	 * ��ȡeNB����ҵ�������б�
	 * 
	 * @return
	 */
	public static Set<String> getAllTableNames(int enbTypeId,
			String protocolVersion) {
		// ���հ汾ǰ��λ��ȡ����
		Set<String> tableNameSet = XUIMetaCache.getInstance().getAllBizName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion);
		return tableNameSet;
	}
	
	/**
	 * ��ȡԪ���ݱ��������ֶ���
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
	 * ��ȡ����TOPO�����ҵ������б�
	 * 
	 * @return
	 */
	public static List<String> getTopoTableNames(int enbTypeId,
			String protocolVersion) throws Exception {
		List<String> topoTableNames = new LinkedList<String>();
		// ���ɱ�������ϵMap
		Map<String, Set<String>> dependentMap = generateDependentMap(enbTypeId,
				protocolVersion);
		// ���������
		Map<String, Integer> inDegrees = computeInDegrees(dependentMap);
		// TOPO����
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
	 * ��ȡ����TOPO�����ҵ������б�
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
	 * ��ȡ��̬����
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
	 * ���ɱ��������ϵͼ
	 * 
	 * @return key-���� value-�ñ������ı��б�
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
	 * ���ݱ�������ϵ���������
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
	 * ����ָ���ֶε���ֵ
	 * 
	 * @param left
	 * @param right
	 */
	public static void updateField(XBizTable left, XBizTable right,
			List<XList> updateFields) {
		// �Ƚϵ����ݲ��ܴ���֧�ֵ��ֶ�
		Map<String, XBizRecord> leftRecordMap = createRecordMap(left);
		Map<String, XBizRecord> rightRecordMap = createRecordMap(right);
		// ������վ��ѯ������Ϊ�գ���ֱ�ӷ���
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
	 * ������Ҫ���ӵ�����
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<XBizRecord> computeAddRecords(XBizTable left,
			XBizTable right) {
		List<XBizRecord> records = new LinkedList<XBizRecord>();
		// �Ƚϵ����ݲ��ܴ���֧�ֵ��ֶ�
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
	 * ������Ҫ�޸ĵ�����
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static List<XBizRecord> computeUpdateRecords(XBizTable left,
			XBizTable right) {
		List<XBizRecord> records = new LinkedList<XBizRecord>();
		// �Ƚϵ����ݲ��ܴ���֧�ֵ��ֶ�
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
	 * ������Ҫɾ��������
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
	 * ������¼Map��������֧�ֵ��ֶ�
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
	 * ��Ԫ���ݶ����˳���ʽ��Record
	 * 
	 * @param tableName
	 * @param record
	 * @return
	 */
	public static XBizRecord formatBizRecord(int enbTypeId,
			String protocolVersion, String tableName, XBizRecord record) {
		XList bizMeta = XUIMetaCache.getInstance().getBizMetaByName(
				MoTypeDD.ENODEB, enbTypeId, protocolVersion, tableName);
		// ����record��field��˳��
		XBizRecord newRecord = new XBizRecord();
		XList[] fieldMetas = bizMeta.getFieldMetaList();
		for (XList fieldMeta : fieldMetas) {
			String fieldName = fieldMeta.getName();
			XBizField field = record.getFieldBy(fieldName);
			// Ԫ���������д��ڸ��ֶβżӽ�������
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
	 * ����¼�е�hexArray�����ֶε�ֵת��Сд
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
	 * ����ĳ���汾������Ԫ��������
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static XCollection getBizMetaBy(int enbTypeId, String protocolVersion) {
		return XUIMetaCache.getInstance().getMoBizMetas(MoTypeDD.ENODEB,
				enbTypeId, protocolVersion);
	}
	
	/**
	 * ��ȡָ�����Ԫ���ݶ���
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
	 * ��ȡָ�����Ԫ���ݶ���
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
	 * �Ƿ���ָ��ҵ�����������
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
	 * �Ƿ���ָ��ҵ�����������
	 * 
	 * @param moId
	 * @param tableName
	 * @return
	 */
	public static boolean hasBizMeta(long moId, String tableName) {
		return getBizMetaBy(moId, tableName) != null;
	}
	
	/**
	 * ��ȡָ�����ֶε�Ԫ���ݶ���
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
	 * �жϱ����ֶ��Ƿ����
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
	 * ��ȡָ�����¼�Ĺؼ����ֶ�
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
	 * ���˵���֧�ֵ��ֶ�
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
	 * ��ȡ��վ������صı�
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
	 * �ж��Ƿ��ǿ�վ������ر�
	 * 
	 * @param tableName
	 * @return
	 */
	public static boolean isActiveRelatedTable(String tableName) {
		return getActiveRelatedTables().contains(tableName);
	}
	
	/**
	 * ��ȡָ���汾������С����ر�
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
	 * �Ƿ���С����ر��������������С��ID
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
	 * ��С����ر��л�ȡ��С��cid�������ֶ�
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
	 * �汾�Ƿ�֧��
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
	 * �жϰ汾���Ƿ�Ϸ�
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
	 * ��ȡЭ��汾��(ǰ��λ)
	 * 
	 * @param softwareVersion
	 * @return
	 */
	public static String getProtocolVersion(String softwareVersion) {
		return softwareVersion.substring(0, softwareVersion.lastIndexOf("."));
	}
	
	/**
	 * �ж�ָ�����ݵĸ�ʽ��ָ���汾��Ԫ���ݸ�ʽ�Ƿ�һ��
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
	 * ��¼�е������Ƿ��뵱ǰ�汾�ĸ�ʽ��ƥ��
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
		// �ֶ����Ƿ�һ��
		if (record.getFieldMap().size() != fieldNames.size())
			return false;
		// Ŀ��汾�е��ֶ��Ƿ����
		for (String fieldName : fieldNames) {
			if (record.getFieldBy(fieldName) == null) {
				dataMatch = false;
				break;
			}
		}
		return dataMatch;
		
	}
	
	/**
	 * �������а汾���ֶε�ȫ������������ȱʧ���ֶ�ֵ
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
	 * ��ȡ���а汾���ֶε�ȫ����Ĭ��ֵ
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
	 * ��ȡ��ǰ֧�ֵ�����eNBЭ��汾
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
	 * ���ݾ����ݴ����°汾��ʽ������
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
		// ����Ԫ������������������
		for (XList fieldMeta : fieldMetas) {
			String fieldName = fieldMeta.getName();
			XBizField field = oldRecord.getFieldBy(fieldName);
			if (field != null) {
				// �����������е��ֶ�ֵ
				newRecord.addField(field);
			}
			else {
				// ������û�и��ֶΣ���ȡĬ��ֵ
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
	 * ����С����ر�����ݣ�ֻ�������������������С��ID�����
	 * 
	 * @param cellTable
	 * @param tableConfig
	 * @return
	 */
	public static List<XBizRecord> createCellRelatedRecords(int enbTypeId,
			String targetVersion, String tableName, XBizTable cellTable) {
		// �����С��
		if (!hasRecord(cellTable))
			return null;
		XList tableConfig = getBizMetaBy(enbTypeId, targetVersion, tableName);
		// ��ȡ�����ֶ���
		String keyField = tableConfig.getIndexList().get(0);
		List<XBizRecord> records = new ArrayList<XBizRecord>();
		for (XBizRecord record : cellTable.getRecords()) {
			XBizField cellId = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			XBizRecord newRecord = new XBizRecord();
			// �����ֶΣ�����Ĭ��ֵ
			for (XList fieldConfig : tableConfig.getAllFields()) {
				String defaultValue = fieldConfig
						.getPropertyValue(XList.P_DEFAULT);
				if (defaultValue == null)
					defaultValue = "";
				XBizField field = new XBizField(fieldConfig.getName(),
						defaultValue);
				newRecord.addField(field);
			}
			// ����С��ID
			newRecord.addField(new XBizField(keyField, cellId.getValue()));
			records.add(newRecord);
		}
		return records;
	}
	
	/**
	 * �Ƿ�bizTable������
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
	 * ��ȡ�����������
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
	 * ��ȡ�Ӿɰ汾���°汾�������ֶ�����
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
		// �°汾���У��ɰ汾��û�е��ֶμ�Ϊ�°汾���������ֶ�
		for (XList newFieldMeta : newTableMeta.getAllFields()) {
			XList fieldMeta = oldTableMeta.getFieldMeta(newFieldMeta.getName());
			if (fieldMeta == null) {
				newFields.add(newFieldMeta);
			}
		}
		return newFields;
		
	}
	
	/**
	 * ����3.0.2�����Ժ�汾��������̫��������Ĺ���ģʽ��ֳ���3���ֶι�ͬ
	 * ��ʾ,���ɵĹ���ģʽΪ���ģʽ���߹��ģʽʱ,��Ҫ���⴦���°汾������
	 * @param newRecord
	 * @param oldRecord
	 */
	public static void ethParaDataConvertor(XBizRecord newRecord, XBizRecord oldRecord) {
		// �����ݵĹ���ģʽ�ֶ�
		String u8WorkMode = oldRecord.getStringValue("u8WorkMode");
		// �����ݵĹ��ģʽ
		String u8PortWorkType = "";
		// �����ݵĵ������
		String u8ElecWorkMode = "";
		
		// �������ģʽ��1(���10Mȫ˫��)
		if("1".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "1";
		}
		// �������ģʽ��2(���100Mȫ˫��)
		else if("2".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "2";
		}
		// �������ģʽ��3(���1000Mȫ˫��)
		else if("3".equals(u8WorkMode)) {
			u8PortWorkType = "1";
			u8ElecWorkMode = "3";
		}
		// �������ģʽ��4(���100Mȫ˫��)
		else if("4".equals(u8WorkMode)) {
			u8PortWorkType = "2";
		}
		// �������ģʽ��5(���1000Mȫ˫��)
		else if("5".equals(u8WorkMode)) {
			u8PortWorkType = "2";
		}
		// �������ģʽ��6(���10000Mȫ˫��)
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
