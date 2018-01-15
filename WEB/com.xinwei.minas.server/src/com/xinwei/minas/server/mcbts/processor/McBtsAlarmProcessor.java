/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import com.xinwei.minas.server.mcbts.helper.McBtsAlarmHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsAlarm;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ�澯��Ϣ��������
 * 
 * @author chenjunhua
 * 
 */

public class McBtsAlarmProcessor extends McBtsMessageProcessor {

	
	private McBtsAlarmHelper mcBtsAlarmHelper;

	public McBtsAlarmProcessor() {
		mcBtsAlarmHelper = OmpAppContext.getCtx().getBean(McBtsAlarmHelper.class);
	}

	/**
	 * ִ������
	 */
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// �澯֪ͨ
		case McBtsMessageConstants.MOC_ALARM_NTFY:
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
	private void processAlarmNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();
		McBtsAlarm mcBtsAlarm = new McBtsAlarm(btsId, message.getContent());
		if (mcBtsAlarmHelper != null) {
			mcBtsAlarmHelper.processMcBtsAlarm(mcBtsAlarm);
		}
	}


}
