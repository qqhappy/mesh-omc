/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.generic;

/**
 * 
 * 通用操作枚举
 * 
 * @author chenjunhua
 * 
 */

public enum GenericOperate {
	ADD, MODIFY, DELETE, QUERY;
	
	public String toString() {
		return name().toLowerCase();
	}
}
