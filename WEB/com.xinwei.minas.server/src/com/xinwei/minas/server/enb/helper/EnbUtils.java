/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.Map;
import java.util.Map.Entry;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.utils.StringRollingFileLogger;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB基站助手类
 * 
 * @author chenjunhua
 * 
 */

public class EnbUtils {

	/**
	 * 打印基站业务消息
	 * 
	 * @param btsId
	 * @param message
	 */
	public static void log(Long enbId, String biz, String message) {
		StringRollingFileLogger logger = EnbBizLoggerPool.getInstance()
				.getLoggerBy(enbId);
		String formattedBtsId = StringUtils.to8HexString(enbId);
		logger.log("[0x" + formattedBtsId + "] [" + biz + "]", message);
	}


}
