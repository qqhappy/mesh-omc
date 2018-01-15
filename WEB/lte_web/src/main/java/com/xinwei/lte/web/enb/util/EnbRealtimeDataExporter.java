/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-9	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.lte.web.enb.util.poi.ModelExcelExporter;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 实时性能数据导出类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeDataExporter extends
		ModelExcelExporter<EnbRealtimeItemData> {

	public static final String[] COLUMN_NAMES = { "时间", "帧号" };

	private List<Long> timeList;

	public EnbRealtimeDataExporter(HSSFWorkbook workBook,
			String targetSheetName, boolean append) {
		super(workBook, targetSheetName, append);
	}

	@Override
	public List<List<String>> getRowDataList(List<EnbRealtimeItemData> modelList) {
		if (modelList == null || modelList.isEmpty())
			return Collections.emptyList();

		List<List<String>> rowDataList = new LinkedList<List<String>>();

		Map<Long, List<EnbRealtimeItemData>> timeMap = organizeData(modelList);

		for (Long time : timeList) {
			List<String> rowData = new ArrayList<String>();
			String timeStr = DateUtils.getStandardTimeFromBriefTime(time);
			rowData.add(timeStr);
			List<EnbRealtimeItemData> itemList = timeMap.get(time);
			Integer frameNo = itemList.get(0).getSystemFrameNo();
			rowData.add(frameNo.toString());

			for (EnbRealtimeItemData itemData : itemList) {
				String value = String.format("%.2f", itemData.getStatValue());
				rowData.add(value);
			}
			rowDataList.add(rowData);
		}
		return rowDataList;
	}

	private Map<Long, List<EnbRealtimeItemData>> organizeData(
			List<EnbRealtimeItemData> modelList) {
		timeList = new LinkedList<Long>();
		for (EnbRealtimeItemData itemData : modelList) {
			long time = itemData.getEndTime();
			if (timeList.contains(time))
				continue;
			timeList.add(time);
		}
		Collections.sort(timeList);
		Map<Long, List<EnbRealtimeItemData>> timeMap = new HashMap<Long, List<EnbRealtimeItemData>>();
		// 按时间分组
		for (EnbRealtimeItemData itemData : modelList) {
			long time = itemData.getEndTime();
			List<EnbRealtimeItemData> itemList = timeMap.get(time);
			if (itemList == null) {
				itemList = new LinkedList<EnbRealtimeItemData>();
				timeMap.put(time, itemList);
			}
			itemList.add(itemData);
		}
		for (Long time : timeList) {
			// 每组中的项按照统计项ID排序
			List<EnbRealtimeItemData> itemList = timeMap.get(time);
			Collections.sort(itemList, new DataComparator());
		}
		return timeMap;
	}

	class DataComparator implements Comparator<EnbRealtimeItemData> {

		@Override
		public int compare(EnbRealtimeItemData o1, EnbRealtimeItemData o2) {
			long temp = o1.getItemId() - o2.getItemId();
			if (temp > 0)
				return 1;
			else if (temp < 0)
				return -1;
			return 0;
		}
	}

}
