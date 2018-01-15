/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.utils;

/**
 * 
 * eNB统计相关常量
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatConstants {

	public static final String ITEM_TYPE_COUNTER = "COUNTER";

	public static final String ITEM_TYPE_KPI = "KPI";

	public static final String STAT_OBJECT_ENB = "ENB";

	public static final String STAT_OBJECT_CELL = "CELL";

	// 统计项配置相关
	public static final String DATA_TYPE_INT = "INTEGER";

	public static final String DATA_TYPE_FLOAT = "FLOAT";

	/**
	 * 上报子系统-L3子系统
	 */
	public static final String REPORT_SYSTEM_L3 = "REPORT_SYSTEM_L3";

	/**
	 * 上报子系统-用户面子系统
	 */
	public static final String REPORT_SYSTEM_USER = "REPORT_SYSTEM_USER";

	/**
	 * 上报子系统-RTS子系统
	 */
	public static final String REPORT_SYSTEM_RTS = "REPORT_SYSTEM_RTS";

	/**
	 * 上报子系统-平台子系统
	 */
	public static final String REPORT_SYSTEM_PLATFORM = "REPORT_SYSTEM_PLATFORM";

	/**
	 * L3子系统的测量子类型-RRC连接
	 */
	public static final String MEASURE_TYPE_RRC = "MEASURE_TYPE_RRC";

	/**
	 * L3子系统的测量子类型-ERAB相关
	 */
	public static final String MEASURE_TYPE_ERAB = "MEASURE_TYPE_ERAB";

	/**
	 * L3子系统的测量子类型-Context相关
	 */
	public static final String MEASURE_TYPE_CONTEXT = "MEASURE_TYPE_CONTEXT";

	/**
	 * L3子系统的测量子类型-切换相关
	 */
	public static final String MEASURE_TYPE_SWITCH = "MEASURE_TYPE_SWITCH";

	/**
	 * L3子系统的测量子类型-用户数统计
	 */
	public static final String MEASURE_TYPE_USER_COUNT = "MEASURE_TYPE_USER_COUNT";

	/**
	 * L3子系统的测量子类型-其它
	 */
	public static final String MEASURE_TYPE_OTHER = "MEASURE_TYPE_OTHER";

	/**
	 * 用户面子系统的测量子类型-QCI相关
	 */
	public static final String MEASURE_TYPE_QCI = "MEASURE_TYPE_QCI";

	/**
	 * 用户面子系统的测量子类型-集群相关
	 */
	public static final String MEASURE_TYPE_PTT = "MEASURE_TYPE_PTT";

	/**
	 * RTS子系统的测量子类型-MCS相关
	 */
	public static final String MEASURE_TYPE_MCS = "MEASURE_TYPE_MCS";

	/**
	 * RTS子系统的测量子类型-RB相关
	 */
	public static final String MEASURE_TYPE_RB = "MEASURE_TYPE_RB";

	/**
	 * RTS子系统的测量子类型-重传相关
	 */
	public static final String MEASURE_TYPE_RETRANSFER = "MEASURE_TYPE_RETRANSFER";

	/**
	 * RTS子系统的测量子类型-公共调度相关
	 */
	public static final String MEASURE_TYPE_COMMON_DISPATHER = "MEASURE_TYPE_COMMON_DISPATHER";

	/**
	 * RTS子系统的测量子类型-CCE相关
	 */
	public static final String MEASURE_TYPE_CCE = "MEASURE_TYPE_CCE";

	/**
	 * RTS子系统的测量子类型-TS相关
	 */
	public static final String MEASURE_TYPE_TS = "MEASURE_TYPE_TS";

	/**
	 * RTS子系统的测量子类型-性能相关
	 */
	public static final String MEASURE_TYPE_PERFORMANCE = "MEASURE_TYPE_PERFORMANCE";

	/**
	 * 平台子系统的测量子类型-硬件资源
	 */
	public static final String MEASURE_TYPE_HARDWARE = "MEASURE_TYPE_HARDWARE";

	/**
	 * 性能子类型-接入性
	 */
	public static final String PERF_TYPE_ACCESS = "PERF_TYPE_ACCESS";

	/**
	 * 性能子类型-稳定性
	 */
	public static final String PERF_TYPE_STABLITY = "PERF_TYPE_STABLITY";

	/**
	 * 性能子类型-移动性
	 */
	public static final String PERF_TYPE_MOBILITY = "PERF_TYPE_MOBILITY";

	/**
	 * 性能子类型-业务量
	 */
	public static final String PERF_TYPE_OPERATION = "PERF_TYPE_OPERATION";

	/**
	 * 性能子类型-时延速率类
	 */
	public static final String PERF_TYPE_DELAY_RATE = "PERF_TYPE_DELAY_RATE";

	/**
	 * 性能子类型-资源类
	 */
	public static final String PERF_TYPE_RESOURCE = "PERF_TYPE_RESOURCE";

	/**
	 * 单位-次
	 */
	public static final String UNIT_ONCE = "UNIT_ONCE";
	public static final String UNIT_MS = "UNIT_MS";
	public static final String UNIT_SECOND = "UNIT_SECOND";
	/**
	 * 单位-个
	 */
	public static final String UNIT_ENTRY = "UNIT_ENTRY";
	public static final String UNIT_BYTE = "UNIT_BYTE";
	public static final String UNIT_BITPS = "UNIT_BITPS";
	public static final String UNIT_PKPS = "UNIT_PKPS";
	/**
	 * 单位-个/秒
	 */
	public static final String UNIT_ENTRYPS = "UNIT_ENTRYPS";

	/**
	 * 单位-bps
	 */
	public static final String UNIT_BPS = "UNIT_BPS";

	/**
	 * 单位-Kbps
	 */
	public static final String UNIT_KBPS = "UNIT_KBPS";

	/**
	 * 单位-Mbps
	 */
	public static final String UNIT_MBPS = "UNIT_MBPS";

	/**
	 * 单位-百分号
	 */
	public static final String UNIT_PERC = "UNIT_PERC";
	/**
	 * 单位-百分之百
	 */
	public static final String UNIT_PERC100 = "UNIT_PERC100";
	/**
	 * 单位-%%
	 */
	public static final String UNIT_2PERC = "UNIT_2PERC";
	/**
	 * 单位-db
	 */
	public static final String UNIT_DB = "UNIT_DB";
	/**
	 * 单位-dbm
	 */
	public static final String UNIT_DBM = "UNIT_DBM";

	/**
	 * 单位-KBIT
	 */
	public static final String UNIT_KBIT = "UNIT_KBIT";

	/**
	 * 单位-KBIT/S
	 */
	public static final String UNIT_KBITPS = "UNIT_KBITPS";

	/**
	 * 单位-百分之一
	 */
	public static final String UNIT_HUNDREDTH = "UNIT_HUNDREDTH";

	/**
	 * 单位-千分之一
	 */
	public static final String UNIT_THOUSANDTH = "UNIT_THOUSANDTH";

	/**
	 * 单位-万分之一
	 */
	public static final String UNIT_TEN_THOUSANDTH = "UNIT_TEN_THOUSANDTH";

	/**
	 * 换算-显示值=原值/10
	 */
	public static final String TEN_TIMES = "TEN_TIMES";
	
	/**
	 * 换算-显示值=原值*4
	 */
	public static final String FOURTIMES="FOURTIMES";

	/**
	 * counter已实现，需要显示
	 */
	public static final int COUNTER_REALITY = 1;

	/**
	 * counter未实现，不需要显示
	 */
	public static final int COUNTER_NOT_REALITY = 0;

}
