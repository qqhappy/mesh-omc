/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.model.message;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.TagConst;

/**
 * 
 * 基站注册应答
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterResponse {

	// 基站ID
	private Long enbId;

	// 软件版本
	private String sortwareVersion;

	// 数据加载状态
	private int dataLoadStatus;

	public EnbRegisterResponse(EnbAppMessage enbAppMessage) {
		enbId = enbAppMessage.getEnbId();
		sortwareVersion = enbAppMessage.getStringValue(TagConst.VERSION);
		dataLoadStatus = enbAppMessage.getIntValue(TagConst.DATA_LOAD_STATUS);
	}

	public Long getEnbId() {
		return enbId;
	}

	public String getSortwareVersion() {
		return sortwareVersion;
	}

	/**
	 * 是否数据已加载
	 * 
	 * @return
	 */
	public boolean isDataLoaded() {
		return dataLoadStatus == 1;
	}

}
