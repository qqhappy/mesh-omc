/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ��վע��Ӧ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterResponse {
	
	public static final int BTS_INIT = 0;
	
	public static final int BTS_RUNNING = 1;
	
	public static final int BOOT_FROM_BTS = 0;
	
	public static final int BOOT_FROM_EMS = 1;
	
	// ��վID
	private Long btsId;
	
	// ����״̬
	private int runningState;

	// ����Դ
	private int bootupSource = -1;

	public McBtsRegisterResponse(Long btsId, byte[] buf) {
		// ��վID
		this.btsId = btsId;
		int offset = 0;
		// ����״̬
		runningState = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		if (buf.length >= 4) {
			// ����Դ
			bootupSource = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
		}
		//
	}
	
	
	

	public Long getBtsId() {
		return btsId;
	}

	public boolean isBtsInit() {
		return runningState == BTS_INIT;
	}

	public boolean isBtsRunning() {
		return runningState == BTS_RUNNING;
	}

	public boolean isBootupFromBts() {
		return bootupSource == BOOT_FROM_BTS;
	}
	
	public boolean isBootupFromEms() {
		return bootupSource == BOOT_FROM_EMS;
	}


	public boolean isBootupUndefined() {
		return bootupSource == -1;
	}

}
