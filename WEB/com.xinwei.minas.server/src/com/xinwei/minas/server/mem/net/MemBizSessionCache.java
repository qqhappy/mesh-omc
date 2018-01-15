package com.xinwei.minas.server.mem.net;

import java.util.HashMap;

public class MemBizSessionCache {

	private static MemBizSessionCache cache = new MemBizSessionCache();

	private HashMap<Long, Integer> sessionMap = new HashMap<Long, Integer>();

	public static MemBizSessionCache getInstance() {
		return cache;
	}

	public void addSession(long btsId, int sessionId) {
		sessionMap.put(btsId, sessionId);
	}

	public void removeSession(long btsId) {
		sessionMap.remove(btsId);
	}

	public int getSession(long btsId) {
		return sessionMap.get(btsId);
	}

	public void clear() {
		sessionMap.clear();
	}
}
