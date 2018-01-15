/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.rruManage;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * RRU硬件信息查询proxy
 * 
 * @author chenshaohua
 * 
 */

// TODO:备用
public interface RRUHWInfoProxy {

	/**
	 * 查询RRU硬件信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 * @throws UnsupportedOperationException
	 */
	McBtsSN queryRRUHWInfo(Long moId) throws Exception,
			UnsupportedOperationException;

}
