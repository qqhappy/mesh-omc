/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.facade.EnbRealtimeMonitorFacade;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;

/**
 * 
 * eNB实时性能统计项配置缓存
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeItemConfigCache {

	private static EnbRealtimeItemConfigCache instance = new EnbRealtimeItemConfigCache();

	private Map<Integer, EnbRealtimeItemConfig> map;

	private EnbRealtimeItemConfigCache() {
	}

	public static EnbRealtimeItemConfigCache getInstance() {
		return instance;
	}

	public synchronized void initialize(
			EnbRealtimeMonitorFacade realtimeMonitorFacade) throws Exception {
		if (map == null) {
			map = new LinkedHashMap<Integer, EnbRealtimeItemConfig>();
		}
		List<EnbRealtimeItemConfig> configList = realtimeMonitorFacade
				.queryItemConfig();
		if (configList != null) {
			for (EnbRealtimeItemConfig config : configList) {
				map.put(config.getItemId(), config);
			}
		}
	}

	/**
	 * 获取所有统计项配置
	 * 
	 * @return
	 */
	public Map<Integer, EnbRealtimeItemConfig> getAllItemConfigs() {
		return map;
	}

	public EnbRealtimeItemConfig getConfig(int itemId) {
		if (map == null)
			return null;
		return map.get(itemId);
	}

}
