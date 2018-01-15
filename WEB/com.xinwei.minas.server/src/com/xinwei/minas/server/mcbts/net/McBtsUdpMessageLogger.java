/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import java.net.DatagramPacket;

import com.xinwei.common.udp.UdpMessageLogger;
import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 
 * Udp��Ϣ��־��¼�ӿ�Ĭ��ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsUdpMessageLogger implements UdpMessageLogger {
	private RollingFileLogger fileLogger = null;

	public McBtsUdpMessageLogger() {
//		fileLogger = new RollingFileLogger("log/mcbts/mcbts-net.log", true,
//				5 * 1024 * 1024, 20);
//		fileLogger.setEnabled(true);
	}

	/**
	 * ��¼���͵���Ϣ
	 * 
	 * @param dp
	 */
	public void logSentMessage(DatagramPacket dp) {
		if (fileLogger != null) {
			byte[] buf = this.getBytesFrom(dp);
			fileLogger.log(" --> ", buf, 0, buf.length);
		}
	}

	/**
	 * ��¼���յ���Ϣ
	 * 
	 * @param dp
	 */
	public void logReceivedMessage(DatagramPacket dp) {
		if (fileLogger != null) {
			byte[] buf = this.getBytesFrom(dp);
			fileLogger.log(" <-- ", buf, 0, buf.length);
		}
	}

	/**
	 * ��UDP���ݰ���ȡ�ֽ�����
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
