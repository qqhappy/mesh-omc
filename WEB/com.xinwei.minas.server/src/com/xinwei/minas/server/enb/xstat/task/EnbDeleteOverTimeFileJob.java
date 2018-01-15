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
 * ɾ�������ļ�Job
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
		// ��ȡҪɾ���ļ�������ʱ��
		long limitTime = calendar.getTimeInMillis();
		File root = new File(dirPath);
		File[] files = root.listFiles();

		int count = 0;

		for (File file : files) {
			String fileName = file.getName();
			// ���ļ����л�ȡ�����ϱ�ʱ��
			long dataTime = statFileParser.getDataTime(fileName);
			// ��yyyyMMDDhhmmssת��Ϊ������ʽ
			dataTime = DateUtils.getMillisecondTimeFromBriefTime(dataTime);
			if (dataTime > limitTime)
				continue;
			// ɾ�����ڵ��ļ�
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
	 * �����ļ�Ŀ¼
	 * 
	 * @param dirPath
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * �����ļ��Ĺ���ʱ��
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
