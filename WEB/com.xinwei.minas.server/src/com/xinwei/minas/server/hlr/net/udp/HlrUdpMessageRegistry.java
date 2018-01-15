/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * HLR UDP发送消息注册表
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpMessageRegistry {

	// 请求Map
	private Map<Integer, FutureResult> map = new ConcurrentHashMap<Integer, FutureResult>();

	// 应答消息缓存Map
	private Map<Integer, List<HlrUdpMessage>> responseMap = new ConcurrentHashMap<Integer, List<HlrUdpMessage>>();
	
	private String isHlrSupport;

	public HlrUdpMessageRegistry() {
	}

	public void register(int transactionId, FutureResult futureResult) {
		map.put(transactionId, futureResult);
		responseMap.remove(transactionId);
	}

	public FutureResult unregister(Object key) {
		responseMap.remove(key);
		return map.remove(key);
	}

	/**
	 * 增加一个HLR应答消息
	 * 
	 * @param hlrUdpMessage
	 * @return 是否消息已收齐
	 */
	public boolean addResponse(HlrUdpMessage hlrUdpMessage) {
		boolean messageReveivedFully = false;
		int transactionId = hlrUdpMessage.getTransactionId();
		int totalPacket = hlrUdpMessage.getTotalPacket();
		if (!responseMap.containsKey(transactionId)) {
			responseMap.put(transactionId, new LinkedList());
		}
		List<HlrUdpMessage> list = responseMap.get(transactionId);
		list.add(hlrUdpMessage);
		// FIXME: 暂时做兼容处理, 老版本HLRtotalPacket=0
		if (isHlrSupportDivideMessage()) {
			if (list.size() == totalPacket) {
				messageReveivedFully = true;
				FutureResult futureResult = this.get(transactionId);
				if (futureResult != null) {
					futureResult.set(list);
				}
				responseMap.remove(transactionId);
			}
		} else {
			if (hlrUdpMessage.isLastPacket()) {
				messageReveivedFully = true;
				FutureResult futureResult = this.get(transactionId);
				if (futureResult != null) {
					futureResult.set(list);
				}
				responseMap.remove(transactionId);
			}
		}

		return messageReveivedFully;
	}

	private FutureResult get(Object key) {
		return map.get(key);
	}

	public void setIsHlrSupport(String isHlrSupport) {
		this.isHlrSupport = isHlrSupport;
	}

	public String getIsHlrSupport() {
		return isHlrSupport;
	}
	
	private boolean isHlrSupportDivideMessage() {
		if ("true".equals(getIsHlrSupport())) {
			return true;
		} else {
			return false;
		}
	}
}
