/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.meta.XItem;
import com.xinwei.omp.core.model.meta.XItems;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * 自学习的eNB数据配置解析器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStudyDataConfigParser {

	/**
	 * 数据配置格式
	 * <p>
	 * T_RACK,S,50;u8RackNO,S,INTEGER,1,PK;u8RackType,S,INTEGER,1,;s8RackName,S,
	 * TEXT,81,;
	 * </p>
	 * 
	 * @param dataConfigStr
	 * @return
	 */
	public static Map<String, XList> parse(String dataConfigStr) {
		if (dataConfigStr == null || dataConfigStr.trim().equals(""))
			return null;
		// 转换换行符
		dataConfigStr = dataConfigStr.replaceAll("\r\n", "#");
		dataConfigStr = dataConfigStr.replaceAll("\r", "#");
		dataConfigStr = dataConfigStr.replaceAll("\n", "#");
		// 分隔成行数据
		String[] lines = dataConfigStr.split("\\#");
		Map<String, XList> tableMap = new HashMap<String, XList>();
		for (String line : lines) {
			String temp = line.trim();
			if (temp.equals(""))
				continue;
			String[] fields = temp.split("\\;");
			String table = fields[0];
			String[] array = table.split(",");
			// 表名
			String tableName = array[0];
			List<XItem> tableXItems = new ArrayList<XItem>();
			List<XList> fieldXLists = new ArrayList<XList>();
			XList tableXList = new XList();
			// 表的size
			XItem xItem = new XItem();
			xItem.setName(XList.P_SIZE);
			xItem.setValue(array[2]);
			tableXItems.add(xItem);
			// 遍历字段
			for (int i = 1; i < fields.length; i++) {
				if (fields[i].equals(""))
					continue;
				array = fields[i].split(",");
				XList field = createField(array);
				fieldXLists.add(field);
			}
			XItems xItems = new XItems();
			XItem[] items = new XItem[tableXItems.size()];
			xItems.setItem(tableXItems.toArray(items));
			tableXList.setItems(new XItems[] { xItems });

			XList[] lists = new XList[fieldXLists.size()];
			tableXList.setList(fieldXLists.toArray(lists));

			tableMap.put(tableName, tableXList);
		}
		return tableMap;
	}

	private static Map<String, XList> parse2(String dataConfigStr) {
		if (dataConfigStr == null || dataConfigStr.trim().equals(""))
			return null;
		// 转换换行符
		dataConfigStr = dataConfigStr.replaceAll("\r\n", "#");
		dataConfigStr = dataConfigStr.replaceAll("\r", "#");
		dataConfigStr = dataConfigStr.replaceAll("\n", "#");
		// 分隔成行数据
		String[] lines = dataConfigStr.split("\\#");
		Map<String, XList> tableMap = new HashMap<String, XList>();
		String currentTable = null;
		List<XList> currentList = new ArrayList<XList>();
		List<XItem> currentItems = new ArrayList<XItem>();
		for (String line : lines) {
			String temp = line.trim();
			if (temp.equals(""))
				continue;
			temp = line.substring(1, line.length() - 2);
			String[] array = temp.split(",");
			// 表名
			if (temp.startsWith("T_")) {
				// 保存上一张表解析的数据
				if (currentTable != null) {
					XList xList = new XList();
					XItems xItems = new XItems();
					XItem[] items = new XItem[currentItems.size()];
					xItems.setItem(currentItems.toArray(items));
					xList.setItems(new XItems[] { xItems });
					XList[] lists = new XList[currentList.size()];
					xList.setList(currentList.toArray(lists));
					tableMap.put(currentTable, new XList());
				}
				String tableName = array[0];
				// 记录当前正在解析的表
				currentTable = tableName;
				// 表的size
				XItem xItem = new XItem();
				xItem.setName(XList.P_SIZE);
				xItem.setValue(array[2]);
				currentItems.add(xItem);
			} else {
				// 字段
				XList field = createField(array);
				currentList.add(field);
			}
		}
		return tableMap;
	}

	private static XList createField(String[] array) {
		String fieldName = array[0];
		String dataType = array[2];
		String dataLength = array[3];

		XList xList = new XList();
		xList.setName(fieldName);

		XItems xItems = new XItems();

		XItem typeItem = new XItem();
		typeItem.setName(XList.P_TYPE);
		typeItem.setValue(dataType);

		XItem lenItem = new XItem();
		lenItem.setName(XList.P_LENGTH);
		lenItem.setValue(dataLength);

		XItem[] itemArray = new XItem[] { typeItem, lenItem };
		xItems.setItem(itemArray);

		XItems[] itemsArray = new XItems[] { xItems };
		xList.setItems(itemsArray);

		return xList;
	}

	public static void main(String[] args) {
		String path = "C:\\Documents and Settings\\fanhaoyu\\桌面\\enbdataconfig.txt";
		try {
			byte[] content = getFileContent(new File(path));
			String string = new String(content);
			parse2(string);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static byte[] getFileContent(File file) throws Exception {
		byte[] content = new byte[0];
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			int fileLength = inputStream.available();
			content = new byte[fileLength];
			inputStream.read(content);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return content;
	}
}
