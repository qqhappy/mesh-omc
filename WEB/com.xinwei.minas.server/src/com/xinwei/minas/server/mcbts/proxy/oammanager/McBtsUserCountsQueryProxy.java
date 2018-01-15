/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.oammanager;

import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;

/**
 * 
 * McBtsStateQueryProxy
 * 
 * @author fangping
 * 
 */

public interface McBtsUserCountsQueryProxy {

	public McBtsUserCountsQuery queryUserCountsQuery(Long moId) throws Exception,
			UnsupportedOperationException;


/*	public McBtsSateQuery queryRRUSN(Long moId) throws Exception,
			UnsupportedOperationException;*/

}
