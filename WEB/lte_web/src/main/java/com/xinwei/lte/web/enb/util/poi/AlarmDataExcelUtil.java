package com.xinwei.lte.web.enb.util.poi;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.xinwei.lte.web.enb.util.StatDataExcelUtil;
import com.xinwei.minas.core.model.alarm.Alarm;

public class AlarmDataExcelUtil {
	private static String[] titles = {"级别","告警内容","eNB名称","定位信息","状态","发生时间","恢复时间","恢复用户","确认时间","确认用户"};
	
	public void exportAlarm(List<Alarm> alarmList,ServletOutputStream outPutStream)throws Exception{
		System.out.println("write file startTime " + (new Date()).toString());
		// 创建workBook
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFFont columnHeadfFont = workBook.createFont();
		columnHeadfFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headCellStyle = workBook.createCellStyle();
		headCellStyle.setFont(columnHeadfFont);
		HSSFSheet sheet = workBook.createSheet("EnbAlarmExport");
		HSSFRow row0 = sheet.createRow(0);		
		
		//设置列名
		for (int i = 0;i<titles.length;i++){
			HSSFCell cell = row0.createCell(i);
			cell.setCellStyle(headCellStyle);
			cell.setCellValue(titles[i]);
		}
		
		if(alarmList != null && !alarmList.isEmpty()){
			int rowcount = 1;
			for (Alarm alarm:alarmList)
			{
				HSSFRow row = sheet.createRow(rowcount ++);
				fillRow(row, alarm);
			}
		}
		setColumnAutoSize(sheet);
		workBook.write(outPutStream);
		if (outPutStream != null) {
			outPutStream.close();
		}

		System.out.println("write file endTime " + (new Date()).toString());		
	}
	
	/**
	 * 设置自动调整列宽
	 * 
	 * @param sheet
	 */
	private void setColumnAutoSize(HSSFSheet sheet) {
		int maxColumnIndex = 0;
		if (!isSheetEmpty(sheet)) {
			int columnIndex = 0;
			@SuppressWarnings("rawtypes")
			Iterator rowIterator = sheet.rowIterator();
			HSSFRow row = null;
			while (rowIterator.hasNext()) {
				row = (HSSFRow) rowIterator.next();
				columnIndex = row.getLastCellNum();
				if (columnIndex > maxColumnIndex)
					maxColumnIndex = columnIndex;
			}
		}
		for (int i = 0; i < maxColumnIndex; i++)
			sheet.autoSizeColumn(i);
	}
	
	/**
	 * 判断工作簿是否为空
	 * 
	 * @param sheet
	 * @return
	 */
	private boolean isSheetEmpty(HSSFSheet sheet) {
		if (sheet.rowIterator().hasNext())
			return false;
		return true;
	}
	
	private void fillRow(HSSFRow row, Alarm alarm) {	
		
		//告警级别
		switch (alarm.getAlarmLevel()) {
		case 1:
			row.createCell(0).setCellValue("紧急");
			break;
		case 2:
			row.createCell(0).setCellValue("重要");
			break;
		case 3:
			row.createCell(0).setCellValue("次要");
			break;
		case 4:
			row.createCell(0).setCellValue("提示");
		default:
			row.createCell(0).setCellValue("");
			break;
		}
		
		row.createCell(1).setCellValue(alarm.getAlarmContent());
		row.createCell(2).setCellValue(alarm.getMoName());
		row.createCell(3).setCellValue(alarm.getLocation());
		
		//告警状态
		if (alarm.getConfirmState() == 0 && alarm.getAlarmState() == 0 ) {
			row.createCell(4).setCellValue("未确认已恢复");
		}
		else if (alarm.getConfirmState() == 0 && alarm.getAlarmState() == 1 ) {
			row.createCell(4).setCellValue("未确认未恢复");
		}
		else if (alarm.getConfirmState() == 1 && alarm.getAlarmState() == 0 ) {
			row.createCell(4).setCellValue("已确认已恢复");
		}
		else if (alarm.getConfirmState() == 1 && alarm.getAlarmState() == 1 ) {
			row.createCell(4).setCellValue("已确认未恢复");
		}
		
		//发生时间
		row.createCell(5).setCellValue(toTimeString(alarm.getFirstAlarmTime()));
		
		//恢复时间
		if (alarm.getRestoredTime() != 0) {
			row.createCell(6).setCellValue(toTimeString(alarm.getRestoredTime()));
		}else {
			row.createCell(6).setCellValue("");
		}
		
		row.createCell(7).setCellValue(alarm.getRestoreUser());
		
		//确认时间
		if (alarm.getConfirmTime() != 0) {
			row.createCell(8).setCellValue(toTimeString(alarm.getRestoredTime()));
		}else {
			row.createCell(8).setCellValue("");
		}
		
		row.createCell(9).setCellValue(alarm.getConfirmUser());		
	}
	
	/**
	 * 将Long型时间串转为String类型表示
	 * 
	 * @return
	 */
	public String toTimeString(long time) {
		String str = String.valueOf(time);
		if(str.length() != 14) {
			return str;
		}
		String str1 = str.substring(0, 4);
		String str2 = str.substring(4, 6);
		String str3 = str.substring(6, 8);
		String str4 = str.substring(8, 10);
		String str5 = str.substring(10, 12);
		String str6 = str.substring(12, 14);
		return str1 + "-" + str2 + "-" + str3 + " " + str4 + ":" + str5 + ":"
				+ str6;
	}
}
