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
 * �ص��ű�ģ��
 * 
 * @author chenjunhua
 * 
 */

public interface CallbackScript {

	/**
	 * ִ�лص��ű�
	 * @param minasClientFacade
	 */
	public void execute(MinasClientFacade minasClientFacade) throws Exception;
	
}
