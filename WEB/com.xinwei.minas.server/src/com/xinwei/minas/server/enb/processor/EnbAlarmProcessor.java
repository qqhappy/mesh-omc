/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-05-19	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.model.message.EnbAlarm;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.mcbts.helper.McBtsAlarmHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsAlarm;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB�澯��Ϣ��������
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarmProcessor extends EnbMessageProcessor {

	public EnbAlarmProcessor() {
		
	}

	/**
	 * ִ������
	 */
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// �澯֪ͨ
		case EnbMessageConstants.MOC_ALARM_NTFY:
			this.processAlarmNotify(message);
			break;
		}
	}

	/**
	 * ��������
	 * 
	 */
	@Override
	public void finishWork() {

	}

	/**
	 * ����澯֪ͨ
	 * 
	 * @param message
	 */
	private void processAlarmNotify(EnbAppMessage message) {		
		EnbAlarm enbAlarm = new EnbAlarm(message);
		EnbAlarmHelper enbAlarmHelper = getEnbAlarmHelper();
		if (enbAlarmHelper != null) {
			// ����澯����
			enbAlarmHelper.processEnbAlarm(enbAlarm);
			// ���͸澯Ӧ��
			sendAlarmResponse(message);
		}
	}
	
	/**
	 * ���͸澯Ӧ��
	 * @param message
	 */
	private void sendAlarmResponse(EnbAppMessage req) {
		EnbAppMessage resp = new EnbAppMessage();
		resp.setEnbId(req.getEnbId());
		resp.setTransactionId(req.getTransactionId());
		resp.setMa(req.getMa());
		resp.setMoc(req.getMoc());
		resp.setActionType(req.getActionType());
		resp.setMessageType(EnbMessageConstants.MESSAGE_RESPONSE);
		resp.addTagValue(TagConst.RESULT, 0);
		EnbConnector enbConnector = getEnbConnector();
		if (enbConnector != null) {
			try {
				enbConnector.asyncInvoke(resp);
			} catch (Exception e) {
			}
		}
	}


	private EnbAlarmHelper getEnbAlarmHelper() {
		return OmpAppContext.getCtx().getBean(EnbAlarmHelper.class);
	}
	
	private EnbConnector getEnbConnector() {
		return OmpAppContext.getCtx().getBean(EnbConnector.class);
	}
}
