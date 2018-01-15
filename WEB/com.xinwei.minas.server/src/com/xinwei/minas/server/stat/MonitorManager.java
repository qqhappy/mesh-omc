/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * 监视器缓存
 * 
 * @author fanhaoyu
 * 
 */

public class MonitorManager {

	private Log log = LogFactory.getLog(MonitorManager.class);

	private static final MonitorManager instance = new MonitorManager();

	private Map<String, HashSet<String>> cache = new ConcurrentHashMap<String, HashSet<String>>();

	private Map<String, Long> timeCache = new ConcurrentHashMap<String, Long>();

	private long handShakeTimeOut;

	public static MonitorManager getInstance() {
		return instance;
	}

	public void initialize() {
		handShakeTimeOut = SystemContext.getInstance().getHandShakeTimeOut();
		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new HandShakeTimeOutTask(),
				handShakeTimeOut, handShakeTimeOut, TimeUnit.SECONDS);
	}

	public void addMonitor(MonitorItem item, String sessionId) {
		String key = item.getKey();
		HashSet<String> value = cache.get(key);
		if (value == null || value.isEmpty()) {
			value = new HashSet<String>();
			cache.put(key, value);
		}
		synchronized (value) {
			value.add(sessionId);
		}
	}

	public void removeMonitor(MonitorItem item, String sessionId) {
		HashSet<String> value = cache.get(item.getKey());
		if (value == null || value.isEmpty())
			return;
		synchronized (value) {
			value.remove(sessionId);
		}
		timeCache.remove(sessionId);
	}

	public boolean isMonitoring(MonitorItem item) {
		HashSet<String> value = cache.get(item.getKey());
		if (value == null || value.isEmpty())
			return false;
		return true;
	}

	public HashSet<String> getClientSet(MonitorItem item) {
		return cache.get(item.getKey());
	}

	public void handshake(String sessionId) {
		this.timeCache.put(sessionId, System.currentTimeMillis());
	}

	class HandShakeTimeOutTask extends TimerTask {

		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			Set<String> idSet = timeCache.keySet();
			for (String sessionId : idSet) {
				long lastTime = timeCache.get(sessionId);
				// 握手超时，将客户端从缓存中移除
				if (currentTime - lastTime > handShakeTimeOut) {
					log.debug("Monitor handshake timeout. SessionId="
							+ sessionId);
					Set<String> keySet = cache.keySet();
					for (String key : keySet) {
						HashSet<String> clientSet = cache.get(key);
						if (clientSet.contains(sessionId)) {
							synchronized (clientSet) {
								clientSet.remove(sessionId);
							}
						}
					}
					timeCache.remove(sessionId);
				}
			}
		}

	}

}
