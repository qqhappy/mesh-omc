/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.analyze.DataAnalyzer;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * ԭʼ���ݹ�����
 * 
 * @author fanhaoyu
 * 
 */

public class OriginalDataManager {

	private Log log = LogFactory.getLog(OriginalDataManager.class);

	private static OriginalDataManager instance = new OriginalDataManager();

	/**
	 * ��ͳ��ͼ��ͳ�Ƽ��
	 */
	private long dailyStatInterval;

	/**
	 * ͳ�����ݻ���, key-btsId, value-ָ���豸��ԭʼͳ����ֵ����Map
	 */
	private Map<Long, Map<Long, List<StatData>>> statDataCache = new HashMap<Long, Map<Long, List<StatData>>>();

	/**
	 * ���ݷ�����
	 */
	private DataAnalyzer dataAnalyzer;

	/**
	 * ���ݴ����̳߳�
	 */
	private ThreadPoolExecutor dataProcessThreadPool = null;

	private OriginalDataManager() {
		int threadNum = SystemContext.getInstance().getMaxThreadNum();
		dataProcessThreadPool = new ThreadPoolExecutor(1, threadNum, 15,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static OriginalDataManager getInstance() {
		return instance;
	}

	/**
	 * ��ʼ��ԭʼ���ݹ�����
	 * 
	 * @param analysisAgent
	 *            ����ϵͳAgent
	 * @param dataAnalyzer
	 *            ���ݷ�����
	 * @param dailyStatInterval
	 *            ������ͳ�Ƽ��,��λ:��
	 * @throws Exception
	 */
	public void initialize(DataAnalyzer dataAnalyzer, long dailyStatInterval)
			throws Exception {
		this.dataAnalyzer = dataAnalyzer;
		this.dailyStatInterval = dailyStatInterval;
		if (SystemContext.getInstance().saveRealTimeData()) {
			// ϵͳ������,���ȶ�OriginalDataManager���г�ʼ��,�����ݿ���װ��1Сʱ��ʵʱ���ݵ�����
			loadData2Cache();
		}
	}

	/**
	 * װ��һСʱ���ݵ�����
	 * 
	 * @throws Exception
	 */
	private void loadData2Cache() throws Exception {
		long currentTimeSeq = calcTimeSeqBy(System.currentTimeMillis());

		long endTime = calcStartTimeBy(currentTimeSeq);
		long startTime = endTime - 1000 * 60 * 60;

		List<StatData> statDatas = SystemContext.getInstance().getStatDataDAO()
				.getData(startTime, endTime, StatUtil.COLLECT_TYPE_REALTIME);

		if (statDatas == null || statDatas.isEmpty())
			return;

		for (StatData statData : statDatas) {
			long btsId = statData.getBtsId();
			if (!statDataCache.containsKey(btsId)) {
				Map<Long, List<StatData>> map = new HashMap<Long, List<StatData>>();
				statDataCache.put(btsId, map);
			}
			// ���㵱ǰ��������ʱ��Ƭ
			long timeSeq = calcTimeSeqBy(statData.getCollectTime());
			Map<Long, List<StatData>> oneTimeSeqDataMap = statDataCache
					.get(btsId);
			if (!oneTimeSeqDataMap.containsKey(timeSeq)) {
				oneTimeSeqDataMap.put(timeSeq, new ArrayList<StatData>());
			}
			List<StatData> itemDataList = oneTimeSeqDataMap.get(timeSeq);
			// ���Ӳɼ�����
			itemDataList.add(statData);
		}
	}

	/**
	 * �������ʵʱ�ϱ�
	 * 
	 * @param itemData
	 */
	private void notifyRealTimeData(SingleStatItemData itemData) {
		long btsId = itemData.getBtsId();
		int itemId = itemData.getItemId();
		// �����ͳ�������ڱ�����
		if (MonitorManager.getInstance().isMonitoring(
				new MonitorItem(btsId, itemId))) {
			// �������ϱ�����ϵͳ
			try {
				ClientNotifier.getInstance().sendData(itemData);
			} catch (Exception e) {
				log.error(
						"OriginalDataManager send SingleStatItemData to client error.",
						e);
			}
		}
	}

	/**
	 * ����ԭʼ�ɼ�����
	 * 
	 * @param statData
	 *            ԭʼ�ɼ�����
	 */
	public void add(StatData statData) {
		// �������ʵʱ�ϱ�
		for (SingleStatItemData itemData : statData.getItemDatas()) {
			notifyRealTimeData(itemData);
		}
		// �����ݼ��뻺��
		addData2Cache(statData);

	}

	private void addData2Cache(StatData statData) {
		long collectTime = statData.getCollectTime();
		long btsId = statData.getBtsId();
		if (!statDataCache.containsKey(btsId)) {
			Map<Long, List<StatData>> map = new HashMap<Long, List<StatData>>(
					32);
			statDataCache.put(btsId, map);
		}
		Map<Long, List<StatData>> oneTimeSeqDataMap = statDataCache.get(btsId);
		// ����ͳ��ʱ�����ʱ��Ƭ
		long timeSeq = calcTimeSeqBy(collectTime);
		if (!oneTimeSeqDataMap.containsKey(timeSeq)) {
			List<StatData> dataList = new ArrayList<StatData>(16);
			oneTimeSeqDataMap.put(timeSeq, dataList);
		}
		List<StatData> dataList = oneTimeSeqDataMap.get(timeSeq);
		// ���Ӳɼ�����
		dataList.add(statData);

		// �������鲢����,��鲢�ϸ�ʱ��Ƭ�ڵ�ԭʼ����
		if (shouldMergeData(dataList)) {
			long lastTimeSeq = timeSeq - 1;
			List<StatData> lastDataList = oneTimeSeqDataMap.get(lastTimeSeq);
			if (lastDataList == null) {
				return;
			}
			// ���ݴ������鲢���ݴ�⡢���ݹ鲢
			dataProcessThreadPool.execute(new DataProcessTask(lastTimeSeq,
					lastDataList));

			// ɾ������һ��Сʱ������
			Iterator<Long> keyItr = oneTimeSeqDataMap.keySet().iterator();
			List<Long> removeList = new ArrayList<Long>();

			while (keyItr.hasNext()) {
				long _timeSeq = keyItr.next();
				if (!isTimeSeqInOneHour(_timeSeq, timeSeq)) {
					removeList.add(_timeSeq);
				}
			}
			log.debug("Remove overtime RealTimeData in cache. number="
					+ removeList.size());
			for (int i = 0; i < removeList.size(); i++) {
				long key = removeList.get(i);
				oneTimeSeqDataMap.remove(key);
			}
			removeList = null;
		}
	}

	/**
	 * ���ݵ�ǰʱ��Ƭ��ͳ�����ݸ������ж��Ƿ�ù鲢�ϸ�ʱ��Ƭ������
	 * 
	 * @param currentDataList
	 *            ��ǰʱ��Ƭ�ڵ������б�
	 * @return �Ƿ�ù鲢�ϸ�ʱ��Ƭ������
	 */
	private boolean shouldMergeData(List<StatData> currentDataList) {
		return (currentDataList != null && currentDataList.size() == 2);
	}

	/**
	 * �ж�����ʱ��Ƭ�����Ƿ���һ��Сʱ��Χ��
	 * 
	 * @param timeSeq1
	 *            ʱ��Ƭ����
	 * @param timeSeq2
	 *            ʱ��Ƭ����
	 * @return true-����ʱ��Ƭ������һ��Сʱ��Χ��, false-����ʱ��Ƭ���в���һ��Сʱ��Χ��
	 */
	public boolean isTimeSeqInOneHour(long timeSeq1, long timeSeq2) {
		long timeSeqDiff = Math.abs(timeSeq1 - timeSeq2);
		if (timeSeqDiff <= 60 * 60 / dailyStatInterval) {
			return true;
		}
		return false;
	}

	/**
	 * ����ָ����վ��ͳ�������һСʱ��ԭʼ����
	 * 
	 * @return ָ����վ��ͳ�������һСʱ��ԭʼ�����б�
	 */
	public List<SingleStatItemData> getDataOfRecentOneHour(long btsId,
			int itemId) {
		Map<Long, List<StatData>> map = statDataCache.get(btsId);
		List<SingleStatItemData> itemDataList = new ArrayList<SingleStatItemData>();
		if (map == null) {
			return itemDataList;
		}
		long currentTime = System.currentTimeMillis();
		long currentTimeSeq = calcTimeSeqBy(currentTime);
		Iterator<Long> keyItr = map.keySet().iterator();
		while (keyItr.hasNext()) {
			long timeSeq = keyItr.next();
			if (isTimeSeqInOneHour(timeSeq, currentTimeSeq)) {
				for (StatData statData : map.get(timeSeq)) {
					SingleStatItemData itemData = statData.getItemData(itemId);
					if (itemData != null) {
						itemDataList.add(itemData);
					}
				}
			}
		}
		return itemDataList;
	}

	/**
	 * ����ʱ�����ʱ��Ƭ����
	 * 
	 * @param time
	 *            ʱ��
	 * @return ʱ��Ƭ����
	 */
	public long calcTimeSeqBy(long time) {
		return time / 1000 / dailyStatInterval;
	}

	/**
	 * ����ʱ��Ƭ���м�����ʼʱ��
	 * 
	 * @param timeSeq
	 *            ʱ��Ƭ����
	 * @return ��ʼʱ��
	 */
	public long calcStartTimeBy(long timeSeq) {
		return timeSeq * 1000 * dailyStatInterval;
	}

	/**
	 * ���ݴ�������
	 * 
	 * @author chenjunhua
	 * 
	 */
	class DataProcessTask implements Runnable {

		private long timeSeq;

		private List<StatData> dataList;

		public DataProcessTask(long timeSeq, List<StatData> dataList) {
			this.timeSeq = timeSeq;
			this.dataList = dataList;
		}

		public void run() {
			// �������Ϊ����ʵʱ����
			if (SystemContext.getInstance().saveRealTimeData()) {
				saveStatDataToDB();
			}
			mergeData();
		}

		private void saveStatDataToDB() {
			try {
				long startTime = System.currentTimeMillis();
				SystemContext.getInstance().getStatDataDAO().saveData(dataList);
				long finishTime = System.currentTimeMillis();
				long costTime = finishTime - startTime;
				log.debug("Save realTimeData to DB. startTime=" + startTime
						+ " , finishTime=" + finishTime + ", costTime="
						+ costTime + "ms");
			} catch (Exception e) {
				log.error("Save realTimeData to DB error.", e);
			}
		}

		/*
		 * ���ݹ鲢
		 */
		private void mergeData() {
			// ���ݹ鲢
			long startTime = calcStartTimeBy(timeSeq);
			List<StatData> mergedDataList = dataAnalyzer.analyze(dataList,
					StatUtil.COLLECT_TYPE_DAILY, dailyStatInterval, startTime);
			// ���鲢����������ӵ������ݹ�������
			for (int i = 0; i < mergedDataList.size(); i++) {
				StatData data = mergedDataList.get(i);
				DailyDataManager.getInstance().add(data);
			}
		}
	}

}
