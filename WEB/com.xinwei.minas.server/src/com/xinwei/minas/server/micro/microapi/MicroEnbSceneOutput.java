/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-30	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.microapi;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * eNodeB场景输出模型
 * 
 * @author chenjunhua
 * 
 */

public class MicroEnbSceneOutput {
	
	// 两级Map: 表名-->字段名-->字段数值
	private Map<String, Map<String, String>> tableMap = new HashMap();

	public Map<String, Map<String, String>> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, Map<String, String>> tableMap) {
		this.tableMap = tableMap;
	}
}







