/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;
import java.util.Date;

/**
 * �ڽӱ�����ʧ�ܵ�ʵ����
 * @author zhuxiaozhan
 *
 */
public class McBtsNeighborFailed implements Serializable{
	
	//����ֵ
	private long idx;
	
	//��վmoId
	private long moId;
	
	//����ʱ��
	private Date updateTime;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
}
