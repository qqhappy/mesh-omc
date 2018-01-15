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
 * 数据分析器
 * 
 * @author fanhaoyu
 * 
 */

public class DataAnalyzer {

	/**
	 * 数据分析公用方法
	 * 
	 * @param statDataList
	 *            --原始数据列表
	 * @param item
	 *            --统计项目
	 * @param type
	 *            --目标数据统计类型，如将日数据统计为年数据
	 * @param analyzeInterval
	 *            --目标数据分析间隔，单位为秒
	 * @return 分析后的数据列表
	 */
	public List<StatData> analyze(List<StatData> statDataList, int type,
			long analyzeInterval, long startTime) {
		/**
		 * 1：将待分析数据按照采集时间升序排序 2：将排序后的数据按照目标采集时间间隔分组
		 * 3：分析各组数据，生成目标数据，目标数据中的采集时间为源数据中最靠前的数据采集时间
		 */
		List<StatData> result = new ArrayList<StatData>();
		List<StatData> orderList = this.orderByCollectedTime(statDataList);// 将待分析数据按照升序排序
		List<List<StatData>> timeSeqGroup = this.groupByCollectedInterval(
				orderList, analyzeInterval, startTime);// 排序后的数据按照目标采集时间间隔分组
		for (List<StatData> oneSeqList : timeSeqGroup) {
			// 如果数据为空，则跳过
			if (oneSeqList == null || oneSeqList.size() == 0)
				continue;
			StatData data = this.analyzeOneStatData(oneSeqList, type,
					analyzeInterval, startTime);// 对分组后的数据进行统计
			result.add(data);
		}
		return result;
	}

	/**
	 * 按照采集时间排序，按照升序排列
	 * 
	 * @param list
	 * @return
	 */
	private List<StatData> orderByCollectedTime(List<StatData> statData) {
		CompareStatData compare = new CompareStatData();
		if (statData != null && statData.size() > 0) {
			Collections.sort(statData, compare);// 按照compare中的规则排序
		}
		return statData;
	}

	/**
	 * 将按时间排序后的源数据按照目标分析时间间隔分组
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
			// 采集数据截止时间
			long collectEndTime = startTime + analyzeInterval * 1000;
			for (int i = 0; i < statDataList.size(); i++) {
				StatData statData = statDataList.get(i);
				// 如果当前数据的采集时间小于该段终点时间,则放入temp
				// 如果数据在当前时间段内，则放入temp
				StatData tempData = statData.copy();

				if (tempData.getCollectTime() < collectEndTime
						&& tempData.getCollectTime() >= startTime) {
					tempData.setCollectTime(startTime);// 设置数据采集时间为起始时间
					oneTimeSeqList.add(tempData);
				} else if (tempData.getCollectTime() >= collectEndTime) {
					List<StatData> groupList = new ArrayList<StatData>();// 创建分组列表
					groupList.addAll(oneTimeSeqList);// 将临时存储队列中的数据添加到分组列表中
					timeSeqList.add(groupList);// 将分组列表添加到返回列表
					oneTimeSeqList.clear();// 清空临时列表
					startTime = collectEndTime;
					collectEndTime = collectEndTime + analyzeInterval * 1000;// 更新采集段終點時間，上一截止时间+采集时间间隔
					tempData.setCollectTime(startTime);// 设置采集时间为起始时间
					oneTimeSeqList.add(tempData);// 將数据新时间段内数据加入临时列表
				} else {
					// do nothing,discard this data
				}
			}
			// 如果临时列表中仍有数据，则将临时列表添加到返回列表中
			if (oneTimeSeqList != null && oneTimeSeqList.size() > 0) {
				timeSeqList.add(oneTimeSeqList);
			}
		}
		return timeSeqList;
	}

	/**
	 * 将一个队列中的采集数据按照指定采集间隔计算为一个值
	 * 
	 * @param statDataList
	 * @param itemId
	 *            采集项
	 * @param timeType
	 *            采集类型，将要归并成的类型值
	 * @param analyzeInterval
	 * @return
	 */
	private StatData analyzeOneStatData(List<StatData> statDataList,
			int timeType, long analyzeInterval, long starttime) {

		StatData resultData = new StatData();

		StatData oneData = statDataList.get(0);
		long btsId = oneData.getBtsId();// 采集基站编号
		resultData.setBtsId(btsId);
		resultData.setInterval(analyzeInterval);
		resultData.setTimeType(timeType);
		resultData.setCollectTime(starttime);

		Set<Integer> itemSet = oneData.getItemMap().keySet();

		for (Integer itemId : itemSet) {

			double value = 0.0;// 计算值
			double average;// 采集平均值
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
	 * 实现Comparator接口的内部类，负责比较SingleStatItemData
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
