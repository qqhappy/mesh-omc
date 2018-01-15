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
 * eNB告警消息处理任务
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarmProcessor extends EnbMessageProcessor {

	public EnbAlarmProcessor() {
		
	}

	/**
	 * 执行任务
	 */
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// 告警通知
		case EnbMessageConstants.MOC_ALARM_NTFY:
			this.processAlarmNotify(message);
			break;
		}
	}

	/**
	 * 结束任务
	 * 
	 */
	@Override
	public void finishWork() {

	}

	/**
	 * 处理告警通知
	 * 
	 * @param message
	 */
	private void processAlarmNotify(EnbAppMessage message) {		
		EnbAlarm enbAlarm = new EnbAlarm(message);
		EnbAlarmHelper enbAlarmHelper = getEnbAlarmHelper();
		if (enbAlarmHelper != null) {
			// 处理告警请求
			enbAlarmHelper.processEnbAlarm(enbAlarm);
			// 发送告警应答
			sendAlarmResponse(message);
		}
	}
	
	/**
	 * 发送告警应答
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
