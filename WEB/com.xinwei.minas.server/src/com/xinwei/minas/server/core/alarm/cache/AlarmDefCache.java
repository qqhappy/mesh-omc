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
 * �澯���建���ࡣ �������еĸ澯������Ϣ
 * 
 */
public class AlarmDefCache {

	private static final AlarmDefCache instance = new AlarmDefCache();

	private Map<Long, AlarmDef> defMap = new ConcurrentHashMap();

	/**
	 * ��ȡ�����Ψһʵ����
	 * 
	 * @return �����Ψһʵ����
	 */
	public static AlarmDefCache getInstance() {
		return instance;
	}

	/**
	 * ���캯����private��ֹ�ⲿ�����µ�ʵ����
	 */
	private AlarmDefCache() {
	}

	/**
	 * �жϸû������Ƿ���ڸ澯ID����alarmID�ĸ澯���塣
	 * 
	 * @param alarmDefId
	 *            �澯ID��
	 * @return true��ʾ���ڣ�false��ʾ�����ڡ�
	 */
	public boolean exist(long alarmDefId) {
		return defMap.containsKey(alarmDefId);
	}

	/**
	 * ��ջ��档
	 */
	public void clear() {
		defMap.clear();
	}

	/**
	 * ���ݸ澯ID�����Ӧ��AlarmInfo����
	 * 
	 * @param alarmDefId
	 *            �澯ID��
	 * @return �͸澯IDƥ���AlarmInfo��������������򷵻�null��
	 */
	public AlarmDef get(long alarmDefId) {
		return (AlarmDef) defMap.get(alarmDefId);
	}

	/**
	 * �򻺴����һ���澯���壬���Ѿ����ڣ���ʲôҲ������
	 * 
	 * @param alarmDef
	 *            ������ĸ澯���塣
	 */
	public void add(AlarmDef alarmDef) {
		defMap.put(alarmDef.getAlarmDefId(), alarmDef);
	}

	/**
	 * �Ӹû���ɾ��alarmID��Ӧ��AlarmInfo��
	 * 
	 * @param alarmDefId
	 *            �澯ID��
	 * @return ��ɾ����AlarmInfo�����������򷵻�null��
	 */
	public AlarmDef remove(long alarmDefId) {
		return (AlarmDef) defMap.remove(alarmDefId);
	}

	/**
	 * ��ȡ�û����и澯�������Ŀ��
	 * 
	 * @return �û����и澯�������Ŀ��
	 */
	public int getSize() {
		return defMap.size();
	}

	/**
	 * ����һ���������и澯�����Collection��
	 * 
	 * @return �������и澯�����Collection��
	 */
	public Collection getAllAlarmDef() {
		return defMap.values();
	}

}
