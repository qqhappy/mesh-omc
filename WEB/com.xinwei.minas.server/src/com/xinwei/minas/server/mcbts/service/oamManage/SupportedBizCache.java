/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

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

public class SupportedBizCache {

	private static final SupportedBizCache instance = new SupportedBizCache();

	// <type+version, <moc, value>>
	private Map<String, Map<Integer, Integer>> btsSupportedMocMap = new HashMap<String, Map<Integer, Integer>>();

	private SupportedBizCache() {

	}

	public static SupportedBizCache getInstance() {
		return instance;
	}

	/**
	 * ���ݻ�վ���͡��汾�š�mocֵ��ѯ�Ƿ�֧��
	 * 
	 * @param
	 * @return
	 */
	public Integer queryByBtsTypeAndVersionAndMoc(int btsType, String version,
			int moc) {
		String typeAndVersion = btsType + version;
		Map<Integer, Integer> mocMap = btsSupportedMocMap.get(typeAndVersion);
		if (mocMap == null) {
			return null;
		} else {
			Iterator it = mocMap.keySet().iterator();
			while (it.hasNext()) {
				Integer mocMapKey = (Integer) it.next();
				if (mocMapKey.intValue() == moc) {
					return mocMap.get(mocMapKey);
				}
			}
			return null;
		}
	}

	/**
	 * ������߸���һ��ӳ��
	 * 
	 * @param mcbtsSupportedBiz
	 */
	public void saveOrUpdate(McbtsSupportedBiz supportedBiz) {
		// btsSupportedMocMap��key
		String key = supportedBiz.getBtsType()
				+ supportedBiz.getSoftwareVersion();
		if (btsSupportedMocMap.get(key) == null) {
			// ������û�д� type+version,������type+version
			Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
			tmpMap.put(supportedBiz.getMoc(), supportedBiz.getSupport());
			btsSupportedMocMap.put(key, tmpMap);
		} else {
			// �������д�type+version,�����mocMap
			Map<Integer, Integer> mocMap = btsSupportedMocMap.get(key);
			mocMap.put(supportedBiz.getMoc(), supportedBiz.getSupport());
		}

	}

	// public void saveOrUpdate(McbtsSupportedBiz supportedBiz) {
	// // btsSupportedMocMap��key
	// String key = supportedBiz.getBtsType()
	// + supportedBiz.getSoftwareVersion() + supportedBiz.getMoc();
	// btsSupportedMocMap.put(key, value);
	//
	// }

	/**
	 * ���ݻ�վ���͡��汾�š�mocֵ��ѯ�Ƿ�֧��
	 * 
	 * @param typeAndVersion
	 * @return
	 */
	// public Boolean queryIfSupported(String typeAndVersion, int moc) {
	// Map<Integer, Integer> mocMap = btsSupportedMocMap.get(typeAndVersion);
	// if (mocMap == null) {
	// // û�и�����¼��������
	// return true;
	// } else {
	// Iterator it = mocMap.keySet().iterator();
	// while (it.hasNext()) {
	// if (mocMap.get(it.next()).intValue() == moc) {
	// return true;
	// }
	// }
	// return false;
	// }
	// }

	/**
	 * �������
	 */
	public void clearCache(int btsType, String version) {
		String key = btsType + version;
		btsSupportedMocMap.remove(key);
	}

	/**
	 * ����ֵ
	 * 
	 * @param btsType
	 * @param version
	 * @param moc
	 * @param value
	 */
	public void update(int btsType, String version, int moc, Integer value) {
		String key = btsType + version;
		Map<Integer, Integer> mocMap = btsSupportedMocMap.get(key);
		mocMap.put(moc, value);
	}

}
