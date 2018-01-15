/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-5	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.sysManage;

import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author zhuxiaozhan
 * 
 */
public interface TerminalDetectiveService {

	public UserTerminal detectiveQuery(Long moId, String eid) throws Exception;
}
