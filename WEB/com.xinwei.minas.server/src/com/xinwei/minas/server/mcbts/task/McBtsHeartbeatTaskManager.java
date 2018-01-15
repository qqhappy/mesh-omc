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
 * McBts心跳任务
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

	// 连续心跳失败次数阈值 (提取到配置文件)
	private int failedTimesThreashold = 10;

	// 心跳任务启动延迟时间(单位:秒)
	private int delay = 5;

	// 心跳任务周期(单位:秒)
	private int period = 3;

	private ScheduledExecutorService exec;

	private McBtsHeartbeatTaskManager() {

	}

	public static McBtsHeartbeatTaskManager getInstance() {
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
		connector = AppContext.getCtx().getBean(McBtsConnector.class);
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
	public void handleResponse(McBtsMessage response) {
		Long btsId = response.getBtsId();
		debug(btsId, "receive mcbts heartbeat response.");
		SendInfo sendInfo = sentMessageMap.get(btsId);
		if (sendInfo != null) {
			sendInfo.resetSendTimes();
		}
	}

	/**
	 * 创建心跳消息
	 * 
	 * @param btsId
	 * @return
	 */
	private McBtsMessage buildHeartbeatMessage(Long btsId) {
		McBtsHeartbeatRequest request = new McBtsHeartbeatRequest(btsId);
		return request.toMcBtsMessage();
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
			Set<Long> mcBtsIdCollection = McBtsCache.getInstance()
					.queryAllBtsId();
			for (Long btsId : mcBtsIdCollection) {
				try {
					McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
					if (mcBts == null) {
						continue;
					}
					if (mcBts.isDisconnected() || mcBts.isDeleted()) {
						// 如果基站处于未连接状态，则不发送心跳消息
						continue;
					}
					if (!sentMessageMap.containsKey(btsId)) {
						sentMessageMap.put(btsId, new SendInfo());
					}
					// 获取指定基站的心跳发送信息
					SendInfo sendInfo = sentMessageMap.get(btsId);
					// 连续无应答次数超过阈值
					if (sendInfo.isOverThreshold(failedTimesThreashold)) {
						debug(btsId,
								"mcbts heartbeat response overtime, set disconnected.");
						// 设置为未连接
						McBtsStatusChangeHelper.setDisconnected(mcBts);
						// 清除发送列表
						sentMessageMap.remove(btsId);
						continue;
					}
					// 增加发送次数
					sendInfo.increaseSendTimes();
					// 构造并异步发送心跳消息
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
