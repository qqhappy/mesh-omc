/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.conf;

/**
 * 
 * key desc 
 * 
 * @author chenjunhua
 * 
 */

public class KeyDesc implements java.io.Serializable{

	// key
	private Integer key;
	
	// √Ë ˆ
	private String desc;
	
	public KeyDesc(Integer key, String desc) {
		this.setKey(key);
		this.setDesc(desc);
	}
	
	public KeyDesc(String key, String desc) {
		this.setKey(Integer.parseInt(key));
		this.setDesc(desc);
	}
	
	public KeyDesc(Object key, Object desc) {
		this.setKey(Integer.parseInt(String.valueOf(key)));
		this.setDesc(String.valueOf(desc));
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
