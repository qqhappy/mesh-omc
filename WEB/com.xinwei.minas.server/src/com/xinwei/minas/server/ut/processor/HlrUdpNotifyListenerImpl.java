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
 * HLR UDP֪ͨ
 * <p>
 * HLR������ƽ̨��������Ϣ�����
 * </p>
 * 
 * @author tiance
 * 
 */

public class HlrUdpNotifyListenerImpl implements HlrUdpNotifyListener {

	private static final Log log = LogFactory
			.getLog(HlrUdpNotifyListenerImpl.class);

	private int nThreads = 10;

	// �������̳߳�
	private ExecutorService executor = Executors.newFixedThreadPool(nThreads);

	// ����ӳ���MA/Processor��
	private Map<Integer, HlrMessageProcessor> processors = new HashMap<Integer, HlrMessageProcessor>();

	public HlrUdpNotifyListenerImpl() {
		// ��ʼ��������
		initProcesrors();
		// ����������
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
	 * ��ʼ��������
	 * 
	 */
	private void initProcesrors() {
		processors.put(HlrUdpMessage.UT_REGISTER_NTFY,
				new UTRegisterProcessor());
		processors.put(HlrUdpMessage.QUERY_BTS_REQ,
				new QueryBtsRequestProcessor());
	}

	/**
	 * ����������
	 * 
	 */
	private void startProcessors() {
		for (Runnable processor : processors.values()) {
			executor.execute(processor);
		}
	}
}
