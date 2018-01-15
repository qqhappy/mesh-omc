/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.server.mcbts.utils.StringRollingFileLogger;
import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * eNB业务操作日志池
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizLoggerPool {
	private static final EnbBizLoggerPool instance = new EnbBizLoggerPool();

	// Logger池
	// key - btsId, value - RollingFileLogger
	// private Map loggerPool = new ConcurrentHashMap();
	// 20131107 由于基站日志数目太多会导致linux环境下提示too many open files, 因此合并基站业务日志为一个。
	private StringRollingFileLogger logger = null;

	// 是否运行记录日志
	private boolean enabled = true;

	public static EnbBizLoggerPool getInstance() {
		return instance;
	}

	private EnbBizLoggerPool() {
		logger = new StringRollingFileLogger("log/enb/enb-biz.log", true,
				5 * 1024 * 1024, 10);
		logger.setEnabled(enabled);

	}

	/**
	 * 根据设备编号获取对应的日志记录器
	 * 
	 * @param deviceID
	 *            设备编号
	 * @return
	 */
	public StringRollingFileLogger getLoggerBy(Long btsId) {
		// if (!loggerPool.containsKey(btsId)) {
		// String formattedBtsId = StringUtils.to8HexString(btsId);
		// StringRollingFileLogger logger = new
		// StringRollingFileLogger("log/mcbts/"
		// + formattedBtsId + "-biz.log", true, 5 * 1024 * 1024, 3);
		// logger.setEnabled(enabled);
		// loggerPool.put(btsId, logger);
		// }
		// return (StringRollingFileLogger) loggerPool.get(btsId);

		return logger;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
