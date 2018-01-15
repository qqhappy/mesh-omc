/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;

/**
 * 
 * 数据下载任务
 * 
 * @author chenshaohua
 * 
 */

public class McBtsDownloadDataTaskManager {

	private static final Log log = LogFactory
			.getLog(McBtsDownloadDataTaskManager.class);

	private static final McBtsDownloadDataTaskManager instance = new McBtsDownloadDataTaskManager();

	// 加载任务Map
	private Map<Long, McBtsDownloadDataTask> taskMap = new ConcurrentHashMap<Long, McBtsDownloadDataTask>();

	private ThreadPoolExecutor threadPoolExecutor;

	private McBtsDownloadDataTaskManager() {
		threadPoolExecutor = new ThreadPoolExecutor(5, 20, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(512),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static McBtsDownloadDataTaskManager getInstance() {
		return instance;
	}

	/**
	 * 处理数据下载通知
	 * 
	 * @param message
	 */
	public void handleDownloadDataNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();		
		processDownload(btsId);
	}

	/**
	 * 执行基站数据下载
	 * 
	 * @param btsId
	 *            基站ID
	 */
	public void processDownload(Long btsId) {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			log.error("failed to processDownload. bts is not exist. btsId="
					+ btsId);
			return;
		}
		// 如果有正在执行的任务，则不再执行数据加载
		if (taskMap.containsKey(btsId)) {
			log.warn("bts is downloading, btsId=0x" + mcBts.getHexBtsId());
			return;
		}
		McBtsDownloadDataTask task = new McBtsDownloadDataTask(btsId, this);
		taskMap.put(btsId, task);
		threadPoolExecutor.execute(task);
	}

	public void removeTask(Long btsId) {
		taskMap.remove(btsId);
	}



}
