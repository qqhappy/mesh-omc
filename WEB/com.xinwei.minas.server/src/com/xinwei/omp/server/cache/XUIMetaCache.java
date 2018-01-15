/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * UI元数据缓存
 * 
 * @author chenjunhua
 * 
 */

public class XUIMetaCache {

	private static final XUIMetaCache instance = new XUIMetaCache();

	// 功能哈希表(Key:设备类型 , Value:[key:版本,value=功能集合])
	private Map<Integer, Map<String, XCollection>> uiMap = new HashMap<Integer, Map<String, XCollection>>();

	private boolean initialized = false;

	private XUIMetaCache() {
	}

	public static XUIMetaCache getInstance() {
		return instance;
	}

	/**
	 * 根据网元类型ID和子类型ID创建KEY
	 * 
	 * @param moTypeId
	 * @param subTypeId
	 * @return
	 */
	public int createKey(int moTypeId, int subTypeId) {
		return moTypeId * 65536 + subTypeId;
	}

	/**
	 * 增加设备菜单
	 * 
	 * @param moTypeId
	 * @param version
	 * @param collection
	 */
	public void addMoBizMetas(int moTypeId, int subTypeId, String version,
			XCollection collection) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> map = uiMap.get(key);
		if (map == null) {
			map = new HashMap<String, XCollection>();
			uiMap.put(key, map);
		}
		map.put(version, collection);
	}

	/**
	 * 获取指定设备类型指定版本的功能菜单
	 * 
	 * @param moTypeId
	 * @param version
	 * @return
	 */
	public XCollection getMoBizMetas(int moTypeId, int subTypeId, String version) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> map = uiMap.get(key);
		if (map == null)
			return null;
		return map.get(version);
	}

	/**
	 * 根据UI名称获取描述
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public String getBizDescByName(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			return bizMeta.getDesc();
		} catch (Exception e) {
			return "!" + bizName + "!";
		}
	}

	/**
	 * 获取指定版本指定表的所有字段
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public XList[] getFiledMetas(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			XList[] fieldMetas = bizMeta.getList();
			return fieldMetas;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定网元类型的指定版本的使用业务名称set
	 * 
	 * @param moTypeId
	 * @param version
	 * @return
	 */
	public Set<String> getAllBizName(int moTypeId, int subTypeId, String version) {
		XCollection moUICollection = this.getMoBizMetas(moTypeId, subTypeId,
				version);
		Map<String, XList> bizMap = moUICollection.getBizMap();
		Set<String> bizNameSet = bizMap.keySet();
		return bizNameSet;
	}

	/**
	 * 获取指定版本表的信息
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public XList getBizMetaByName(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			return bizMeta;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定版本表字段的信息
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @param fieldName
	 * @return
	 */
	public XList getFiledMetaByName(int moTypeId, int subTypeId,
			String version, String bizName, String fieldName) {
		XList bizMeta = getBizMetaByName(moTypeId, subTypeId, version, bizName);
		return bizMeta.getFieldMeta(fieldName);
	}

	/**
	 * 获取指定版本中表字段的信息
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @param fieldIndex
	 * @return
	 */
	public XList getFiledMetaByIndex(int moTypeId, int subTypeId,
			String version, String bizName, int fieldIndex) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			List<XList> fieldMetas = bizMeta.getVisiableList();
			return fieldMetas.get(fieldIndex);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定网元类型的UI映射
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, XCollection> getUiMap(Integer key) {
		return uiMap.get(key);
	}

	public Map<Integer, Map<String, XCollection>> getUiMap() {
		return uiMap;
	}

	/**
	 * 获取网管支持指定网元的所有协议版本号(前三位版本号)
	 * 
	 * @param moTypeId
	 * @return
	 */
	public Set<String> getSupportedVersions(Integer moTypeId, int subTypeId) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> versionMap = uiMap.get(key);
		return versionMap.keySet();
	}

	/**
	 * 将缓存设置为已初始化完成
	 */
	public synchronized void setInitialized() {
		this.initialized = true;
	}

	/**
	 * 查看缓存是否已初始化完成
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

}
