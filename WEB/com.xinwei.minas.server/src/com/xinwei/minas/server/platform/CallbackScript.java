/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform;

import com.xinwei.minas.core.facade.MinasClientFacade;

/**
 * 
 * 回调脚本模型
 * 
 * @author chenjunhua
 * 
 */

public interface CallbackScript {

	/**
	 * 执行回调脚本
	 * @param minasClientFacade
	 */
	public void execute(MinasClientFacade minasClientFacade) throws Exception;
	
}
