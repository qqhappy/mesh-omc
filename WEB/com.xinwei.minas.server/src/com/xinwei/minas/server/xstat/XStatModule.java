/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat;

/**
 * 
 * 话务统计模块
 * 
 * @author fanhaoyu
 * 
 */

public class XStatModule {

	private static final XStatModule instance = new XStatModule();

	public static XStatModule getInstance() {
		return instance;
	}

	public void initialize() throws Exception {
	}

}
