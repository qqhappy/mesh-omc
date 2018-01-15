/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

import java.net.DatagramPacket;

import com.xinwei.common.udp.UdpMessageLogger;
import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 
 * HLR UDP��Ϣ��־��¼�ӿ�Ĭ��ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpMessageLogger implements UdpMessageLogger {
	private RollingFileLogger fileLogger = null;

	public HlrUdpMessageLogger() {
		fileLogger = new RollingFileLogger("log/hlr/hlr-net.log", true,
				5 * 1024 * 1024, 20);
		fileLogger.setEnabled(true);
	}

	/**
	 * ��¼���͵���Ϣ
	 * 
	 * @param dp
	 */
	public void logSentMessage(DatagramPacket dp) {
		byte[] buf = this.getBytesFrom(dp);
		fileLogger.log(" --> ", buf, 0, buf.length);
	}

	/**
	 * ��¼���յ���Ϣ
	 * 
	 * @param dp
	 */
	public void logReceivedMessage(DatagramPacket dp) {
		byte[] buf = this.getBytesFrom(dp);
		fileLogger.log(" <-- ", buf, 0, buf.length);
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
