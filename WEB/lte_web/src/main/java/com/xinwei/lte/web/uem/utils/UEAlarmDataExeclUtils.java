package com.xinwei.lte.web.uem.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class UEAlarmDataExeclUtils {
	private static String[] titles = {"级别","告警内容","UEID","IMSI","状态","发生时间","恢复时间","恢复用户","确认时间","确认用户"};
	
	public void exportUEAlarm(List<JSONObject> alarmList,ServletOutputStream outPutStream)throws Exception{
		System.out.println("write file startTime " + (new Date()).toString());
		// 创建workBook
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFFont columnHeadfFont = workBook.createFont();
		columnHeadfFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headCellStyle = workBook.createCellStyle();
		headCellStyle.setFont(columnHeadfFont);
		HSSFSheet sheet = workBook.createSheet("UEAlarmExport");
		HSSFRow row0 = sheet.createRow(0);		
		
		//设置列名
		for (int i = 0;i<titles.length;i++){
			HSSFCell cell = row0.createCell(i);
			cell.setCellStyle(headCellStyle);
			cell.setCellValue(titles[i]);
		}
		try{
			if(alarmList != null && alarmList.size() != 0){
				int rowcount = 1;
				int alarmIndex = 0;			
				for (alarmIndex=0; alarmIndex < alarmList.size(); alarmIndex++){
					JSONObject alarmObj = (JSONObject)alarmList.get(alarmIndex);
					HSSFRow row = sheet.createRow(rowcount ++);
					fillRow(row, alarmObj);
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("exportUEAlarm exception" + e);
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
	
	private void fillRow(HSSFRow row, JSONObject alarmObj) {			
		int index = 0;
		JSONObject alarm = (JSONObject)alarmObj.get("alarm");
		for(index=0;index < titles.length;index++){
			String ParaName = titles[index];	
			if (ParaName.equals("级别")){
				int levelNum = (Integer)alarm.get("alarmLevel");
				row.createCell(0).setCellValue(transAlarmLevel(levelNum));
			}else if (ParaName.equals("告警内容")){
				String alarmContent = alarm.get("alarmContent") + "";
				if (alarmContent.equals("null")){
					row.createCell(1).setCellValue("");
				}else{				
					row.createCell(1).setCellValue(alarmContent);
				}
			}else if (ParaName.equals("UEID")){
				String UEId = (Integer)alarmObj.get("UEId") + "";
				if (UEId.equals("null")){
					row.createCell(2).setCellValue("");
				}else{
					row.createCell(2).setCellValue(UEId);
				}
			}else if (ParaName.equals("IMSI")){
				String IMSI = alarm.get("IMSI") + "";
				if (IMSI.equals("null")){
					row.createCell(3).setCellValue("");
				}else{
					row.createCell(3).setCellValue(IMSI);
				}

			}else if (ParaName.equals("状态")){
				int confirmState = (Integer)alarm.get("confirmState");
				int alarmState  = (Integer)alarm.get("alarmState");
				row.createCell(4).setCellValue(getAlarmStatus(confirmState, alarmState));
			}else if (ParaName.equals("发生时间")){
				String firstAlarmTime = alarmObj.get("lastAlarmTimeString") + "";
				if (firstAlarmTime.equals("null")) {
					row.createCell(5).setCellValue("");	
				}else {
					row.createCell(5).setCellValue(firstAlarmTime);
				}

			}else if (ParaName.equals("恢复时间")){
				String restoredTime = alarmObj.get("restoredTimeString") + "";
				if (restoredTime.equals("null")) {
					row.createCell(6).setCellValue("");	
				}else {				
					row.createCell(6).setCellValue(restoredTime);
				}
			}else if (ParaName.equals("恢复用户")){
				String confirmUser = alarm.get("confirmUser") + "";
				if (confirmUser.equals("null")) {
					row.createCell(7).setCellValue("");	
				}else {
					row.createCell(7).setCellValue(confirmUser);
				}
			}else if (ParaName.equals("确认时间")){
				String confirmTime = alarmObj.get("confirmTimeString") + "";
				if (confirmTime.equals("null")) {
					row.createCell(8).setCellValue("");	
				}else {
					row.createCell(8).setCellValue(confirmTime);
				}
			}else if (ParaName.equals("确认用户")){
				String restoreUser = alarm.get("restoreUser") + "";
				if (restoreUser.equals("null")) {
					row.createCell(9).setCellValue("");	
				}else {
					row.createCell(9).setCellValue(restoreUser);
				}
			}
		}	
	}
	
	
	private String transAlarmLevel(int levelNum){
		//告警级别
		switch (levelNum) {
		case 1:
			return "紧急";
		case 2:
			return "重要";
		case 3:
			return "次要";
		case 4:
			return "提示";
		default:
			return "";
		}
	}
	
	
	private String getAlarmStatus(int confirmState,int alarmState){
		//告警状态
		if (confirmState == 0 && alarmState == 0 ) {
			return "未确认已恢复";
		}
		else if (confirmState == 0 && alarmState == 1 ) {
			return "未确认未恢复";
		}
		else if (confirmState == 1 && alarmState == 0 ) {
			return "已确认已恢复";
		}
		else if (confirmState == 1 && alarmState == 1 ) {
			return "已确认未恢复";
		}else{
			return "";
		}
	}
	
}
