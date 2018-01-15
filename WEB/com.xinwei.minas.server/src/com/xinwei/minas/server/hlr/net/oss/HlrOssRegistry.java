/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */
package com.xinwei.minas.server.hlr.net.oss;

import java.util.HashMap;
import java.util.Map;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;

/**
 * HLR消息发送注册表
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssRegistry {
	// 单例模式
	private static HlrOssRegistry instance = new HlrOssRegistry();

	private Map<Integer, HlrOssSyncResult> resultCache = new HashMap();

	private HlrOssRegistry() {

	}

	public static HlrOssRegistry getInstance() {
		return instance;
	}

	/**
	 * 注册一条消息
	 * 
	 * @param sessionId
	 *            消息的事务编号
	 * @return 消息同步等待结果
	 */
	public HlrOssSyncResult register(int sessionId) {
		if (!resultCache.containsKey(sessionId)) {
			resultCache.put(sessionId, new HlrOssSyncResult(sessionId));
		}
		return resultCache.get(sessionId);
	}

	/**
	 * 注销一条消息
	 * 
	 * @param sessionId
	 *            消息的事务编号
	 * @return 消息同步等待结果
	 */
	public HlrOssSyncResult deregister(int sessionId) {
		return resultCache.remove(sessionId);
	}

	/**
	 * 根据消息的事务编号获取同步等待结果
	 * 
	 * @param sessionId
	 *            消息的事务编号
	 * @return 消息同步等待结果
	 */
	public HlrOssSyncResult getBy(int sessionId) {
		return resultCache.get(sessionId);
	}
	
	public void addResponse(HlrOssBizMessage hlrOssBizMessage) {
		int sessionId = hlrOssBizMessage.getSessionId();
		HlrOssSyncResult result = this.getBy(sessionId);
		if (result != null) {
			HlrOssBizMessage fullMessage = result.append(hlrOssBizMessage);
		}
	}
}
