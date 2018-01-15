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
	 * ���澯�����ļ�ת��ΪSQL���
	 * @param defFile
	 * @param sheetName
	 * @throws Exception
	 */
	public void defFile2Sql(String defFile, String sheetName) throws Exception {
		// ���������xls�ļ������ɸ澯����ģ���б�
		List<AlarmDefExt> defList = this.generateAlarmDefList(defFile, sheetName);
		// ���澯����ģ���б�ת��ΪSQL������
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
	 * ���������xls�ļ������ɸ澯����ģ���б�
	 * @param defFile
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public List<AlarmDefExt> generateAlarmDefList(String defFile, String sheetName) throws Exception {
		List<AlarmDefExt> defList = new LinkedList();
		HSSFWorkbook workbook = PoiUtil.parseWorkbook(defFile);
		HSSFSheet targetSheet = workbook.getSheet(sheetName);
		// ����������
		int rowNum = targetSheet.getLastRowNum();
		int columnNum = 5;
		for (int i = 1; i <= rowNum; i++) {
			HSSFRow row = targetSheet.getRow(i);
			if (PoiUtil.isRowBlank(row, 0, columnNum))
				continue;
			List<String> rowData = PoiUtil.getRowData(row, columnNum);
			AlarmDefExt def = new AlarmDefExt();
			def.setMoType(String.valueOf(MoTypeDD.ENODEB));
			// ����澯����ID
			long alarmDefId = 0;
			long enbType = MoTypeDD.ENODEB;
			long alarmCode = Long.parseLong(rowData.get(0));
			long alarmSubCode = Long.parseLong(rowData.get(1));		
			// 2���ֽ��豸����+4�ֽڸ澯��+2�ֽڸ澯����
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
		String defFile = "F:\\Project\\2015��Ŀ\\McLTE΢��վ\\����\\�澯����\\LTE΢��վ�澯���_fangping_0512.xls";
		String sheetName = "���ܸ澯����";
		// ���澯�����ļ�ת��ΪSQL���
		utils.defFile2Sql(defFile, sheetName);
	}

}
