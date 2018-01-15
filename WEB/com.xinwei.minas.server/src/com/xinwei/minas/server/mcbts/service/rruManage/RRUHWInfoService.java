/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.rruManage;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * RRU硬件信息查询service
 * 
 * @author chenshaohua
 * 
 */

// TODO:备用
public interface RRUHWInfoService {

	/**
	 * 向基站查询RRU硬件信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsSN queryHWInfoFromNE(Long moId) throws Exception;
}
