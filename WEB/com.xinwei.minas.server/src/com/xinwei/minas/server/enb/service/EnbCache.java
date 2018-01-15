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
 * Enb����
 * 
 * @author chenjunhua
 * 
 */

public class EnbCache {
	
	private static final EnbCache instance = new EnbCache();
	
	private Map<Long, Enb> mapByEnbId = new ConcurrentHashMap<Long, Enb>();
	
	private Map<Long, Enb> mapByMoId = new ConcurrentHashMap<Long, Enb>();
	
	// 1��map��keyΪenbID,����map��keyΪС���߼�ID,valueΪС������ID
	private Map<Long, Map<Integer, Integer>> pciMap = new ConcurrentHashMap<Long, Map<Integer, Integer>>();
	
	// 1��map��keyΪenbID,����map��keyΪС���߼�ID,valueΪС���߼�������
	private Map<Long, Map<Integer, Integer>> rsiMap = new ConcurrentHashMap<Long, Map<Integer, Integer>>();
	
	// �ʲ���Ϣ����
	private Map<Long, Map<String, EnbAsset>> assetsMap = new ConcurrentHashMap<Long, Map<String, EnbAsset>>();
	
	private EnbCache() {
	}
	
	public static EnbCache getInstance() {
		return instance;
	}
	
	/**
	 * �򻺴��м����ʲ�
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
	 * ���ݻ�վID��ѯ�ʲ�
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<String, EnbAsset> queryAssetByEnbId(long enbId) {
		return assetsMap.get(enbId);
	}
	
	/**
	 * ɾ���ʲ�
	 * @param enbId
	 * @param productionSN
	 */
	public void deleteAsset(long enbId,String productionSN) {
		Map<String, EnbAsset> map = queryAssetByEnbId(enbId);
		map.remove(productionSN);
	}
	
	/**
	 * ����enbId��ȡ�û�վ�µ��ʲ��б�
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
	 * ����enbId�����кŲ�ѯ�ʲ�
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
	 * �򻺴��м���enb������С����С���߼�ID��С������ID�Ķ�Ӧ��ϵ
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
	 * ����enbIdɾ��pci����
	 * @param enbId
	 */
	public void deletePciByEnb(long enbId) {
		pciMap.remove(enbId);
	}
	
	/**
	 * ��ȡPci��map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Long, Map<Integer, Integer>> getPciMap() {
		return pciMap;
	}
	
	/**
	 * ����enbId��ȡС���߼�ID��С������ID������map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Integer, Integer> getPciMapByEnbId(long enbId) {
		return pciMap.get(enbId);
	}
	
	/**
	 * �򻺴��м���С���������ñ��߼�����������
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
	 * ��ȡrsi��map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Long, Map<Integer, Integer>> getRsiMap() {
		return rsiMap;
	}
	
	/**
	 * ����enbIdɾ��rsi����
	 * @param enbId
	 */
	public void deleteRsiByEnb(long enbId) {
		rsiMap.remove(enbId);
	}
	
	/**
	 * ����enbId��ȡС���߼�ID��С���߼������е�����map
	 * 
	 * @param enbId
	 * @return
	 */
	public Map<Integer, Integer> getRsiMapByEnbId(long enbId) {
		return rsiMap.get(enbId);
	}
	
	/**
	 * ����enbId��ѯEnb
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
	 * ����Mo Id��ѯEnb
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
	 * ���ӻ����Enb
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
		// ����Mo����
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
	 * ɾ��McBTS
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
	 * ��ѯ���л�վ
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
	 * ��ѯ����EnbId�б�
	 * 
	 * @return
	 */
	public Set<Long> queryAllEnbId() {
		return mapByEnbId.keySet();
	}
	
	/**
	 * ��ȡ��moIdΪkey�Ļ�վmap
	 * 
	 * @return
	 */
	public Map<Long, Enb> getMapByMoId() {
		return mapByMoId;
	}
	
	/**
	 * ��ȡ��enbIdΪkey�Ļ�վmap
	 * 
	 * @return
	 */
	public Map<Long, Enb> getMapByEnbId() {
		return mapByEnbId;
	}
	
}
