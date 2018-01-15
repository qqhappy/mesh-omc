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
 * HLR UDP������Ϣע���
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpMessageRegistry {

	// ����Map
	private Map<Integer, FutureResult> map = new ConcurrentHashMap<Integer, FutureResult>();

	// Ӧ����Ϣ����Map
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
	 * ����һ��HLRӦ����Ϣ
	 * 
	 * @param hlrUdpMessage
	 * @return �Ƿ���Ϣ������
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
		// FIXME: ��ʱ�����ݴ���, �ϰ汾HLRtotalPacket=0
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
