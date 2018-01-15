/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

/**
 * 
 * С��վ�źŷ��ͷ�ʽ
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class MicroBtsSignalSendSetting implements Serializable {

	// ������������
	public static final int SENDMODE_OUTDOOR = 0;
	
	// �ֲ�ʽ��������
	public static final int SENDMODE_DISTRIBUTE = 1;
	
	private Long idx;

	private Long moId;
	
	// ���ͷ�ʽ
	private int sendMode;
	
	private int antIndex;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public int getSendMode() {
		return sendMode;
	}

	public void setSendMode(int sendMode) {
		this.sendMode = sendMode;
	}

	public int getAntIndex() {
		return antIndex;
	}

	public void setAntIndex(int antIndex) {
		this.antIndex = antIndex;
	}
	
	
}
