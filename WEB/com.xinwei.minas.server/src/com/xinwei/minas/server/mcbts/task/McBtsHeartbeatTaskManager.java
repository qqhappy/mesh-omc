/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.helper.McBtsStatusChangeHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsHeartbeatRequest;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * McBts��������
 * 
 * @author chenjunhua
 * 
 */

public class McBtsHeartbeatTaskManager {

	private static final Log log = LogFactory
			.getLog(McBtsHeartbeatTaskManager.class);

	private static final McBtsHeartbeatTaskManager instance = new McBtsHeartbeatTaskManager();

	private McBtsConnector connector;

	private Map<Long, SendInfo> sentMessageMap = new ConcurrentHashMap();

	// ��������ʧ�ܴ�����ֵ (��ȡ�������ļ�)
	private int failedTimesThreashold = 10;

	// �������������ӳ�ʱ��(��λ:��)
	private int delay = 5;

	// ������������(��λ:��)
	private int period = 3;

	private ScheduledExecutorService exec;

	private McBtsHeartbeatTaskManager() {

	}

	public static McBtsHeartbeatTaskManager getInstance() {
		return instance;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param timeoutThreashold
	 *            ��������ʧ�ܴ�����ֵ
	 * @param delay
	 *            �������������ӳ�ʱ��(��λ:��)
	 * @param period
	 *            ������������(��λ:��)
	 */
	public void initialize(int failedTimesThreashold, int delay, int period) {
		this.failedTimesThreashold = failedTimesThreashold;
		this.delay = delay;
		this.period = period;
		// ��ʼ������
		initializeTask();
	}

	/**
	 * ��ʼ������
	 */
	private void initializeTask() {
		connector = AppContext.getCtx().getBean(McBtsConnector.class);
		exec = Executors.newScheduledThreadPool(1);
		// ��ʱִ�з�������
		exec.scheduleAtFixedRate(new SendTask(), delay, period,
				TimeUnit.SECONDS);
	}

	/**
	 * ��������Ӧ��
	 * 
	 * @param response
	 */
	public void handleResponse(McBtsMessage response) {
		Long btsId = response.getBtsId();
		debug(btsId, "receive mcbts heartbeat response.");
		SendInfo sendInfo = sentMessageMap.get(btsId);
		if (sendInfo != null) {
			sendInfo.resetSendTimes();
		}
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param btsId
	 * @return
	 */
	private McBtsMessage buildHeartbeatMessage(Long btsId) {
		McBtsHeartbeatRequest request = new McBtsHeartbeatRequest(btsId);
		return request.toMcBtsMessage();
	}

	/**
	 * ������Ϣ
	 * 
	 * @author chenjunhua
	 * 
	 */
	class SendInfo {

		// ���ʹ���
		private AtomicInteger sendTimes = new AtomicInteger(0);

		/**
		 * ���ӷ��ʹ���
		 * 
		 */
		public int increaseSendTimes() {
			return sendTimes.incrementAndGet();
		}

		/**
		 * ���÷��ʹ���
		 * 
		 */
		public void resetSendTimes() {
			sendTimes.set(0);
		}

		/**
		 * �ж�����������Ӧ������Ƿ񳬹���ֵ
		 * 
		 * @param threshold
		 * @return
		 */
		public boolean isOverThreshold(int threshold) {
			return sendTimes.get() >= threshold;
		}
	}

	/**
	 * ��ʱ��������
	 * 
	 * @author chenjunhua
	 * 
	 */
	class SendTask implements Runnable {
		public void run() {
			Set<Long> mcBtsIdCollection = McBtsCache.getInstance()
					.queryAllBtsId();
			for (Long btsId : mcBtsIdCollection) {
				try {
					McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
					if (mcBts == null) {
						continue;
					}
					if (mcBts.isDisconnected() || mcBts.isDeleted()) {
						// �����վ����δ����״̬���򲻷���������Ϣ
						continue;
					}
					if (!sentMessageMap.containsKey(btsId)) {
						sentMessageMap.put(btsId, new SendInfo());
					}
					// ��ȡָ����վ������������Ϣ
					SendInfo sendInfo = sentMessageMap.get(btsId);
					// ������Ӧ�����������ֵ
					if (sendInfo.isOverThreshold(failedTimesThreashold)) {
						debug(btsId,
								"mcbts heartbeat response overtime, set disconnected.");
						// ����Ϊδ����
						McBtsStatusChangeHelper.setDisconnected(mcBts);
						// ��������б�
						sentMessageMap.remove(btsId);
						continue;
					}
					// ���ӷ��ʹ���
					sendInfo.increaseSendTimes();
					// ���첢�첽����������Ϣ
					debug(btsId, "send heartbeat to mcbts.");
					McBtsMessage request = buildHeartbeatMessage(mcBts
							.getBtsId());

					connector.asyncInvoke(request);

				} catch (Exception e) {
					log.error("Error sending heart beating task.", e);
				}
			}
		}
	}

	private void debug(Long btsId, String message) {
		// McBtsUtils.log(btsId, "Heartbeat", message);
	}
}
