/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

/**
 * 
 * eNodeB��̬��Ϣ��ѯ����
 * 
 * @author chenjunhua
 * 
 */

public class EnbDynamicInfoCondition implements java.io.Serializable{
	
	// XW7102 ״̬��Ϣ��ѯ
	public static final int XW7102_STATUS = 101;
	
	// XW7102 ��Ƶ״̬��ѯ
	public static final int XW7102_RF_STATUS = 102;	
	
	// XW7102 ������Ϣ��ѯ
	public static final int XW7102_THRESHOLD = 103;
	
	// XW7102 ����״̬��ѯ
	public static final int XW7102_RUNNING_STATUS = 104;
	
	// XW7102 ���״̬��ѯ
	public static final int XW7102_FIBER_STATUS = 105;
	
	// XW7102 ����״̬��Ϣ��ѯ
	public static final int XW7102_BOARD_STATUS = 106;
	
	// ��ԪID
	private long moId;
	
	// ��ѯ��ʶ
	private int queryFlag;
	
	
	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(int queryFlag) {
		this.queryFlag = queryFlag;
	}

	@Override
	public String toString() {
		return "EnbDynamicInfoCondition [moId=" + moId + ", queryFlag="
				+ queryFlag + "]";
	}
	
	
	
}
