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

import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * 统计明细信息接口
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDetailDAO {

	/**
	 * 存储或更新统计明细信息
	 * 
	 * @param detail
	 * @throws Exception
	 */
	public void saveOrUpdate(StatDetail detail) throws Exception;

	/**
	 * 取得最近一次，指定统计类型且统计成功的统计明细信息
	 * 
	 * @param timeType
	 *            --统计类型
	 * @return --统计明细
	 * @throws Exception
	 */
	public StatDetail getMaxSuccessStatDetail(int timeType) throws Exception;

	/**
	 * 取得某种统计类型最近一次统计结束时间
	 * 
	 * @param timeType
	 *            --统计类型
	 * @return
	 * @throws Exception
	 */
	public long getLatestStatTime(int timeType) throws Exception;

	/**
	 * 按类型查找失败的统计信息
	 * 
	 * @param timeType
	 *            --统计类型
	 * @return --统计明细
	 * @throws Exception
	 */
	public List<StatDetail> getFailedStatDetail(int timeType) throws Exception;

	/**
	 * 取得指定统计类型、统计结果，在指定统计结束时间之前的统计明细列表
	 * 
	 * @param timeType
	 *            --统计类型
	 * @param latestStatTime
	 *            --统计结束时间
	 * @param flag
	 *            --结果标识
	 * @return --统计明细列表
	 * @throws Exception
	 */
	public List<StatDetail> getStatDetailBeforeSpecTime(int timeType,
			long latestStatTime, int flag) throws Exception;

	/**
	 * 取得某指定统计类型在指定时间前的所有统计明细
	 * 
	 * @param timeType
	 *            --统计类型
	 * @param latestStatTime
	 *            --统计结束时间
	 * @return --统计明细列表
	 * @throws Exception
	 */
	public List<StatDetail> getFailedStatDetailBeforeSpecTime(int timeType,
			long latestStatTime) throws Exception;

	/**
	 * 刪除指定统计类型，统计结束时间，统计结果的统计明细
	 * 
	 * @param timeType
	 *            --统计类型
	 * @param endtime
	 *            --统计结束时间
	 * @param flag
	 *            --统计结果标识
	 * @throws Exception
	 */
	public void deleteStatDetail(int timeType, long endtime, int flag)
			throws Exception;
}
