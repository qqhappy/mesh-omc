/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-09-03	| fanhaoyu 	| 	create the file                    
 */

package com.xinwei.minas.server.mem.net;

import java.net.DatagramPacket;

import com.xinwei.common.udp.UdpMessageLogger;
import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 
 * MEM业务消息日志
 * 
 * @author fanhaoyu
 * 
 */

public class MemBizMessageLogger implements UdpMessageLogger {
	private RollingFileLogger fileLogger = null;

	public MemBizMessageLogger() {
		fileLogger = new RollingFileLogger("log/mem/mem-net.log", true,
				5 * 1024 * 1024, 20);
		fileLogger.setEnabled(true);
	}

	/**
	 * 记录发送的消息
	 * 
	 * @param dp
	 */
	public void logSentMessage(DatagramPacket dp) {
		byte[] buf = this.getBytesFrom(dp);
		fileLogger.log(" --> ", buf, 0, buf.length);
	}

	/**
	 * 记录接收的消息
	 * 
	 * @param dp
	 */
	public void logReceivedMessage(DatagramPacket dp) {
		byte[] buf = this.getBytesFrom(dp);
		fileLogger.log(" <-- ", buf, 0, buf.length);
	}

	/**
	 * 从UDP数据包获取字节数组
	 * 
	 * @param dp
	 * @return
	 */
	private byte[] getBytesFrom(DatagramPacket dp) {
		byte[] data = dp.getData();
		int len = dp.getLength();
		byte[] buf = new byte[len];
		System.arraycopy(data, 0, buf, 0, len);

		return buf;
	}

}
