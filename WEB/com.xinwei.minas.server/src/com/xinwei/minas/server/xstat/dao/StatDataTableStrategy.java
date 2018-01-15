/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;

import com.xinwei.minas.server.xstat.model.TableStrategyModel;

/**
 * 
 * 统计数据持久化分表策略
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDataTableStrategy {

	/**
	 * 获取目标数据库和表
	 * 
	 * @param moId
	 * @param startTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @param endTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getOriginalTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * 根据时间获取目标数据库名
	 * 
	 * @param time
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public String getTargetDataBase(long time);

	/**
	 * 获取小时预统计数据的目标数据库和表
	 * 
	 * @param moId
	 * @param startTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @param endTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getPreOneHourTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * 获取天预统计数据的目标数据库和表
	 * 
	 * @param moId
	 * @param startTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @param endTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getPreOneDayTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * 根据起始结束时间获取目标数据库列表
	 * <p>
	 * 如果startTime为20120201000000，结束时间为20120401000000，则返回结果为201202,201203
	 * </p>
	 * <p>
	 * 如果startTime为20120206000000，结束时间为20120401000000，则返回结果为201202,201203
	 * </p>
	 * <p>
	 * 如果startTime为20120206000000，结束时间为20120406000000，则返回结果为201202,201203
	 * </p>
	 * 
	 * @param startTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @param endTime
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public List<String> getTargetDataBaseList(long startTime, long endTime);

	/**
	 * 获取目标数据库和表
	 * 
	 * @param moId
	 * @param time
	 *            时间格式为:YYYYMMDDhhmmss
	 * @param itemId
	 *            统计项ID
	 * @return
	 */
	public TableStrategyModel getOriginalTarget(Long moId, Long time);

	/**
	 * 获取小时预统计数据的目标数据库和表
	 * 
	 * @param moId
	 * @param time
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public TableStrategyModel getPreOneHourTarget(Long moId, Long time);

	/**
	 * 获取天预统计数据的目标数据库和表
	 * 
	 * @param moId
	 * @param time
	 *            时间格式为:YYYYMMDDhhmmss
	 * @return
	 */
	public TableStrategyModel getPreOneDayTarget(Long moId, Long time);
}
