/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.processor;

import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.ut.task.UTForceUpgradeTaskManager;

/**
 * 
 * ÖÕ¶Ë×¢²á´¦ÀíÆ÷
 * 
 * 
 * @author tiance
 * 
 */

public class UTRegisterProcessor extends HlrMessageProcessor {

	@Override
	protected void doWork(HlrUdpMessage message) {
		UTForceUpgradeTaskManager.getInstance().handleUTRegisterNotify(message);
	}

}
