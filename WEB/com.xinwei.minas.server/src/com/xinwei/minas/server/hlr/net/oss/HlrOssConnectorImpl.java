/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.oss;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpConnector;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpNotifyListener;

/**
 * 
 * HLR OSS ͨ��������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssConnectorImpl implements HlrOssConnector {

	private HlrUdpConnector hlrUdpConnector;

	private String hlrIp;

	private int hlrPort;
	
	private HlrUdpNotifyListener listener;

	public HlrOssConnectorImpl(HlrUdpConnector hlrUdpConnector, String hlrIp,
			int hlrPort, HlrUdpNotifyListener listener) {
		this.hlrUdpConnector = hlrUdpConnector;
		this.hlrIp = hlrIp;
		this.hlrPort = hlrPort;
		this.listener = listener;
		hlrUdpConnector.addOssListener(listener);
		
	}

	@Override
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage)
			throws TimeoutException, Exception {
		// Ĭ�ϳ�ʱʱ��
		int timeout = 5 * 1000;
		return syncInvoke(hlrOssBizMessage, timeout);
	}

	@Override
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage,
			int timeout) throws TimeoutException, Exception {	
		int sessionId = hlrOssBizMessage.getSessionId();
		HlrOssRegistry.getInstance().register(sessionId);
		try {
			// ʹ���첽����
			asyncInvoke(hlrOssBizMessage);
			// ͬ���ȴ����
			HlrOssSyncResult result = HlrOssRegistry.getInstance().getBy(sessionId);
			return result.timeWait(timeout);
		} finally {
			HlrOssRegistry.getInstance().deregister(sessionId);
		}		
	}

	@Override
	public void asyncInvoke(HlrOssBizMessage hlrOssBizMessage) throws Exception {
		HlrUdpMessage request = this.createHlrUdpMessage(hlrOssBizMessage);
		hlrUdpConnector.asyncInvoke(request);
	}

	private HlrUdpMessage createHlrUdpMessage(HlrOssBizMessage hlrOssBizMessage) {
		HlrUdpMessage hlrUdpMessage = new HlrUdpMessage();
		hlrUdpMessage.setHlrIp(hlrIp);
		hlrUdpMessage.setHlrPort(hlrPort);
		hlrUdpMessage.setMessageType(HlrUdpMessage.OSS_CONTENT);
		// TODO: �˴���Ҫȷ�ϰ汾��
		hlrUdpMessage.setVersion(3);
		// ���һ��
		hlrUdpMessage.setLastPacketFlag(1);
		// ������Ϣ��
		byte[] content = hlrOssBizMessage.toBytes();
		hlrUdpMessage.setContent(content);
		return hlrUdpMessage;
	}

}
