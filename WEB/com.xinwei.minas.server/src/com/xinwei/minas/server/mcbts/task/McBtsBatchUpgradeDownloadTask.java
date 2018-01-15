/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;

/**
 * 
 * 基站批量升级 下载基站软件的任务
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeDownloadTask implements Runnable {

	private Log log = LogFactory.getLog(McBtsBatchUpgradeDownloadTask.class);

	private McBtsBatchUpgradeTaskManager mcBtsBatchUpgradeTaskManager;

	private static final int SLEEP_NO_TASK = 30;

	private static final int SLEEP_REACH_MAX_DOWNLOADS = 5;

	public McBtsBatchUpgradeDownloadTask(
			McBtsBatchUpgradeTaskManager mcBtsBatchUpgradeTaskManager) {

		this.mcBtsBatchUpgradeTaskManager = mcBtsBatchUpgradeTaskManager;

	}

	private volatile boolean running = true;

	@Override
	public void run() {
		while (running) {
			doWork();
		}
	}

	private void doWork() {
		// 处理超时任务
		try {
			handleTimeoutTask();

			if (mcBtsBatchUpgradeTaskManager.reachMaxDownload()) {
				TimeUnit.SECONDS.sleep(SLEEP_REACH_MAX_DOWNLOADS);
				return;
			} else {
				// 处理一个下载的任务
				try {
					startDownload();
				} catch (NullPointerException e) {
					// 如果没有任务,就延迟30秒
					TimeUnit.SECONDS.sleep(SLEEP_NO_TASK);
				}
			}
		} catch (Exception e) {
			log.error("Error during batch upgrade.", e);
		}
	}

	/**
	 * 开始一个下载
	 */
	private void startDownload() throws Exception {
		mcBtsBatchUpgradeTaskManager.configDownload();
	}

	/**
	 * 处理超时的任务
	 */
	private synchronized void handleTimeoutTask() {
		List<UpgradeInfo> timeoutList = mcBtsBatchUpgradeTaskManager
				.hasTimeoutUpgradeInfo();

		if (timeoutList == null || timeoutList.isEmpty())
			return;

		for (UpgradeInfo info : timeoutList) {
			mcBtsBatchUpgradeTaskManager.markTimeout(info);
			mcBtsBatchUpgradeTaskManager.downloadCountDecrease();

		}
	}

	private void shutdown() {
		running = false;
	}

}
