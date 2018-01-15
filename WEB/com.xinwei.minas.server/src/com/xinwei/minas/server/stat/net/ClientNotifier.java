/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.net;

import java.net.DatagramPacket;
import java.util.ArrayList;
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
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.stat.MonitorManager;
import com.xinwei.minas.server.stat.SystemContext;
import com.xinwei.minas.stat.core.model.AbstractData;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatDataPackage;
import com.xinwei.minas.stat.core.model.TargetAddress;

/**
 * 
 * 客户端通知器
 * 
 * @author fanhaoyu
 * 
 */

public class ClientNotifier {

	private Log log = LogFactory.getLog(ClientNotifier.class);

	private static final ClientNotifier instance = new ClientNotifier();

	private UdpServer udpServer;

	public static ClientNotifier getInstance() {
		return instance;
	}

	private ClientNotifier() {
	}

	public void initialize() throws Exception {
		// 接收队列缓存 单位：byte
		int bufferSize = 2 * 1000 * 1000;
		BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>(
				10000);
		BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
				10000);
		int listeningPort = SystemContext.getInstance().getServerUdpPort();
		udpServer = new DefaultUdpServer(listeningPort, bufferSize,
				receiveQueue, sendQueue, null);
	}

	public void sendData(AbstractData abstractData,
			List<TargetAddress> udpAddressList) throws Exception {
		SingleStatItemData data = (SingleStatItemData) abstractData;
		if (udpAddressList == null || udpAddressList.isEmpty()) {
			log.error("ClientNotifier targetList is null. " + data.toString());
			return;
		}
		data.setUdpAddressList(udpAddressList);
		this.send(data);

	}

	public void sendData(AbstractData abstractData) throws Exception {
		SingleStatItemData data = (SingleStatItemData) abstractData;
		MonitorItem item = new MonitorItem(data.getBtsId(), data.getItemId());
		Set<String> sessionList = MonitorManager.getInstance().getClientSet(
				item);
		if (sessionList == null || sessionList.isEmpty()) {
			log.error("ClientNotifier sessionList is null. " + item.toString());
			return;
		}
		// 获取目的客户端
		List<TargetAddress> udpAddressList = new ArrayList<TargetAddress>();
		for (String sessionId : sessionList) {
			LoginUser loginUser = LoginUserCache.getInstance().queryOnlineUser(
					sessionId, false);
			udpAddressList.add(new TargetAddress(loginUser.getLoginIp(),
					SystemContext.getInstance().getClientUdpPort()));
		}
		data.setUdpAddressList(udpAddressList);

		this.send(data);
	}

	private void send(SingleStatItemData statData) throws Exception {
		List<SingleStatItemData> statDatas = new ArrayList<SingleStatItemData>();
		statDatas.add(statData);
		List<TargetAddress> targetList = statData.getUdpAddressList();
		List<UdpAddress> udpAddressList = new ArrayList<UdpAddress>();
		for (TargetAddress target : targetList) {
			udpAddressList.add(new UdpAddress(target.getInetAddress(), target
					.getPort()));
		}
		StatDataPackage pkg = new StatDataPackage(statDatas);
		byte[] buf = pkg.toBytes();
		log.info("ClientNotifier is sending statData to clients. ClientList="
				+ targetList.toString());
		udpServer.send(udpAddressList, buf);
//		log.info("finish sending statData to clients.");
		pkg = null;
	}

}
