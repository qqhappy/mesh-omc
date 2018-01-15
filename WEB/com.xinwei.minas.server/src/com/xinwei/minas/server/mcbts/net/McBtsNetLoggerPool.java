/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import java.util.HashMap;
import java.util.Map;

import com.xinwei.nms.common.util.RollingFileLogger;
import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * McBts日志池
 * 
 * @author chenjunhua
 * 
 */

public class McBtsNetLoggerPool {
	private static final McBtsNetLoggerPool instance = new McBtsNetLoggerPool();

	// Logger池
	// key - btsId, value - RollingFileLogger
	private Map loggerPool = new HashMap();
	private RollingFileLogger logger;

	// 是否运行记录日志
	private boolean enabled = true;

	public static McBtsNetLoggerPool getInstance() {
		return instance;
	}

	private McBtsNetLoggerPool() {
		logger = new RollingFileLogger("log/mcbts/bts-net.log", true,
				5 * 1024 * 1024, 30);
		logger.setEnabled(enabled);
	}

	/**
	 * 根据设备编号获取对应的日志记录器
	 * 
	 * @param deviceID
	 *            设备编号
	 * @return
	 */
	public RollingFileLogger getLoggerBy(Long btsId) {
		// if (!loggerPool.containsKey(btsId)) {
		// String formattedBtsId = StringUtils.to8HexString(btsId);
		// RollingFileLogger logger = new RollingFileLogger("log/mcbts/"
		// + formattedBtsId + "-net.log", true, 5 * 1024 * 1024, 3);
		// logger.setEnabled(enabled);
		// loggerPool.put(btsId, logger);
		// }
		// return (RollingFileLogger) loggerPool.get(btsId);
		return logger;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
