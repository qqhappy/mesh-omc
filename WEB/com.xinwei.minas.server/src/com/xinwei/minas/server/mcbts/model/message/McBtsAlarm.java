/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 基站告警模型 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsAlarm {
	
	// 告警
	public static final int FLAG_ALARM = 1;
	
	// 恢复
	public static final int FLAG_RESTORED = 0;
	
	// 告警实体类型
	public static final int TYPE_L3 = 1;
	public static final int TYPE_L2 = 2;
	public static final int TYPE_MCP = 3;
	public static final int TYPE_AUX = 4;
	public static final int TYPE_ENV = 5;
	public static final int TYPE_PLL = 6;
	public static final int TYPE_RF = 7;
	public static final int TYPE_GPS = 8;
	public static final int TYPE_L1 = 9;
	public static final int TYPE_FPGA = 0xa;
	public static final int TYPE_FEP = 0xb;
	public static final int TYPE_CORE9 = 0xc;
	public static final int TYPE_RRU = 0xd;
	public static final int TYPE_AIF = 0xe;
	public static final int TYPE_BATTERY = 0xf;
	
	// 基站Id
	private long btsId;
	
	// 基站上报的告警序列号
	private long seqNo;
	
	// 告警标示: 0-告警恢复;	1-告警产生
	private int flag;
	
	// 告警实体类型
	private int type;
	
	// 告警实体索引号
	private int index;
	
	// 告警定义Id
	private long alarmDefId;
	
	// 告警等级
	private int alarmLevel;
	
	// 告警内容
	private String alarmContent;
	
	
	public McBtsAlarm(Long btsId) {
		this.btsId = btsId;
	}
	
	public McBtsAlarm(Long btsId, byte[] buf) {
		// 基站ID
		this.btsId = btsId;
		int offset = 0;
		// 基站上报的告警序列号
		seqNo = ByteUtils.toLong(buf, offset, 4);
		offset += 4;
		// 告警标示
		flag = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		// 跳过7字节的年月日时分秒
		offset += 7;
		// 告警实体类型
		type = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// 告警实体索引号
		index = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// 告警定义Id
		alarmDefId = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// 告警等级
		alarmLevel = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		// 告警内容
		int contentLen = ByteUtils.toInt(buf, offset, 2);
		offset +=2;
		alarmContent = ByteUtils.toString(buf, offset, contentLen, "US-ASCII"); 
		if (alarmContent != null) {
			alarmContent = alarmContent.trim();
		}
	}

	
	/**
	 * 判断是否是告警消息
	 * @return
	 */
	public boolean isAlarm() {
		return flag > 0;
	}

	
	/**
	 * 判断是否是恢复消息
	 * @return
	 */
	public boolean isRestored() {
		return flag == 0;
	}
	
	public String getAlarmContent() {
		return alarmContent;
	}


	public long getAlarmDefId() {
		return alarmDefId;
	}


	public int getAlarmLevel() {
		return alarmLevel;
	}


	public long getBtsId() {
		return btsId;
	}


	public int getFlag() {
		return flag;
	}


	public int getIndex() {
		return index;
	}


	public long getSeqNo() {
		return seqNo;
	}


	public int getType() {
		return type;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setAlarmDefId(long alarmDefId) {
		this.alarmDefId = alarmDefId;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}
	
	
}
