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
 * HLR OSS 通信连接器实现
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
		// 默认超时时间
		int timeout = 5 * 1000;
		return syncInvoke(hlrOssBizMessage, timeout);
	}

	@Override
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage,
			int timeout) throws TimeoutException, Exception {	
		int sessionId = hlrOssBizMessage.getSessionId();
		HlrOssRegistry.getInstance().register(sessionId);
		try {
			// 使用异步调用
			asyncInvoke(hlrOssBizMessage);
			// 同步等待结果
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
		// TODO: 此处需要确认版本号
		hlrUdpMessage.setVersion(3);
		// 最后一包
		hlrUdpMessage.setLastPacketFlag(1);
		// 设置消息体
		byte[] content = hlrOssBizMessage.toBytes();
		hlrUdpMessage.setContent(content);
		return hlrUdpMessage;
	}

}
