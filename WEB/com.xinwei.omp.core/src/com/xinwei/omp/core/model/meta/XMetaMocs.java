/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.io.Serializable;

/**
 * Mocs元数据
 * 
 * @author chenshaohua
 * 
 */

public class XMetaMocs implements Serializable {

	// 多个moc
	private String[] moc;

	public String[] getMoc() {
		return moc;
	}

	public void setMoc(String[] moc) {
		this.moc = moc;
	}
	
	

}
