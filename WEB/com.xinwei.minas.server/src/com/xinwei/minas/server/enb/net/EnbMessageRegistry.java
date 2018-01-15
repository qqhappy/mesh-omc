/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-04-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

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

public class EnbMessageRegistry {

	private Map<Long, FutureResult> map = new ConcurrentHashMap<Long, FutureResult>();

	private static final EnbMessageRegistry instance = new EnbMessageRegistry();

	public static EnbMessageRegistry getInstance() {
		return instance;
	}

	private EnbMessageRegistry() {
	}

	public synchronized void register(long transactionId,
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
