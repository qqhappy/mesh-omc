/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.common;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * »ùÕ¾ÐòÁÐºÅproxy
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNProxy {

	public McBtsSN querySN(Long moId) throws Exception,
			UnsupportedOperationException;

	public McBtsSN queryRRUSN(Long moId) throws Exception,
			UnsupportedOperationException;

}
