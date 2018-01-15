/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.oammanager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * 发送消息注册表
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalListRegistry {

	private Map<Integer, FutureResult> map = new ConcurrentHashMap<Integer, FutureResult>();

	private static final McBtsOnlineTerminalListRegistry instance = new McBtsOnlineTerminalListRegistry();

	public static McBtsOnlineTerminalListRegistry getInstance() {
		return instance;
	}

	public McBtsOnlineTerminalListRegistry() {
	}

	public synchronized void register(int transactionId,
			FutureResult futureResult) {
		map.put(transactionId, futureResult);
	}

	public synchronized FutureResult unregister(int transactionId) {
		McBtsOnlineTerminalListCache.getInstance().clear(transactionId);
		return map.remove(transactionId);
	}

	public synchronized FutureResult get(Object key) {
		return map.get(key);
	}
}
