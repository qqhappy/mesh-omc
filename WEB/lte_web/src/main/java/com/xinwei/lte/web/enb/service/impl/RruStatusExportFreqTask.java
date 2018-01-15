/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-12-31	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service.impl;


/**
 * 
 * RRU定时动态信息导出
 * 
 * 
 * @author chenlong
 * 
 */

public class RruStatusExportFreqTask implements Runnable {
    
	// 开关
	private int exportSwitch;
	
	// 查询频率 单位为min
	private int freq;
	
	public RruStatusExportFreqTask(int exportSwitch, int freq) {
		this.exportSwitch = exportSwitch;
		this.freq = freq;
	}

	@Override
	public void run() {
		if(0 == exportSwitch) {
			if(freq < 1) {
				freq = 1;
			}
			long sleepTime = 1000 * 60 * freq;
			while(true) {
				// 查询并导出RRU数据
				try {
					RruStatusExportFreqManager.getInstance().export();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					try {
						Thread.sleep(sleepTime);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			
		}
	}
}
