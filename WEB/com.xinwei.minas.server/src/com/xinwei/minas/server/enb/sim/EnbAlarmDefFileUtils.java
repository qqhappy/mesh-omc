package com.xinwei.minas.server.enb.sim;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.server.enb.xstat.test.PoiUtil;

public class EnbAlarmDefFileUtils {

	/**
	 * 将告警定义文件转换为SQL输出
	 * @param defFile
	 * @param sheetName
	 * @throws Exception
	 */
	public void defFile2Sql(String defFile, String sheetName) throws Exception {
		// 根据输入的xls文件，生成告警定义模型列表
		List<AlarmDefExt> defList = this.generateAlarmDefList(defFile, sheetName);
		// 将告警定义模型列表转换为SQL语句输出
		for (AlarmDefExt def : defList) {
			StringBuffer buf = new StringBuffer();
			buf.append("insert into alarm_def(alarm_def_id, mo_type, alarm_level, alarm_name_zh, alarm_name_en)");
			buf.append(" values (");
			buf.append(def.getAlarmDefId()).append(",");
			buf.append(def.getMoType()).append(",");
			buf.append(def.getAlarmLevel()).append(",");
			buf.append("'").append(def.getAlarmNameZh()).append("',");
			buf.append("'").append(def.getAlarmNameEn()).append("');");
			
			System.out.println(buf.toString());
		}
	}

	/**
	 * 根据输入的xls文件，生成告警定义模型列表
	 * @param defFile
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public List<AlarmDefExt> generateAlarmDefList(String defFile, String sheetName) throws Exception {
		List<AlarmDefExt> defList = new LinkedList();
		HSSFWorkbook workbook = PoiUtil.parseWorkbook(defFile);
		HSSFSheet targetSheet = workbook.getSheet(sheetName);
		// 行数和列数
		int rowNum = targetSheet.getLastRowNum();
		int columnNum = 5;
		for (int i = 1; i <= rowNum; i++) {
			HSSFRow row = targetSheet.getRow(i);
			if (PoiUtil.isRowBlank(row, 0, columnNum))
				continue;
			List<String> rowData = PoiUtil.getRowData(row, columnNum);
			AlarmDefExt def = new AlarmDefExt();
			def.setMoType(String.valueOf(MoTypeDD.ENODEB));
			// 计算告警定义ID
			long alarmDefId = 0;
			long enbType = MoTypeDD.ENODEB;
			long alarmCode = Long.parseLong(rowData.get(0));
			long alarmSubCode = Long.parseLong(rowData.get(1));		
			// 2个字节设备类型+4字节告警码+2字节告警子码
			alarmDefId = (enbType << 48) + (alarmCode << 16) + alarmSubCode;
			def.setAlarmDefId(String.valueOf(alarmDefId));
			def.setAlarmLevel(rowData.get(2));
			def.setAlarmNameZh(rowData.get(3));
			def.setAlarmNameEn(rowData.get(4));
			
			defList.add(def);
		}
		return defList;
	}

	public static void main(String[] args) throws Exception {
		EnbAlarmDefFileUtils utils = new EnbAlarmDefFileUtils();
		String defFile = "F:\\Project\\2015项目\\McLTE微基站\\需求\\告警定义\\LTE微基站告警设计_fangping_0512.xls";
		String sheetName = "网管告警定义";
		// 将告警定义文件转换为SQL输出
		utils.defFile2Sql(defFile, sheetName);
	}

}
