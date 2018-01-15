/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.sxc;


/**
 * 
 * SXC模块
 * 
 * @author chenjunhua
 * 
 */

public class SxcModule {

	private static final SxcModule instance = new SxcModule();



	private SxcModule() {
	}

	public static SxcModule getInstance() {
		return instance;
	}

	/**
	 * 模块初始化
	 */
	public void initialize()
			throws Exception {
	}


}
