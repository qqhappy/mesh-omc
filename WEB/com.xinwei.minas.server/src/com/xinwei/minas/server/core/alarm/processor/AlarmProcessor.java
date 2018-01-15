/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-27	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.processor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmConstants;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.core.alarm.model.AlarmBuffer;
import com.xinwei.minas.server.core.alarm.model.AlarmEvent;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 告警事件服务
 * 
 * @author chenjunhua
 * 
 */

public class AlarmProcessor {

	private final Log log = LogFactory.getLog(AlarmProcessor.class);

	private AlarmDAO alarmDAO;

	// 线程执行框架
	private ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();
	// 线程池
	private ExecutorService threadPool = Executors.newFixedThreadPool(1);

	// 是否自动确认
	private boolean confirmAutomatically = false;

	// 系统确认用户
	private String systemConfirmUser = "system";

	// 系统恢复用户
	private String systemRestoreUser = "system";

	// 缓冲交换器
	private Exchanger<AlarmBuffer> exchanger = new Exchanger<AlarmBuffer>();

	// 告警记录缓存
	private AlarmBuffer alarmSaveBuffer = new AlarmBuffer();

	// 告警处理缓存
	private AlarmBuffer alarmProcessBuffer = new AlarmBuffer();

	// 交换缓冲区标识
	private boolean bufferShouldExchange = false;

	// 告警队列
	private BlockingQueue<Alarm> alarmQueue = new LinkedBlockingQueue<Alarm>();

	// 内部告警处理线程执行间隔
	private int alarmProcessInterval = 30;

	// 闪断告警阈值
	private int flashThreshold = 4;

	public void initialize() {
		executorService.scheduleAtFixedRate(new InnerAlarmProcessor(),
				alarmProcessInterval, alarmProcessInterval, TimeUnit.SECONDS);
		threadPool.submit(new AlarmSaveProcessor());
	}

	public synchronized boolean isBufferShouldExchange() {
		return bufferShouldExchange;
	}

	public synchronized void setBufferShouldExchange(
			boolean bufferShouldExchange) {
		this.bufferShouldExchange = bufferShouldExchange;
	}

	/**
	 * 处理告警。
	 * 
	 * @param alarm
	 *            待处理的告警。
	 */
	public void process(AlarmEvent alarmEvent) {
		// 将告警事件转换为告警
		Alarm alarm = eventToAlarm(alarmEvent);
		try {
			alarmQueue.offer(alarm, 10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
	}

	public AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	public void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	public boolean isConfirmAutomatically() {
		return confirmAutomatically;
	}

	public void setConfirmAutomatically(boolean confirmAutomatically) {
		this.confirmAutomatically = confirmAutomatically;
	}

	/**
	 * 告警缓存任务
	 * 
	 * @author chenjunhua
	 * 
	 */
	private class AlarmSaveProcessor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					doWork();
				} catch (InterruptedException e) {
					break;
				} catch (Exception e1) {
				}
			}
		}

		private void doWork() throws Exception {
			try {
				Alarm alarm = alarmQueue.poll(10, TimeUnit.MILLISECONDS);
				if (alarm != null) {
					// 加入告警缓存
					alarmSaveBuffer.add(alarm);
				}
			} catch (Exception e) {
			}
			// 交换缓冲区
			if (isBufferShouldExchange()) {
				try {
					alarmSaveBuffer = exchanger.exchange(alarmSaveBuffer);
				} finally {
					// 设定交换缓冲区标识为false
					setBufferShouldExchange(false);
				}
			}
		}
	}

	/**
	 * 将告警事件转换为告警
	 * 
	 * @param alarmEvent
	 * @return
	 */
	private Alarm eventToAlarm(AlarmEvent alarmEvent) {
		Alarm alarm = new Alarm();
		long moId = alarmEvent.getMoId();
		Mo mo = MoCache.getInstance().queryByMoId(moId);
		if (mo != null) {
			String moName = mo.getName();
			alarm.setMoName(moName);
		}
		alarm.setMoId(alarmEvent.getMoId());
		alarm.setEntityOid(alarmEvent.getEntityOid());
		alarm.setEntityType(alarmEvent.getEntityType());
		alarm.setAlarmDefId(alarmEvent.getAlarmDefId());
		alarm.setAlarmContent(alarmEvent.getAlarmContent());
		alarm.setAlarmLevel(alarmEvent.getAlarmLevel());
		alarm.setAlarmState(alarmEvent.getAlarmState());
		alarm.setFirstAlarmTime(alarmEvent.getEventTime());
		alarm.setLastAlarmTime(alarmEvent.getEventTime());
		if (alarmEvent.isRestored()) {
			alarm.setRestoredTime(alarmEvent.getEventTime());
		} else {
			alarm.setAlarmTimes(1);
		}
		return alarm;
	}

	/**
	 * 处理告警恢复
	 * 
	 * @param alarm
	 */
	private void processRestoredAlarm(Alarm alarm) throws Exception {
		Alarm alarmInDB = alarmDAO.queryCurrentBy(alarm.getMoId(),
				alarm.getEntityType(), alarm.getEntityOid(),
				alarm.getAlarmDefId());
		if (alarmInDB != null && alarmInDB.isAlarm()) {
			// 只有数据库中存在此告警才进行处理
			alarmInDB.setAlarmState(AlarmConstants.RESTORED);
			alarmInDB.setRestoredTime(alarm.getRestoredTime());
			alarmInDB.setRestoreUser(systemRestoreUser);
			alarmInDB.setRestoreFlag(AlarmConstants.AUTORESTORE);
			if (isConfirmAutomatically() && !alarmInDB.isConfirmed()) {
				// 如果是自动确认且尚未被确认
				alarmInDB.setConfirmUser(systemConfirmUser);
				alarmInDB.setConfirmTime(alarm.getRestoredTime());
				alarmInDB.setConfirmState(AlarmConstants.CONFIRMED);
			}
			// 更新告警当前表
			alarmDAO.update(alarmInDB);
			// 如果告警已确认，则转移到历史表中
			if (alarmInDB.isConfirmed()) {
				alarmDAO.move2History(alarmInDB);
			}
		}
	}

	/**
	 * 处理新告警
	 * 
	 * @param alarm
	 */
	private void processNewAlarm(Alarm alarm) throws Exception {
		Alarm alarmInDB = alarmDAO.queryCurrentBy(alarm.getMoId(),
				alarm.getEntityType(), alarm.getEntityOid(),
				alarm.getAlarmDefId());
		if (alarmInDB == null) {
			// 当前告警库无此告警
			alarmDAO.add(alarm);
		} else {
			// 当前告警未恢复
			if (alarmInDB.isAlarm()) {
				// 更新最后一次告警时间和告警次数
				alarmInDB.setLastAlarmTime(alarm.getLastAlarmTime());
				alarmInDB.increaseAlarmTimes();
				alarmDAO.update(alarmInDB);
			}
			// 当前告警已恢复
			else {
				// 先将当前告警自动确认，并转移到历史库
				alarmInDB.setConfirmUser(systemConfirmUser);
				long confirmTime = DateUtils
						.getBriefTimeFromMillisecondTime(System
								.currentTimeMillis());
				alarmInDB.setConfirmTime(confirmTime);
				alarmInDB.setConfirmState(AlarmConstants.CONFIRMED);
				alarmDAO.update(alarmInDB);
				alarmDAO.move2History(alarmInDB);
				// 新增一条告警
				alarmDAO.add(alarm);
			}
		}

	}

	/**
	 * 内部告警处理线程
	 * 
	 * @author chenjunhua
	 * 
	 */
	class InnerAlarmProcessor implements Runnable {

		// 实际处理的告警数目
		private int processNum = 0;

		@Override
		public void run() {
			try {
				doWork();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("failed to process alarm", e);
			} finally {
			}
		}

		/**
		 * 执行任务
		 */
		private void doWork() throws Exception {
			// 设定交换缓冲区标识为true
			setBufferShouldExchange(true);
			// 交换缓存
			alarmProcessBuffer = exchanger.exchange(alarmProcessBuffer);
			// 初始化参数
			long beginTime = System.currentTimeMillis();
			int alarmNum = 0;
			processNum = 0;
			// 遍历告警缓存
			Map<String, List<Alarm>> cache = alarmProcessBuffer.getCache();
			Iterator<String> keyItr = cache.keySet().iterator();
			while (keyItr.hasNext()) {
				String key = keyItr.next();
				List<Alarm> alarmList = cache.get(key);
				if (alarmList == null || alarmList.isEmpty()) {
					continue;
				}
				alarmNum += alarmList.size();
				// 处理告警
				process(alarmList);
				// 需要睡眠一下，避免CPU高
				Thread.sleep(10);
			}
			// 清空缓存
			alarmProcessBuffer.clear();
			// 计算告警处理耗时及处理数量
			long endTime = System.currentTimeMillis();
			long useTime = (endTime - beginTime) / 1000;
			log.debug("--AlarmProcessor-- alarmNum=" + alarmNum
					+ ", processNum=" + processNum + ", time=" + useTime
					+ "(sec).");
		}

		/**
		 * 处理告警列表
		 * 
		 * @param alarmList
		 */
		private void process(List<Alarm> alarmList) throws Exception {
			List<Alarm> finalList = new LinkedList();
			// 判断是否是闪断告警 ARAR
			// 告警状态翻转次数
			int turnNum = alarmList.size() - 1;
			if (isFlashAlarm(turnNum)) {
				// 如果是闪断告警，合并告警为一条，更新总告警次数和告警首末时间
				int alarmTimes = calculateAlarmTimes(alarmList);
				Alarm firstAlarm = alarmList.get(0);
				Alarm lastAlarm = alarmList.get(alarmList.size() - 1);
				firstAlarm.setAlarmState(AlarmConstants.ALARM);
				firstAlarm.setAlarmTimes(alarmTimes);
				firstAlarm.setLastAlarmTime(lastAlarm.getLastAlarmTime());
				finalList.add(firstAlarm);
				// 如果最后一条闪断告警是告警恢复，则需要增加一条恢复告警
				if (lastAlarm.isRestored()) {
					finalList.add(lastAlarm);
				}
			} else {
				finalList = alarmList;
			}
			// 处理归并后的告警列表
			for (Alarm alarm : finalList) {
				// 过滤不存在网元的告警
				long moId = alarm.getMoId();
				Mo mo = MoCache.getInstance().queryByMoId(moId);
				if (mo == null) {
					log.debug("--AlarmProcessor-- Mo is not existing. moId="
							+ moId);
					continue;
				}

				processNum++;
				if (alarm.isRestored()) {
					// 如果是告警恢复
					processRestoredAlarm(alarm);
				} else {
					// 如果是新告警
					processNewAlarm(alarm);
				}
			}
		}

		/**
		 * 计算告警次数
		 * 
		 * @param alarmList
		 * @return
		 */
		private int calculateAlarmTimes(List<Alarm> alarmList) {
			int num = 0;
			try {
				for (Alarm alarm : alarmList) {
					num += alarm.getAlarmTimes();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}

		/**
		 * 判断是否是闪断告警
		 * 
		 * @param turnNum
		 *            告警状态翻转次数
		 * @return
		 */
		private boolean isFlashAlarm(int turnNum) {
			return turnNum >= flashThreshold;
		}

	}

	public void setAlarmProcessInterval(int alarmProcessInterval) {
		this.alarmProcessInterval = alarmProcessInterval;
	}

	public void setFlashThreshold(int flashThreshold) {
		this.flashThreshold = flashThreshold;
	}

}
