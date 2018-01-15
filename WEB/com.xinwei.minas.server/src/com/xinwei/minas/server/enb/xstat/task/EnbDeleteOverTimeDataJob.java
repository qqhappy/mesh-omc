/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-30	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xinwei.minas.server.enb.xstat.service.EnbStatBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 删除过期统计数据Job
 * 
 * @author fanhaoyu
 * 
 */

public class EnbDeleteOverTimeDataJob extends QuartzJobBean {

	private Log log = LogFactory.getLog(EnbDeleteOverTimeDataJob.class);

	private long overTime;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		long currentTime = System.currentTimeMillis();
		long startTime = DateUtils.getBriefTimeFromMillisecondTime(currentTime
				- 2 * overTime * 1000);
		long endTime = DateUtils.getBriefTimeFromMillisecondTime(currentTime
				- overTime * 1000);
		log.debug("delete overtime xstat data. startTime=" + startTime
				+ ", endTime=" + endTime);
		try {
			EnbStatBizService service = AppContext.getCtx().getBean(
					EnbStatBizService.class);
			service.deleteStatData(startTime, endTime);
		} catch (Exception e) {
			log.error("delete xstat data failed. startTime=" + startTime
					+ ", endTime=" + endTime, e);
		}

	}

	public long getOverTime() {
		return overTime;
	}

	/**
	 * 设置数据的过期时间
	 * 
	 * @param overTime
	 */
	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

}
