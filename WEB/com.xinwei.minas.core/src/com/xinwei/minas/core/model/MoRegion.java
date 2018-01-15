/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-10-22	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.minas.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 被管对象与地区的映射关系
 * 
 * @author chenjunhua
 * 
 */
public class MoRegion implements Serializable{

	// MO编号
	private long moId;
	
	// MO所属地区编号列表
	private long regionId;
	
	
	public MoRegion() {
		
	}
	
	public MoRegion(long moId, long regionId) {
		this.setMoId(moId);
		this.setRegionId(regionId);
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	
	
	

}
