/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeMonitorService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB实时性能消息处理器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeMsgHandler {

	private Log log = LogFactory.getLog(EnbRealtimeMsgHandler.class);

	private static final EnbRealtimeMsgHandler instance = new EnbRealtimeMsgHandler();

	private Map<Long, Enb> enbToStop = new ConcurrentHashMap<Long, Enb>();

	private ScheduledExecutorService executorService;

	private ExecutorService stopOneEnbService;

	private BlockingQueue<EnbAppMessage> messageQueue;

	private MessageProcessor messageProcessor;

	private EnbRealtimeDataSender enbRealtimeDataSender;

	private EnbRealtimeMessageDecoder enbRealtimeMessageDecoder;

	private EnbRealtimeMsgHandler() {
		// 默认10s执行一次
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new StopMonitorTask(), 10, 10,
				TimeUnit.SECONDS);

		messageQueue = new LinkedBlockingQueue<EnbAppMessage>();
		messageProcessor = new MessageProcessor(messageQueue);
		messageProcessor.start();
	}

	public static EnbRealtimeMsgHandler getInstance() {
		return instance;
	}

	public void handle(EnbAppMessage appMessage) {
		try {
			messageQueue.offer(appMessage);
		} catch (Throwable e) {
			log.error("EnbRealtimeMsgHandler handle message error.", e);
		}
	}

	public void dispose() {
		if (executorService != null) {
			executorService.shutdownNow();
		}
		if (stopOneEnbService != null) {
			stopOneEnbService.shutdownNow();
		}
		if (messageProcessor != null) {
			messageProcessor.shutdown();
			messageQueue = null;
		}
	}

	class MessageProcessor extends Thread {

		private volatile boolean running = true;

		private BlockingQueue<EnbAppMessage> queue;

		public MessageProcessor(BlockingQueue<EnbAppMessage> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (running) {
				try {
					process();
				} catch (Throwable e) {
					if (running) {
						log.warn("process enb realtime message with error.", e);
					}
				}
			}
		}

		private void process() throws Exception {
			EnbAppMessage appMessage = queue.take();
			long enbId = appMessage.getEnbId();
			Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
			if (enb == null)
				return;
			// 未监控状态下收到基站的上报消息，给基站下发停止命令
			if (!enb.isMonitoring()) {
				enbToStop.put(enb.getEnbId(), enb);
				return;
			}
			// 解析消息
			List<EnbRealtimeItemData> itemDataList = enbRealtimeMessageDecoder
					.decode(appMessage);
			if (enbRealtimeDataSender != null) {
				// 将数据按小区分组发送，防止过长
				Map<String, List<EnbRealtimeItemData>> entityOidMap = new HashMap<String, List<EnbRealtimeItemData>>();
				for (EnbRealtimeItemData itemData : itemDataList) {
					String entityOid = itemData.getEntityOid();
					List<EnbRealtimeItemData> dataList = entityOidMap
							.get(entityOid);
					if (dataList == null) {
						dataList = new LinkedList<EnbRealtimeItemData>();
						entityOidMap.put(entityOid, dataList);
					}
					dataList.add(itemData);
				}
				for (String entityOid : entityOidMap.keySet()) {
					// 发送给客户端
					enbRealtimeDataSender.sendData(enb.getMoId(),
							entityOidMap.get(entityOid));
				}
			}
		}

		public void shutdown() {
			running = false;
			this.interrupt();
		}

	}

	class StopMonitorTask implements Runnable {

		@Override
		public void run() {
			try {
				// 无内容直接结束
				if (enbToStop.isEmpty())
					return;
				CopyOnWriteArraySet<Long> enbIdSet = new CopyOnWriteArraySet<Long>(
						enbToStop.keySet());
				if (stopOneEnbService == null) {
					stopOneEnbService = Executors.newCachedThreadPool();
				}
				for (final Long enbId : enbIdSet) {
					log.debug("stop monitor. enbId="
							+ enbToStop.get(enbId).getHexEnbId());
					// 防止eNB回复超时，阻塞任务，每个eNB单独一个线程处理
					stopOneEnbService.execute(new StopTask(enbId));
				}
			} catch (Throwable e) {
				log.error("StopMonitorTask execute with error.", e);
			}

		}

	}

	class StopTask implements Runnable {

		private long enbId;

		public StopTask(long enbId) {
			this.enbId = enbId;
		}

		@Override
		public void run() {
			Enb enb = enbToStop.get(enbId);
			try {
				boolean enbExist = EnbCache.getInstance().enbExists(enbId);
				// eNB存在，下发停止命令
				if (enbExist) {
					EnbRealtimeMonitorService service = OmpAppContext.getCtx()
							.getBean(EnbRealtimeMonitorService.class);
					service.stopMonitor("", enb.getMoId());
					enbToStop.remove(enbId);
				} else {
					// eNB不存在，直接移除
					enbToStop.remove(enbId);
				}
			} catch (Exception e) {
				log.warn(
						"send stop monitor msg failed. engId"
								+ enb.getHexEnbId(), e);
			}
		}

	}

	public void setEnbRealtimeDataSender(
			EnbRealtimeDataSender enbRealtimeDataSender) {
		this.enbRealtimeDataSender = enbRealtimeDataSender;
	}

	public void setEnbRealtimeMessageDecoder(
			EnbRealtimeMessageDecoder enbRealtimeMessageDecoder) {
		this.enbRealtimeMessageDecoder = enbRealtimeMessageDecoder;
	}

}
