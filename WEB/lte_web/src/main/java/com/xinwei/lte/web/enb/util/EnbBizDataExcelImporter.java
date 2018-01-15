/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.lte.web.enb.util.poi.ModelExcelImporter;
import com.xinwei.lte.web.enb.util.poi.RowData;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB数据配置导入器类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataExcelImporter extends
		ModelExcelImporter<Map<String, XBizTable>> {

	public static final String[] COLUMN_NAMES = { "表名", "数据" };

	public EnbBizDataExcelImporter(HSSFWorkbook workbook, String targetSheetName)
			throws Exception {
		super(workbook, targetSheetName);
	}

	@Override
	public List<Map<String, XBizTable>> getModelList(List<RowData> rowDataList) {
		if (rowDataList == null || rowDataList.isEmpty())
			return Collections.emptyList();
		List<Map<String, XBizTable>> list = new LinkedList<Map<String, XBizTable>>();
		Map<String, XBizTable> dataMap = new LinkedHashMap<String, XBizTable>();
		list.add(dataMap);
		String enbId = null;
		for (RowData rowData : rowDataList) {
			for (String columnName : columnList) {
				if (columnName.equals("eNB ID")) {
					enbId = rowData.getColumnValue(columnName);
					if (!dataMap.containsKey(enbId)) {
						dataMap.put(enbId, new XBizTable(0l, targetSheetName));
					}
				}

			}
		}
		for (RowData rowData : rowDataList) {
			XBizRecord xBizRecord = new XBizRecord();
			enbId=rowData.getColumnValue("eNB ID");
			for (String columnName : columnList) {
				if (!columnName.equals("eNB ID")) {
					String recordValue = rowData.getColumnValue(columnName);
					XBizField xBizField = new XBizField();
					xBizField.setName(columnName);
					xBizField.setValue(recordValue);
					xBizRecord.addField(xBizField);

				}
			}
			if(!xBizRecord.getFieldMap().isEmpty()){
				dataMap.get(enbId).addRecord(xBizRecord);
			}
		}
		return list;
	}

	/*
	 * @Override public List<XBizTable> getModelList(List<RowData> rowDataList)
	 * { if (rowDataList == null || rowDataList.isEmpty()) return
	 * Collections.emptyList(); Map<String, List<XBizRecord>> dataMap = new
	 * HashMap<String, List<XBizRecord>>(); for (RowData rowData : rowDataList)
	 * { // 遍历行，将每一行转换成XBizRecord String tableName =
	 * rowData.getColumnValue(COLUMN_NAMES[0]); String recordValue =
	 * rowData.getColumnValue(COLUMN_NAMES[1]); XBizRecord record =
	 * XJsonUtils.jsonObjStr2Object(recordValue, XBizRecord.class); //
	 * 将XBizRecord加入到tableName的记录列表中 List<XBizRecord> records =
	 * dataMap.get(tableName); if (records == null) { records = new
	 * LinkedList<XBizRecord>(); dataMap.put(tableName, records); }
	 * records.add(record); } // 将记录列表转化成XBizTable List<XBizTable> tableList =
	 * new ArrayList<XBizTable>(); for (String tableName : dataMap.keySet()) {
	 * List<XBizRecord> records = dataMap.get(tableName); XBizTable bizTable =
	 * new XBizTable(0l, tableName); bizTable.setRecords(records);
	 * 
	 * tableList.add(bizTable); } return tableList; }
	 */
	@Override
	protected void checkRowData(RowData rowData) throws Exception {
		for(String columnName:columnList){
			String columnValue=rowData.getColumnValue(columnName);
			 if (columnValue.equals("")) {
			 throw new Exception(columnName + "的值不能为空");
			 }
		}
		// String tableName = rowData.getColumnValue(COLUMN_NAMES[0]);
		// String recordValue = rowData.getColumnValue(COLUMN_NAMES[1]);
		// if (tableName.equals("")) {
		// throw new Exception(COLUMN_NAMES[0] + "的值不能为空");
		// }
		// if (recordValue.equals("")) {
		// throw new Exception(COLUMN_NAMES[1] + "的值不能为空");
		// }
	}

}
