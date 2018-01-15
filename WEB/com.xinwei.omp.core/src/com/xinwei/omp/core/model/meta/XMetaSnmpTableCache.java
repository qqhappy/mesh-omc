/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * SNMP表定义元数据缓存
 * 
 * @author chenjunhua
 * 
 */

public class XMetaSnmpTableCache {

	private XMetaSnmpTable[] type;
	
	private Map<String, XMetaSnmpTable> tablesByName = new HashMap();

	public XMetaSnmpTable[] getType() {
		return type;
	}

	public void setType(XMetaSnmpTable[] type) {
		this.type = type;
		for (XMetaSnmpTable table : type) {
			tablesByName.put(table.getName(), table);
		}
	}
	
	/**
	 * 根据表名获取对应的表定义元数据
	 * @param bizName
	 * @return
	 */
	public XMetaSnmpTable getMetaSnmpTableByName(String bizName) {
		return tablesByName.get(bizName);
	}
	
	
	
}
