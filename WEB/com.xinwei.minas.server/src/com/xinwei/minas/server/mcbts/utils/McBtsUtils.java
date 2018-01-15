/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.util.Map;
import java.util.Map.Entry;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 宽带基站助手类
 * 
 * @author chenjunhua
 * 
 */

public class McBtsUtils {

	/**
	 * 打印基站业务消息
	 * 
	 * @param btsId
	 * @param message
	 */
	public static void log(Long btsId, String biz, String message) {
		StringRollingFileLogger logger = McBtsBizLoggerPool.getInstance()
				.getLoggerBy(btsId);
		String formattedBtsId = StringUtils.to8HexString(btsId);
		logger.log("[0x" + formattedBtsId + "] [" + biz + "]", message);
	}

	/**
	 * 触发批量更新基站异常
	 * 
	 * @param failedMap
	 *            失败的基站列表
	 * @throws Exception
	 */
	public static void fireBatchUpdateException(Map<McBts, String> failedMap)
			throws Exception {
		StringBuilder buf = new StringBuilder();
		if (!failedMap.isEmpty()) {
			buf.append(OmpAppContext.getMessage("mcbts.failed_to_update_bts")
					+ "\n");
			for (Entry<McBts, String> item : failedMap.entrySet()) {
				McBts bts = item.getKey();
				buf.append(bts.getHexBtsId()).append("(").append(bts.getName())
						.append(")").append(":").append(item.getValue())
						.append("\n");
			}
			// buf.deleteCharAt(buf.length() - 1);
		}
		if (buf.length() > 0) {
			throw new Exception(buf.toString());
		}
	}

}
