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
 * McBts协议-业务元数据
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolBizMeta {
	
	// 业务名称
	private String name;
	
	// 业务操作
	private String operation;
	
	// 协议头
	private McBtsProtocolHeaderMeta header;
}
