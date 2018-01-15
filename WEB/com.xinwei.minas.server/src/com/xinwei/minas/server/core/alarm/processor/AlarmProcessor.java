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
 * �澯�¼�����
 * 
 * @author chenjunhua
 * 
 */

public class AlarmProcessor {

	private final Log log = LogFactory.getLog(AlarmProcessor.class);

	private AlarmDAO alarmDAO;

	// �߳�ִ�п��
	private ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();
	// �̳߳�
	private ExecutorService threadPool = Executors.newFixedThreadPool(1);

	// �Ƿ��Զ�ȷ��
	private boolean confirmAutomatically = false;

	// ϵͳȷ���û�
	private String systemConfirmUser = "system";

	// ϵͳ�ָ��û�
	private String systemRestoreUser = "system";

	// ���彻����
	private Exchanger<AlarmBuffer> exchanger = new Exchanger<AlarmBuffer>();

	// �澯��¼����
	private AlarmBuffer alarmSaveBuffer = new AlarmBuffer();

	// �澯������
	private AlarmBuffer alarmProcessBuffer = new AlarmBuffer();

	// ������������ʶ
	private boolean bufferShouldExchange = false;

	// �澯����
	private BlockingQueue<Alarm> alarmQueue = new LinkedBlockingQueue<Alarm>();

	// �ڲ��澯�����߳�ִ�м��
	private int alarmProcessInterval = 30;

	// ���ϸ澯��ֵ
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
	 * ����澯��
	 * 
	 * @param alarm
	 *            ������ĸ澯��
	 */
	public void process(AlarmEvent alarmEvent) {
		// ���澯�¼�ת��Ϊ�澯
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
	 * �澯��������
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
					// ����澯����
					alarmSaveBuffer.add(alarm);
				}
			} catch (Exception e) {
			}
			// ����������
			if (isBufferShouldExchange()) {
				try {
					alarmSaveBuffer = exchanger.exchange(alarmSaveBuffer);
				} finally {
					// �趨������������ʶΪfalse
					setBufferShouldExchange(false);
				}
			}
		}
	}

	/**
	 * ���澯�¼�ת��Ϊ�澯
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
	 * ����澯�ָ�
	 * 
	 * @param alarm
	 */
	private void processRestoredAlarm(Alarm alarm) throws Exception {
		Alarm alarmInDB = alarmDAO.queryCurrentBy(alarm.getMoId(),
				alarm.getEntityType(), alarm.getEntityOid(),
				alarm.getAlarmDefId());
		if (alarmInDB != null && alarmInDB.isAlarm()) {
			// ֻ�����ݿ��д��ڴ˸澯�Ž��д���
			alarmInDB.setAlarmState(AlarmConstants.RESTORED);
			alarmInDB.setRestoredTime(alarm.getRestoredTime());
			alarmInDB.setRestoreUser(systemRestoreUser);
			alarmInDB.setRestoreFlag(AlarmConstants.AUTORESTORE);
			if (isConfirmAutomatically() && !alarmInDB.isConfirmed()) {
				// ������Զ�ȷ������δ��ȷ��
				alarmInDB.setConfirmUser(systemConfirmUser);
				alarmInDB.setConfirmTime(alarm.getRestoredTime());
				alarmInDB.setConfirmState(AlarmConstants.CONFIRMED);
			}
			// ���¸澯��ǰ��
			alarmDAO.update(alarmInDB);
			// ����澯��ȷ�ϣ���ת�Ƶ���ʷ����
			if (alarmInDB.isConfirmed()) {
				alarmDAO.move2History(alarmInDB);
			}
		}
	}

	/**
	 * �����¸澯
	 * 
	 * @param alarm
	 */
	private void processNewAlarm(Alarm alarm) throws Exception {
		Alarm alarmInDB = alarmDAO.queryCurrentBy(alarm.getMoId(),
				alarm.getEntityType(), alarm.getEntityOid(),
				alarm.getAlarmDefId());
		if (alarmInDB == null) {
			// ��ǰ�澯���޴˸澯
			alarmDAO.add(alarm);
		} else {
			// ��ǰ�澯δ�ָ�
			if (alarmInDB.isAlarm()) {
				// �������һ�θ澯ʱ��͸澯����
				alarmInDB.setLastAlarmTime(alarm.getLastAlarmTime());
				alarmInDB.increaseAlarmTimes();
				alarmDAO.update(alarmInDB);
			}
			// ��ǰ�澯�ѻָ�
			else {
				// �Ƚ���ǰ�澯�Զ�ȷ�ϣ���ת�Ƶ���ʷ��
				alarmInDB.setConfirmUser(systemConfirmUser);
				long confirmTime = DateUtils
						.getBriefTimeFromMillisecondTime(System
								.currentTimeMillis());
				alarmInDB.setConfirmTime(confirmTime);
				alarmInDB.setConfirmState(AlarmConstants.CONFIRMED);
				alarmDAO.update(alarmInDB);
				alarmDAO.move2History(alarmInDB);
				// ����һ���澯
				alarmDAO.add(alarm);
			}
		}

	}

	/**
	 * �ڲ��澯�����߳�
	 * 
	 * @author chenjunhua
	 * 
	 */
	class InnerAlarmProcessor implements Runnable {

		// ʵ�ʴ���ĸ澯��Ŀ
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
		 * ִ������
		 */
		private void doWork() throws Exception {
			// �趨������������ʶΪtrue
			setBufferShouldExchange(true);
			// ��������
			alarmProcessBuffer = exchanger.exchange(alarmProcessBuffer);
			// ��ʼ������
			long beginTime = System.currentTimeMillis();
			int alarmNum = 0;
			processNum = 0;
			// �����澯����
			Map<String, List<Alarm>> cache = alarmProcessBuffer.getCache();
			Iterator<String> keyItr = cache.keySet().iterator();
			while (keyItr.hasNext()) {
				String key = keyItr.next();
				List<Alarm> alarmList = cache.get(key);
				if (alarmList == null || alarmList.isEmpty()) {
					continue;
				}
				alarmNum += alarmList.size();
				// ����澯
				process(alarmList);
				// ��Ҫ˯��һ�£�����CPU��
				Thread.sleep(10);
			}
			// ��ջ���
			alarmProcessBuffer.clear();
			// ����澯�����ʱ����������
			long endTime = System.currentTimeMillis();
			long useTime = (endTime - beginTime) / 1000;
			log.debug("--AlarmProcessor-- alarmNum=" + alarmNum
					+ ", processNum=" + processNum + ", time=" + useTime
					+ "(sec).");
		}

		/**
		 * ����澯�б�
		 * 
		 * @param alarmList
		 */
		private void process(List<Alarm> alarmList) throws Exception {
			List<Alarm> finalList = new LinkedList();
			// �ж��Ƿ������ϸ澯 ARAR
			// �澯״̬��ת����
			int turnNum = alarmList.size() - 1;
			if (isFlashAlarm(turnNum)) {
				// ��������ϸ澯���ϲ��澯Ϊһ���������ܸ澯�����͸澯��ĩʱ��
				int alarmTimes = calculateAlarmTimes(alarmList);
				Alarm firstAlarm = alarmList.get(0);
				Alarm lastAlarm = alarmList.get(alarmList.size() - 1);
				firstAlarm.setAlarmState(AlarmConstants.ALARM);
				firstAlarm.setAlarmTimes(alarmTimes);
				firstAlarm.setLastAlarmTime(lastAlarm.getLastAlarmTime());
				finalList.add(firstAlarm);
				// ������һ�����ϸ澯�Ǹ澯�ָ�������Ҫ����һ���ָ��澯
				if (lastAlarm.isRestored()) {
					finalList.add(lastAlarm);
				}
			} else {
				finalList = alarmList;
			}
			// ����鲢��ĸ澯�б�
			for (Alarm alarm : finalList) {
				// ���˲�������Ԫ�ĸ澯
				long moId = alarm.getMoId();
				Mo mo = MoCache.getInstance().queryByMoId(moId);
				if (mo == null) {
					log.debug("--AlarmProcessor-- Mo is not existing. moId="
							+ moId);
					continue;
				}

				processNum++;
				if (alarm.isRestored()) {
					// ����Ǹ澯�ָ�
					processRestoredAlarm(alarm);
				} else {
					// ������¸澯
					processNewAlarm(alarm);
				}
			}
		}

		/**
		 * ����澯����
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
		 * �ж��Ƿ������ϸ澯
		 * 
		 * @param turnNum
		 *            �澯״̬��ת����
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
