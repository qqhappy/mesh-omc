/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;

/**
 * 
 * Counterͳ�������
 * 
 * @author fanhaoyu
 * 
 */

public class CounterItemConfigImporter extends
		ModelExcelImporter<CounterItemConfig> {

	public CounterItemConfigImporter(String targetFile, String targetSheetName)
			throws Exception {
		super(targetFile, targetSheetName);
	}

	private Map<String, String> constantMap;

	public static final String[] COUNTER_COLUMNS = { "��������", "�ϱ���ϵͳ", "����������",
			"�����¼�", "CounterId", "Counter��������", "CounterӢ������", "��������",
			"Ӣ������", "�¼���", "�¼��ź궨��", "Counter����궨��", "����������������",
			"���ݲ�����", "��λ", "��������", "����ʱ����ܷ�ʽ", "���ܿռ���ܷ�ʽ", "��ע", "Counter����" };

	@Override
	protected List<CounterItemConfig> getModelList(List<RowData> rowDataList) {

		List<CounterItemConfig> configList = new ArrayList<CounterItemConfig>();

		for (RowData rowData : rowDataList) {
			CounterItemConfig config = convert(rowData);
			configList.add(config);
		}

		return configList;
	}

	private CounterItemConfig convert(RowData rowData) {

		CounterItemConfig config = new CounterItemConfig();

		int index = 0;
		String statObject = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		try {
			config.setStatObject(getStatObject(statObject));
		} catch (Exception e1) {
			System.err.println("rowIndex=" + rowData.getRowIndex()
					+ ", statObject=" + statObject);
		}
		index++;

		String reportSystem = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setReportSystem(getConstant(reportSystem));
		index++;

		String measureType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setMeasureType(getConstant(measureType));
		index++;

		String measureEvent = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setMeasureEvent(getConstant(measureEvent));
		index++;

		String counterIdStr = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setCounterId(Integer.valueOf(counterIdStr.substring(1)));
		index++;

		String nameZh = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setCounterName_zh(nameZh);
		index++;

		String nameEn = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setCounterName_en(nameEn);
		index++;

		String descZh = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setCounterDesc_en(descZh);
		index++;

		String descEn = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setCounterDesc_zh(descEn);
		index++;

		// ���� �¼��� �¼��ź궨�� Counter����궨�� ���������������� ���ݲ�����
		index++;
		index++;
		index++;
		index++;
		index++;

		String unit = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		try {
			config.setUnit(getUnitStr(unit));
		} catch (Exception e1) {
			System.err.println("rowIndex=" + rowData.getRowIndex() + ", unit="
					+ unit + ", " + e1.getLocalizedMessage());
		}
		index++;

		String dataType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		try {
			config.setDataType(getDataType(dataType));
		} catch (Exception e) {
			System.err.println("rowIndex=" + rowData.getRowIndex()
					+ ", dataType=" + dataType);
		}
		index++;

		String timeType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setTimeType(timeType);
		index++;

		String spaceType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setSpaceType(spaceType);
		index++;

		String remark = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setRemark(EnbStatConstants.COUNTER_NOT_REALITY);
		if (remark.contains("��ʵ��")) {
			config.setRemark(EnbStatConstants.COUNTER_REALITY);
		}
		index++;

		String exp = rowData.getColumnValue(COUNTER_COLUMNS[index]);
		config.setExp(exp);

		return config;

	}

	private static final String LEVEL_CELL = "С��";

	private static final String LEVEL_BTS = "��վ";

	private static final String DATA_TYPE_FLOAT = "������";

	private static final String DATA_TYPE_INT = "����";

	private static String getStatObject(String value) throws Exception {
		if (value.equals(LEVEL_BTS)) {
			return EnbStatConstants.STAT_OBJECT_ENB;
		} else if (value.equals(LEVEL_CELL)) {
			return EnbStatConstants.STAT_OBJECT_CELL;
		}
		throw new Exception("statObject is blank");
	}

	private static String getDataType(String value) throws Exception {
		if (value.equals(DATA_TYPE_FLOAT)) {
			return EnbStatConstants.DATA_TYPE_FLOAT;
		} else if (value.equals(DATA_TYPE_INT)) {
			return EnbStatConstants.DATA_TYPE_INT;
		}
		throw new Exception("dataType is blank");
	}

	private static final String UNIT_ONCE = "��";
	private static final String UNIT_MS = "����";
	private static final String UNIT_SECOND = "��";
	private static final String UNIT_ENTRY = "��";
	private static final String UNIT_BYTE = "BYTE";
	private static final String UNIT_BYTE2 = "Byte";
	private static final String UNIT_KBITPS = "KBIT/S";
	private static final String UNIT_BITPS = "BIT/S";
	private static final String UNIT_PKPS = "��/��";
	private static final String UNIT_ENTRYPS = "��/��";
	private static final String UNIT_KBPS = "Kb/��";
	private static final String UNIT_KBIT = "KBIT";
	private static final String UNIT_PERC = "%";
	private static final String UNIT_PERC100 = "%*100";

	private static String getUnitStr(String unit) throws Exception {
		if (unit.equals(UNIT_ENTRY)) {
			return EnbStatConstants.UNIT_ENTRY;
		} else if (unit.equals(UNIT_ENTRYPS)) {
			return EnbStatConstants.UNIT_ENTRYPS;
		} else if (unit.equals(UNIT_MS)) {
			return EnbStatConstants.UNIT_MS;
		} else if (unit.equals(UNIT_ONCE)) {
			return EnbStatConstants.UNIT_ONCE;
		} else if (unit.equals(UNIT_PKPS)) {
			return EnbStatConstants.UNIT_PKPS;
		} else if (unit.equals(UNIT_SECOND)) {
			return EnbStatConstants.UNIT_SECOND;
		} else if (unit.equals(UNIT_KBPS)) {
			return EnbStatConstants.UNIT_KBPS;
		} else if (unit.equals(UNIT_BYTE)) {
			return EnbStatConstants.UNIT_BYTE;
		} else if (unit.equals(UNIT_BITPS)) {
			return EnbStatConstants.UNIT_BITPS;
		} else if (unit.equals(UNIT_PERC)) {
			return EnbStatConstants.UNIT_PERC;
		} else if (unit.equals(UNIT_PERC100)) {
			return EnbStatConstants.UNIT_PERC100;
		} else if (unit.equals(UNIT_KBIT)) {
			return EnbStatConstants.UNIT_KBIT;
		} else if (unit.equals(UNIT_BYTE2)) {
			return EnbStatConstants.UNIT_BYTE;
		} else if (unit.equals(UNIT_KBITPS)) {
			return EnbStatConstants.UNIT_KBITPS;
		}
		throw new Exception("unit is blank");
	}

	@Override
	protected void checkRowData(RowData rowData) throws Exception {

	}

	public static final String UNDEFINED = "undefined";

	public String getConstant(String name) {
		String reString = constantMap.get(name);
		if (reString == null) {
			reString = UNDEFINED;
			System.out.println("undefined " + name);
		}
		return reString;
	}

	public void setConstantMap(Map<String, String> constantMap) {
		this.constantMap = constantMap;
	}

	public Map<String, String> getConstantMap() {
		return constantMap;
	}

	public static void main(String[] args) {
		try {
			String targetFile = "D:\\_WorkBox\\eNB_Stat\\kpi.xls";
			importConfigs(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void importConfigs(String configFilePath) throws Exception {
		InputStream inputStream = CounterItemConfigImporter.class
				.getClassLoader()
				.getResourceAsStream(
						"com/xinwei/minas/server/enb/xstat/test/ͳ�����.properties");
		// ���볣��
		Map<String, String> map = StatConstantImporter
				.importConstants(inputStream);

		String targetSheetName = "counter";
		CounterItemConfigImporter importer = new CounterItemConfigImporter(
				configFilePath, targetSheetName);
		List<String> columnList = new LinkedList<String>();
		Collections.addAll(columnList,
				CounterItemConfigImporter.COUNTER_COLUMNS);
		importer.setColumnList(columnList);
		importer.setConstantMap(map);

		List<CounterItemConfig> configList = importer.importModelList();
		System.out.println("config count=" + configList.size());
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into enb_counter_config values\n");
		// ('1', 'CELL', 'REPORT_SYSTEM_L3', 'MEASURE_TYPE_RRC',
		// 'KPI_EV_L3_RRC_CONN_SETUP', 'RRC���ӽ����������', 'RRC���ӽ����������', '', '',
		// 'UNIT_ONCE', 'INTEGER', 'ADD', 'ADD', '', '1'),
		for (CounterItemConfig config : configList) {
			sqlBuilder.append("('").append(config.getCounterId()).append("','")
					.append(config.getStatObject()).append("','")
					.append(config.getReportSystem()).append("','")
					.append(config.getMeasureType()).append("','")
					.append(config.getMeasureEvent()).append("','")
					.append(config.getCounterName_zh()).append("','")
					.append(config.getCounterName_en()).append("','")
					.append(config.getCounterDesc_zh()).append("','")
					.append(config.getCounterDesc_en()).append("','")
					.append(config.getUnit()).append("','")
					.append(config.getDataType()).append("','")
					.append(config.getTimeType()).append("','")
					.append(config.getSpaceType()).append("','")
					.append(config.getExp()).append("','")
					.append(config.getRemark()).append("'),\n");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(";");

		System.out.println(sqlBuilder.toString());
		// EnbStatItemConfigDAO enbStatItemConfigDAO = OmpAppContext.getCtx()
		// .getBean(EnbStatItemConfigDAO.class);
		// enbStatItemConfigDAO.saveCounterConfig(configList);
	}

}
