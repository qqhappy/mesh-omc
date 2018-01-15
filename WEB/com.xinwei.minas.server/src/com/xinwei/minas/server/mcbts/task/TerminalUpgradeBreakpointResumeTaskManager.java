/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * �ն������ϵ��������������
 * 
 * 
 * @author tiance
 * 
 */

public class TerminalUpgradeBreakpointResumeTaskManager {
	Log log = LogFactory
			.getLog(TerminalUpgradeBreakpointResumeTaskManager.class);

	private TerminalUpgradeBreakpointResumeTaskManager() {
	}

	private static TerminalUpgradeBreakpointResumeTaskManager instance = new TerminalUpgradeBreakpointResumeTaskManager();

	public static TerminalUpgradeBreakpointResumeTaskManager getInstance() {
		return instance;
	}

	public void handleUTUpgradeBreakpoint(McBtsMessage message) {
		byte[] content = message.getContent();
		int offset = 0;

		// ������Ϣ
		long eid = ByteUtils.toUnsignedNumber(content, offset, 4);
		offset += 4;

		int transId = ByteUtils.toInt(content, offset, 2);
		offset += 2;

		int utType = ByteUtils.toInt(content, offset, 1);
		offset += 1;

		int fileNameLength = ByteUtils.toInt(content, offset, 1);
		offset += 1;

		String fileName = ByteUtils.toString(content, offset, fileNameLength,
				McBtsConstants.CHARSET_US_ASCII);
		offset += fileNameLength;

		int seq = ByteUtils.toInt(content, offset, 2);
		offset += 2;

		int progress = ByteUtils.toInt(content, offset, 1);
		offset += 1;

		long newBtsId = ByteUtils.toInt(content, offset, 4);

		McBts mcBts = McBtsCache.getInstance().queryByBtsId(newBtsId);

		if (mcBts == null) {
			// �µĻ�վ��û����EMS��ע��.
			log.error("New McBts is not registered on EMS");
			return;
		}

		// ���öϵ�����������Ϣ
		GenericBizData data = new GenericBizData(
				"ut_breakpoint_resume_upgrade_config");
		data.addProperty(new GenericBizProperty("eid", eid));
		data.addProperty(new GenericBizProperty("transId", transId));
		data.addProperty(new GenericBizProperty("fileName", fileName));
		data.addProperty(new GenericBizProperty("seq", seq));
		data.addProperty(new GenericBizProperty("progress", progress));
		data.addProperty(new GenericBizProperty("utType", utType));

		McBtsBizService mcBtsBizService = AppContext.getCtx().getBean(
				McBtsBizService.class);

		try {
			mcBtsBizService.sendCommand(mcBts.getMoId(), data);
		} catch (Exception e) {
			log.error("Error config ut breakpoint resume upgrade.", e);
		}

	}
}
