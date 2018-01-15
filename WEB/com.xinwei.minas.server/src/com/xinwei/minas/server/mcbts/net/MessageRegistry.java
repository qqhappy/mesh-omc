/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * 发送消息注册表
 * 
 * @author chenjunhua
 * 
 */

public class MessageRegistry {

	private Map<Integer, FutureResult> map = new ConcurrentHashMap<Integer, FutureResult>();

	private static final MessageRegistry instance = new MessageRegistry();

	public static MessageRegistry getInstance() {
		return instance;
	}

	public MessageRegistry() {
	}

	public synchronized void register(int transactionId,
			FutureResult futureResult) {
		map.put(transactionId, futureResult);
	}

	public synchronized FutureResult unregister(Object key) {
		return map.remove(key);
	}

	public synchronized FutureResult get(Object key) {
		return map.get(key);
	}
}
