/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.mcbts.core.model.oamManage.McbtsSupportedBiz;

/**
 * 
 * 基站所支持的业务 缓存
 * 
 * @author chenshaohua
 * 
 */

public class UnsupportedMocCache {

	private static final UnsupportedMocCache instance = new UnsupportedMocCache();

	// <type+version, <moc, value>>
	private static Map<String, Map<Integer, Integer>> studyResultMap 
		= new ConcurrentHashMap<String, Map<Integer, Integer>>();

	private UnsupportedMocCache() {

	}

	public static UnsupportedMocCache getInstance() {
		return instance;
	}

	/**
	 * 根据基站类型、版本号、moc值查询是否支持
	 * 
	 * @param
	 * @return
	 */
	public boolean queryIsSupported(int btsType, String version, int moc) {
		String typeAndVersion = createKey(btsType, version);
		Map<Integer, Integer> mocMap = studyResultMap.get(typeAndVersion);
		if (mocMap != null && mocMap.get(moc) != null && mocMap.get(moc) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 保存一个映射
	 * 
	 * @param mcbtsSupportedBiz
	 */
	public void addOneRecord(int btsType, String version, int moc, Integer value) {
		// btsSupportedMocMap的key
		String key = createKey(btsType, version);
		if (studyResultMap.get(key) == null) {
			// 缓存中没有此 type+version,则加入该type+version
			Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
			tmpMap.put(moc, value);
			studyResultMap.put(key, tmpMap);
		} else {
			studyResultMap.get(key).put(moc, value);
		}

	}

	/**
	 * 清除缓存
	 */
	public void clearCache(int btsType, String version) {
		String key = createKey(btsType, version);
		studyResultMap.remove(key);
	}
	
	private String createKey(int btsType, String version) {
		String key = btsType + "/" + version;
		return key;
	}

	public Map<String, Map<Integer, Integer>> getBtsUnsupportedMocMap() {
		return studyResultMap;
	}
	

}
