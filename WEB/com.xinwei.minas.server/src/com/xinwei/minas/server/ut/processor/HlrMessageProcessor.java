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
 * Hlr��Ϣ������������
 * 
 * 
 * @author tiance
 * 
 */

public abstract class HlrMessageProcessor implements Runnable {
	private static final Log log = LogFactory.getLog(HlrMessageProcessor.class);

	// ���е���󳤶�
	private int capacity = 4096;

	private BlockingQueue<HlrUdpMessage> queue = new LinkedBlockingQueue<HlrUdpMessage>(
			capacity);

	private volatile boolean shutdownRequest = false;

	/**
	 * ������Ϣ
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
	 * ֹͣ����
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
	 * ������Ϣ���ݵĳ��󷽷�
	 * 
	 * @param message
	 */
	protected abstract void doWork(HlrUdpMessage message);

	/**
	 * ��������
	 * 
	 */
	public void finishWork() {

	}
}
