/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.xinwei.minas.server.xstat.dao.StatDataTableStrategy;
import com.xinwei.minas.server.xstat.model.TableStrategyModel;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 统计数据持久化分表策略实现
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataTableStrategyImpl implements StatDataTableStrategy {

	public static final String DATA_BASE_PREFIX = "minas_stat_";

	public static final String ORIGINAL_TABLE_NAME = "original_stat_data_day{0}_moid{1}";

	public static final String HOUR_PRE_STAT_DATA_TABLE_NAME = "hour_pre_stat_data_day{0}_moid{1}";

	public static final String DAY_PRE_STAT_DATA_TABLE_NAME = "day_pre_stat_data_";

	public static final int TYPE_ORIGINAL = 1;

	public static final int TYPE_ONE_HOUR = 2;

	public static final int TYPE_ONE_DAY = 3;

	@Override
	@SuppressWarnings("deprecation")
	public TableStrategyModel getOriginalTarget(Long moId, Long time) {
		Date date = DateUtils.getDateByYYYYMMDDHHMMSS(String.valueOf(time));
		int day = date.getDate();
		long hashCode = calMoIdHashCode(moId);
		String tableName = MessageFormat.format(ORIGINAL_TABLE_NAME, day,
				hashCode);
		String dataBaseName = getTargetDataBase(time);
		return new TableStrategyModel(dataBaseName, tableName);
	}

	@Override
	@SuppressWarnings("deprecation")
	public TableStrategyModel getPreOneHourTarget(Long moId, Long time) {
		Date date = DateUtils.getDateByYYYYMMDDHHMMSS(String.valueOf(time));
		int day = date.getDate();
		long hashCode = calMoIdHashCode(moId);
		String tableName = MessageFormat.format(HOUR_PRE_STAT_DATA_TABLE_NAME,
				day, hashCode);
		String dataBaseName = getTargetDataBase(time);
		return new TableStrategyModel(dataBaseName, tableName);
	}

	@Override
	@SuppressWarnings("deprecation")
	public TableStrategyModel getPreOneDayTarget(Long moId, Long time) {
		Date date = DateUtils.getDateByYYYYMMDDHHMMSS(String.valueOf(time));
		int day = date.getDate();
		String tableName = DAY_PRE_STAT_DATA_TABLE_NAME + day;
		String dataBaseName = getTargetDataBase(time);
		return new TableStrategyModel(dataBaseName, tableName);
	}

	/**
	 * 计算moId的hash值
	 * 
	 * @param moId
	 * @return
	 */
	private long calMoIdHashCode(long moId) {
		return moId % 1000;
	}

	@Override
	public String getTargetDataBase(long time) {
		String timeString = String.valueOf(time).substring(0, 6);
		return DATA_BASE_PREFIX + timeString;
	}

	@Override
	public List<String> getTargetDataBaseList(long startTime, long endTime) {
		startTime = (startTime / 10000000) * 10000000 + 1000000;
		endTime = (endTime / 10000000) * 10000000 + 1000000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.getDateByYYYYMMDDHHMMSS(String
				.valueOf(startTime)));

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(DateUtils.getDateByYYYYMMDDHHMMSS(String
				.valueOf(endTime)));
		List<String> dbList = new ArrayList<String>();
		while (calendar.compareTo(calendar2) < 0) {
			long time = DateUtils.getBriefTimeFromMillisecondTime(calendar
					.getTimeInMillis());
			dbList.add(getTargetDataBase(time));
			calendar.add(Calendar.MONTH, 1);
		}
		return dbList;
	}

	@Override
	public List<TableStrategyModel> getOriginalTarget(Long moId,
			Long startTime, Long endTime) {
		return getTargetList(moId, startTime, endTime, TYPE_ORIGINAL);
	}

	@Override
	public List<TableStrategyModel> getPreOneHourTarget(Long moId,
			Long startTime, Long endTime) {
		return getTargetList(moId, startTime, endTime, TYPE_ONE_HOUR);
	}

	@Override
	public List<TableStrategyModel> getPreOneDayTarget(Long moId,
			Long startTime, Long endTime) {
		return getTargetList(moId, startTime, endTime, TYPE_ONE_DAY);
	}

	private List<TableStrategyModel> getTargetList(Long moId, Long startTime,
			Long endTime, int type) {
		List<TableStrategyModel> targetList = new ArrayList<TableStrategyModel>();
		long hashCode = calMoIdHashCode(moId);
		List<String> dayList = getTimeList(startTime, endTime);
		for (String dayString : dayList) {
			String dbPostfix = dayString.substring(0, 6);
			int day = Integer.valueOf(dayString.substring(6, 8));
			String dataBaseName = DATA_BASE_PREFIX + dbPostfix;
			String tableName = null;
			if (type == TYPE_ORIGINAL) {
				tableName = MessageFormat.format(ORIGINAL_TABLE_NAME, day,
						hashCode);
			} else if (type == TYPE_ONE_HOUR) {
				tableName = MessageFormat.format(HOUR_PRE_STAT_DATA_TABLE_NAME,
						day, hashCode);
			} else {
				tableName = DAY_PRE_STAT_DATA_TABLE_NAME + day;
			}
			targetList.add(new TableStrategyModel(dataBaseName, tableName));
		}
		return targetList;
	}

	/**
	 * 获取开始时间结束时间所跨的天
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private List<String> getTimeList(long startTime, long endTime) {
		startTime = startTime / 1000000 * 1000000;
		List<String> dayList = new ArrayList<String>();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(DateUtils.getDateByYYYYMMDDHHMMSS(String
				.valueOf(startTime)));
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(DateUtils.getDateByYYYYMMDDHHMMSS(String
				.valueOf(endTime)));
		if (calendar1.compareTo(calendar2) == 0) {
			dayList.add(getDayTimeStr(calendar1));
		} else {
			while (calendar1.compareTo(calendar2) < 0) {
				dayList.add(getDayTimeStr(calendar1));
				calendar1.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return dayList;
	}

	private String getDayTimeStr(Calendar calendar) {
		String time = String.valueOf(DateUtils
				.getBriefTimeFromMillisecondTime(calendar.getTimeInMillis()));
		return time.substring(0, 8);
	}

}