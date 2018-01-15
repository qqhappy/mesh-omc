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
 * enbBiz配置有关所有需要生成唯一标识的管理类
 * 
 * @author chenlong
 * 
 */

public class EnbBizUniqueIdHelper {
	
	private static final Log log = LogFactory.getLog(EnbBizUniqueIdHelper.class);
	
	/**
	 * 向缓存中增加或者修改对应的map数据
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
			// 向缓存中增加小区物理ID的数据
			EnbCache.getInstance().addOrUpdatePci(enbId, map);
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// 向缓存中增加小区逻辑根序列
			EnbCache.getInstance().addOrUpdateRsi(enbId, map);
		}
	}
	
	/**
	 * 过滤并且删除响应的缓存数据
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizKey
	 */
	public static void postDelete(long moId, String tableName, XBizRecord bizKey) {
		
		// 小区参数表中需要处理的字段
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// 删除pci缓存数据
			EnbBizUniqueIdHelper.handleDelete(tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
					EnbBizUniqueIdHelper.getEnbIdByMoId(moId),
					bizKey.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
		}
		// 小区PRACH配置表中需要处理的字段
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// 删除rsi缓存数据
			EnbBizUniqueIdHelper.handleDelete(tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX,
					EnbBizUniqueIdHelper.getEnbIdByMoId(moId),
					bizKey.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
			
		}
	}
	
	/**
	 * 删除缓存map数据
	 * 
	 * @param tableName
	 * @param tableParam
	 * @param enbId
	 * @param cid
	 */
	public static void handleDelete(String tableName, String tableParam,
			long enbId, int cid) {
		// 查询出要删除的map
		Map<Integer, Integer> map = EnbBizUniqueIdHelper.getCacheMapByEnbId(
				enbId, tableName, tableParam);
		if (map != null) {
			// 根据cid删除对应数据
			map.remove(cid);
		}
	}
	
	/**
	 * 根据enbId查询缓存map
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
			// 查询pci缓存map
			map = EnbCache.getInstance().getPciMapByEnbId(enbId);
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// 查询rsi缓存map
			map = EnbCache.getInstance().getRsiMapByEnbId(enbId);
		}
		if (map == null) {
			log.warn("getCacheMapByEnbId is null: enbId=" + enbId
					+ ", tableName=" + tableName + ", tableParam=" + tableParam);
		}
		return map;
	}
	
	/**
	 * 查询对应map所有已经被使用的标识
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
			// 获取小区物理ID的map,1级map
			firstMap = EnbCache.getInstance().getPciMap();
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// 获取小区逻辑根序列的map,1级map
			firstMap = EnbCache.getInstance().getRsiMap();
			
		}
		// 遍历1级map
		Set<Long> keySet = firstMap.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			// 获取2级Map
			Map<Integer, Integer> secondMap = firstMap.get(it.next());
			// 遍历2级map
			Set<Integer> set = secondMap.keySet();
			Iterator<Integer> iterator = set.iterator();
			while (iterator.hasNext()) {
				// 获取参数ID
				Integer id = secondMap.get(iterator.next());
				// 去掉重复的标识
				if (!list.contains(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}
	
	/**
	 * 为小区参数表分配全网唯一的小区物理ID 为小区参数配置表分配全网唯一的逻辑根序列
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
		// 小区参数表中需要处理的字段
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// 获取小区物理标识范围
			/*
			 * int pciMin = 0; int pciMax = 503;
			 */
			EnbBizUniqueIdHelper.handleData(enb, records, tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		}
		// 小区PRACH配置表中需要处理的字段
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// 获取根序列标识范围
			/*
			 * int rsiMin = 0; int rsiMax = 837;
			 */
			EnbBizUniqueIdHelper.handleData(enb, records, tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
			
		}
	}
	
	/**
	 * 处理分配业务
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
		// 获取当前所有被使用的标识
		List<Integer> ids = EnbBizUniqueIdHelper
				.queryAll(tableName, tableParam);
		
		if (null == ids) {
			ids = new ArrayList<Integer>();
		}
		
		// 获取需要分配ID的数量
		int idNums = records.size();
		
		// 获取分配范围
		XList bizMetaBy = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		XList fieldMeta = bizMetaBy.getFieldMeta(tableParam);
		int[] range = fieldMeta.getRangeByRangeText();
		int min = range[0];
		int max = range[1];
		
		// 判断标识数量是否够分配
		if ((ids.size() + idNums) > (max - min + 1)) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_not_enough"));
		}
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int index = 0;
		// 在标识范围中寻找空闲的标识
		for (int i = min; i <= max; i++) {
			if (!ids.contains(i)) {
				// 获取要修改的一条数据
				XBizRecord xBizRecord = records.get(index);
				
				int cellId = xBizRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
				// 如果该标识空闲,使用该标识修改模板数据
				xBizRecord
						.addField(new XBizField(tableParam, String.valueOf(i)));
				// 记录对应关系
				map.put(cellId, i);
				
				// 判断标识是否已经分配完
				if (0 == (idNums - index - 1)) {
					break;
				}
				// 换一条数据
				index++;
			}
		}
		// 将数据存入缓存中
		EnbBizUniqueIdHelper.addOrUpdate(tableName, tableParam, enb.getEnbId(),
				map);
	}
	
	/**
	 * 根据enbId和cid查询对应的psi、rsi等缓存数据
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
	 * 更新psi、rsi等缓存map数据
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 */
	public static void postUpdate(Long moId, String tableName,
			XBizRecord bizRecord) {
		if (EnbConstantUtils.TABLE_NAME_T_CELL_PARA.equals(tableName)) {
			// 更新pci缓存
			EnbBizUniqueIdHelper.handleUpdate(moId, bizRecord, tableName,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)) {
			// 更新rsi缓存
			EnbBizUniqueIdHelper.handleUpdate(moId, bizRecord, tableName,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
		}
	}
	
	/**
	 * 根据enbId更新缓存map
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param tableName
	 * @param tableParam
	 */
	public static void handleUpdate(long moId, XBizRecord bizRecord,
			String tableName, String tableParam) {
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		// 获取缓存
		Map<Integer, Integer> map = EnbBizUniqueIdHelper.getCacheMapByEnbId(
				enbId, tableName, tableParam);
		if (map == null) {
			return;
		}
		int value = bizRecord.getIntValue(tableParam);
		int key = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		map.put(key, value);
		// 更新缓存
		EnbBizUniqueIdHelper.addOrUpdate(tableName, tableParam, enbId, map);
	}
	
	/**
	 * 通过MoId获取enbId
	 * 
	 * @param moId
	 * @return
	 */
	public static long getEnbIdByMoId(long moId) {
		// 获取enbId
		Map<Long, Enb> mapByMoId = EnbCache.getInstance().getMapByMoId();
		Enb enb = mapByMoId.get(moId);
		long enbId = enb.getEnbId();
		return enbId;
	}
	
	/**
	 * 获取一个空闲的pci、rsi等
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getFreeValue(int enbType, String protocolVersion,
			String tableName, String tableParam) throws Exception {
		// 获取当前所有被使用的标识
		List<Integer> ids = EnbBizUniqueIdHelper
				.queryAll(tableName, tableParam);
		if (null == ids) {
			ids = new ArrayList<Integer>();
		}
		// 获取分配范围
		XList bizMetaBy = EnbBizHelper.getBizMetaBy(enbType, protocolVersion,
				tableName);
		XList fieldMeta = bizMetaBy.getFieldMeta(tableParam);
		int[] range = fieldMeta.getRangeByRangeText();
		int min = range[0];
		int max = range[1];
		
		// 判断标识数量是否够分配
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
		
		// 如果没有找到,则表示无空闲标识
		if (-1 == result) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_not_enough"));
		}
		return result;
	}
	
	/**
	 * 向缓存中增加pci
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
	 * 向缓存中增加rsi
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
	 * 删除缓存pci
	 * @param enbId
	 * @param cid
	 */
	public static void deletePciCache(long enbId, int cid) {
		handleDelete(EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID, enbId, cid);
	}
	
	/**
	 * 删除缓存rsi
	 * @param enbId
	 * @param cid
	 */
	public static void deleteRsiCache(long enbId, int cid) {
		handleDelete(EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, enbId, cid);
	}
	
	/**
	 * 查询有多少小区使用该pci或rsi
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
			// 获取小区物理ID的map,1级map
			firstMap = EnbCache.getInstance().getPciMap();
			
		}
		else if (EnbConstantUtils.TABLE_NAME_T_CEL_PRACH.equals(tableName)
				&& EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX
						.equals(tableParam)) {
			// 获取小区逻辑根序列的map,1级map
			firstMap = EnbCache.getInstance().getRsiMap();
			
		}
		// 遍历1级map
		Set<Long> keySet = firstMap.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			// 获取2级Map
			Map<Integer, Integer> secondMap = firstMap.get(it.next());
			// 遍历2级map
			Set<Integer> set = secondMap.keySet();
			Iterator<Integer> iterator = set.iterator();
			while (iterator.hasNext()) {
				// 获取参数ID
				Integer id = secondMap.get(iterator.next());
				// 去掉重复的标识
				if (id == value) {
					list.add(id);
				}
			}
		}
		return list.size();
	}
}
