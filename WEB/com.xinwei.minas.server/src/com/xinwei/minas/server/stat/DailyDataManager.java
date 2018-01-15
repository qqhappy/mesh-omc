/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * �����ݹ�����
 * 
 * @author fanhaoyu
 * 
 */

public class DailyDataManager {

	private Log log = LogFactory.getLog(DailyDataManager.class);

	private static DailyDataManager instance = new DailyDataManager();

	/**
	 * btsId - List<StatData>
	 */
	private Map<Long, List<StatData>> dailyDataCache = new HashMap<Long, List<StatData>>();

	private StatDataDAO statDataDAO;

	private DailyDataManager() {
	}

	public static DailyDataManager getInstance() {
		return instance;
	}

	/**
	 * ��ʼ�������ݹ�����
	 * 
	 * @param statDataDAO
	 *            �ɼ�����DAO
	 * @throws Exception
	 */
	public void initialize(StatDataDAO statDataDAO) throws Exception {
		this.statDataDAO = statDataDAO;
		// ϵͳ������,���ȶ�DayDataManager���г�ʼ��,�����ݿ���װ��1������ݵ�����
		long endtime = System.currentTimeMillis();
		long begintime = endtime - 24 * 60 * 60 * 1000;

		int type = StatUtil.COLLECT_TYPE_DAILY;
		List<StatData> statDatas = statDataDAO
				.getData(begintime, endtime, type);

		if (statDatas == null || statDatas.isEmpty())
			return;

		for (int j = 0; j < statDatas.size(); j++) {
			StatData statData = statDatas.get(j);
			this.addData2Cache(statData, true);
		}

	}

	/**
	 * ������������ϱ�
	 * 
	 * @param data
	 */
	private void notifyDaliyData(SingleStatItemData data) {
		long btsId = data.getBtsId();
		// ��������ݵ�ͳ�������ڱ�����
		if (MonitorManager.getInstance().isMonitoring(
				new MonitorItem(btsId, data.getItemId()))) {
			// �������ϱ�����ϵͳ
			try {
				ClientNotifier.getInstance().sendData(data);
			} catch (Exception e) {
				log.error("DailyDataManager send StatData to client error.", e);
			}
		}
	}

	/**
	 * ����������
	 * 
	 * @param statData
	 */
	public void add(StatData statData) {
		int timeType = statData.getTimeType();
		if (timeType != StatUtil.COLLECT_TYPE_DAILY) {// ���˷�������
			return;
		}
		// ������������ϱ�
		for (SingleStatItemData itemData : statData.getItemDatas()) {
			notifyDaliyData(itemData);
		}
		// �����ݼ��뻺��
		this.addData2Cache(statData, false);
		// �����ݳ־û�
		try {
			statDataDAO.saveData(statData);
		} catch (Exception e) {
			log.error("DailyDataManager save StatData to DB error.", e);
		}

	}

	/**
	 * �����ݼ��뻺��
	 * 
	 * @param statData
	 */
	private void addData2Cache(StatData statData, boolean isInit) {
		long btsId = statData.getBtsId();
		List<StatData> dataList = dailyDataCache.get(btsId);
		if (dataList == null) {
			dataList = new LinkedList<StatData>();
			dailyDataCache.put(btsId, dataList);
		}
		dataList.add(statData);

		// �ǳ�ʼ�����̣�ɾ�����ڵ�����
		if (!isInit) {
			this.removeOverTimeData(dataList);
		}

	}

	/**
	 * ɾ�������б��еĹ�������
	 * 
	 * @param dataList
	 *            �����б�
	 */
	private void removeOverTimeData(List<StatData> dataList) {
		// ɾ������һ�������
		int count = 0;
		for (int i = 0; i < dataList.size(); i++) {
			StatData data = dataList.get(i);
			if (dataOverOneDay(data.getCollectTime())) {
				count++;
				dataList.remove(i);
				i--;
			}
			data = null;
		}
		log.debug("Remove overtime DailyData in cache. number=" + count);
	}

	/**
	 * �жϲɼ������Ƿ񳬹�һ��
	 * 
	 * @param collectedData
	 *            �ɼ�����
	 * @return true if data is over one day, false if not
	 */
	private boolean dataOverOneDay(long collectTime) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - collectTime > 24 * 60 * 60 * 1000) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����ָ����վ��ͳ��������һ���ͳ������
	 * 
	 * @return ָ����վ��ͳ��������һ���ͳ�������б�
	 */
	public List<SingleStatItemData> getDataOfRecentOneDay(long btsId, int itemId) {
		List<SingleStatItemData> oneDayDataList = new ArrayList<SingleStatItemData>();
		List<StatData> dataList = dailyDataCache.get(btsId);
		if (dataList == null) {
			dataList = new LinkedList<StatData>();
			dailyDataCache.put(btsId, dataList);
		}
		for (int i = 0; i < dataList.size(); i++) {
			StatData statData = dataList.get(i);
			if (!dataOverOneDay(statData.getCollectTime())) {
				SingleStatItemData itemData = statData.getItemData(itemId);
				if (itemData != null) {
					oneDayDataList.add(itemData);
				}
			}
		}
		return oneDayDataList;
	}
}
