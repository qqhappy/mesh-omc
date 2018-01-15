/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy;

import java.util.List;

import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * McBts基本业务代理接口
 * 
 * @author tiance
 * 
 */

public interface UTBasicProxy {
	public UTQueryResult queryUTByCondition(UTCondition utc) throws Exception;

}
