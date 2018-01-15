/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-7-16	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;

/**
 * 
 * @author chenlong
 * 
 */

public class EnbProperty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -894766037121396596L;

	private String name;
	
	private String value;
	
	public EnbProperty(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "EnbProperty [name=" + name + ", value=" + value + "]";
	}
	
}
