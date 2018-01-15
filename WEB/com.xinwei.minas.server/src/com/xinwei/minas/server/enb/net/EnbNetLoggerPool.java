/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 
 * eNB 日志池
 * 
 * @author fanhaoyu
 * 
 */

public class EnbNetLoggerPool {

	private static final EnbNetLoggerPool instance = new EnbNetLoggerPool();

	// 传输层日志
	private RollingFileLogger transLogger;

	// 应用层日志
	private RollingFileLogger appLogger;

	// 是否记录传输层日志
	private boolean enableTransLogger = true;

	// 是否记录应用层日志
	private boolean enableAppLogger = true;

	public static EnbNetLoggerPool getInstance() {
		return instance;
	}

	private EnbNetLoggerPool() {
		appLogger = new RollingFileLogger("log/enb/enb-net-app.log", true,
				50 * 1024 * 1024, 30);
		appLogger.setEnabled(enableAppLogger);
		transLogger = new RollingFileLogger("log/enb/enb-net-transport.log",
				true, 50 * 1024 * 1024, 30);
		transLogger.setEnabled(enableTransLogger);
	}

	/**
	 * 获取传输层日志记录对象
	 * 
	 * @return
	 */
	public RollingFileLogger getTransLogger() {
		return transLogger;
	}

	/**
	 * 获取应用层日志记录对象
	 * 
	 * @return
	 */
	public RollingFileLogger getAppLogger() {
		return appLogger;
	}

	public void setEnableAppLogger(boolean enableAppLogger) {
		this.enableAppLogger = enableAppLogger;
	}

	public boolean isEnableAppLogger() {
		return enableAppLogger;
	}

	public void setEnableTransLogger(boolean enableTransLogger) {
		this.enableTransLogger = enableTransLogger;
	}

	public boolean isEnableTransLogger() {
		return enableTransLogger;
	}

}
