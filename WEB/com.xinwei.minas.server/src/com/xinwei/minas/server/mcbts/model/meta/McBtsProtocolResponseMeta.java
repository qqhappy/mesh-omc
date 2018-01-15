/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

/**
 * 
 * McBts协议 应答消息元数据 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolResponseMeta {
	
	private McBtsProtocolHeaderMeta header;
	
	private McBtsProtocolBodyMeta body;

	public McBtsProtocolHeaderMeta getHeader() {
		return header;
	}

	public void setHeader(McBtsProtocolHeaderMeta header) {
		this.header = header;
	}

	public McBtsProtocolBodyMeta getBody() {
		return body;
	}

	public void setBody(McBtsProtocolBodyMeta body) {
		this.body = body;
	}
	
	
	
}
