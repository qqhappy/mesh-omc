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
 * ��վ�澯ģ�� 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsAlarm {
	
	// �澯
	public static final int FLAG_ALARM = 1;
	
	// �ָ�
	public static final int FLAG_RESTORED = 0;
	
	// �澯ʵ������
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
	
	// ��վId
	private long btsId;
	
	// ��վ�ϱ��ĸ澯���к�
	private long seqNo;
	
	// �澯��ʾ: 0-�澯�ָ�;	1-�澯����
	private int flag;
	
	// �澯ʵ������
	private int type;
	
	// �澯ʵ��������
	private int index;
	
	// �澯����Id
	private long alarmDefId;
	
	// �澯�ȼ�
	private int alarmLevel;
	
	// �澯����
	private String alarmContent;
	
	
	public McBtsAlarm(Long btsId) {
		this.btsId = btsId;
	}
	
	public McBtsAlarm(Long btsId, byte[] buf) {
		// ��վID
		this.btsId = btsId;
		int offset = 0;
		// ��վ�ϱ��ĸ澯���к�
		seqNo = ByteUtils.toLong(buf, offset, 4);
		offset += 4;
		// �澯��ʾ
		flag = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		// ����7�ֽڵ�������ʱ����
		offset += 7;
		// �澯ʵ������
		type = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// �澯ʵ��������
		index = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// �澯����Id
		alarmDefId = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// �澯�ȼ�
		alarmLevel = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		// �澯����
		int contentLen = ByteUtils.toInt(buf, offset, 2);
		offset +=2;
		alarmContent = ByteUtils.toString(buf, offset, contentLen, "US-ASCII"); 
		if (alarmContent != null) {
			alarmContent = alarmContent.trim();
		}
	}

	
	/**
	 * �ж��Ƿ��Ǹ澯��Ϣ
	 * @return
	 */
	public boolean isAlarm() {
		return flag > 0;
	}

	
	/**
	 * �ж��Ƿ��ǻָ���Ϣ
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
