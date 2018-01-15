/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * eNB业务配置缓存
 * 
 * @author fanhaoyu
 * 
 */

public class EnbVersionBizConfigCache {

	private Log log = LogFactory.getLog(EnbVersionBizConfigCache.class);

	private static final EnbVersionBizConfigCache instance = new EnbVersionBizConfigCache();

	// eNB类型 -- 版本号  -- 功能集合
	private Map<Integer, Map<String, XCollection>> configMap = new HashMap();

	private EnbVersionBizConfigCache() {
	}

	public static EnbVersionBizConfigCache getInstance() {
		return instance;
	}

	private int createKey(int moTypeId, int enbTypeId) {
		return moTypeId * 65536 + enbTypeId;
	}

	/**
	 * 初始化
	 * 
	 * @param facade
	 * @throws Exception
	 */
	public synchronized void initialize(XMoBizConfigFacade facade)
			throws Exception {
		Set<Integer> enbTypeIds = EnbTypeDD.getSupportedTypeIds();
		for (int enbTypeId : enbTypeIds) {
			Map map = facade.queryUIMap(MoTypeDD.ENODEB, enbTypeId);
			configMap.put(enbTypeId, map);
		}
		// 初始化缓存成功
		log.debug("init EnbVersionBizConfigCache success");
	}

	/**
	 * 获取指定版本的所有配置
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public XCollection getVersionConfig(int enbTypeId, String protocolVersion) {
		try {
			return configMap.get(enbTypeId).get(protocolVersion);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取表配置
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public XList getTableConfig(int enbTypeId, String protocolVersion,
			String tableName) {
		XCollection xCollection = getVersionConfig(enbTypeId, protocolVersion);
		if (xCollection == null)
			return null;
		return xCollection.getBizMetaBy(tableName);
	}

	/**
	 * 获取指定版本的指定业务表中的所有字段配置
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public List<XList> getFieldConfigs(int enbTypeId, String protocolVersion,
			String tableName) {
		if (configMap == null)
			return null;
		XList tableConfig = getTableConfig(enbTypeId, protocolVersion,
				tableName);
		if (tableConfig == null)
			return null;
		return tableConfig.getAllFields();
	}

	/**
	 * 获取表中所有字段名
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public List<String> getFields(int enbTypeId, String protocolVersion,
			String tableName) {
		List<XList> fieldConfigs = getFieldConfigs(enbTypeId, protocolVersion,
				tableName);
		if (fieldConfigs == null)
			return Collections.emptyList();
		List<String> fields = new LinkedList<String>();
		for (XList fieldConfig : fieldConfigs) {
			fields.add(fieldConfig.getName());
		}
		return fields;
	}

	public Map<Integer, Map<String, XCollection>> getConfigMap() {
		return configMap;
	}
	
	/**
	 * 返回支持的网元类型和版本号.  版本类型ID -- 版本号列表
	 * @return
	 */
	public Map<Integer, List<String>> getSupportedVersions() {
		Map map = new HashMap();
		try {
			Iterator<Integer> itr = configMap.keySet().iterator();
			while (itr.hasNext()) {
				int enbTypeId = itr.next();
				Map<String, XCollection> subMap = configMap.get(enbTypeId);
				Set<String> versions = subMap.keySet();
				List<String> versionList = new LinkedList();
				versionList.addAll(versions);
				Collections.sort(versionList, new Comparator<String>(){
					@Override
					public int compare(String o1, String o2) {						
						return o2.compareToIgnoreCase(o1);
					}					
					
				});
				map.put(enbTypeId, versionList);
			}
		} catch (Exception e) {
			
		}
		return map;
	}
}
