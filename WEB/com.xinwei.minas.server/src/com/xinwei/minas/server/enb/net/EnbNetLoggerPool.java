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
 * eNB ��־��
 * 
 * @author fanhaoyu
 * 
 */

public class EnbNetLoggerPool {

	private static final EnbNetLoggerPool instance = new EnbNetLoggerPool();

	// �������־
	private RollingFileLogger transLogger;

	// Ӧ�ò���־
	private RollingFileLogger appLogger;

	// �Ƿ��¼�������־
	private boolean enableTransLogger = true;

	// �Ƿ��¼Ӧ�ò���־
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
	 * ��ȡ�������־��¼����
	 * 
	 * @return
	 */
	public RollingFileLogger getTransLogger() {
		return transLogger;
	}

	/**
	 * ��ȡӦ�ò���־��¼����
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
