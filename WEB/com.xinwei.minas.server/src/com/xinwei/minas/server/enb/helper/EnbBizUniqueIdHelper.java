/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-4-23	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * enbBiz�����й�������Ҫ����Ψһ��ʶ�Ĺ�����
 * 
 * @author chenlong
 * 
 */

public class EnbBizUniqueIdHelper {
	
	private static final Log log = LogFactory.getLog(EnbBizUniqueIdHelper.class);
	
	/**
	 * �򻺴������ӻ����޸Ķ�Ӧ��map����
	 * 
	 * @param tableName
	 * @param tableParam
	 * @param enbId
	 * @param map
	 */
	public static void addOrUpdate(String tableName, String tableParam,
			long enbId, Map<Integer, Integer> map) {
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_PHY_CELL_ID.equals(tableParam)) {
			// �򻺴�������С������ID������
			EnbCache.getInstance().addOrUpdatePci(enbId, map);
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// �򻺴�������С���߼�������
			EnbCache.getInstance().addOrUpdateRsi(enbId, map);
		}
	}
	
	/**
	 * ���˲���ɾ����Ӧ�Ļ�������
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizKey
	 */
	public static void postDelete(long moId, String tableName, XBizRecord bizKey) {
		
		// С������������Ҫ������ֶ�
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// ɾ��pci��������
			EnbBizUniqueIdHelper.handleDelete(tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
					EnbBizUniqueIdHelper.getEnbIdByMoId(moId),
					bizKey.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
		}
		// С��PRACH���ñ�����Ҫ������ֶ�
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// ɾ��rsi��������
			EnbBizUniqueIdHelper.handleDelete(tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX,
					EnbBizUniqueIdHelper.getEnbIdByMoId(moId),
					bizKey.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
			
		}
	}
	
	/**
	 * ɾ������map����
	 * 
	 * @param tableName
	 * @param tableParam
	 * @param enbId
	 * @param cid
	 */
	public static void handleDelete(String tableName, String tableParam,
			long enbId, int cid) {
		// ��ѯ��Ҫɾ����map
		Map<Integer, Integer> map = EnbBizUniqueIdHelper.getCacheMapByEnbId(
				enbId, tableName, tableParam);
		if (map != null) {
			// ����cidɾ����Ӧ����
			map.remove(cid);
		}
	}
	
	/**
	 * ����enbId��ѯ����map
	 * 
	 * @param enbId
	 * @param tableName
	 * @param tableParam
	 * @return
	 */
	public static Map<Integer, Integer> getCacheMapByEnbId(long enbId,
			String tableName, String tableParam) {
		
		Map<Integer, Integer> map = null;
		
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_PHY_CELL_ID.equals(tableParam)) {
			// ��ѯpci����map
			map = EnbCache.getInstance().getPciMapByEnbId(enbId);
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// ��ѯrsi����map
			map = EnbCache.getInstance().getRsiMapByEnbId(enbId);
		}
		if (map == null) {
			log.warn("getCacheMapByEnbId is null: enbId=" + enbId
					+ ", tableName=" + tableName + ", tableParam=" + tableParam);
		}
		return map;
	}
	
	/**
	 * ��ѯ��Ӧmap�����Ѿ���ʹ�õı�ʶ
	 * 
	 * @param tableName
	 * @param tableParam
	 * @return
	 */
	public static List<Integer> queryAll(String tableName, String tableParam) {
		
		List<Integer> list = new ArrayList<Integer>();
		
		Map<Long, Map<Integer, Integer>> firstMap = null;
		
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_PHY_CELL_ID.equals(tableParam)) {
			// ��ȡС������ID��map,1��map
			firstMap = EnbCache.getInstance().getPciMap();
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// ��ȡС���߼������е�map,1��map
			firstMap = EnbCache.getInstance().getRsiMap();
			
		}
		// ����1��map
		Set<Long> keySet = firstMap.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			// ��ȡ2��Map
			Map<Integer, Integer> secondMap = firstMap.get(it.next());
			// ����2��map
			Set<Integer> set = secondMap.keySet();
			Iterator<Integer> iterator = set.iterator();
			while (iterator.hasNext()) {
				// ��ȡ����ID
				Integer id = secondMap.get(iterator.next());
				// ȥ���ظ��ı�ʶ
				if (!list.contains(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}
	
	/**
	 * ΪС�����������ȫ��Ψһ��С������ID ΪС���������ñ����ȫ��Ψһ���߼�������
	 * 
	 * @param enb
	 * @param records
	 * @param min
	 * @param max
	 * @param tableName
	 * @param tableParam
	 * @throws Exception
	 */
	public static synchronized void createNetUniqueId(Enb enb,
			List<XBizRecord> records, String tableName) throws Exception {
		// С������������Ҫ������ֶ�
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// ��ȡС�������ʶ��Χ
			/*
			 * int pciMin = 0; int pciMax = 503;
			 */
			EnbBizUniqueIdHelper.handleData(enb, records, tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		}
		// С��PRACH���ñ�����Ҫ������ֶ�
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// ��ȡ�����б�ʶ��Χ
			/*
			 * int rsiMin = 0; int rsiMax = 837;
			 */
			EnbBizUniqueIdHelper.handleData(enb, records, tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
			
		}
	}
	
	/**
	 * �������ҵ��
	 * 
	 * @param enb
	 * @param records
	 * @param min
	 * @param max
	 * @param tableName
	 * @param tableParam
	 * @throws Exception
	 */
	public static void handleData(Enb enb, List<XBizRecord> records,
			String tableName, String tableParam) throws Exception {
		// ��ȡ��ǰ���б�ʹ�õı�ʶ
		List<Integer> ids = EnbBizUniqueIdHelper
				.queryAll(tableName, tableParam);
		
		if (null == ids) {
			ids = new ArrayList<Integer>();
		}
		
		// ��ȡ��Ҫ����ID������
		int idNums = records.size();
		
		// ��ȡ���䷶Χ
		XList bizMetaBy = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		XList fieldMeta = bizMetaBy.getFieldMeta(tableParam);
		int[] range = fieldMeta.getRangeByRangeText();
		int min = range[0];
		int max = range[1];
		
		// �жϱ�ʶ�����Ƿ񹻷���
		if ((ids.size() + idNums) > (max - min + 1)) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_not_enough"));
		}
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int index = 0;
		// �ڱ�ʶ��Χ��Ѱ�ҿ��еı�ʶ
		for (int i = min; i <= max; i++) {
			if (!ids.contains(i)) {
				// ��ȡҪ�޸ĵ�һ������
				XBizRecord xBizRecord = records.get(index);
				
				int cellId = xBizRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
				// ����ñ�ʶ����,ʹ�øñ�ʶ�޸�ģ������
				xBizRecord
						.addField(new XBizField(tableParam, String.valueOf(i)));
				// ��¼��Ӧ��ϵ
				map.put(cellId, i);
				
				// �жϱ�ʶ�Ƿ��Ѿ�������
				if (0 == (idNums - index - 1)) {
					break;
				}
				// ��һ������
				index++;
			}
		}
		// �����ݴ��뻺����
		EnbBizUniqueIdHelper.addOrUpdate(tableName, tableParam, enb.getEnbId(),
				map);
	}
	
	/**
	 * ����enbId��cid��ѯ��Ӧ��psi��rsi�Ȼ�������
	 * 
	 * @param enbId
	 * @param cid
	 * @param tableName
	 * @param tableParam
	 * @return
	 */
	public static int getValueByKey(long enbId, int cid, String tableName,
			String tableParam) {
		
		Map<Integer, Integer> map = EnbBizUniqueIdHelper.getCacheMapByEnbId(
				enbId, tableName, tableParam);
		if (null == map || !map.containsKey(cid)) {
			return -1;
		}
		Integer value = map.get(cid);
		return value;
	}
	
	/**
	 * ����psi��rsi�Ȼ���map����
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 */
	public static void postUpdate(Long moId, String tableName,
			XBizRecord bizRecord) {
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// ����pci����
			EnbBizUniqueIdHelper.handleUpdate(moId, bizRecord, tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// ����rsi����
			EnbBizUniqueIdHelper.handleUpdate(moId, bizRecord, tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
		}
	}
	
	/**
	 * ����enbId���»���map
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param tableName
	 * @param tableParam
	 */
	public static void handleUpdate(long moId, XBizRecord bizRecord,
			String tableName, String tableParam) {
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		// ��ȡ����
		Map<Integer, Integer> map = EnbBizUniqueIdHelper.getCacheMapByEnbId(
				enbId, tableName, tableParam);
		if (map == null) {
			return;
		}
		int value = bizRecord.getIntValue(tableParam);
		int key = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		map.put(key, value);
		// ���»���
		EnbBizUniqueIdHelper.addOrUpdate(tableName, tableParam, enbId, map);
	}
	
	/**
	 * ͨ��MoId��ȡenbId
	 * 
	 * @param moId
	 * @return
	 */
	public static long getEnbIdByMoId(long moId) {
		// ��ȡenbId
		Map<Long, Enb> mapByMoId = EnbCache.getInstance().getMapByMoId();
		Enb enb = mapByMoId.get(moId);
		long enbId = enb.getEnbId();
		return enbId;
	}
	
	/**
	 * ��ȡһ�����е�pci��rsi��
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getFreeValue(int enbType, String protocolVersion,
			String tableName, String tableParam) throws Exception {
		// ��ȡ��ǰ���б�ʹ�õı�ʶ
		List<Integer> ids = EnbBizUniqueIdHelper
				.queryAll(tableName, tableParam);
		if (null == ids) {
			ids = new ArrayList<Integer>();
		}
		// ��ȡ���䷶Χ
		XList bizMetaBy = EnbBizHelper.getBizMetaBy(enbType, protocolVersion,
				tableName);
		XList fieldMeta = bizMetaBy.getFieldMeta(tableParam);
		int[] range = fieldMeta.getRangeByRangeText();
		int min = range[0];
		int max = range[1];
		
		// �жϱ�ʶ�����Ƿ񹻷���
		if ((ids.size() + 1) > (max - min + 1)) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_not_enough"));
		}
		
		int result = -1;
		for (int i = min; i <= max; i++) {
			if (!ids.contains(i)) {
				result = i;
				break;
			}
		}
		
		// ���û���ҵ�,���ʾ�޿��б�ʶ
		if (-1 == result) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_not_enough"));
		}
		return result;
	}
	
	/**
	 * �򻺴�������pci
	 * 
	 * @param enbId
	 * @param cid
	 * @param pci
	 */
	public static void addPciCache(long enbId, int cid, int pci) {
		Map<Integer, Integer> pciMap = EnbCache.getInstance().getPciMapByEnbId(
				enbId);
		if (null == pciMap) {
			pciMap = new HashMap<Integer, Integer>();
		}
		pciMap.put(cid, pci);
		EnbCache.getInstance().addOrUpdatePci(enbId, pciMap);
	}
	
	/**
	 * �򻺴�������rsi
	 * 
	 * @param enbId
	 * @param cid
	 * @param rsi
	 */
	public static void addRsiCache(long enbId, int cid, int rsi) {
		Map<Integer, Integer> rsiMap = EnbCache.getInstance().getRsiMapByEnbId(
				enbId);
		if (null == rsiMap) {
			rsiMap = new HashMap<Integer, Integer>();
		}
		rsiMap.put(cid, rsi);
		EnbCache.getInstance().addOrUpdateRsi(enbId, rsiMap);
	}
	
	/**
	 * ɾ������pci
	 * @param enbId
	 * @param cid
	 */
	public static void deletePciCache(long enbId, int cid) {
		handleDelete(EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID, enbId, cid);
	}
	
	/**
	 * ɾ������rsi
	 * @param enbId
	 * @param cid
	 */
	public static void deleteRsiCache(long enbId, int cid) {
		handleDelete(EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, enbId, cid);
	}
	
	/**
	 * ��ѯ�ж���С��ʹ�ø�pci��rsi
	 * @param tableName
	 * @param tableParam
	 * @param oldValue
	 * @return
	 */
	public static int queryNumSameValue(String tableName, String tableParam,
			int value) {
		List<Integer> list = new ArrayList<Integer>();
		
		Map<Long, Map<Integer, Integer>> firstMap = null;
		
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_PHY_CELL_ID.equals(tableParam)) {
			// ��ȡС������ID��map,1��map
			firstMap = EnbCache.getInstance().getPciMap();
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// ��ȡС���߼������е�map,1��map
			firstMap = EnbCache.getInstance().getRsiMap();
			
		}
		// ����1��map
		Set<Long> keySet = firstMap.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			// ��ȡ2��Map
			Map<Integer, Integer> secondMap = firstMap.get(it.next());
			// ����2��map
			Set<Integer> set = secondMap.keySet();
			Iterator<Integer> iterator = set.iterator();
			while (iterator.hasNext()) {
				// ��ȡ����ID
				Integer id = secondMap.get(iterator.next());
				// ȥ���ظ��ı�ʶ
				if (id == value) {
					list.add(id);
				}
			}
		}
		return list.size();
	}
}
