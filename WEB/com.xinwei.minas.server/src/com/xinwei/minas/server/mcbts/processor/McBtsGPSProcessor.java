/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.common.udp.vo.DefaultUdpData;
import com.xinwei.common.udp.vo.UdpData;
//import com.xinwei.minas.server.location.LocationMessageProcessor;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.task.McBtsGPSDataTaskManager;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * GPS管理消息处理任务
 * 
 * @author tiance
 * 
 */

public class McBtsGPSProcessor extends McBtsMessageProcessor {

	@Override
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();

		if (moc == McBtsMessageConstants.MOC_GPS_DATA_NOTIFY) {
			McBtsGPSDataTaskManager mcBtsGPSDataTaskManager = AppContext
					.getCtx().getBean(McBtsGPSDataTaskManager.class);
			mcBtsGPSDataTaskManager.handleGPSDataNotify(message);
		}
//		if (moc == McBtsMessageConstants.MOC_LOCATION_RESPONSE) {
//			// 通知定位回复消息
//			LocationMessageProcessor locMsgProcessor = AppContext.getCtx()
//					.getBean(LocationMessageProcessor.class);
//			DefaultUdpData udpData = new DefaultUdpData();
//			udpData.setData(message.toBytes());
//			List<UdpData> udpDataList = new ArrayList<UdpData>();
//			udpDataList.add(udpData);
//			locMsgProcessor.notifyLocationResp(udpDataList);
//		}
	}
}
