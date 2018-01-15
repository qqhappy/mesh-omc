/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao;

import java.util.List;

import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * 统计数据DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDataDAO {

	/**
	 * 存储统计数据列表
	 * 
	 * @param dataList
	 * @throws Exception
	 */

	public void saveData(List<StatData> dataList) throws Exception;

	/**
	 * 存储统计数据
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveData(StatData data) throws Exception;

	/**
	 * 查询指定时间段内的统计数据
	 * 
	 * @param begintime
	 *            --统计开始时间
	 * @param endtime
	 *            --统计结束时间
	 * @param timeType
	 *            --统计类型
	 * @return --统计数据列表
	 * @throws Exception
	 */
	public List<StatData> getData(long begintime, long endtime, int timeType)
			throws Exception;

	/**
	 * 查询指定基站在指定时间段内的统计数据
	 * 
	 * @param btsId
	 *            --基站编号
	 * @param begintime
	 *            --统计开始时间
	 * @param endtime
	 *            --统计结束时间
	 * @param type
	 *            --统计类型
	 * @return --统计数据列表
	 * @throws Exception
	 */
	public List<StatData> getData(long btsId, long begintime, long endtime,
			int timeType) throws Exception;

	/**
	 * 删除指定时间段内统计数据
	 * 
	 * @param beginTime
	 *            --统计开始时间
	 * @param endTime
	 *            --统计结束时间
	 * @param timeType
	 *            --统计类型
	 * @return --删除的记录数
	 * @throws Exception
	 */
	public int deleteData(long beginTime, long endTime, int timeType)
			throws Exception;

	/**
	 * 删除某时间点之前的所有统计数据
	 * 
	 * @param endTime
	 *            --统计结束时间
	 * @param timeType
	 *            --统计类型
	 * @return --删除的记录数
	 * @throws Exception
	 */
	public int deleteData(long endTime, int timeType) throws Exception;

}
