/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.oammanager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.minas.server.mcbts.model.message.McBtsOnlineTerminalList;

/**
 * 
 * BTS在线终端列表数据包缓存
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalListCache {
	
	private static final McBtsOnlineTerminalListCache instance = new McBtsOnlineTerminalListCache();

	private Map<Integer, List<McBtsOnlineTerminalList>> listByTransId = new ConcurrentHashMap();

	private McBtsOnlineTerminalListCache() {
		
	}
	
	public static McBtsOnlineTerminalListCache getInstance() {
		return instance;
	}
	
	/**
	 * 在收到新消息时调用, 如果收齐全， 返回rsp，如果没有，返回null
	 * 
	 * @param rsp
	 *            新收到消息
	 * @return
	 */
	public McBtsOnlineTerminalList receive(
			McBtsOnlineTerminalList rsp) {
		if (rsp.getFragment() == 0) {
			return rsp;
		} 
		// 非相关更新，不处理
		int transId = rsp.getTransID();
		if (!listByTransId.containsKey(transId)) {
			listByTransId.put(transId, new LinkedList());
		}
		List<McBtsOnlineTerminalList> rsps = listByTransId.get(transId);
		//// 组包:以resp.getTransID()为key,以McBtsOnlineTerminalList resp为value
		rsps.add(rsp);
		int total = rsp.getTotalCount();
		int currcount = 0;
		for (McBtsOnlineTerminalList rspitem : rsps) {
			currcount += rspitem.getActiveUsers().size();
		}
		// 已经收齐merge组包Result
		if (currcount >= total) {
			McBtsOnlineTerminalList result = mergeResult(transId);
			return result;
		} else {
			return null;
		}

	}

	/**
	 * 清除指定transId的缓存
	 * @param transId
	 */
	public void clear(int transId) {
		listByTransId.remove(transId);
	}

	/**
	 * 合并指定transId缓存中的数据
	 * @param transId
	 * @return
	 */
	private McBtsOnlineTerminalList mergeResult(int transId) {
		List<McBtsOnlineTerminalList> rsps = listByTransId.get(transId);
		// 获取收到数据大小
		int currcount = 0;
		for (McBtsOnlineTerminalList rspitem : rsps) {
			currcount += rspitem.getActiveUsers().size();
		}

		// 拷贝收到的数值
		List<ActiveUserInfo> total = new ArrayList<ActiveUserInfo>();
		for (McBtsOnlineTerminalList rspitem : rsps) {
			total.addAll(rspitem.getActiveUsers());
		}
		McBtsOnlineTerminalList result = rsps.get(0);
		result.setActiveUsers(total);
		return result;
	}
}
