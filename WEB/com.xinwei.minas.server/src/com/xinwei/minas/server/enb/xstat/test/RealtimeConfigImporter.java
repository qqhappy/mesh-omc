/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;

/**
 * 
 * eNB实时性能统计项导入器
 * 
 * @author fanhaoyu
 * 
 */

public class RealtimeConfigImporter extends
		ModelExcelImporter<EnbRealtimeItemConfig> {

	public RealtimeConfigImporter(String targetFile, String targetSheetName)
			throws Exception {
		super(targetFile, targetSheetName);
	}

	private static final String UNIT_ONCE = "次";
	private static final String UNIT_TEN_THOUSANDTH = "%%";
	private static final String UNIT_HUNDREDTH = "%";
	private static final String UNIT_ENTRY = "个";
	private static final String UNIT_NONE = "无";
	private static final String UNIT_DBM = "dbm";
	private static final String UNIT_BPS = "bps";

	public static final String[] COUNTER_COLUMNS = { "测量对象", "上报子系统", "测量子类型",
			"测量事件", "CounterId", "Counter中文名称", "Counter英文名称", "中文描述", "英文描述",
			"事件号", "TAG名称", "TAG ID", "换算", "单位", "数据类型", "网管时间汇总方式",
			"网管空间汇总方式", "备注" };

	private Map<String, String> constantMap;

	@Override
	protected List<EnbRealtimeItemConfig> getModelList(List<RowData> rowDataList) {

		List<EnbRealtimeItemConfig> configList = new LinkedList<EnbRealtimeItemConfig>();

		for (RowData rowData : rowDataList) {
			EnbRealtimeItemConfig config = new EnbRealtimeItemConfig();

			int index = 0;

			String statObject = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			try {
				config.setStatObject(getStatObject(statObject));
			} catch (Exception e1) {
				System.err.println("rowIndex=" + rowData.getRowIndex()
						+ ", statObject=" + statObject);
			}
			index++;

			String reportSystem = rowData
					.getColumnValue(COUNTER_COLUMNS[index]);
			config.setReportSystem(getConstant(reportSystem));
			index++;

			String measureType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setMeasureType(getConstant(measureType));
			index++;

			String measureEvent = rowData
					.getColumnValue(COUNTER_COLUMNS[index]);
			config.setMeasureEvent(getConstant(measureEvent));
			index++;

			String itemIdString = rowData
					.getColumnValue(COUNTER_COLUMNS[index]);
			int itemId = Integer.valueOf(itemIdString.substring(1));
			config.setItemId(itemId);
			index++;

			String name_zh = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setName_zh(name_zh);
			index++;

			String name_en = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setName_en(name_en);
			index++;

			String desc_zh = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setDesc_zh(desc_zh);
			index++;

			String desc_en = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setDesc_en(desc_en);
			index++;
			// 跳过 事件号 TAG名称
			index++;
			index++;

			String tagId = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setTagId(Integer.valueOf(tagId));
			index++;

			String exp = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setExp(getExpStr(exp));
			index++;

			String unitString = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			String unit = null;
			unit = getUnitStr(unitString);
			config.setUnit(unit);
			index++;

			String dataType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setDataType(dataType);
			index++;

			String timeType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setTimeType(timeType);
			index++;

			String spaceType = rowData.getColumnValue(COUNTER_COLUMNS[index]);
			config.setSpaceType(spaceType);
			index++;

			configList.add(config);
		}

		return configList;
	}

	public static final String UNDEFINED = "undefined";

	private String getConstant(String name) {
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

	private String getExpStr(String exp) {
		if (exp.equals("显示值=原值*10"))
			return EnbStatConstants.TEN_TIMES;
		return "";
	}

	private static String getUnitStr(String unit) {
		if (unit.equals(UNIT_ENTRY)) {
			return EnbStatConstants.UNIT_ENTRY;
		} else if (unit.equals(UNIT_ONCE)) {
			return EnbStatConstants.UNIT_ONCE;
		} else if (unit.equals(UNIT_NONE)) {
			return "";
		} else if (unit.equals(UNIT_DBM)) {
			return EnbStatConstants.UNIT_DBM;
		} else if (unit.equals(UNIT_BPS)) {
			return EnbStatConstants.UNIT_BPS;
		} else if (unit.equals(UNIT_TEN_THOUSANDTH)) {
			return EnbStatConstants.UNIT_TEN_THOUSANDTH;
		} else if (unit.equals(UNIT_HUNDREDTH)) {
			return EnbStatConstants.UNIT_HUNDREDTH;
		} else if (unit.equals("")) {
			return "";
		}
		System.out.println("unit is undefined");
		return UNDEFINED;
	}

	public void setConstantMap(Map<String, String> constantMap) {
		this.constantMap = constantMap;
	}

	private static final String LEVEL_CELL = "小区";

	private static final String LEVEL_BTS = "基站";

	private static String getStatObject(String value) throws Exception {
		if (value.equals(LEVEL_BTS)) {
			return EnbStatConstants.STAT_OBJECT_ENB;
		} else if (value.equals(LEVEL_CELL)) {
			return EnbStatConstants.STAT_OBJECT_CELL;
		}
		throw new Exception("statObject is blank");
	}

	public static void importConfigs(String configFilePath) throws Exception {
		InputStream inputStream = CounterItemConfigImporter.class
				.getClassLoader()
				.getResourceAsStream(
						"com/xinwei/minas/server/enb/xstat/test/统计项常量.properties");
		// 导入常量
		Map<String, String> map = StatConstantImporter
				.importConstants(inputStream);

		String targetSheetName = "实时counter";
		RealtimeConfigImporter importer = new RealtimeConfigImporter(
				configFilePath, targetSheetName);
		importer.setConstantMap(map);

		List<String> columnList = new LinkedList<String>();
		Collections.addAll(columnList, RealtimeConfigImporter.COUNTER_COLUMNS);

		importer.setColumnList(columnList);
		List<EnbRealtimeItemConfig> configList = importer.importModelList();
		System.out.println("realtime config item number=" + configList.size());

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into enb_realtime_config values\n");

		// ('26001', '1', 'CELL', 'REPORT_SYSTEM_RTS',
		// 'MEASURE_TYPE_PERFORMANCE', 'MEASURE_EVENT_UL_CELL', 'L2上行吞吐量',
		// 'L2上行吞吐量', '', '', '', 'UNIT_BPS', 'U32', 'MAX', 'MAX'),

		// 《KPI计数器及指标规则_V3.0-20150402.xlsx》(对应基站3.0.1版本)中新增了“换算”列，实时性能统计项的配置模型做了更改，数据库表也加了exp列
		for (EnbRealtimeItemConfig config : configList) {
			sqlBuilder.append("('").append(config.getItemId()).append("','")
					.append(config.getTagId()).append("','")
					.append(config.getStatObject()).append("','")
					.append(config.getReportSystem()).append("','")
					.append(config.getMeasureType()).append("','")
					.append(config.getMeasureEvent()).append("','")
					.append(config.getName_zh()).append("','")
					.append(config.getName_en()).append("','")
					.append(config.getDesc_zh()).append("','")
					.append(config.getDesc_en()).append("','")
					.append(config.getExp()).append("','")
					.append(config.getUnit()).append("','")
					.append(config.getDataType()).append("','")
					.append(config.getTimeType()).append("','")
					.append(config.getSpaceType()).append("'),\n");
		}

		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(";");

		System.out.println(sqlBuilder.toString());

		// EnbRealtimeItemConfigDAO dao = OmpAppContext.getCtx().getBean(
		// EnbRealtimeItemConfigDAO.class);
		// dao.saveItemConfig(configList);
	}

	public static void main(String[] args) {
		try {
			String targetFile = "C:\\Documents and Settings\\fanhaoyu\\桌面\\KPI.xls";
			importConfigs(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
