/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * 模型excel导入器类
 * 
 * @author fanhaoyu
 * 
 */

public abstract class ModelExcelImporter<T> {

	private String targetSheetName;

	private List<String> columnList;

	private HSSFWorkbook workbook;

	public ModelExcelImporter(String targetFile, String targetSheetName)
			throws Exception {
		this.targetSheetName = targetSheetName;
		this.workbook = PoiUtil.parseWorkbook(targetFile);
	}

	public ModelExcelImporter(HSSFWorkbook workbook, String targetSheetName)
			throws Exception {
		this.targetSheetName = targetSheetName;
		this.workbook = workbook;
	}

	public List<T> importModelList() throws Exception {
		if (columnList == null || columnList.isEmpty()) {
			throw new Exception("标题行列名不能为�?");
		}

		HSSFSheet targetSheet = workbook.getSheet(targetSheetName);
		// 目标工作簿不存在
		if (targetSheet == null) {
			throw new Exception("目标工作簿不存在! 工作簿名�?" + targetSheetName);
		}
		// 数据格式不符
		if (!isTargetSheetFormatLegal(targetSheet)) {
			throw new Exception("目标文件中的数据与规定格式不�?");
		}

		// 遍历�?
		int rowNum = targetSheet.getLastRowNum();
		int columnNum = columnList.size();
		List<RowData> rowDataList = new LinkedList<RowData>();
		for (int i = 1; i <= rowNum; i++) {
			HSSFRow row = targetSheet.getRow(i);
			// 行为空，继续下一�?
			if (PoiUtil.isRowBlank(row, 0, columnNum))
				continue;
			List<String> rowData = PoiUtil.getRowData(row, columnNum);
			RowData data = new RowData(i + 1);
			for (int j = 0; j < columnList.size(); j++) {
				data.addCell(columnList.get(j), rowData.get(j));
			}
			// 验证行数据是否符合规�?
			checkRowData(data);

			rowDataList.add(data);
		}

		return getModelList(rowDataList);
	}

	/**
	 * 目标工作簿格式是否合�?
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

	/**
	 * 获取
	 * 
	 * @param rowDataList
	 * @return
	 */
	protected abstract List<T> getModelList(List<RowData> rowDataList);

	/**
	 * 验证某一行的数据是否符合规则
	 * 
	 * @param rowData
	 * @throws Exception
	 */
	protected abstract void checkRowData(RowData rowData) throws Exception;

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

}
