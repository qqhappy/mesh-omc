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
 * SXCģ��
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
	 * ģ���ʼ��
	 */
	public void initialize()
			throws Exception {
	}


}
