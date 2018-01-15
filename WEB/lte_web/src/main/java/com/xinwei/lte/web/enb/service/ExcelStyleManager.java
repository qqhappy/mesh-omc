/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-10-31	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellRangeAddress;

import com.xinwei.lte.web.enb.model.check.AlarmCheckForm;
import com.xinwei.lte.web.enb.model.check.DetailCheckResult;
import com.xinwei.lte.web.enb.model.check.EnbCheckConstants;
import com.xinwei.lte.web.enb.model.check.EnbCheckForm;
import com.xinwei.lte.web.enb.model.check.EnbCheckResult;

/**
 * 
 * excel样式管理
 * 
 * @author chenlong
 * 
 */

public class ExcelStyleManager {
	
	private static ExcelStyleManager instance;
	
	// 单元格内容位置靠左
	private final int ALIGN_LEFT = 0;
	
	// 单元格内容位置剧中
	private final int ALIGN_CENTER = 1;
	
	// 单元格内容位置靠右
	private final int ALIGN_RIGHT = 2;
	
	// 无背景颜色
	private final short NO_BACKGROUND_COLOR = -1;
	
	private final String INDEX_SHEET_NAME = "健康检查结果摘要";
	
	private final String ALARM_CHECK_FORM_NAME = "基站告警检查结果";
	
	public static ExcelStyleManager getInstance() {
		if (null == instance) {
			instance = new ExcelStyleManager();
		}
		return instance;
	}
	
	/**
	 * 导出基站健康检查结果的报表
	 */
	public void exportEnbCheckExcel(List<EnbCheckResult> checkResults,String path)
			throws Exception {
		System.out.println("Excel export start.");
		String currentDate = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		HSSFWorkbook wb = new HSSFWorkbook();
		Map<Long, Integer> enbRowMap = new HashMap<Long, Integer>();
		// 创建检查结果主页
		inputDataToIndexSheet(wb, checkResults, currentDate, enbRowMap);
		// 创建检查结果详细页
		inputDataToDetailSheet(wb, checkResults, enbRowMap);
		
		int enbSize = checkResults.size();
		int breakEnb = 0;
		for (EnbCheckResult enbCheckResult : checkResults) {
			if(EnbCheckConstants.NOT_NORMAL == enbCheckResult.getCheckResult()) {
				breakEnb++;
			}
		}
		String filePath = path + File.separator + "enbHealthCheck_" + currentDate + "_" + enbSize
				+ "_" + breakEnb + ".xls";
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		wb.write(fos);
		System.out.println("Excel export end.");
	}
	
	/**
	 * 生成健康检查主页数据
	 * 
	 * @param wb
	 * @param checkResults
	 * @param currentDate 
	 * @param enbRowMap
	 */
	private void inputDataToIndexSheet(HSSFWorkbook wb,
			List<EnbCheckResult> checkResults, String currentDate, Map<Long, Integer> enbRowMap) {
		HSSFSheet indexSheet = wb.createSheet(INDEX_SHEET_NAME);
		// 标题----------------------------------------------------------------------------------
		int formRowNum = 0;
		formRowNum = inputTheme(wb, indexSheet, formRowNum, "基站健康检查",
				HSSFColor.BLUE.index, 20);
		
		// 检查时间-------------------------------------------------------------------------------
		formRowNum++;
		String time = "检查时间:"
				+ getTimeFromLong(currentDate);
		formRowNum = inputTheme(wb, indexSheet, formRowNum, time,
				HSSFColor.BLACK.index, 13);
		// 检查结果列表----------------------------------------------------------------------------
		formRowNum++;
		// 创建标题行--------------------------------------------------
		HSSFRow titleRow = indexSheet.createRow(formRowNum);
		HSSFFont titleFont = getFontStyle(wb, 11, HSSFColor.BLACK.index, true);
		HSSFCellStyle titleStyle = getCellStyle(wb, ALIGN_CENTER,
				NO_BACKGROUND_COLOR, titleFont);
		String[] indexFormTitle = EnbCheckConstants.INDEX_FORM_TITLE;
		for (int i = 0; i < indexFormTitle.length; i++) {
			HSSFCell cell = titleRow.createCell(i);
			cell.setCellValue(indexFormTitle[i]);
			cell.setCellStyle(titleStyle);
		}
		formRowNum++;
		// 创建结果显示-------------------------------------------------
		for (EnbCheckResult enbCheckResult : checkResults) {
			HSSFCellStyle resultStyle = null;
			HSSFRow contentRow = indexSheet.createRow(formRowNum);
			// 保存基站结果数据所在的行数,实现sheet页跳转回主页对应位置
			enbRowMap.put(enbCheckResult.getEnbId(), formRowNum + 1);
			HSSFCell resultCell0 = contentRow.createCell(0);
			resultCell0.setCellType(HSSFCell.CELL_TYPE_STRING);
			resultCell0.setCellValue(switchEnbId(enbCheckResult.getEnbId()));
			// 增加sheet页跳转
			String sheetName = enbCheckResult.getEnbName() + "("
					+ switchEnbId(enbCheckResult.getEnbId()) + ")" + "检查结果";
			Hyperlink hyperlink = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
			hyperlink.setAddress("'" + sheetName + "'!A1");
			resultCell0.setHyperlink(hyperlink);
			HSSFCell resultCell1 = contentRow.createCell(1);
			resultCell1.setCellValue(enbCheckResult.getEnbName());
			HSSFCell resultCell2 = contentRow.createCell(2);
			resultCell2.setCellValue(enbCheckResult.getEnbVersion());
			HSSFCell resultCell3 = contentRow.createCell(3);
			
			HSSFCell resultCell4 = contentRow.createCell(4);
			HSSFCell resultCell5 = contentRow.createCell(5);
			if (enbCheckResult.isConnection()) {
				resultCell3.setCellValue(enbCheckResult.getSoftEnbVersion());
				resultCell4.setCellValue("正常");
				if (EnbCheckConstants.NORMAL == enbCheckResult.getCheckResult()) {
					resultCell5.setCellValue("正常");
					resultStyle = getCellStyle(wb, NO_BACKGROUND_COLOR);
				}
				else {
					resultCell5.setCellValue("故障");
					resultStyle = getCellStyle(wb, HSSFColor.RED.index);
				}
			}
			else {
				resultCell3.setCellValue("");
				resultCell4.setCellValue("故障");
				resultCell5.setCellValue("");
				resultStyle = getCellStyle(wb, HSSFColor.YELLOW.index);
			}
			HSSFCell resultCell6 = contentRow.createCell(6);
			resultCell6.setCellValue(enbCheckResult.getCheckDesc());
			
			resultCell0.setCellStyle(resultStyle);
			resultCell1.setCellStyle(resultStyle);
			resultCell2.setCellStyle(resultStyle);
			resultCell3.setCellStyle(resultStyle);
			resultCell4.setCellStyle(resultStyle);
			resultCell5.setCellStyle(resultStyle);
			resultCell6.setCellStyle(resultStyle);
			formRowNum++;
		}
	}
	
	/**
	 * 生成健康检查详细页数据
	 * 
	 * @param wb
	 * @param checkResults
	 * @param enbRowMap
	 * @throws Exception
	 */
	private void inputDataToDetailSheet(HSSFWorkbook wb,
			List<EnbCheckResult> checkResults, Map<Long, Integer> enbRowMap)
			throws Exception {
		if (null != checkResults && checkResults.size() > 0) {
			for (EnbCheckResult enbCheckResult : checkResults) {
				String sheetName = enbCheckResult.getEnbName() + "("
						+ switchEnbId(enbCheckResult.getEnbId()) + ")" + "检查结果";
				HSSFSheet detailSheet = wb.createSheet(sheetName);
				int formRowNum = 0;
				// sheet页标题
				formRowNum = inputTheme(wb, detailSheet, formRowNum, sheetName,
						HSSFColor.BLUE.index, 20);
				HSSFCell themeCell = detailSheet.getRow(0).getCell(0);
				// 设置跳转回主页
				Hyperlink hyperlink = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
				hyperlink.setAddress("'" + INDEX_SHEET_NAME + "'!A"
						+ enbRowMap.get(enbCheckResult.getEnbId()));
				themeCell.setHyperlink(hyperlink);
				formRowNum++;
				// 告警检查项表格生成
				formRowNum = inputAlarmCheckForm(wb, detailSheet, enbCheckResult, formRowNum);
				// 动态检查项表格生成
				List<DetailCheckResult> detailCheckResultList = enbCheckResult
						.getDetailCheckResultList();
				for (DetailCheckResult detailCheckResult : detailCheckResultList) {
					formRowNum = inputDetailCheckForm(wb, detailSheet,
							detailCheckResult, formRowNum);
				}
				
			}
		}
	}
	
	/**
	 * 生成告警检查项表格数据
	 * @param enbCheckResult
	 * @return
	 */
	private int inputAlarmCheckForm(HSSFWorkbook wb, HSSFSheet detailSheet,EnbCheckResult enbCheckResult,int formRowNum) {
		formRowNum = inputTheme(wb, detailSheet, formRowNum,
				ALARM_CHECK_FORM_NAME, HSSFColor.BLUE.index, 20);
		formRowNum++;
		// 创建表格标题行
		HSSFRow formTitleRow = detailSheet.createRow(formRowNum);
		HSSFFont titleFont = getFontStyle(wb, 11, HSSFColor.BLACK.index, true);
		HSSFCellStyle titleStyle = getCellStyle(wb, ALIGN_CENTER,
				NO_BACKGROUND_COLOR, titleFont);
		
		String[] formTitle = EnbCheckConstants.ALARM_FORM_TITLE;
		int firstCol = 0;
		int lastCol = firstCol + 1;
		for (int i = 0; i < formTitle.length; i++) {
			HSSFCell titleCell = formTitleRow.createCell(i);
			titleCell.setCellValue(formTitle[i]);
			titleCell.setCellStyle(titleStyle);
			firstCol = lastCol + 1;
			lastCol = firstCol + 1;
		}
		formRowNum++;
		// 创建表格内容行
		List<AlarmCheckForm> checkForms = enbCheckResult.getAlarmCheckForm();
		for (AlarmCheckForm alarmCheckForm : checkForms) {
			HSSFRow formContentRow = detailSheet.createRow(formRowNum);
			HSSFCellStyle contentStyle = getCellStyle(wb, ALIGN_CENTER,
					NO_BACKGROUND_COLOR, null);
			HSSFCell contentCell0 = formContentRow.createCell(0);
			contentCell0.setCellValue(alarmCheckForm.getLevel());
			contentCell0.setCellStyle(contentStyle);
			
			HSSFCell contentCell1 = formContentRow.createCell(1);
			contentCell1.setCellValue(alarmCheckForm.getContent());
			contentCell1.setCellStyle(contentStyle);
			
			HSSFCell contentCell2 = formContentRow.createCell(2);
			contentCell2.setCellValue(alarmCheckForm.getEnbName());
			contentCell2.setCellStyle(contentStyle);
			
			HSSFCell contentCell3 = formContentRow.createCell(3);
			contentCell3.setCellValue(alarmCheckForm.getLocation());
			contentCell3.setCellStyle(contentStyle);
			
			HSSFCell contentCell4 = formContentRow.createCell(4);
			contentCell4.setCellValue(alarmCheckForm.getStatus());
			contentCell4.setCellStyle(contentStyle);
			
			HSSFCell contentCell5 = formContentRow.createCell(5);
			contentCell5.setCellValue(alarmCheckForm.getHappenTime());
			contentCell5.setCellStyle(contentStyle);
			formRowNum++;
		}
		return formRowNum;
	}

	/**
	 * 生成详细页检查项的表格数据
	 * 
	 * @param wb
	 * @param detailSheet
	 * @param detailCheckResult
	 * @param formRowNum
	 * @return
	 * @throws Exception
	 */
	private int inputDetailCheckForm(HSSFWorkbook wb, HSSFSheet detailSheet,
			DetailCheckResult detailCheckResult, int formRowNum)
			throws Exception {
		
		formRowNum = inputTheme(wb, detailSheet, formRowNum,
				detailCheckResult.getCheckName(), HSSFColor.BLUE.index, 20);
		formRowNum++;
		// 创建表格标题行
		HSSFRow formTitleRow = detailSheet.createRow(formRowNum);
		HSSFFont titleFont = getFontStyle(wb, 11, HSSFColor.BLACK.index, true);
		HSSFCellStyle titleStyle = getCellStyle(wb, ALIGN_CENTER,
				NO_BACKGROUND_COLOR, titleFont);
		
		String[] formTitle = EnbCheckConstants.FORM_TITLE;
		int firstCol = 0;
		int lastCol = firstCol + 1;
		for (int i = 0; i < formTitle.length; i++) {
			HSSFCell titleCell = getMergeCell(detailSheet, formTitleRow,
					formRowNum, formRowNum, firstCol, lastCol);
			titleCell.setCellValue(formTitle[i]);
			titleCell.setCellStyle(titleStyle);
			firstCol = lastCol + 1;
			lastCol = firstCol + 1;
		}
		
		formRowNum++;
		// 创建表格内容行
		List<EnbCheckForm> checkForms = detailCheckResult.getCheckForm();
		for (EnbCheckForm enbCheckForm : checkForms) {
			HSSFRow formContentRow = detailSheet.createRow(formRowNum);
			HSSFCellStyle contentStyle = null;
			if (EnbCheckConstants.NOT_NORMAL == enbCheckForm.getCheckResult()) {
				contentStyle = getCellStyle(wb, ALIGN_CENTER,
						HSSFColor.RED.index, null);
			}
			else {
				contentStyle = getCellStyle(wb, ALIGN_CENTER,
						NO_BACKGROUND_COLOR, null);
			}
			
			firstCol = 0;
			lastCol = firstCol + 1;
			HSSFCell contentCell0 = getMergeCell(detailSheet, formContentRow,
					formRowNum, formRowNum, firstCol, lastCol);
			contentCell0.setCellValue(enbCheckForm.getCheckName());
			contentCell0.setCellStyle(contentStyle);
			
			firstCol = lastCol + 1;
			lastCol = firstCol + 1;
			
			HSSFCell contentCell1 = getMergeCell(detailSheet, formContentRow,
					formRowNum, formRowNum, firstCol, lastCol);
			contentCell1.setCellValue(enbCheckForm.getCheckResultDesc());
			contentCell1.setCellStyle(contentStyle);
			
			firstCol = lastCol + 1;
			lastCol = firstCol + 1;
			
			HSSFCell contentCell2 = getMergeCell(detailSheet, formContentRow,
					formRowNum, formRowNum, firstCol, lastCol);
			contentCell2.setCellValue(enbCheckForm.getCheckHopeDesc());
			contentCell2.setCellStyle(contentStyle);
			
			formRowNum++;
		}
		return formRowNum;
	}
	
	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param row
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 * @return
	 */
	private HSSFCell getMergeCell(HSSFSheet sheet, HSSFRow row, int firstRow,
			int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
				lastCol));
		return row.createCell(firstCol);
	}
	
	/**
	 * 创建标题和主题等单行合并单元格的条目
	 * 
	 * @param wb
	 * @param sheet
	 * @param formRowNum
	 * @param theme
	 * @return
	 */
	private int inputTheme(HSSFWorkbook wb, HSSFSheet sheet, int formRowNum,
			String theme, short color, int size) {
		int themeFirstRow = formRowNum;
		int themeLastRow = themeFirstRow + 1;
		int themeFirstCol = 0;
		int themeLastCol = themeFirstCol + 5;
		sheet.addMergedRegion(new CellRangeAddress(themeFirstRow, themeLastRow,
				themeFirstCol, themeLastCol));
		HSSFCell ThemeCell = sheet.createRow(themeFirstRow).createCell(
				themeFirstCol);
		ThemeCell.setCellValue(theme);
		HSSFFont ThemeFont = getFontStyle(wb, size, color, true);
		HSSFCellStyle ThemeStyle = getCellStyle(wb, ALIGN_LEFT,
				NO_BACKGROUND_COLOR, ThemeFont);
		ThemeCell.setCellStyle(ThemeStyle);
		
		formRowNum = themeLastRow;
		return formRowNum;
	}
	
	/**
	 * 获取字体样式
	 * 
	 * @param wb
	 *            excel工作簿
	 * @param size
	 *            字体大小
	 * @param color
	 *            字体颜色
	 * @param isBoldweight
	 *            是否加粗
	 * @return
	 */
	public HSSFFont getFontStyle(HSSFWorkbook wb, int size, int color,
			boolean isBoldweight) {
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) size); // 字号
		if (isBoldweight) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 加粗
		}
		font.setColor((short) color); // 颜色
		return font;
	}
	
	/**
	 * excel单元样式
	 * 
	 * @param wb
	 *            excel工作簿
	 * @param alignment
	 *            单元格内容位置,剧中、靠左、靠右
	 * @return
	 */
	public HSSFCellStyle getCellStyle(HSSFWorkbook wb, int alignment) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置位置
		if (ALIGN_LEFT == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		}
		else if (ALIGN_CENTER == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		else if (ALIGN_RIGHT == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}
		return cellStyle;
	}
	
	/**
	 * excel单元样式
	 * 
	 * @param wb
	 *            excel工作簿
	 * @param font
	 *            单元格字体
	 * @return
	 */
	public HSSFCellStyle getCellStyle(HSSFWorkbook wb, HSSFFont font) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置字体
		if (null != font) {
			cellStyle.setFont(font);
		}
		return cellStyle;
	}
	
	/**
	 * excel单元样式
	 * 
	 * @param wb
	 *            excel工作簿
	 * @param backgroundColor
	 *            单元格背景颜色
	 * @return
	 */
	public HSSFCellStyle getCellStyle(HSSFWorkbook wb, short backgroundColor) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		if (NO_BACKGROUND_COLOR != backgroundColor) {
			// 设置背景颜色
			cellStyle.setFillForegroundColor((short) backgroundColor);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		return cellStyle;
	}
	
	/**
	 * excel单元样式
	 * 
	 * @param wb
	 *            excel工作簿
	 * @param alignment
	 *            单元格内容位置,剧中、靠左、靠右
	 * @param backgroundColor
	 *            单元格背景颜色
	 * @param font
	 *            单元格字体
	 * @return
	 */
	public HSSFCellStyle getCellStyle(HSSFWorkbook wb, int alignment,
			short backgroundColor, HSSFFont font) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置位置
		if (ALIGN_LEFT == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		}
		else if (ALIGN_CENTER == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		else if (ALIGN_RIGHT == alignment) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}
		// 设置字体
		if (null != font) {
			cellStyle.setFont(font);
		}
		if (NO_BACKGROUND_COLOR != backgroundColor) {
			// 设置背景颜色
			cellStyle.setFillForegroundColor((short) backgroundColor);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		return cellStyle;
	}
	
	/**
	 * yyyyMMddHHmmss to yyyy-MM-dd HH:mm:ss
	 * @param longTime
	 * @return
	 */
	public String getTimeFromLong(String longTime) {
		if(14 == longTime.length()) {
			StringBuilder sb = new StringBuilder();
			sb.append(longTime.substring(0, 4) + "-");
			sb.append(longTime.substring(4, 6) + "-");
			sb.append(longTime.substring(6, 8) + " ");
			sb.append(longTime.substring(8, 10) + ":");
			sb.append(longTime.substring(10, 12) + ":");
			sb.append(longTime.substring(12, 14));
			return sb.toString();
		}
		return longTime;
	}
	
	public String switchEnbId(long enbId) {
		String enbIdHex = Long.toHexString(enbId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8 - enbIdHex.length(); i++) {
			sb.append(0);
		}
		sb.append(enbIdHex);
		return sb.toString();
	}
	
}
