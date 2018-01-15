/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsNotifyListener;
import com.xinwei.minas.server.mcbts.service.McBtsCache;

/**
 * 
 * McBts��Ϣ֪ͨ�������ӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsNotifyListenerImpl implements McBtsNotifyListener {
	
	private static final Logger logger = Logger.getLogger(McBtsNotifyListenerImpl.class);

	private int nThreads = 10;
	
	// �������̳߳�
	private ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		
	// ����ӳ���MA/Processor��
	private Map<Integer, McBtsMessageProcessor> processors = new HashMap();
	
	
	public McBtsNotifyListenerImpl() {
		// ��ʼ��������
		initProcessors();
		// ����������
		startProcessors();
	}
	
	
	
	/**
	 * ����McBts��Ϣ
	 * 
	 * @param message
	 *            McBts��Ϣ
	 */
	public void receive(McBtsMessage message) throws Exception {
		int ma = message.getMa();
		McBtsMessageProcessor processor = processors.get(ma);
		// �����վ�����߹���״̬������Ի�վ����֪ͨ����Ϣ
		Long btsId = message.getBtsId();
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);		
		if (mcBts != null && mcBts.isOfflineManage()) {
			// logger.error("BTS is offline, discard message. btsId=0x" + mcBts.getHexBtsId());
			return;
		}
		if (processor != null) {
			processor.handle(message);
		}
	}
	
	
	/**
	 * ��ʼ��������
	 *
	 */
	private void initProcessors() {	
		processors.put(McBtsMessage.MA_CONF, new McBtsConfProcessor());		
		processors.put(McBtsMessage.MA_ALARM, new McBtsAlarmProcessor());
		processors.put(McBtsMessage.MA_PERF, new McBtsPerfProcessor());
		processors.put(McBtsMessage.MA_UT, new McBtsUTProcessor());
		processors.put(McBtsMessage.MA_FILE, new McBtsFileProcessor());
		processors.put(McBtsMessage.MA_BTS_SECU, new McBtsSecuProcessor());		
		processors.put(McBtsMessage.MA_GPS, new McBtsGPSProcessor());
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
