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
 * McBts协议 请求消息元数据 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolRequestMeta {
	
	private McBtsProtocolHeaderMeta header = new McBtsProtocolHeaderMeta();
	
	private McBtsProtocolBodyMeta body = new McBtsProtocolBodyMeta();

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
