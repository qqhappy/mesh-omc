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
 * McBts协议 消息体属性  元数据  
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolBodyItemMeta {
	
	// 属性名称
	private String name;
	
	// 属性类型
	private String type;
	
	// 属性长度
	private String length;
	
	// change by yinbinqiang 添加构造函数
	public McBtsProtocolBodyItemMeta() {
		
	}
	// change by yinbinqiang 添加构造函数
	public McBtsProtocolBodyItemMeta(String name, String type, String length) {
		this.name = name;
		this.type = type;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	
}
