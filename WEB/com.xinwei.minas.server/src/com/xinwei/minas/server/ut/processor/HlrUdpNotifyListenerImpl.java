/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpNotifyListener;

/**
 * 
 * HLR UDP通知
 * <p>
 * HLR向网管平台发请求消息的情况
 * </p>
 * 
 * @author tiance
 * 
 */

public class HlrUdpNotifyListenerImpl implements HlrUdpNotifyListener {

	private static final Log log = LogFactory
			.getLog(HlrUdpNotifyListenerImpl.class);

	private int nThreads = 10;

	// 处理器线程池
	private ExecutorService executor = Executors.newFixedThreadPool(nThreads);

	// 处理映射表（MA/Processor）
	private Map<Integer, HlrMessageProcessor> processors = new HashMap<Integer, HlrMessageProcessor>();

	public HlrUdpNotifyListenerImpl() {
		// 初始化处理器
		initProcesrors();
		// 启动处理器
		startProcessors();
	}

	@Override
	public void receive(HlrUdpMessage message) throws Exception {
		int messageType = message.getMessageType();

		HlrMessageProcessor processor = processors.get(messageType);

		if (processor != null)
			processor.handle(message);
	}

	/**
	 * 初始化处理器
	 * 
	 */
	private void initProcesrors() {
		processors.put(HlrUdpMessage.UT_REGISTER_NTFY,
				new UTRegisterProcessor());
		processors.put(HlrUdpMessage.QUERY_BTS_REQ,
				new QueryBtsRequestProcessor());
	}

	/**
	 * 启动处理器
	 * 
	 */
	private void startProcessors() {
		for (Runnable processor : processors.values()) {
			executor.execute(processor);
		}
	}
}
