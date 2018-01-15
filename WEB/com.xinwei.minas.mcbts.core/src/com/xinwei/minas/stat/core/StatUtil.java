/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core;

/**
 * 
 * 统计助手
 * 
 * @author fanhaoyu
 * 
 */

public class StatUtil {

	/**
	 * 默认使用分隔符
	 */
	public static final String DEFAULT_SPLIT_CHAR = "#";

	/**
	 * JACORB配置文件参数名
	 */
	public static final String JACORB_CONFIG_FILE_NAME = "jacorbConfigFileName";

	/**
	 * 数据源配置文件参数名
	 */
	public static final String SYSTEM_DATA_SOURCE_FILE_NAME = "systemDataSourceFileName";

	/**
	 * 上行带宽
	 */
	public final static int COLLECT_ITEM_BWUP = 0;

	/**
	 * 下行带宽
	 */
	public final static int COLLECT_ITEM_BWDOWN = 1;

	/**
	 * 使用功率
	 */
	public final static int COLLECT_ITEM_POWER_USED = 2;

	/**
	 * 使用信道
	 */
	public final static int COLLECT_ITEM_CHANNEL_USED = 3;

	/**
	 * 活动用户数
	 */
	public final static int COLLECT_ITEM_ACTIVE_USER = 4;

	/**
	 * 信噪比
	 */
	public final static int COLLECT_ITEM_AVERAGE_CI = 5;

	/**
	 * 上行不可用信道
	 */
	public static final int COLLECT_ITEM_UNAVAILABLE_UP_CHANNEL = 6;

	/**
	 * 下行不可用信道
	 */
	public static final int COLLECT_ITEM_UNAVAILABLE_DOWN_CHANNEL = 7;

	/**
	 * 上行空闲信道
	 */
	public static final int COLLECT_ITEM_IDLE_UP_CHANNEL = 8;

	/**
	 * 下行空闲信道
	 */
	public static final int COLLECT_ITEM_IDLE_DOWN_CHANNEL = 9;

	/**
	 * 总空闲信道
	 */
	public static final int COLLECT_ITEM_IDLE_CHANNEL = 10;

	// SCG含义：SUB CHANNEL GROUP
	/**
	 * 时隙1，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT1_SCG1_CI = 11;

	public static final int COLLECT_ITEM_SLOT1_SCG2_CI = 12;

	public static final int COLLECT_ITEM_SLOT1_SCG3_CI = 13;

	public static final int COLLECT_ITEM_SLOT1_SCG4_CI = 14;

	public static final int COLLECT_ITEM_SLOT1_SCG5_CI = 15;

	/**
	 * 时隙2，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT2_SCG1_CI = 21;

	public static final int COLLECT_ITEM_SLOT2_SCG2_CI = 22;

	public static final int COLLECT_ITEM_SLOT2_SCG3_CI = 23;

	public static final int COLLECT_ITEM_SLOT2_SCG4_CI = 24;

	public static final int COLLECT_ITEM_SLOT2_SCG5_CI = 25;

	/**
	 * 时隙3，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT3_SCG1_CI = 31;

	public static final int COLLECT_ITEM_SLOT3_SCG2_CI = 32;

	public static final int COLLECT_ITEM_SLOT3_SCG3_CI = 33;

	public static final int COLLECT_ITEM_SLOT3_SCG4_CI = 34;

	public static final int COLLECT_ITEM_SLOT3_SCG5_CI = 35;

	/**
	 * 时隙4，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT4_SCG1_CI = 41;

	public static final int COLLECT_ITEM_SLOT4_SCG2_CI = 42;

	public static final int COLLECT_ITEM_SLOT4_SCG3_CI = 43;

	public static final int COLLECT_ITEM_SLOT4_SCG4_CI = 44;

	public static final int COLLECT_ITEM_SLOT4_SCG5_CI = 45;

	/**
	 * 时隙5，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT5_SCG1_CI = 51;

	public static final int COLLECT_ITEM_SLOT5_SCG2_CI = 52;

	public static final int COLLECT_ITEM_SLOT5_SCG3_CI = 53;

	public static final int COLLECT_ITEM_SLOT5_SCG4_CI = 54;

	public static final int COLLECT_ITEM_SLOT5_SCG5_CI = 55;

	/**
	 * 时隙6，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT6_SCG1_CI = 61;

	public static final int COLLECT_ITEM_SLOT6_SCG2_CI = 62;

	public static final int COLLECT_ITEM_SLOT6_SCG3_CI = 63;

	public static final int COLLECT_ITEM_SLOT6_SCG4_CI = 64;

	public static final int COLLECT_ITEM_SLOT6_SCG5_CI = 65;

	/**
	 * 时隙7，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT7_SCG1_CI = 71;

	public static final int COLLECT_ITEM_SLOT7_SCG2_CI = 72;

	public static final int COLLECT_ITEM_SLOT7_SCG3_CI = 73;

	public static final int COLLECT_ITEM_SLOT7_SCG4_CI = 74;

	public static final int COLLECT_ITEM_SLOT7_SCG5_CI = 75;

	/**
	 * 时隙8，SCG1--SCG5平均CI
	 */
	public static final int COLLECT_ITEM_SLOT8_SCG1_CI = 81;

	public static final int COLLECT_ITEM_SLOT8_SCG2_CI = 82;

	public static final int COLLECT_ITEM_SLOT8_SCG3_CI = 83;

	public static final int COLLECT_ITEM_SLOT8_SCG4_CI = 84;

	public static final int COLLECT_ITEM_SLOT8_SCG5_CI = 85;

	/**
	 * 实时统计数据
	 */
	public final static int COLLECT_TYPE_REALTIME = 0;

	/**
	 * 日统计数据
	 */
	public final static int COLLECT_TYPE_DAILY = 1;

	/**
	 * 周统计数据
	 */
	public final static int COLLECT_TYPE_WEEK = 2;

	/**
	 * 月统计数据
	 */
	public final static int COLLECT_TYPE_MONTH = 3;

	/**
	 * 年统计数据
	 */
	public final static int COLLECT_TYPE_YEAR = 4;

	/**
	 * 统计项数组
	 */
	public static final int[] COLLECT_ITEMS = { COLLECT_ITEM_BWUP,
			COLLECT_ITEM_BWDOWN, COLLECT_ITEM_POWER_USED,
			COLLECT_ITEM_CHANNEL_USED, COLLECT_ITEM_ACTIVE_USER,
			COLLECT_ITEM_AVERAGE_CI, COLLECT_ITEM_UNAVAILABLE_UP_CHANNEL,
			COLLECT_ITEM_UNAVAILABLE_DOWN_CHANNEL,
			COLLECT_ITEM_IDLE_UP_CHANNEL, COLLECT_ITEM_IDLE_DOWN_CHANNEL,
			COLLECT_ITEM_IDLE_CHANNEL,

			COLLECT_ITEM_SLOT1_SCG1_CI, COLLECT_ITEM_SLOT1_SCG2_CI,
			COLLECT_ITEM_SLOT1_SCG3_CI, COLLECT_ITEM_SLOT1_SCG4_CI,
			COLLECT_ITEM_SLOT1_SCG5_CI,

			COLLECT_ITEM_SLOT2_SCG1_CI, COLLECT_ITEM_SLOT2_SCG2_CI,
			COLLECT_ITEM_SLOT2_SCG3_CI, COLLECT_ITEM_SLOT2_SCG4_CI,
			COLLECT_ITEM_SLOT2_SCG5_CI,

			COLLECT_ITEM_SLOT3_SCG1_CI, COLLECT_ITEM_SLOT3_SCG2_CI,
			COLLECT_ITEM_SLOT3_SCG3_CI, COLLECT_ITEM_SLOT3_SCG4_CI,
			COLLECT_ITEM_SLOT3_SCG5_CI,

			COLLECT_ITEM_SLOT4_SCG1_CI, COLLECT_ITEM_SLOT4_SCG2_CI,
			COLLECT_ITEM_SLOT4_SCG3_CI, COLLECT_ITEM_SLOT4_SCG4_CI,
			COLLECT_ITEM_SLOT4_SCG5_CI,

			COLLECT_ITEM_SLOT5_SCG1_CI, COLLECT_ITEM_SLOT5_SCG2_CI,
			COLLECT_ITEM_SLOT5_SCG3_CI, COLLECT_ITEM_SLOT5_SCG4_CI,
			COLLECT_ITEM_SLOT5_SCG5_CI,

			COLLECT_ITEM_SLOT6_SCG1_CI, COLLECT_ITEM_SLOT6_SCG2_CI,
			COLLECT_ITEM_SLOT6_SCG3_CI, COLLECT_ITEM_SLOT6_SCG4_CI,
			COLLECT_ITEM_SLOT6_SCG5_CI,

			COLLECT_ITEM_SLOT7_SCG1_CI, COLLECT_ITEM_SLOT7_SCG2_CI,
			COLLECT_ITEM_SLOT7_SCG3_CI, COLLECT_ITEM_SLOT7_SCG4_CI,
			COLLECT_ITEM_SLOT7_SCG5_CI,

			COLLECT_ITEM_SLOT8_SCG1_CI, COLLECT_ITEM_SLOT8_SCG2_CI,
			COLLECT_ITEM_SLOT8_SCG3_CI, COLLECT_ITEM_SLOT8_SCG4_CI,
			COLLECT_ITEM_SLOT8_SCG5_CI };

	/**
	 * 统计类型数组
	 */
	public static final int[] COLLECT_TYPES = { COLLECT_TYPE_REALTIME,
			COLLECT_TYPE_DAILY, COLLECT_TYPE_WEEK, COLLECT_TYPE_MONTH,
			COLLECT_TYPE_YEAR };

	/**
	 * 根据统计类型取得各类数据保存时间
	 * 
	 * @param timeType
	 * @return
	 */
	public static long getDefaultDataReservedPeriod(int timeType) {
		long period = 24 * 60 * 60 * 1000L;
		switch (timeType) {
		case StatUtil.COLLECT_TYPE_DAILY:
			period = 24 * 60 * 60 * 1000L;
			break;
		case StatUtil.COLLECT_TYPE_WEEK:
			period = 8 * 24 * 60 * 60 * 1000L;
			break;
		case StatUtil.COLLECT_TYPE_MONTH:
			period = 31 * 24 * 60 * 60 * 1000L;
			break;
		case StatUtil.COLLECT_TYPE_YEAR:
			period = 366 * 24 * 60 * 60 * 1000L;
			break;
		default:
			break;
		}
		return period;
	}

	/**
	 * 获取当前统计类型的数据的源数据类型
	 * <p>
	 * 周数据由日数据统计得到，月数据由日数据统计得到，年数据由月数据统计得到
	 * </p>
	 * 
	 * @param timeType
	 * @return
	 */
	public static int getSubType(int timeType) {
		int subType = StatUtil.COLLECT_TYPE_DAILY;
		switch (timeType) {
		case StatUtil.COLLECT_TYPE_DAILY:
			subType = StatUtil.COLLECT_TYPE_DAILY;
			break;
		case StatUtil.COLLECT_TYPE_WEEK:
			subType = StatUtil.COLLECT_TYPE_DAILY;
			break;
		case StatUtil.COLLECT_TYPE_MONTH:
			subType = StatUtil.COLLECT_TYPE_DAILY;
			break;
		case StatUtil.COLLECT_TYPE_YEAR:
			subType = StatUtil.COLLECT_TYPE_MONTH;
			break;
		default:
			subType = StatUtil.COLLECT_TYPE_DAILY;
			break;
		}
		return subType;
	}

}
