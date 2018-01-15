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
 * 切换附加邻接表实体类
 * @author zhuxiaozhan
 *
 */
public class McBtsAppendNeighbor {
	//记录索引
	private long idx;
	
	//基站moId
	private long moId;
	
	//邻站moId
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
