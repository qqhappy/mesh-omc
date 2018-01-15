/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-11	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhuxiaozhan
 *
 */
public class NeighborFailedProcessTimerTask {
	private Log log = LogFactory.getLog(NeighborFailedProcessTimerTask.class);
	
	private NeighborService neighborService;
	
	public void execute() {
		Date date = new Date();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		log.info("The timer task processed the neighbor configured excute time is: " + dataFormat.format(date));
		neighborService.processNbConfigFailedBts();
	}

	public void setNeighborService(NeighborService neighborService) {
		this.neighborService = neighborService;
	}
}
