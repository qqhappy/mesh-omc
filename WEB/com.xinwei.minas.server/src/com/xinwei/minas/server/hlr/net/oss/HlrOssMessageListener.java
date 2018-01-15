/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.oss;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpNotifyListener;

/**
 * 
 * Hlr OssÏûÏ¢ÕìÌýÆ÷
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssMessageListener implements HlrUdpNotifyListener {
	
	private static final Log log = LogFactory.getLog(HlrOssMessageListener.class);

	@Override
	public void receive(HlrUdpMessage message) throws Exception {
		try {
			process(message);
		} catch (Exception e) {
			log.error("HlrOssMessageListener process message error", e);
		}
	}
	
	
	private void process(HlrUdpMessage message) {
		byte[] ossContent = message.getContent();
		HlrOssBizMessage ossMessage = new HlrOssBizMessage(ossContent, 0);		
		HlrOssRegistry.getInstance().addResponse(ossMessage);
	}

}
