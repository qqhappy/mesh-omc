/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;

/**
 * 
 * Hlr消息处理公共抽象类
 * 
 * 
 * @author tiance
 * 
 */

public abstract class HlrMessageProcessor implements Runnable {
	private static final Log log = LogFactory.getLog(HlrMessageProcessor.class);

	// 队列的最大长度
	private int capacity = 4096;

	private BlockingQueue<HlrUdpMessage> queue = new LinkedBlockingQueue<HlrUdpMessage>(
			capacity);

	private volatile boolean shutdownRequest = false;

	/**
	 * 处理消息
	 * 
	 * @param message
	 */
	public void handle(HlrUdpMessage message) {
		try {
			queue.offer(message, 10, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error("Error putting hlr message into queue.", e);
		}
	}

	/**
	 * 停止任务
	 * 
	 */
	public void shutdown() {
		shutdownRequest = true;
	}

	@Override
	public void run() {
		while (!shutdownRequest) {
			HlrUdpMessage message;
			try {
				message = queue.take();
				doWork(message);
			} catch (InterruptedException e) {
				log.error("Error taking a message from the queue.", e);
				break;
			} catch (Exception e) {
				log.error("Error working on a hlr message.", e);
			}
		}

		finishWork();
	}

	/**
	 * 处理消息内容的抽象方法
	 * 
	 * @param message
	 */
	protected abstract void doWork(HlrUdpMessage message);

	/**
	 * 结束任务
	 * 
	 */
	public void finishWork() {

	}
}
