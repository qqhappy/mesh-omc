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
 * ���ܶ����������ӳ���ϵ
 * 
 * @author chenjunhua
 * 
 */
public class MoRegion implements Serializable{

	// MO���
	private long moId;
	
	// MO������������б�
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
