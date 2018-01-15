/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;

/**
 * 
 * McBts消息处理器公共抽象类
 * 
 * @author chenjunhua
 * 
 */

public abstract class McBtsMessageProcessor implements Runnable{
	
	private static final Log log = LogFactory.getLog(McBtsMessageProcessor.class);
	
	private int capacity = 2048;
	
	private BlockingQueue<McBtsMessage> queue = new LinkedBlockingQueue(capacity);;
	
	private volatile boolean shutdownRequest = false;
	
	/**
	 * 处理消息
	 * @param message
	 */
	public void handle(McBtsMessage message) {
		try {
			queue.offer(message, 10, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 停止任务
	 *
	 */
	public void shutdown() {
		shutdownRequest = true;
	}
	
	public void run() {
		while (!shutdownRequest) {
			try {
				McBtsMessage message = queue.take();
				doWork(message);
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				log.error("failed to process message.", e);
			}
		}
		finishWork();
	}
	
	/**
	 * 执行任务
	 * @param message
	 */
	public abstract void doWork(McBtsMessage message)  ;
	
	
	/**
	 * 结束任务
	 *
	 */
	public void finishWork() {
		
	}
}
