/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 
 * POI助手�?
 * 
 * @author fanhaoyu
 * 
 */

public class PoiUtil {

	public static final String POST_FIX = ".xls";

	public static final String DEFAULT_SHEET_NAME = "sheet1";

	/**
	 * 创建xls格式的workbook
	 * 
	 * @return
	 * @throws IOException
	 */
	public static HSSFWorkbook createXlsWorkbook() throws IOException {
		return createXlsWorkbook(DEFAULT_SHEET_NAME);
	}

	/**
	 * 创建xls格式的workbook
	 * 
	 * @param defaultSheetName
	 * @return
	 * @throws IOException
	 */
	public static HSSFWorkbook createXlsWorkbook(String defaultSheetName)
			throws IOException {
		if (defaultSheetName == null || defaultSheetName.equals("")) {
			defaultSheetName = DEFAULT_SHEET_NAME;
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		workbook.createSheet(defaultSheetName);
		return workbook;
	}

	/**
	 * 在指定位置创建行
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param valueList
	 * @return
	 */
	public static HSSFRow createRowAt(HSSFSheet sheet, int rowIndex,
			List<String> valueList) {
		HSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < valueList.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(valueList.get(i));
		}
		return row;
	}

	/**
	 * 在最后一行的下一行创建行
	 * 
	 * @param sheet
	 * @param valueList
	 * @return
	 */
	public static HSSFRow createRow(HSSFSheet sheet, List<String> valueList) {
		return createRowAt(sheet, sheet.getLastRowNum(), valueList);
	}

	/**
	 * 将数据填充到�?
	 * 
	 * @param row
	 * @param valueList
	 */
	public static void fillRowData(HSSFRow row, List<String> valueList) {
		for (int index = 0; index < valueList.size(); index++) {
			HSSFCell cell = row.getCell(index);
			if (cell == null)
				cell = row.createCell(index);
			cell.setCellValue(valueList.get(index));
		}
	}

	/**
	 * 获取�?��中从第一个单元格�?��到第columnNum个单元格为止的数值列�?
	 * 
	 * @param row
	 * @param columnNum
	 * @return
	 */
	public static List<String> getRowData(HSSFRow row, int columnNum) {
		if (columnNum == 0 || isRowBlank(row))
			return Collections.emptyList();
		List<String> rowData = new LinkedList<String>();
		for (int i = 0; i < columnNum; i++) {
			rowData.add(getCellValue(row.getCell(i)));
		}
		return rowData;
	}

	/**
	 * 获取�?��中所有单元格的数值列�?
	 * 
	 * @param row
	 * @return
	 */
	public static List<String> getRowData(HSSFRow row) {
		if (isRowBlank(row))
			return Collections.emptyList();
		int columnNum = row.getLastCellNum();
		return getRowData(row, columnNum);
	}

	/**
	 * 获取单元格中的�?
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(HSSFCell cell) {
		if (cell == null)
			return "";
		// TODO all type takes as string
		int type = cell.getCellType();
		switch (type) {
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
	}

	/**
	 * 获取�?��工作簿名�?
	 * 
	 * @param workbook
	 * @return
	 */
	public static List<String> getSheetNames(HSSFWorkbook workbook) {
		if (workbook == null)
			return Collections.emptyList();
		int sheetNum = workbook.getNumberOfSheets();
		if (sheetNum == 0)
			return Collections.emptyList();
		List<String> sheetNames = new LinkedList<String>();
		for (int i = 0; i < sheetNum; i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			sheetNames.add(sheet.getSheetName());
		}
		return sheetNames;
	}

	/**
	 * 从输入流中解析workbook
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static HSSFWorkbook parseWorkbook(FileInputStream inputStream)
			throws IOException {
		try {
			POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
			return new HSSFWorkbook(fileSystem);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public static HSSFWorkbook parseWorkbook(String filePath)
			throws IOException {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
			return new HSSFWorkbook(fileSystem);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 指定行中的数据是否与指定数据�?��
	 * 
	 * @param row
	 * @param valueList
	 * @return
	 */
	public static boolean matchRowData(HSSFRow row, List<String> valueList) {
		if (isRowBlank(row)) {
			if (valueList == null || valueList.isEmpty()) {
				return true;
			}
		}
		int columnNum = row.getLastCellNum();

		List<String> rowData = getRowData(row, valueList.size());
		if (!rowData.equals(valueList)) {
			return false;
		} else {
			if (columnNum > valueList.size()) {
				return isRowBlank(row, valueList.size(), columnNum);
			}
			return true;
		}
	}

	/**
	 * 判断某一行是否是空行
	 * 
	 * @param row
	 * @return
	 */
	public static boolean isRowBlank(HSSFRow row) {
		if (row == null)
			return true;
		int columnNum = row.getLastCellNum();
		if (columnNum == 0)
			return true;
		return isRowBlank(row, 0, columnNum);
	}

	/**
	 * 判断�?��中从begin到end之间的单元格数�?是否都为�?
	 * 
	 * @param row
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean isRowBlank(HSSFRow row, int begin, int end) {
		if (row == null)
			return true;
		for (int i = begin; i < end; i++) {
			String value = getCellValue(row.getCell(i));
			if (!value.equals(""))
				return false;
		}
		return true;
	}

	/**
	 * 将单元格内的值转成字符串类型
	 * 
	 * @param value
	 * @return
	 */
	public static String valueToString(double value) {
		String temp = String.valueOf(value);
		if (isNumberInteger(value)) {
			int index = temp.lastIndexOf(".");
			return temp.substring(0, index);
		}
		return temp;

	}

	/**
	 * 判断单元格中的数值是否是整数
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumberInteger(double value) {
		String temp = String.valueOf(value);
		if (temp.contains(".")) {
			int index = temp.lastIndexOf(".");
			temp = temp.substring(index + 1);
		}
		temp = temp.replaceAll("0", "");
		if (temp.equals(""))
			return true;
		return false;
	}

	/**
	 * 判断文件是否是xls文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isXlsFile(String filePath) {
		return filePath.toLowerCase().endsWith(POST_FIX);
	}

	/**
	 * 清除sheet中所有Row
	 * 
	 * @param sheet
	 */
	public static void clearRowsofSheet(HSSFSheet sheet) {
		if (isSheetEmpty(sheet))
			return;
		while (sheet.rowIterator().hasNext()) {
			sheet.removeRow(sheet.rowIterator().next());
		}
	}

	/**
	 * 工作簿是否为�?
	 * 
	 * @param sheet
	 * @return
	 */
	public static boolean isSheetEmpty(HSSFSheet sheet) {
		if (sheet.rowIterator().hasNext())
			return false;
		return true;
	}

	/**
	 * 清除�?��sheet，因为必须要保留�?��，所以只保留第一个sheet
	 * 
	 * @param workBook
	 */
	public static void clearSheetofWorkBook(HSSFWorkbook workBook) {
		while (workBook.getNumberOfSheets() > 1) {
			// 每次删除第一个，直到只剩�?��
			workBook.removeSheetAt(0);
		}
	}
}
