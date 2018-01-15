/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class KpiItemConfigImporter extends ModelExcelImporter<KpiItemConfig> {

	public KpiItemConfigImporter(String targetFile, String targetSheetName)
			throws Exception {
		super(targetFile, targetSheetName);
	}

	private Map<String, String> constantMap;

	private static final String[] KPI_COLUMNS = { "测量对象", "性能子类型", "KPIId",
			"KPI中文名称", "KPI英文名称", "中文描述", "英文描述", "计算公式", "计算公式中文描述",
			"单位", "数据类型", "网管时间汇总方式", "网管空间汇总方式", "默认值", "需求来源", "备注" };

	@Override
	protected List<KpiItemConfig> getModelList(List<RowData> rowDataList) {
		List<KpiItemConfig> configList = new ArrayList<KpiItemConfig>();
		for (RowData rowData : rowDataList) {
			KpiItemConfig config = convert(rowData);
			configList.add(config);
		}
		return configList;
	}

	private KpiItemConfig convert(RowData rowData) {
		KpiItemConfig config = new KpiItemConfig();
		int index = 0;
		String statObject = rowData.getColumnValue(KPI_COLUMNS[index]);
		try {
			config.setStatObject(getStatObject(statObject));
		} catch (Exception e) {
			System.err.println("rowIndex=" + rowData.getRowIndex()
					+ ", statObject=" + statObject);
		}
		index++;

		String perfType = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setPerfType(getConstant(perfType));
		index++;

		String idString = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setKpiId(Integer.valueOf(idString.substring(1)));
		index++;

		String nameZh = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setKpiName_zh(nameZh);
		index++;

		String nameEn = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setKpiName_en(nameEn);
		index++;

		String descZh = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setKpiDesc_en(descZh);
		index++;

		String descEn = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setKpiDesc_zh(descEn);
		index++;

		String exp = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setExp(exp);
		index++;
		// 跳过计算公式中文描述
		index++;

		String unit = rowData.getColumnValue(KPI_COLUMNS[index]);
		try {
			config.setUnit(getUnitStr(unit));
		} catch (Exception e1) {
			System.err.println("rowIndex=" + rowData.getRowIndex() + ", unit="
					+ unit + ", " + e1.getLocalizedMessage());
		}
		index++;

		String dataType = rowData.getColumnValue(KPI_COLUMNS[index]);
		try {
			config.setDataType(getDataType(dataType));
		} catch (Exception e) {
			System.err.println("rowIndex=" + rowData.getRowIndex()
					+ ", dataType=" + dataType);
		}
		index++;

		String timeType = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setTimeType(timeType);
		index++;

		String spaceType = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setSpaceType(spaceType);
		index++;

		String defaultValue = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setDefaultValue(getDefaultValue(defaultValue));
		index++;
		// 跳过需求来源
		index++;

		String remark = rowData.getColumnValue(KPI_COLUMNS[index]);
		config.setRemark(EnbStatConstants.COUNTER_NOT_REALITY);
		if (remark.contains("已实现")) {
			config.setRemark(EnbStatConstants.COUNTER_REALITY);
		}

		return config;
	}

	private double getDefaultValue(String defaultValue) {
		if (defaultValue.endsWith("%")) {
			return Double.valueOf(defaultValue.substring(0,
					defaultValue.length() - 1)) / 100.0;
		}
		return Double.valueOf(defaultValue);
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

	@Override
	protected void checkRowData(RowData rowData) throws Exception {
	}

	private static final String LEVEL_CELL = "小区";

	private static final String LEVEL_BTS = "基站";

	private static final String DATA_TYPE_FLOAT = "浮点型";

	private static final String DATA_TYPE_INT = "整型";

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

	private static final String UNIT_ONCE = "次";
	private static final String UNIT_MS = "毫秒";
	private static final String UNIT_SECOND = "秒";
	private static final String UNIT_ENTRY = "个";
	private static final String UNIT_BYTE = "BYTE";
	private static final String UNIT_BYTE2 = "Byte";
	private static final String UNIT_KBITPS = "KBIT/S";
	private static final String UNIT_BITPS = "BIT/S";
	private static final String UNIT_PKPS = "包/秒";
	private static final String UNIT_ENTRYPS = "个/秒";
	private static final String UNIT_KBPS = "Kb/秒";
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
						"com/xinwei/minas/server/enb/xstat/test/统计项常量.properties");
		// 导入常量
		Map<String, String> map = StatConstantImporter
				.importConstants(inputStream);

		String targetSheetName = "KPI";
		KpiItemConfigImporter importer = new KpiItemConfigImporter(
				configFilePath, targetSheetName);
		List<String> columnList = new LinkedList<String>();
		Collections.addAll(columnList, KpiItemConfigImporter.KPI_COLUMNS);
		importer.setColumnList(columnList);
		importer.setConstantMap(map);

		List<KpiItemConfig> configList = importer.importModelList();
		System.out.println("config count=" + configList.size());

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into enb_kpi_config values\n");

		// ('1', 'CELL', 'PERF_TYPE_ACCESS', 'RRC连接建立成功率', 'RRC连接建立成功率', '', '',
		// 'UNIT_PERC', 'FLOAT', 'AVERAGE', 'AVERAGE', 'C000002/C000001',
		// '1.0000', '1'),

		for (KpiItemConfig config : configList) {
			sqlBuilder.append("('").append(config.getKpiId()).append("','")
					.append(config.getStatObject()).append("','")
					.append(config.getPerfType()).append("','")
					.append(config.getKpiName_zh()).append("','")
					.append(config.getKpiName_en()).append("','")
					.append(config.getKpiDesc_zh()).append("','")
					.append(config.getKpiDesc_en()).append("','")
					.append(config.getUnit()).append("','")
					.append(config.getDataType()).append("','")
					.append(config.getTimeType()).append("','")
					.append(config.getSpaceType()).append("','")
					.append(config.getExp()).append("','")
					.append(config.getDefaultValue()).append("','")
					.append(config.getRemark()).append("'),\n");
		}

		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(";");

		System.out.println(sqlBuilder.toString());
		// EnbStatItemConfigDAO enbStatItemConfigDAO = OmpAppContext.getCtx()
		// .getBean(EnbStatItemConfigDAO.class);
		// enbStatItemConfigDAO.saveKpiConfig(configList);
	}

}
