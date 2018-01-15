/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * ���ݷ�����
 * 
 * @author fanhaoyu
 * 
 */

public class DataAnalyzer {

	/**
	 * ���ݷ������÷���
	 * 
	 * @param statDataList
	 *            --ԭʼ�����б�
	 * @param item
	 *            --ͳ����Ŀ
	 * @param type
	 *            --Ŀ������ͳ�����ͣ��罫������ͳ��Ϊ������
	 * @param analyzeInterval
	 *            --Ŀ�����ݷ����������λΪ��
	 * @return ������������б�
	 */
	public List<StatData> analyze(List<StatData> statDataList, int type,
			long analyzeInterval, long startTime) {
		/**
		 * 1�������������ݰ��ղɼ�ʱ���������� 2�������������ݰ���Ŀ��ɼ�ʱ��������
		 * 3�������������ݣ�����Ŀ�����ݣ�Ŀ�������еĲɼ�ʱ��ΪԴ�������ǰ�����ݲɼ�ʱ��
		 */
		List<StatData> result = new ArrayList<StatData>();
		List<StatData> orderList = this.orderByCollectedTime(statDataList);// �����������ݰ�����������
		List<List<StatData>> timeSeqGroup = this.groupByCollectedInterval(
				orderList, analyzeInterval, startTime);// ���������ݰ���Ŀ��ɼ�ʱ��������
		for (List<StatData> oneSeqList : timeSeqGroup) {
			// �������Ϊ�գ�������
			if (oneSeqList == null || oneSeqList.size() == 0)
				continue;
			StatData data = this.analyzeOneStatData(oneSeqList, type,
					analyzeInterval, startTime);// �Է��������ݽ���ͳ��
			result.add(data);
		}
		return result;
	}

	/**
	 * ���ղɼ�ʱ�����򣬰�����������
	 * 
	 * @param list
	 * @return
	 */
	private List<StatData> orderByCollectedTime(List<StatData> statData) {
		CompareStatData compare = new CompareStatData();
		if (statData != null && statData.size() > 0) {
			Collections.sort(statData, compare);// ����compare�еĹ�������
		}
		return statData;
	}

	/**
	 * ����ʱ��������Դ���ݰ���Ŀ�����ʱ��������
	 * 
	 * @param list
	 * @param analyzeInterval
	 * @return
	 */
	private List<List<StatData>> groupByCollectedInterval(
			List<StatData> statDataList, long analyzeInterval, long startTime) {
		List<List<StatData>> timeSeqList = new ArrayList<List<StatData>>();
		if (statDataList != null && statDataList.size() > 0) {
			List<StatData> oneTimeSeqList = new ArrayList<StatData>();
			// �ɼ����ݽ�ֹʱ��
			long collectEndTime = startTime + analyzeInterval * 1000;
			for (int i = 0; i < statDataList.size(); i++) {
				StatData statData = statDataList.get(i);
				// �����ǰ���ݵĲɼ�ʱ��С�ڸö��յ�ʱ��,�����temp
				// ��������ڵ�ǰʱ����ڣ������temp
				StatData tempData = statData.copy();

				if (tempData.getCollectTime() < collectEndTime
						&& tempData.getCollectTime() >= startTime) {
					tempData.setCollectTime(startTime);// �������ݲɼ�ʱ��Ϊ��ʼʱ��
					oneTimeSeqList.add(tempData);
				} else if (tempData.getCollectTime() >= collectEndTime) {
					List<StatData> groupList = new ArrayList<StatData>();// ���������б�
					groupList.addAll(oneTimeSeqList);// ����ʱ�洢�����е�������ӵ������б���
					timeSeqList.add(groupList);// �������б���ӵ������б�
					oneTimeSeqList.clear();// �����ʱ�б�
					startTime = collectEndTime;
					collectEndTime = collectEndTime + analyzeInterval * 1000;// ���²ɼ��νK�c�r�g����һ��ֹʱ��+�ɼ�ʱ����
					tempData.setCollectTime(startTime);// ���òɼ�ʱ��Ϊ��ʼʱ��
					oneTimeSeqList.add(tempData);// ��������ʱ��������ݼ�����ʱ�б�
				} else {
					// do nothing,discard this data
				}
			}
			// �����ʱ�б����������ݣ�����ʱ�б���ӵ������б���
			if (oneTimeSeqList != null && oneTimeSeqList.size() > 0) {
				timeSeqList.add(oneTimeSeqList);
			}
		}
		return timeSeqList;
	}

	/**
	 * ��һ�������еĲɼ����ݰ���ָ���ɼ��������Ϊһ��ֵ
	 * 
	 * @param statDataList
	 * @param itemId
	 *            �ɼ���
	 * @param timeType
	 *            �ɼ����ͣ���Ҫ�鲢�ɵ�����ֵ
	 * @param analyzeInterval
	 * @return
	 */
	private StatData analyzeOneStatData(List<StatData> statDataList,
			int timeType, long analyzeInterval, long starttime) {

		StatData resultData = new StatData();

		StatData oneData = statDataList.get(0);
		long btsId = oneData.getBtsId();// �ɼ���վ���
		resultData.setBtsId(btsId);
		resultData.setInterval(analyzeInterval);
		resultData.setTimeType(timeType);
		resultData.setCollectTime(starttime);

		Set<Integer> itemSet = oneData.getItemMap().keySet();

		for (Integer itemId : itemSet) {

			double value = 0.0;// ����ֵ
			double average;// �ɼ�ƽ��ֵ
			for (int i = 0; i < statDataList.size(); i++) {
				StatData statData = statDataList.get(i);
				SingleStatItemData itemData = statData.getItemData(itemId);
				if (itemData != null) {
					value += statData.getItemData(itemId).getValue();
				}
			}
			average = value / statDataList.size();

			resultData.addItemData(itemId,
					Double.valueOf(String.format("%.2f", average)));
		}
		return resultData;
	}

	/**
	 * ʵ��Comparator�ӿڵ��ڲ��࣬����Ƚ�SingleStatItemData
	 * 
	 * @author fanhaoyu
	 * 
	 */
	private class CompareStatData implements Comparator<StatData> {

		public int compare(StatData data1, StatData data2) {
			long flag = data1.getCollectTime() - data2.getCollectTime();
			if (flag == 0) {
				return 0;
			} else if (flag > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

}
