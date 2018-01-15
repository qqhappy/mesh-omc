/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-4-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * 
 * Ibts光口状态集合 
 * 
 * @author chenjunhua
 * 
 */

public class IbtsOpticalStatusCollection implements EnbDynamicInfo{
	
	// Ibts光口状态列表
	private List<IbtsOpticalStatus> opticalStatusList = new LinkedList();
	
	public List<IbtsOpticalStatus> getOpticalStatusList() {
		return opticalStatusList;
	}

	public void setOpticalStatusList(List<IbtsOpticalStatus> opticalStatusList) {
		this.opticalStatusList = opticalStatusList;
	}

	public void addIbtsOpticalStatus(IbtsOpticalStatus opticalStatus) {
		opticalStatusList.add(opticalStatus);
	}
}
