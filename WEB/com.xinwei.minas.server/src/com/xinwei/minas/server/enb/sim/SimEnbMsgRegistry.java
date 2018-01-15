/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-04-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.sim;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * 模拟eNB发送消息注册表
 * 
 * @author fanhaoyu
 * 
 */

public class SimEnbMsgRegistry {

	private Map<Long, FutureResult> map = new ConcurrentHashMap<Long, FutureResult>();

	private static final SimEnbMsgRegistry instance = new SimEnbMsgRegistry();

	public static SimEnbMsgRegistry getInstance() {
		return instance;
	}

	private SimEnbMsgRegistry() {
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
