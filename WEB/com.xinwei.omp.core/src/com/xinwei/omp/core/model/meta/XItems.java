/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

/**
 * 
 * ItemsÄ£ÐÍ 
 * 
 * @author chenjunhua
 * 
 */

public class XItems implements java.io.Serializable{
	private XItem[] item = new XItem[0];

	public XItem[] getItem() {
		return item;
	}

	public void setItem(XItem[] item) {
		this.item = item;
	}
	
	
}
