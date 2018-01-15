/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * �л������ڽӱ�ʵ����
 * @author zhuxiaozhan
 *
 */
public class McBtsAppendNeighbor {
	//��¼����
	private long idx;
	
	//��վmoId
	private long moId;
	
	//��վmoId
	private long appendNeighborMoId;
	
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
	public long getAppendNeighborMoId() {
		return appendNeighborMoId;
	}
	public void setAppendNeighborMoId(long appendNeighborMoId) {
		this.appendNeighborMoId = appendNeighborMoId;
	}
	
}
