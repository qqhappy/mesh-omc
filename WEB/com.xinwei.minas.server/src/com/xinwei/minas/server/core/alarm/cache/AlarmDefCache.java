/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.minas.server.core.alarm.cache;

import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.alarm.AlarmDef;

/**
 * 
 * 告警定义缓存类。 缓存所有的告警定义信息
 * 
 */
public class AlarmDefCache {

	private static final AlarmDefCache instance = new AlarmDefCache();

	private Map<Long, AlarmDef> defMap = new ConcurrentHashMap();

	/**
	 * 获取该类的唯一实例。
	 * 
	 * @return 该类的唯一实例。
	 */
	public static AlarmDefCache getInstance() {
		return instance;
	}

	/**
	 * 构造函数，private防止外部构造新的实例。
	 */
	private AlarmDefCache() {
	}

	/**
	 * 判断该缓存中是否存在告警ID等于alarmID的告警定义。
	 * 
	 * @param alarmDefId
	 *            告警ID。
	 * @return true表示存在，false表示不存在。
	 */
	public boolean exist(long alarmDefId) {
		return defMap.containsKey(alarmDefId);
	}

	/**
	 * 清空缓存。
	 */
	public void clear() {
		defMap.clear();
	}

	/**
	 * 根据告警ID获得相应的AlarmInfo对象。
	 * 
	 * @param alarmDefId
	 *            告警ID。
	 * @return 和告警ID匹配的AlarmInfo对象，如果不存在则返回null。
	 */
	public AlarmDef get(long alarmDefId) {
		return (AlarmDef) defMap.get(alarmDefId);
	}

	/**
	 * 向缓存放入一个告警定义，若已经存在，则什么也不做。
	 * 
	 * @param alarmDef
	 *            被放入的告警定义。
	 */
	public void add(AlarmDef alarmDef) {
		defMap.put(alarmDef.getAlarmDefId(), alarmDef);
	}

	/**
	 * 从该缓存删除alarmID对应的AlarmInfo。
	 * 
	 * @param alarmDefId
	 *            告警ID。
	 * @return 被删除的AlarmInfo。若不存在则返回null。
	 */
	public AlarmDef remove(long alarmDefId) {
		return (AlarmDef) defMap.remove(alarmDefId);
	}

	/**
	 * 获取该缓存中告警定义的数目。
	 * 
	 * @return 该缓存中告警定义的数目。
	 */
	public int getSize() {
		return defMap.size();
	}

	/**
	 * 返回一个包含所有告警定义的Collection。
	 * 
	 * @return 包含所有告警定义的Collection。
	 */
	public Collection getAllAlarmDef() {
		return defMap.values();
	}

}
