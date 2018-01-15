/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.alarm.Alarm;

/**
 * 
 * 告警缓存默认实现 
 * 
 * @author chenjunhua
 * 
 */
@Deprecated
public class AlarmCacheDefaultImpl {

	// 告警缓存
	private Map<Long, Map<String, Alarm>> cacheByMoId = new ConcurrentHashMap();
	
	/**
	 * 初始化告警缓存，将数据库中的当前告警装载到缓存
	 * @param alarms
	 */
	public void initialize(List<Alarm> alarms) {
		// TODO:
	}
	
	
	/**
	 * 按指定条件查询当前告警
	 * 
	 * @param moId
	 *            MO编号
	 * @param entityType
	 *            实体类型
	 * @param entityOid
	 *            实体OID
	 * @param alarmDefId
	 *            告警定义Id
	 * @return
	 */
	public Alarm queryBy(long moId, String entityType, String entityOid,
			long alarmDefId) throws Exception {
		return null;
	}
}
