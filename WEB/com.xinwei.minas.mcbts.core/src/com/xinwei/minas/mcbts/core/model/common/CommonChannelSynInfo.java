/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

/**
 * 
 * 网管信息同步模型
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class CommonChannelSynInfo implements Serializable{
	private long idx;
	private long moId;
	//同步的次数
	private int synTimes;
	
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
	
	public int getSynTimes() {
		return synTimes;
	}
	
	public void setSynTimes(int synTimes) {
		this.synTimes = synTimes;
	}
}
