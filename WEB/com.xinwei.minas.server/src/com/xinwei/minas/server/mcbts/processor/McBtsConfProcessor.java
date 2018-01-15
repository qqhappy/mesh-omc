/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.model.message.McBtsEmsTimeResponse;
import com.xinwei.minas.server.mcbts.model.message.McBtsIpNotification;
import com.xinwei.minas.server.mcbts.model.message.McBtsIpRequest;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.task.CalibFileUploadTaskManager;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 配置消息处理任务
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsConfProcessor extends McBtsMessageProcessor {

	private static final Log log = LogFactory.getLog(McBtsConfProcessor.class);

	@Override
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		case McBtsMessageConstants.MC_CALIB_FILE_UPLOADED_NOTIFY:
			// 校准结果上传到FTP完成通知
			CalibFileUploadTaskManager manager = AppContext.getCtx().getBean(
					CalibFileUploadTaskManager.class);
			manager.handleCalibFileNotify(message);
			break;
		case McBtsMessageConstants.MOC_BTS_IP_REQ_NOTIFY:
			// 基站IP请求通知
			this.processBtsIpReqNofity(message);
			break;
		case McBtsMessageConstants.MOC_EMS_TIME_REQ:
			// 基站向EMS查询EMS时间
			this.processTimeRequest(message);
			break;
		}
	}

	/**
	 * 处理基站IP请求通知
	 * 
	 * @param message
	 */
	private void processBtsIpReqNofity(McBtsMessage message) {
		long btsId = message.getBtsId();
		byte[] buf = message.getContent();
		McBtsIpNotification btsIpNofitication = new McBtsIpNotification(btsId,
				buf);
		long targetBtsId = btsIpNofitication.getTargetBtsId();
		String targetBtsPublicIp = "0.0.0.0";
		int targetBtsPort = 0;
		McBts targetBts = McBtsCache.getInstance().queryByBtsId(targetBtsId);
		if (targetBts == null || StringUtils.isEmpty(targetBts.getPublicIp())) {
			// 如果目标基站不存在，则返回默认值
			targetBtsPublicIp = "0.0.0.0";
			targetBtsPort = 0;
		}
		else {
			targetBtsPublicIp = targetBts.getPublicIp();
			targetBtsPort = targetBts.getPublicPort();
		}
		// 构造基站IP请求
		McBtsIpRequest req = new McBtsIpRequest();
		req.setBtsId(btsId);
		req.setTargetBtsId(targetBtsId);
		req.setTargetBtsPublicIp(targetBtsPublicIp);
		req.setTargetBtsPublicPort(targetBtsPort);
		// 下发基站IP请求
		McBtsConnector connector = AppContext.getCtx().getBean(
				McBtsConnector.class);
		try {
			connector.asyncInvoke(req.toMcBtsMessage());
		} catch (Exception e) {
			log.error(
					"failed to send bts ip req to bts: 0x"
							+ Long.toHexString(btsId), e);
		}

	}
	
	
	/**
	 * 处理基站请求EMS时间
	 * 
	 * @param message
	 */
	private void processTimeRequest(McBtsMessage message) {
		long btsId = message.getBtsId();

		// 构造应答
		McBtsEmsTimeResponse resp = new McBtsEmsTimeResponse();
		resp.setBtsId(btsId);
		// 下发应答
		McBtsConnector connector = AppContext.getCtx().getBean(
				McBtsConnector.class);
		try {
			connector.asyncInvoke(resp.toMcBtsMessage());
		} catch (Exception e) {
			log.error(
					"failed to send ems time to bts: 0x"
							+ Long.toHexString(btsId), e);
		}

	}
}
