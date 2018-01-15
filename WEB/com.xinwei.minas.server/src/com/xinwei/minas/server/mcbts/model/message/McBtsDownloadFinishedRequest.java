/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;

/**
 * 
 * 基站数据下载结束请求
 * 
 * @author chenshaohua
 * 
 */

public class McBtsDownloadFinishedRequest {

	private Long btsId;

	public McBtsDownloadFinishedRequest() {

	}

	public McBtsDownloadFinishedRequest(Long btsId) {
		this.btsId = btsId;
	}
	
	public McBtsMessage toMcBtsMessage() {
		McBtsMessage request = new McBtsMessage();
		request.setBtsId(btsId);
		request.setMa(McBtsMessage.MA_BTS_SECU);
		request.setMoc(McBtsMessageConstants.MOC_DOWNLOAD_FINISHED_REQ);
		request.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		
		return request;
	}
}
