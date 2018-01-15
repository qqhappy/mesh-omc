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
 * GPS��Ϣ���������
 * 
 * @author tiance
 * 
 */

public class McBtsGPSDataTaskManager {
	Log log = LogFactory.getLog(McBtsGPSDataTaskManager.class);

	// ��վID��Ϊkey��GPS��Ϣ
	private Map<Long, GPSData> gpsMap = new ConcurrentHashMap<Long, GPSData>();

	private static McBtsGPSDataTaskManager instance = null;

	private SequenceService sequenceService;
	private GPSData2Dao gpsData2Dao;

	private McBtsGPSDataTaskManager(int period) {
		// �����߳�,ÿ��period�����gps�����ݿ�
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
		// ��������,������γ��,����,����. Ȼ�����map��
		int offset = 0;
		int len = 4;
		Long latitude = ByteUtils.toLong(body, offset, len);
		offset += 4;
		Long longitude = ByteUtils.toLong(body, offset, len);
		offset += 4;
		Long altitude = ByteUtils.toLong(body, offset, len);

		// ����GPSData
		GPSData data = new GPSData();
		// ��ȡһ��ID,����һ���ᱻ�õ�.
		data.setIdx(sequenceService.getNext());
		// ����moId��ȡBtsId
		data.setMoId(McBtsCache.getInstance().queryByBtsId(message.getBtsId())
				.getMoId());
		data.setLatitude(latitude);
		data.setLongitude(longitude);
		data.setHeight(altitude);
		data.setGmtOffset(0L);
		// Ĭ��3��
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
					// ��ȡmap�е����м����task
					taskList = new CopyOnWriteArrayList<GPSData>(
							gpsMap.values());
				}

				// �ѻ�ȡ��tasks�����ݿ�
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
