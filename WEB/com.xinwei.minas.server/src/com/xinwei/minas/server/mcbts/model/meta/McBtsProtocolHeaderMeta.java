/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * McBts 协议消息头 元数据 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolHeaderMeta {

	private McBtsProtocolHeaderItemMeta[] item = new McBtsProtocolHeaderItemMeta[0];
	
	private Map<String, McBtsProtocolHeaderItemMeta> itemMap = new LinkedHashMap();

	public McBtsProtocolHeaderItemMeta[] getItem() {
		return item;
	}

	public void setItem(McBtsProtocolHeaderItemMeta[] item) {
		this.item = item;
		for (McBtsProtocolHeaderItemMeta i : item) {
			itemMap.put(i.getName(), i);
		}
	}
	
	
}
