/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.server.mcbts.dao.common.GPSData2Dao;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * GPS消息任务管理器
 * 
 * @author tiance
 * 
 */

public class McBtsGPSDataTaskManager {
	Log log = LogFactory.getLog(McBtsGPSDataTaskManager.class);

	// 基站ID作为key的GPS消息
	private Map<Long, GPSData> gpsMap = new ConcurrentHashMap<Long, GPSData>();

	private static McBtsGPSDataTaskManager instance = null;

	private SequenceService sequenceService;
	private GPSData2Dao gpsData2Dao;

	private McBtsGPSDataTaskManager(int period) {
		// 启动线程,每隔period秒更新gps到数据库
		final ScheduledExecutorService scheduledExecutor = Executors
				.newScheduledThreadPool(1);

		Thread gpsSync = new Thread(new GPSDataUpdater());
		gpsSync.setName("gpsSync");
		scheduledExecutor.scheduleAtFixedRate(gpsSync, 0, period,
				TimeUnit.SECONDS);

		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public static McBtsGPSDataTaskManager getInstance(int period) {
		if (instance == null) {
			return new McBtsGPSDataTaskManager(period);
		}

		return instance;
	}

	public void handleGPSDataNotify(McBtsMessage message) {
		byte[] body = message.getContent();
		// 解析内容,分析出纬度,经度,海拔. 然后存入map中
		int offset = 0;
		int len = 4;
		Long latitude = ByteUtils.toLong(body, offset, len);
		offset += 4;
		Long longitude = ByteUtils.toLong(body, offset, len);
		offset += 4;
		Long altitude = ByteUtils.toLong(body, offset, len);

		// 构造GPSData
		GPSData data = new GPSData();
		// 获取一个ID,但不一定会被用到.
		data.setIdx(sequenceService.getNext());
		// 基于moId获取BtsId
		data.setMoId(McBtsCache.getInstance().queryByBtsId(message.getBtsId())
				.getMoId());
		data.setLatitude(latitude);
		data.setLongitude(longitude);
		data.setHeight(altitude);
		data.setGmtOffset(0L);
		// 默认3个
		data.setMinimumTrackingsatellite(3);

		synchronized (gpsMap) {
			gpsMap.put(message.getBtsId(), data);
		}
	}

	private class GPSDataUpdater implements Runnable {
		@Override
		public void run() {
			try {
				List<GPSData> taskList = null;

				// log.debug("Starting to save gps data...");

				synchronized (gpsMap) {
					// 获取map中的所有加入的task
					taskList = new CopyOnWriteArrayList<GPSData>(
							gpsMap.values());
				}

				// 把获取的tasks存数据库
				if (taskList != null && taskList.size() != 0)
					persistData(taskList);
			} catch (Exception e) {
				log.error("Error while saving gps data!", e);
			}
		}

		private void persistData(List<GPSData> tasks) {
			gpsData2Dao = OmpAppContext.getCtx().getBean(GPSData2Dao.class);
			
			gpsData2Dao.batchSaveOrUpdate(tasks);
		}
	}

}
