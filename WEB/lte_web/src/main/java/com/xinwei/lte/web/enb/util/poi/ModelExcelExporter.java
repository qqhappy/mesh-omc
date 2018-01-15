/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-1	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util.poi;

import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * 模型excel导出器虚基类
 * 
 * @author fanhaoyu
 * 
 */

public abstract class ModelExcelExporter<T> {

	private String targetSheetName;

	protected List<String> columnList;

	private HSSFWorkbook workBook;

	private boolean append = false;

	public ModelExcelExporter(String targetSheetName) {
		this.targetSheetName = targetSheetName;
	}

	public ModelExcelExporter(HSSFWorkbook workBook, String targetSheetName) {
		this.workBook = workBook;
		this.targetSheetName = targetSheetName;
	}

	public ModelExcelExporter(HSSFWorkbook workBook, String targetSheetName,
			boolean append) {
		this.workBook = workBook;
		this.targetSheetName = targetSheetName;
		this.append = append;
	}

	/**
	 * 导出到输出流
	 * 
	 * @param modelList
	 * @param outputStream
	 * @throws Exception
	 */
	public void exportModelList(List<T> modelList, OutputStream outputStream)
			throws Exception {

		exportModelList(modelList);

		workBook.write(outputStream);
	}

	public HSSFWorkbook exportModelList(List<T> modelList) throws Exception {
		if (columnList == null || columnList.isEmpty()) {
			throw new Exception("标题行列名不能为空!");
		}

		// 如果无输入，则创建新xls格式的字节流
		if (workBook == null) {
			workBook = PoiUtil.createXlsWorkbook();
		}

		// 初始化workBook
		initWorkBook(workBook);

		HSSFSheet targetSheet = workBook.getSheet(targetSheetName);
		fillSheet(targetSheet, modelList);

		return workBook;
	}

	/**
	 * 初始化workBook，创建目标sheet
	 * 
	 * @param workBook
	 * @throws Exception
	 */
	private void initWorkBook(HSSFWorkbook workBook) throws Exception {
		// 如果追加
		if (append) {
			HSSFSheet targetSheet = workBook.getSheet(targetSheetName);
			// 如果目标sheet不存在，则先创建sheet，创建标题行
			if (targetSheet == null) {
				targetSheet = workBook.createSheet(targetSheetName);
				PoiUtil.createRowAt(targetSheet, 0, columnList);
			} else {
				// 如果目标sheet存在，则校验sheet的格式是否与当前相符，不相符则报错
				if (!isTargetSheetFormatLegal(targetSheet)) {
					throw new Exception("目标文件中内容格式错误!");
				}
			}
		} else {
			// 如果不追加，清除sheet
			PoiUtil.clearSheetofWorkBook(workBook);
			HSSFSheet firstSheet = workBook.getSheetAt(0);
			// 如果第一个sheet是目标sheet，则清空
			if (firstSheet.getSheetName().equals(targetSheetName)) {
				PoiUtil.clearRowsofSheet(firstSheet);
				PoiUtil.createRowAt(firstSheet, 0, columnList);
			} else {
				// 如果不是目标sheet，则创建目标sheet，然后删除第一个sheet
				HSSFSheet targetSheet = workBook.createSheet(targetSheetName);
				workBook.removeSheetAt(0);
				PoiUtil.createRowAt(targetSheet, 0, columnList);
			}
		}
	}

	/**
	 * 目标工作簿格式是否合法
	 * 
	 * @param targetSheet
	 * @return
	 */
	private boolean isTargetSheetFormatLegal(HSSFSheet targetSheet) {
		if (PoiUtil.isSheetEmpty(targetSheet))
			return false;
		HSSFRow headerRow = targetSheet.getRow(0);
		return PoiUtil.matchRowData(headerRow, columnList);
	}

	private void fillSheet(HSSFSheet targetSheet, List<T> modelList) {
		// 如果无要导出的数据，则直接返回
		if (modelList == null || modelList.isEmpty())
			return;
		List<List<String>> rowDataList = getRowDataList(modelList);
		for (List<String> rowData : rowDataList) {
			// 创建数据行
			HSSFRow dataRow = targetSheet
					.createRow(targetSheet.getLastRowNum() + 1);
			PoiUtil.fillRowData(dataRow, rowData);
		}
	}

	public abstract List<List<String>> getRowDataList(List<T> modelList);

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public boolean isAppend() {
		return append;
	}

}
