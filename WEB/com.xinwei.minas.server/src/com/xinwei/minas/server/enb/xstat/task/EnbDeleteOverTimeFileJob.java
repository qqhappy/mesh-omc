/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-31	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.task;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xinwei.minas.server.xstat.service.StatFileParser;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 删除过期文件Job
 * 
 * @author fanhaoyu
 * 
 */

public class EnbDeleteOverTimeFileJob extends QuartzJobBean {

	private Log log = LogFactory.getLog(EnbDeleteOverTimeFileJob.class);

	private String dirPath;

	private int overTime;

	private StatFileParser statFileParser;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		log.debug("delete overtime xstat file.");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 0 - overTime);
		// 获取要删除文件的最早时间
		long limitTime = calendar.getTimeInMillis();
		File root = new File(dirPath);
		File[] files = root.listFiles();

		int count = 0;

		for (File file : files) {
			String fileName = file.getName();
			// 从文件名中获取数据上报时间
			long dataTime = statFileParser.getDataTime(fileName);
			// 从yyyyMMDDhhmmss转换为毫秒形式
			dataTime = DateUtils.getMillisecondTimeFromBriefTime(dataTime);
			if (dataTime > limitTime)
				continue;
			// 删除过期的文件
			try {
				if (!file.delete()) {
					log.error("delete xstat file failed. filePath="
							+ file.getAbsolutePath());
				}
				count++;
			} catch (Exception e) {
				log.error("delete xstat file failed. filePath="
						+ file.getAbsolutePath());
			}
		}
		log.debug("delete overtime xstat file. fileCount=" + count);

	}

	/**
	 * 设置文件目录
	 * 
	 * @param dirPath
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * 设置文件的过期时间
	 * 
	 * @param overTime
	 */
	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}

	public void setStatFileParser(StatFileParser statFileParser) {
		this.statFileParser = statFileParser;
	}

}
