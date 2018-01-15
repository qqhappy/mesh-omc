/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.server.core.conf.service.MoCache;

/**
 * 
 * Enb缓存
 * 
 * @author chenjunhua
 * 
 */

public class EnbCache {
	
	private static final EnbCache instance = new EnbCache();
	
	private Map<Long, Enb> mapByEnbId = new ConcurrentHashMap<Long, Enb>();
	
	private Map<Long, Enb> mapByMoId = new ConcurrentHashMap<Long, Enb>();
	
	// 1级map中key为enbID,二级map中key为小区逻辑ID,value为小区物理ID
	private Map<Long, Map<Integer, Integer>> pciMap = new ConcurrentHashMap<Long, Map<Integer, Integer>>();
	
	// 1级map中key为enbID,二级map中key为小区逻辑ID,value为小区逻辑根序列
	private Map<Long, Map<Integer, Integer>> rsiMap = new ConcurrentHashMap<Long, Map<Integer, Integer>>();
	
	// 资产信息缓存
	private Map<Long, Map<String, EnbAsset>> assetsMap = new ConcurrentHashMap<Long, Map<String, EnbAsset>>();
	
	private EnbCache() {
	}
	
	public static EnbCache getInstance() {
		return instance;
	}
	
	/**
	 * 向缓存中加入资产
	 * 
	 * @param enbId
	 * @param asset
	 */
	public void addOrUpdateAsset(long enbId, EnbAsset asset) {
		if (null != asset && null != asset.getProductionSN()
				&& !"".equals(asset.getProductionSN())) {
			Map<String, EnbAsset> map = queryAssetByEnbId(enbId);
			if(null == map) {
				map = new ConcurrentHashMap<String, EnbAsset>();
				assetsMap.put(enbId, map);
			}
			map.put(asset.getProductionSN(), asset);
		}
	}
	
	/**
	 * 根据基站ID查询资产
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<String, EnbAsset> queryAssetByEnbId(long enbId) {
		return assetsMap.get(enbId);
	}
	
	/**
	 * 删除资产
	 * @param enbId
	 * @param productionSN
	 */
	public void deleteAsset(long enbId,String productionSN) {
		Map<String, EnbAsset> map = queryAssetByEnbId(enbId);
		map.remove(productionSN);
	}
	
	/**
	 * 根据enbId获取该基站下的资产列表
	 * @param enbId
	 * @return
	 */
	public List<EnbAsset> getAssetListByEnbId(long enbId) {
		List<EnbAsset> assets = new ArrayList<EnbAsset>();
		Map<String, EnbAsset> map = queryAssetByEnbId(enbId);
		if(null != map) {
			for (String productionSN : map.keySet()) {
				assets.add(map.get(productionSN));
			}
		}
		return assets;
	}
	
	/**
	 * 根据enbId和序列号查询资产
	 * @param enbId
	 * @param productionSN
	 * @return
	 */
	public EnbAsset queryAssetByEnbAndProdSN(long enbId,String productionSN) {
		Map<String, EnbAsset> map = queryAssetByEnbId(enbId);
		if(null == map) {
			return null;
		}
		return map.get(productionSN);
	}
	
	
	/**
	 * 向缓存中加入enb下所有小区的小区逻辑ID与小区物理ID的对应关系
	 * 
	 * @param enbId
	 * @param phyCid
	 */
	public void addOrUpdatePci(long enbId, Map<Integer, Integer> map) {
		if (null != map) {
			pciMap.put(enbId, map);
		}
	}
	
	/**
	 * 根据enbId删除pci缓存
	 * @param enbId
	 */
	public void deletePciByEnb(long enbId) {
		pciMap.remove(enbId);
	}
	
	/**
	 * 获取Pci的map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Long, Map<Integer, Integer>> getPciMap() {
		return pciMap;
	}
	
	/**
	 * 根据enbId获取小区逻辑ID与小区物理ID的数据map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Integer, Integer> getPciMapByEnbId(long enbId) {
		return pciMap.get(enbId);
	}
	
	/**
	 * 向缓存中加入小区参数配置表逻辑根序列数据
	 * 
	 * @param enbId
	 * @param phyCid
	 */
	public void addOrUpdateRsi(long enbId, Map<Integer, Integer> map) {
		if (null != map) {
			rsiMap.put(enbId, map);
		}
	}
	
	/**
	 * 获取rsi的map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Long, Map<Integer, Integer>> getRsiMap() {
		return rsiMap;
	}
	
	/**
	 * 根据enbId删除rsi缓存
	 * @param enbId
	 */
	public void deleteRsiByEnb(long enbId) {
		rsiMap.remove(enbId);
	}
	
	/**
	 * 根据enbId获取小区逻辑ID与小区逻辑根序列的数据map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Integer, Integer> getRsiMapByEnbId(long enbId) {
		return rsiMap.get(enbId);
	}
	
	/**
	 * 根据enbId查询Enb
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	public Enb queryByEnbId(Long enbId) {
		Enb enb = mapByEnbId.get(enbId);
		return enb;
	}
	
	/**
	 * 根据Mo Id查询Enb
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Enb queryByMoId(Long moId) {
		Enb enb = mapByMoId.get(moId);
		return enb;
	}
	
	public boolean enbExists(long enbId) {
		return mapByEnbId.get(enbId) != null;
	}
	
	/**
	 * 增加或更新Enb
	 * 
	 * @param mcBts
	 */
	public void addOrUpdate(Enb enb) {
		Enb cacheBts = mapByMoId.get(enb.getMoId());
		if (cacheBts != null) {
			update(enb);
		}
		mapByEnbId.put(enb.getEnbId(), enb);
		mapByMoId.put(enb.getMoId(), enb);
		// 加入Mo缓存
		MoCache.getInstance().addOrUpdate(enb);
	}
	
	private void update(Enb enb) {
		Enb cacheBts = mapByMoId.get(enb.getMoId());
		cacheBts.setDescription(enb.getDescription());
		cacheBts.setEnbType(enb.getEnbType());
		cacheBts.setManageStateCode(enb.getManageStateCode());
		cacheBts.setName(enb.getName());
		cacheBts.setPrivateIp(enb.getPrivateIp());
		cacheBts.setPublicIp(enb.getPublicIp());
		cacheBts.setPublicPort(enb.getPublicPort());
		cacheBts.setRegisterState(enb.getRegisterState());
		cacheBts.setProtocolVersion(enb.getProtocolVersion());
		cacheBts.setSoftwareVersion(enb.getSoftwareVersion());
		cacheBts.setSyncDirection(enb.getSyncDirection());
	}
	
	/**
	 * 删除McBTS
	 * 
	 * @param moId
	 */
	public void delete(Long moId) {
		Enb enb = mapByMoId.remove(moId);
		if (enb != null) {
			mapByEnbId.remove(enb.getEnbId());
		}
		MoCache.getInstance().delete(moId);
	}
	
	/**
	 * 查询所有基站
	 * 
	 * @return
	 */
	public List<Enb> queryAll() {
		List<Enb> enbList = new ArrayList<Enb>();
		Set<Long> keySet = mapByMoId.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			Enb enb = mapByMoId.get(it.next());
			enbList.add(enb);
		}
		
		Collections.sort(enbList, new Comparator<Enb>() {
			
			@Override
			public int compare(Enb o1, Enb o2) {
				return (int) (o1.getEnbId().longValue() - o2.getEnbId()
						.longValue());
			}
		});
		
		return enbList;
	}
	
	/**
	 * 查询所有EnbId列表
	 * 
	 * @return
	 */
	public Set<Long> queryAllEnbId() {
		return mapByEnbId.keySet();
	}
	
	/**
	 * 获取以moId为key的基站map
	 * 
	 * @return
	 */
	public Map<Long, Enb> getMapByMoId() {
		return mapByMoId;
	}
	
	/**
	 * 获取以enbId为key的基站map
	 * 
	 * @return
	 */
	public Map<Long, Enb> getMapByEnbId() {
		return mapByEnbId;
	}
	
}
