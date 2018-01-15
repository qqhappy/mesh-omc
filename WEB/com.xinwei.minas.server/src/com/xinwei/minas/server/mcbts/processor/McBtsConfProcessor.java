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
 * ������Ϣ��������
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
			// У׼����ϴ���FTP���֪ͨ
			CalibFileUploadTaskManager manager = AppContext.getCtx().getBean(
					CalibFileUploadTaskManager.class);
			manager.handleCalibFileNotify(message);
			break;
		case McBtsMessageConstants.MOC_BTS_IP_REQ_NOTIFY:
			// ��վIP����֪ͨ
			this.processBtsIpReqNofity(message);
			break;
		case McBtsMessageConstants.MOC_EMS_TIME_REQ:
			// ��վ��EMS��ѯEMSʱ��
			this.processTimeRequest(message);
			break;
		}
	}

	/**
	 * �����վIP����֪ͨ
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
			// ���Ŀ���վ�����ڣ��򷵻�Ĭ��ֵ
			targetBtsPublicIp = "0.0.0.0";
			targetBtsPort = 0;
		}
		else {
			targetBtsPublicIp = targetBts.getPublicIp();
			targetBtsPort = targetBts.getPublicPort();
		}
		// �����վIP����
		McBtsIpRequest req = new McBtsIpRequest();
		req.setBtsId(btsId);
		req.setTargetBtsId(targetBtsId);
		req.setTargetBtsPublicIp(targetBtsPublicIp);
		req.setTargetBtsPublicPort(targetBtsPort);
		// �·���վIP����
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
	 * �����վ����EMSʱ��
	 * 
	 * @param message
	 */
	private void processTimeRequest(McBtsMessage message) {
		long btsId = message.getBtsId();

		// ����Ӧ��
		McBtsEmsTimeResponse resp = new McBtsEmsTimeResponse();
		resp.setBtsId(btsId);
		// �·�Ӧ��
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
