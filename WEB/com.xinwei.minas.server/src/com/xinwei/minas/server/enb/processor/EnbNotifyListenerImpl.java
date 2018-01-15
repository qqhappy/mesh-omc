/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbNotifyListener;
import com.xinwei.minas.server.enb.service.EnbCache;

/**
 * 
 * eNB消息通知侦听器实现
 * 
 * @author chenjunhua
 * 
 */

public class EnbNotifyListenerImpl implements EnbNotifyListener {

	private static final Log logger = LogFactory
			.getLog(EnbNotifyListenerImpl.class);

	// 处理器线程池
	private ExecutorService executor;

	// 处理映射表（MA/Processor）
	private Map<Integer, EnbMessageProcessor> processors = new HashMap();

	public EnbNotifyListenerImpl() {
		// 初始化处理器
		initProcessors();
		// 启动处理器
		startProcessors();
	}

	@Override
	public void receive(EnbAppMessage message) throws Exception {
		Long enbId = message.getEnbId();
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		// 如果基站不存在，或者是离线管理状态，则忽略基站来的通知类消息
		if (enb == null || enb.isOfflineManage()) {
			return;
		}
		int ma = message.getMa();
		EnbMessageProcessor processor = processors.get(ma);
		if (processor != null) {
			processor.handle(message);
		}
	}

	/**
	 * 初始化处理器
	 * 
	 */
	private void initProcessors() {
		processors.put(EnbMessageConstants.MA_CONF, new EnbConfProcessor());
		processors.put(EnbMessageConstants.MA_ALARM, new EnbAlarmProcessor());
		processors.put(EnbMessageConstants.MA_PERF, new EnbPerfProcessor());
		// processors.put(McBtsMessage.MA_UT, new McBtsUTProcessor());
		processors.put(EnbMessageConstants.MA_FILE, new EnbFileProcessor());
		processors.put(EnbMessageConstants.MA_SECU, new EnbSecuProcessor());
	}

	/**
	 * 启动处理器
	 * 
	 */
	private void startProcessors() {
		int nThreads = processors.size();
		executor = Executors.newFixedThreadPool(nThreads);
		for (Runnable processor : processors.values()) {
			executor.execute(processor);
		}
	}

}
