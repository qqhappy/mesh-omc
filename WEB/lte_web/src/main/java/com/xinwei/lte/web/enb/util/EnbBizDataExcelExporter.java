/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-1	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.lte.web.enb.util.poi.ModelExcelExporter;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB业务数据excel导入助手实现类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataExcelExporter extends ModelExcelExporter<XBizTable> {

	public static final String[] COLUMN_NAMES = { "表名", "数据" };

	public EnbBizDataExcelExporter(HSSFWorkbook workBook,
			String targetSheetName, boolean append) {
		super(workBook, targetSheetName, append);
	}

	@Override
	public List<List<String>> getRowDataList(List modelList) {
		List<List<String>> rowDataList = new LinkedList<List<String>>();
		Map map=(Map) modelList.get(0);
		Iterator itertor=map.entrySet().iterator();
		while(itertor.hasNext()){
			Map.Entry<Enb,XBizTable> entry=(Entry<Enb, XBizTable>) itertor.next();
			String enbId=entry.getKey().getHexEnbId();
			XBizTable xBizTable=entry.getValue();
				List<XBizRecord> recordList=xBizTable.getRecords();
				for(XBizRecord xBizRecord:recordList){
					List<String> rowData = new LinkedList<String>();
					Map<String, XBizField> fieldMap=xBizRecord.getFieldMap();
					Iterator ite=fieldMap.entrySet().iterator();
					rowData.add(enbId);
					for(String columnName:columnList){
						if(!columnName.equals("eNB ID")){
							XBizField xBizField=fieldMap.get(columnName);
							if(xBizField!=null){
								String value=fieldMap.get(columnName).getValue();
								rowData.add(value);
							}else{
								rowData.add(null);
							}
							
						}
					}
//					while(ite.hasNext()){
//						Map.Entry<String, XBizField> me=(Entry<String, XBizField>) ite.next();
//						rowData.add(me.getValue().getValue());
//						
//					}
					rowDataList.add(rowData);
				}
				
			
		}
		return rowDataList;
	}

//	@Override
//	public List<List<String>> getRowDataList(List<XBizTable> tableList) {
//		List<List<String>> rowDataList = new LinkedList<List<String>>();
//
//		// 遍历业务表
//		for (XBizTable bizTable : tableList) {
//			List<XBizRecord> recordList = bizTable.getRecords();
//			if (recordList == null || recordList.isEmpty())
//				continue;
//			// 遍历业务表记录
//			for (XBizRecord record : recordList) {
//				List<String> rowData = new LinkedList<String>();
//
//				rowData.add(bizTable.getTableName());
//
//				JSONObject json = new JSONObject();
//				json.put("fieldMap", record.getFieldMap());
//				String recordJsonStr = json.toString();
//				rowData.add(recordJsonStr);
//
//				rowDataList.add(rowData);
//			}
//		}
//
//		return rowDataList;
//	}
}
