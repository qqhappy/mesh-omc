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
 * ��վ��֧�ֵ�ҵ�� ����
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
	 * ���ݻ�վ���͡��汾�š�mocֵ��ѯ�Ƿ�֧��
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
	 * ����һ��ӳ��
	 * 
	 * @param mcbtsSupportedBiz
	 */
	public void addOneRecord(int btsType, String version, int moc, Integer value) {
		// btsSupportedMocMap��key
		String key = createKey(btsType, version);
		if (studyResultMap.get(key) == null) {
			// ������û�д� type+version,������type+version
			Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
			tmpMap.put(moc, value);
			studyResultMap.put(key, tmpMap);
		} else {
			studyResultMap.get(key).put(moc, value);
		}

	}

	/**
	 * �������
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
