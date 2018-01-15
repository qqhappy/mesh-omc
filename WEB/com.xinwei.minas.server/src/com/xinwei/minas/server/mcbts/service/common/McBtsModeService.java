/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * 基站工作模式服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsModeService {
	public McBts queryMcBtsMode(McBts mcbts) throws Exception;
}
