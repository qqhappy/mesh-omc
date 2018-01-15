/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.sysManage;

import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 终端探测代理接口
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalDetectiveProxy {

	/**
	 * 查询网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @return 记录集
	 * @throws Exception
	 */
	UserTerminal query(Long moId, GenericBizData genericBizData)
			throws Exception;

}
