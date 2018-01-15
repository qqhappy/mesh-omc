/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

/**
 * 
 * McBts协议消息体 元数据 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolBodyMeta {

	private McBtsProtocolBodyItemMeta[] item = new McBtsProtocolBodyItemMeta[0];

	public McBtsProtocolBodyItemMeta[] getItem() {
		return item;
	}

	public void setItem(McBtsProtocolBodyItemMeta[] item) {
		this.item = item;
	}


	
}
