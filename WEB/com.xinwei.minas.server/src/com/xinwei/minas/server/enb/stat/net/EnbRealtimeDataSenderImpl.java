/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpAddress;
import com.xinwei.common.udp.UdpServer;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;

/**
 * 
 * 实时性能数据发送器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeDataSenderImpl implements EnbRealtimeDataSender {

	private Log log = LogFactory.getLog(EnbRealtimeDataSenderImpl.class);

	private int clientPort;

	private UdpServer udpServer;

	private EnbRealtimeDataEncoder enbRealtimeDataEncoder;

	public EnbRealtimeDataSenderImpl(int serverPort, int clientPort)
			throws Exception {
		this.clientPort = clientPort;
		// 接收队列缓存 单位：byte
		int bufferSize = 2 * 1000 * 1000;
		BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>();
		BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
				10000);
		udpServer = new DefaultUdpServer(serverPort, bufferSize, receiveQueue,
				sendQueue, null);
	}

	@Override
	public void sendData(long moId, List<EnbRealtimeItemData> itemDataList)
			throws Exception {
		List<String> targetIpList = getTargetIpList();
		if (targetIpList == null || targetIpList.isEmpty()) {
			log.info("RealtimeDataSender targetList is empty.");
			return;
		}
		// 要发送的性能数据进行编码
		byte[] content = enbRealtimeDataEncoder.encode(moId, itemDataList);

		List<UdpAddress> udpAddressList = new ArrayList<UdpAddress>();
		for (String target : targetIpList) {
			udpAddressList.add(new UdpAddress(target, clientPort));
		}
//		log.info("RealtimeDataSender is sending statData to clients. ClientList="
//				+ targetIpList.toString());
		udpServer.send(udpAddressList, content);
//		log.info("finish sending statData to clients.");
	}

	/**
	 * 获取要发送的IP地址列表
	 * 
	 * @return
	 */
	private List<String> getTargetIpList() {
		List<LoginUser> userList = LoginUserCache.getInstance()
				.queryOnlineUser();
		if (userList == null || userList.isEmpty())
			return Collections.emptyList();
		Set<String> ipList = new HashSet<String>();
		for (LoginUser user : userList) {
			ipList.add(user.getLoginIp());
		}
		return new LinkedList<String>(ipList);
	}

	public void setEnbRealtimeDataEncoder(
			EnbRealtimeDataEncoder enbRealtimeDataEncoder) {
		this.enbRealtimeDataEncoder = enbRealtimeDataEncoder;
	}

}
