/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.helper.EnbStatusChangeHelper;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.model.message.EnbHeartbeatRequest;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * eNB��������
 * 
 * @author chenjunhua
 * 
 */

public class EnbHeartbeatTaskManager {

	private static final Log log = LogFactory
			.getLog(EnbHeartbeatTaskManager.class);

	private static final EnbHeartbeatTaskManager instance = new EnbHeartbeatTaskManager();

	private EnbConnector connector;

	private Map<Long, SendInfo> sentMessageMap = new ConcurrentHashMap();

	// ��������ʧ�ܴ�����ֵ (��ȡ�������ļ�)
	private int failedTimesThreashold = 10;

	// �������������ӳ�ʱ��(��λ:��)
	private int delay = 5;

	// ������������(��λ:��)
	private int period = 3;

	private ScheduledExecutorService exec;

	private EnbHeartbeatTaskManager() {

	}

	public static EnbHeartbeatTaskManager getInstance() {
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
		connector = AppContext.getCtx().getBean(EnbConnector.class);
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
	public void handleResponse(EnbAppMessage response) {
		Long enbId = response.getEnbId();
		debug(enbId, "receive enb heartbeat response.");
		SendInfo sendInfo = sentMessageMap.get(enbId);
		if (sendInfo != null) {
			sendInfo.resetSendTimes();
		}
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param enbId
	 * @return
	 */
	private EnbAppMessage buildHeartbeatMessage(Long enbId) {
		EnbHeartbeatRequest req = new EnbHeartbeatRequest(enbId);		
		return req.toEnbAppMessage();
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
			Set<Long> enbIdCollection = EnbCache.getInstance().queryAllEnbId();
			for (Long enbId : enbIdCollection) {
				try {
					Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
					if (enb == null) {
						continue;
					}
					if (enb.isDisconnected() || enb.isDeleted()) {
						// �����վ����δ����״̬���򲻷���������Ϣ
						continue;
					}
					if (!sentMessageMap.containsKey(enbId)) {
						sentMessageMap.put(enbId, new SendInfo());
					}
					// ��ȡָ����վ������������Ϣ
					SendInfo sendInfo = sentMessageMap.get(enbId);
					// ������Ӧ�����������ֵ
					if (sendInfo.isOverThreshold(failedTimesThreashold)) {
						debug(enbId,
								"enb heartbeat response overtime, set disconnected.");
						// ����Ϊδ����
						EnbStatusChangeHelper.setDisconnected(enb);
						// ��������б�
						sentMessageMap.remove(enbId);
						continue;
					}
					// ���ӷ��ʹ���
					sendInfo.increaseSendTimes();
					// ���첢�첽����������Ϣ
					debug(enbId, "send heartbeat to enb.");
					EnbAppMessage request = buildHeartbeatMessage(enb
							.getEnbId());

					connector.asyncInvoke(request);

				} catch (Exception e) {
					log.error("Error sending heart beating task.", e);
				}
			}
		}
	}

	private void debug(Long enbId, String message) {
		EnbUtils.log(enbId, "Heartbeat", message);
	}
}
