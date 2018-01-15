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
 * ��վע��Ӧ��
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterResponse {

	// ��վID
	private Long enbId;

	// ����汾
	private String sortwareVersion;

	// ���ݼ���״̬
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
	 * �Ƿ������Ѽ���
	 * 
	 * @return
	 */
	public boolean isDataLoaded() {
		return dataLoadStatus == 1;
	}

}
