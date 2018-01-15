/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.enb.net.EnbAppMessage;


/**
 * 
 * McBts消息处理器公共抽象类
 * 
 * @author chenjunhua
 * 
 */

public abstract class EnbMessageProcessor implements Runnable{
	
	private static final Log log = LogFactory.getLog(EnbMessageProcessor.class);
	
	private int capacity = 2048;
	
	private BlockingQueue<EnbAppMessage> queue = new LinkedBlockingQueue(capacity);;
	
	private volatile boolean shutdownRequest = false;
	
	/**
	 * 处理消息
	 * @param message
	 */
	public void handle(EnbAppMessage message) {
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
				EnbAppMessage message = queue.take();
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
	public abstract void doWork(EnbAppMessage message)  ;
	
	
	/**
	 * 结束任务
	 *
	 */
	public void finishWork() {
		
	}
}
