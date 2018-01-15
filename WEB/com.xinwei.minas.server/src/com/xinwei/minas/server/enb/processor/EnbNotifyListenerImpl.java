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
 * eNB��Ϣ֪ͨ������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class EnbNotifyListenerImpl implements EnbNotifyListener {

	private static final Log logger = LogFactory
			.getLog(EnbNotifyListenerImpl.class);

	// �������̳߳�
	private ExecutorService executor;

	// ����ӳ���MA/Processor��
	private Map<Integer, EnbMessageProcessor> processors = new HashMap();

	public EnbNotifyListenerImpl() {
		// ��ʼ��������
		initProcessors();
		// ����������
		startProcessors();
	}

	@Override
	public void receive(EnbAppMessage message) throws Exception {
		Long enbId = message.getEnbId();
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		// �����վ�����ڣ����������߹���״̬������Ի�վ����֪ͨ����Ϣ
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
	 * ��ʼ��������
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
	 * ����������
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
