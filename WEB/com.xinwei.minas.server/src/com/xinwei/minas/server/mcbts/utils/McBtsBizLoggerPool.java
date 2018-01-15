/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * McBtsҵ�������־��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizLoggerPool {
	private static final McBtsBizLoggerPool instance = new McBtsBizLoggerPool();

	// Logger��
	// key - btsId, value - RollingFileLogger
	// private Map loggerPool = new ConcurrentHashMap();
	// 20131107 ���ڻ�վ��־��Ŀ̫��ᵼ��linux��������ʾtoo many open files, ��˺ϲ���վҵ����־Ϊһ����
	private StringRollingFileLogger logger = null;

	// �Ƿ����м�¼��־
	private boolean enabled = true;

	public static McBtsBizLoggerPool getInstance() {
		return instance;
	}

	private McBtsBizLoggerPool() {
		logger = new StringRollingFileLogger("log/mcbts/bts-biz.log", true,
				5 * 1024 * 1024, 10);
		logger.setEnabled(enabled);

	}

	/**
	 * �����豸��Ż�ȡ��Ӧ����־��¼��
	 * 
	 * @param deviceID
	 *            �豸���
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
