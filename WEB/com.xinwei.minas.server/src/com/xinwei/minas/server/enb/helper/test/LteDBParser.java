/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.utils.XJsonUtils;

/**
 * 
 * 从lte.db导出sql文件，解析其中的insert语句，生成mysql的可执行sql语句
 * 
 * @author fanhaoyu
 * 
 */

public class LteDBParser {
	
	public static List<String> createTemplateSql(String version, String pathname) {
		Map<String, XBizTable> tableMap = parseFile(pathname);
		
		List<String> sqlList = new ArrayList<String>();
		for (String tableName : tableMap.keySet()) {
			XBizTable bizTable = tableMap.get(tableName);
			List<XBizRecord> records = bizTable.getRecords();
			for (XBizRecord bizRecord : records) {
				
				XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
						EnbTypeDD.XW7400, version, tableName, bizRecord);
				String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
				String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
				String sql = ("insert into enb_biz_template values('" + 0 + "','" + version
						+ "','" + tableName + "','" + keyJsonStr + "','"
						+ recordJsonStr + "');");

				sqlList.add(sql);
			}
		}
		
		return sqlList;
		
	}
	
	private static Map<String, XBizTable> parseFile(String pathname) {
		// String pathname = "C:\\Documents and Settings\\user\\桌面\\2.txt";
		Map<String, XBizTable> tableMap = new Hashtable<String, XBizTable>();
		try {
			List<String> lineList = FileUtils.readLines(new File(pathname));
			for (String line : lineList) {
				if (line == null || !line.startsWith("Insert"))
					continue;
				String tableName = line.substring(14, line.indexOf("]"));
				XBizRecord record = parseLine(line);
				XBizTable bizTable = tableMap.get(tableName);
				if (bizTable == null) {
					bizTable = new XBizTable(-2000l, tableName);
					tableMap.put(tableName, bizTable);
				}
				bizTable.addRecord(record);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return tableMap;
	}
	
	private static XBizRecord parseLine(String lineStr) {
		int start = lineStr.indexOf("(");
		String string = lineStr.substring(start + 1, lineStr.length() - 2);
		String[] array = string.split("\\) Values\\(");
		String[] fieldNames = array[0].split(",");
		String[] values = array[1].split(",");
		
		XBizRecord record = new XBizRecord();
		for (int i = 0; i < fieldNames.length; i++) {
			String fieldName = fieldNames[i];
			fieldName = fieldName.substring(1, fieldName.length() - 1);
			
			String value = values[i];
			if (value.startsWith("X'")) {
				value = value.substring(2, value.length() - 1);
			}
			else {
				value = value.substring(1, value.length() - 1);
			}
			
			record.addField(new XBizField(fieldName, value));
		}
		
		return record;
		
	}
	
	public static void main(String[] args) {
		String pathname = "C:\\Documents and Settings\\fanhaoyu\\桌面\\lte-2.1.4.0-201519.sql";
		parseFile(pathname);
		
	}
	
}
