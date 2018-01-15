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
 * eNB心跳任务
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

	// 连续心跳失败次数阈值 (提取到配置文件)
	private int failedTimesThreashold = 10;

	// 心跳任务启动延迟时间(单位:秒)
	private int delay = 5;

	// 心跳任务周期(单位:秒)
	private int period = 3;

	private ScheduledExecutorService exec;

	private EnbHeartbeatTaskManager() {

	}

	public static EnbHeartbeatTaskManager getInstance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param timeoutThreashold
	 *            连续心跳失败次数阈值
	 * @param delay
	 *            心跳任务启动延迟时间(单位:秒)
	 * @param period
	 *            心跳任务周期(单位:秒)
	 */
	public void initialize(int failedTimesThreashold, int delay, int period) {
		this.failedTimesThreashold = failedTimesThreashold;
		this.delay = delay;
		this.period = period;
		// 初始化任务
		initializeTask();
	}

	/**
	 * 初始化任务
	 */
	private void initializeTask() {
		connector = AppContext.getCtx().getBean(EnbConnector.class);
		exec = Executors.newScheduledThreadPool(1);
		// 定时执行发送任务
		exec.scheduleAtFixedRate(new SendTask(), delay, period,
				TimeUnit.SECONDS);
	}

	/**
	 * 处理心跳应答
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
	 * 创建心跳消息
	 * 
	 * @param enbId
	 * @return
	 */
	private EnbAppMessage buildHeartbeatMessage(Long enbId) {
		EnbHeartbeatRequest req = new EnbHeartbeatRequest(enbId);		
		return req.toEnbAppMessage();
	}

	/**
	 * 发送信息
	 * 
	 * @author chenjunhua
	 * 
	 */
	class SendInfo {

		// 发送次数
		private AtomicInteger sendTimes = new AtomicInteger(0);

		/**
		 * 增加发送次数
		 * 
		 */
		public int increaseSendTimes() {
			return sendTimes.incrementAndGet();
		}

		/**
		 * 重置发送次数
		 * 
		 */
		public void resetSendTimes() {
			sendTimes.set(0);
		}

		/**
		 * 判断心跳发送无应答次数是否超过阈值
		 * 
		 * @param threshold
		 * @return
		 */
		public boolean isOverThreshold(int threshold) {
			return sendTimes.get() >= threshold;
		}
	}

	/**
	 * 定时发送任务
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
						// 如果基站处于未连接状态，则不发送心跳消息
						continue;
					}
					if (!sentMessageMap.containsKey(enbId)) {
						sentMessageMap.put(enbId, new SendInfo());
					}
					// 获取指定基站的心跳发送信息
					SendInfo sendInfo = sentMessageMap.get(enbId);
					// 连续无应答次数超过阈值
					if (sendInfo.isOverThreshold(failedTimesThreashold)) {
						debug(enbId,
								"enb heartbeat response overtime, set disconnected.");
						// 设置为未连接
						EnbStatusChangeHelper.setDisconnected(enb);
						// 清除发送列表
						sentMessageMap.remove(enbId);
						continue;
					}
					// 增加发送次数
					sendInfo.increaseSendTimes();
					// 构造并异步发送心跳消息
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
