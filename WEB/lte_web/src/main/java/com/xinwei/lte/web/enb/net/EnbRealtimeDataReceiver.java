/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.net;

import java.net.DatagramPacket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpServer;
import com.xinwei.lte.web.enb.cache.EnbRealTimeDataCache;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;

/**
 * 
 * eNB实时性能数据接收类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeDataReceiver {

	private Log log = LogFactory.getLog(EnbRealtimeDataReceiver.class);

	private int listeningPort = 22580;

	private UdpServer udpServer;

	private InnerReceiver receiver;

	private EnbRealtimeDataDecoder enbRealtimeDataDecoder;

	public EnbRealtimeDataReceiver(int listeningPort) throws Exception {
		this.listeningPort = listeningPort;
		// 接收队列缓存 单位：byte
		int bufferSize = 2 * 1000 * 1000;
		BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>();
		BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
				10000);
		udpServer = new DefaultUdpServer(this.listeningPort, bufferSize,
				receiveQueue, sendQueue, null);

		receiver = new InnerReceiver(receiveQueue);
		receiver.start();
	}

	public void stop() throws Exception {
		if (this.receiver != null) {
			this.receiver.shutdown();
		}
		if (udpServer != null) {
			this.udpServer.shutdown();
		}
	}

	class InnerReceiver extends Thread {

		private volatile boolean running = true;

		private BlockingQueue<DatagramPacket> receiveQueue;

		public InnerReceiver(BlockingQueue<DatagramPacket> receiveQueue) {
			this.receiveQueue = receiveQueue;
		}

		@Override
		public void run() {
			while (running) {
				try {
					process();
				} catch (Throwable e) {
					if (running) {
						log.warn("process enb realtime data with error.", e);
					}
				}
			}
		}

		private void process() throws Exception {
			DatagramPacket packet = receiveQueue.take();
			int queueLength = receiveQueue.size();
			if (queueLength > 100) {
				log.info("receiveQueue length=" + queueLength);
			}
			int length = packet.getLength();
			byte[] dataBytes = new byte[length];
			System.arraycopy(packet.getData(), 0, dataBytes, 0, length);
			// 解析消息
			List<EnbRealtimeItemData> itemDataList = enbRealtimeDataDecoder
					.decode(dataBytes);
			if (itemDataList == null || itemDataList.isEmpty()) {
				return;
			}
			// 放入缓存
			long moId = itemDataList.get(0).getMoId();
			EnbRealTimeDataCache.getInstance().addData(moId, itemDataList);
		}

		public void shutdown() {
			running = false;
			this.interrupt();
		}

	}

	public void setEnbRealtimeDataDecoder(
			EnbRealtimeDataDecoder enbRealtimeDataDecoder) {
		this.enbRealtimeDataDecoder = enbRealtimeDataDecoder;
	}

}
