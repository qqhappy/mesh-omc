/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * 基站序列号service
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNService {

	/**
	 * 向基站查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsSN querySNFromNE(Long moId) throws Exception;

	/**
	 * 向数据库查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsSN> querySNFromDB(long moId) throws Exception;

}
