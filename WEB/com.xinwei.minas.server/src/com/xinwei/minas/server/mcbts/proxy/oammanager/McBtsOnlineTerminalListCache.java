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
 * BTS�����ն��б����ݰ�����
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
	 * ���յ�����Ϣʱ����, �������ȫ�� ����rsp�����û�У�����null
	 * 
	 * @param rsp
	 *            ���յ���Ϣ
	 * @return
	 */
	public McBtsOnlineTerminalList receive(
			McBtsOnlineTerminalList rsp) {
		if (rsp.getFragment() == 0) {
			return rsp;
		} 
		// ����ظ��£�������
		int transId = rsp.getTransID();
		if (!listByTransId.containsKey(transId)) {
			listByTransId.put(transId, new LinkedList());
		}
		List<McBtsOnlineTerminalList> rsps = listByTransId.get(transId);
		//// ���:��resp.getTransID()Ϊkey,��McBtsOnlineTerminalList respΪvalue
		rsps.add(rsp);
		int total = rsp.getTotalCount();
		int currcount = 0;
		for (McBtsOnlineTerminalList rspitem : rsps) {
			currcount += rspitem.getActiveUsers().size();
		}
		// �Ѿ�����merge���Result
		if (currcount >= total) {
			McBtsOnlineTerminalList result = mergeResult(transId);
			return result;
		} else {
			return null;
		}

	}

	/**
	 * ���ָ��transId�Ļ���
	 * @param transId
	 */
	public void clear(int transId) {
		listByTransId.remove(transId);
	}

	/**
	 * �ϲ�ָ��transId�����е�����
	 * @param transId
	 * @return
	 */
	private McBtsOnlineTerminalList mergeResult(int transId) {
		List<McBtsOnlineTerminalList> rsps = listByTransId.get(transId);
		// ��ȡ�յ����ݴ�С
		int currcount = 0;
		for (McBtsOnlineTerminalList rspitem : rsps) {
			currcount += rspitem.getActiveUsers().size();
		}

		// �����յ�����ֵ
		List<ActiveUserInfo> total = new ArrayList<ActiveUserInfo>();
		for (McBtsOnlineTerminalList rspitem : rsps) {
			total.addAll(rspitem.getActiveUsers());
		}
		McBtsOnlineTerminalList result = rsps.get(0);
		result.setActiveUsers(total);
		return result;
	}
}
