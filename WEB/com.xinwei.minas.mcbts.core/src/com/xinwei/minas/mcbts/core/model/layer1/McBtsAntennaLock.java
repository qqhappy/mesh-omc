/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;

/**
 * ���߱�������ʵ����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsAntennaLock implements Serializable {

	// �ر����߱���
	public static final int CLOSE = 0;
	
	// �������߱���
	public static final int OPEN = 1;
	
	// ��¼����
	private Long idx;

	// MO���
	private long moId;

	// ��������
	private int flag;
	
	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}