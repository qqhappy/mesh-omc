/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

/**
 * 
 * 告警定义模型
 * 
 */
public class AlarmDef implements java.io.Serializable{

	// 告警定义Id
	private long alarmDefId;

	// 告警级别
	private int alarmLevel;

	// 网元类型
	private int moType;

	// 告警名称
	private String alarmName;

	/**
	 * 获取告警ID。
	 * 
	 * @return 告警ID，若从没有设置则返回-1。
	 */
	public long getAlarmDefId() {
		return alarmDefId;
	}

	/**
	 * 获取告警级别。
	 * 
	 * @return 告警级别，若从没有设置则返回-1。
	 * @see AlarmLevel。
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 设置告警描述信息。
	 * 
	 * @param alarmDesc
	 *            告警描述信息。
	 */
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	/**
	 * 设置告警ID。
	 * 
	 * @param alarmID
	 *            告警ID。
	 */
	public void setAlarmDefId(long alarmDefId) {
		this.alarmDefId = alarmDefId;
	}

	/**
	 * 设置告警级别。
	 * 
	 * @param alarmLevel
	 *            告警级别。
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 获取告警描述。
	 * 
	 * @return 告警描述，若没有描述则返回null。
	 */
	public String getAlarmName() {
		return alarmName;
	}

	public int getMoType() {
		return moType;
	}

	public void setMoType(int moType) {
		this.moType = moType;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=");
		buf.append(alarmDefId);
		buf.append(";name=");
		buf.append(alarmName);
		buf.append(";level=");
		buf.append(alarmLevel);
		buf.append(";moType=");
		buf.append(moType);

		return buf.toString();
	}

}
