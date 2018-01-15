/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-21	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import com.xinwei.minas.server.enb.net.EnbAppMessage;


/**
 * 
 * 资产消息处理线程
 * 
 * @author chenlong
 * 
 */

public class EnbAssetTask implements Runnable {

	private boolean isShutdown = false;
	
	private long sleepTime = 2000;
	
	@Override
	public void run() {
		while (!isShutdown) {
			try {
				EnbAppMessage message = EnbAssetTaskManager.getInstance()
						.getMessage();
				if(null == message) {
					Thread.sleep(sleepTime);
				}
				EnbAssetTaskManager.getInstance().process(message);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
