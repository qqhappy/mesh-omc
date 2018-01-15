/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.model.message;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * eNB告警模型
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarm {

	// 分隔符
	public static final String DOT = ".";

	// 告警
	public static final int FLAG_ALARM = 1;
	// 恢复
	public static final int FLAG_RESTORED = 0;

	// 机架
	public static final int LOC_TYPE_RACK = 0;
	// 机框
	public static final int LOC_TYPE_SHELF = 1;
	// 槽位
	public static final int LOC_TYPE_BOARD = 2;
	// 小区
	public static final int LOC_TYPE_CELL = 3;
	// 耦连
	public static final int LOC_TYPE_ASSO = 4;
	// 物理光口
	public static final int LOC_TYPE_PHY_SPF_PORT = 5;
	// RRU通道
	public static final int LOC_TYPE_RRU_CHAN = 6;
	// RRU板卡
	public static final int LOC_TYPE_RRU_S_BOARD = 7;
	// 芯片号
	public static final int LOC_TYPE_CHIP = 8;

	// eNB ID
	private long enbId;

	// 告警码
	private long alarmCode;

	// 告警子码
	private int alarmSubCode;

	// 告警标示: 0-告警恢复; 1-告警产生
	private int flag;

	// 实体类型(以.分割)
	private String entityType = "";

	// 实体OID(以.分割)
	private String entityOid = "";

	// 事件时间(14位长度yyyyMMddhhmmss)
	private long eventTime;

	// 告警补充描述(不是告警内容)
	private String alarmDesc = "";

	public EnbAlarm(Long enbId) {
		this.enbId = enbId;
		eventTime = DateUtils.getBriefTimeFromMillisecondTime(System
				.currentTimeMillis());
	}

	public EnbAlarm(EnbAppMessage message) {
		this(message.getEnbId());
		this.alarmCode = message.getLongValue(TagConst.ALARM_DEF_CODE);
		this.alarmSubCode = message.getIntValue(TagConst.ALARM_DEF_SUB_CODE);
		this.flag = message.getIntValue(TagConst.ALARM_FLAG);
		// 解析定位信息
		byte[] locTypeBytes = message.getByteValue(TagConst.ALARM_LOC_TYPE);
		byte[] locIdBytes = message.getByteValue(TagConst.ALARM_LOC_ID);
		this.parseLocationType(locTypeBytes);
		this.parseLocationId(locIdBytes);
		this.eventTime = message.getDateTimeValue(TagConst.ALARM_TIME);
	}

	/**
	 * 解析位置类型
	 * 
	 * @param buf
	 */
	private void parseLocationType(byte[] buf) {
		if (buf == null) {
			return;
		}
		// 定义类型长度
		int len = 2;
		int size = buf.length / len;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			int locType = ByteUtils.toInt(buf, offset, len);
			switch (locType) {
			case LOC_TYPE_RACK:
				entityType += "RACK";
				break;
			case LOC_TYPE_SHELF:
				entityType += "SHELF";
				break;
			case LOC_TYPE_BOARD:
				entityType += "BOARD";
				break;
			case LOC_TYPE_CELL:
				entityType += "CELL";
				break;
			case LOC_TYPE_ASSO:
				entityType += "ASSO_ID";
				break;
			case LOC_TYPE_PHY_SPF_PORT:
				entityType += "PHY_SPF_PORT";
				break;
			case LOC_TYPE_RRU_CHAN:
				entityType += "RRU_CHAN";
				break;
			case LOC_TYPE_RRU_S_BOARD:
				entityType += "RRU_S_BOARD";
				break;
			case LOC_TYPE_CHIP:
				entityType += "CHIP";
				break;
			default:
				entityType += "Unknown";
				break;
			}
			if (i != (size - 1)) {
				entityType += DOT;
			}
			offset += len;
		}
	}

	/**
	 * 解析位置ID
	 * 
	 * @param buf
	 */
	private void parseLocationId(byte[] buf) {
		if (buf == null) {
			return;
		}
		int len = 2;
		int size = buf.length / len;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			int locId = ByteUtils.toInt(buf, offset, len);
			entityOid += locId;
			if (i != (size - 1)) {
				entityOid += DOT;
			}
			offset += len;
		}
	}

	/**
	 * 判断是否是告警消息
	 * 
	 * @return
	 */
	public boolean isAlarm() {
		return flag > 0;
	}

	/**
	 * 判断是否是恢复消息
	 * 
	 * @return
	 */
	public boolean isRestored() {
		return flag == 0;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public long getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(long alarmCode) {
		this.alarmCode = alarmCode;
	}

	public int getAlarmSubCode() {
		return alarmSubCode;
	}

	public void setAlarmSubCode(int alarmSubCode) {
		this.alarmSubCode = alarmSubCode;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getEntityOid() {
		if (entityOid == null) {
			return "";
		}
		return entityOid;
	}

	public void setEntityOid(String entityOid) {
		this.entityOid = entityOid;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public String getEntityType() {
		if (entityType == null) {
			return "";
		}
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getAlarmDesc() {
		if (alarmDesc == null) {
			return "";
		}
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

}
