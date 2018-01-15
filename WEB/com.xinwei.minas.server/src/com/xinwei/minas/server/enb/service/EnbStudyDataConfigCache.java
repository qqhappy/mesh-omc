/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.server.enb.dao.EnbStudyDataConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbStudyDataConfigParser;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * eNB自学习数据配置缓存
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStudyDataConfigCache {

	private static EnbStudyDataConfigCache instance = new EnbStudyDataConfigCache();

	// key=version, value=[key=tableName, value=fieldConfigs]
	private Map<String, Map<String, XList>> cache = new HashMap<String, Map<String, XList>>();

	public static EnbStudyDataConfigCache getInstance() {
		return instance;
	}

	private EnbStudyDataConfigCache() {
	}

	public void initialize(EnbStudyDataConfigDAO enbStudyDataConfigDAO)
			throws Exception {
		Map<String, String> configMap = enbStudyDataConfigDAO.queryAll();
		if (configMap == null || configMap.isEmpty())
			return;
		for (String version : configMap.keySet()) {
			Map<String, XList> tableConfig = EnbStudyDataConfigParser
					.parse(configMap.get(version));
			cache.put(version, tableConfig);
		}
	}

	public Map<String, Map<String, XList>> queryAllConfig() {
		return cache;
	}

	public void addConfig(String version, Map<String, XList> dataConfig) {
		cache.put(version, dataConfig);
	}

	public XList getDataConfig(String version, String tableName) {
		Map<String, XList> map = cache.get(version);
		if (map == null)
			return null;
		return map.get(tableName);
	}

	public Map<String, XList> getDataConfig(String version) {
		return cache.get(version);
	}

	public boolean isConfigExist(String version) {
		Map<String, XList> map = cache.get(version);
		return map != null;
	}

	/**
	 * 获取指定版本支持的指定表中的字段名
	 * 
	 * @param version
	 * @param tableName
	 * @return
	 */
	public List<String> getSupportedFields(String version, String tableName) {
		Map<String, XList> map = cache.get(version);
		if (map == null)
			return null;
		XList xList = map.get(tableName);
		List<XList> fieldList = xList.getAllFields();
		if (fieldList == null || fieldList.isEmpty())
			return null;
		List<String> list = new ArrayList<String>();
		for (XList field : fieldList) {
			list.add(field.getName());
		}
		return list;
	}

}
