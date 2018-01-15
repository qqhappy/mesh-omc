/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * 
 * IBTS射频状态模型
 * 
 * @author chenjunhua
 * 
 */

public class IbtsRfStatus implements EnbDynamicInfo {

	// 射频通道状态列表
	private List<IbtsRfChannelStatus> rfChannelStatusList = new LinkedList();

	public List<IbtsRfChannelStatus> getRfChannelStatusList() {
		return rfChannelStatusList;
	}

	public void setRfChannelStatusList(List<IbtsRfChannelStatus> rfChannelStatusList) {
		this.rfChannelStatusList = rfChannelStatusList;
	}
	
	public void addIbtsRfChannelStatus(IbtsRfChannelStatus rfChannelStatus) {
		rfChannelStatusList.add(rfChannelStatus);
	}
	
}
