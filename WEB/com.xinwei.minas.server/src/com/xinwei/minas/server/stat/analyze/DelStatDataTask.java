/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * 
 * 删除过期的统计数据任务
 * 
 * @author fanhaoyu
 * 
 */

public class DelStatDataTask implements Runnable {

	private Log log = LogFactory.getLog(DelStatDataTask.class);

	/**
	 * 实时数据默认保存时间，单位毫秒
	 */
	private long realTimeDataReserved;

	/**
	 * 日数据默认保存时间，单位毫秒
	 */
	private long dailyDataReserved;

	/**
	 * 周数据默认保存时间，单位毫秒
	 */
	private long weekDataReserved;

	/**
	 * 月数据默认保存时间，单位毫秒
	 */
	private long monthDataReserved;

	/**
	 * 年数据默认保存时间，单位毫秒
	 */
	private long yearDataReserved;

	/**
	 * 统计明细数据访问接口
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * 统计数据访问接口
	 */
	private StatDataDAO statDataDAO;

	public void run() {
		work();
	}

	/**
	 * 删除统计数据任务构造函数
	 * 
	 * @param daily
	 *            日数据保存时间
	 * @param week
	 *            周数据保存时间
	 * @param month
	 *            月数据保存时间
	 * @param year
	 *            年数据保存时间
	 */
	public DelStatDataTask(long realTime, long daily, long week, long month,
			long year) {
		this.realTimeDataReserved = realTime;
		this.dailyDataReserved = daily;
		this.weekDataReserved = week;
		this.monthDataReserved = month;
		this.yearDataReserved = year;
	}

	/**
	 * 执行删除任务
	 * 
	 */
	public void work() {
		try {
			log.debug("DelStatDataTask delete realTime data.");
			this.deleteRealTimeData();
		} catch (Exception e) {
			log.error("Delete realTime data error!", e);
		}
		try {
			log.debug("DelStatDataTask delete daily data.");
			this.deleteDailyData();
		} catch (Exception e) {
			log.error("Delete daily data error!", e);
		}
		try {
			log.debug("DelStatDataTask delete week data.");
			this.deleteWeekData();
		} catch (Exception e) {
			log.error("Delete week data error!", e);
		}
		try {
			log.debug("DelStatDataTask delete month data.");
			this.deleteMonthData();
		} catch (Exception e) {
			log.error("Delete month data error!", e);
		}
		try {
			log.debug("DelStatDataTask delete year data.");
			this.deleteYearData();
		} catch (Exception e) {
			log.error("Delete year data error!", e);
		}
	}

	private void deleteRealTimeData() throws Exception {
		long currentTime = System.currentTimeMillis();
		long endTime = currentTime - getRealTimeDataReserved();

		log.debug("delete data before endTime=" + new Date(endTime));
		int delCount = getStatDataDAO().deleteData(endTime,
				StatUtil.COLLECT_TYPE_REALTIME);
		log.debug("Finish deleteRealTimeData. count=" + delCount);
	}

	/**
	 * 
	 * 删除日统计数据
	 */
	private void deleteDailyData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_WEEK);// 取得最近一次成功的明细
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getDailyDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_WEEK,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// 取出最大值

				long delEndTime = farDetail.getTime();// 取出該明細中的時間
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_DAILY);
				log.debug("Finish deleteDaliyData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_WEEK,
						farDetail.getTime(), 1);
				// 删除周数据统计失败的统计明细
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_WEEK,
						delEndTime - getWeekDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * 删除周统计数据
	 */
	private void deleteWeekData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_MONTH);// 取得最近一次成功的明细
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getWeekDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_MONTH,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// 取出最大值

				long delEndTime = farDetail.getTime();// 取出該明細中的時間
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_WEEK);
				log.debug("Finish deleteWeekData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(
						StatUtil.COLLECT_TYPE_MONTH, farDetail.getTime(), 1);
				// 删除月数据统计失败的统计明细
				getStatDetailDAO().deleteStatDetail(
						StatUtil.COLLECT_TYPE_MONTH,
						delEndTime - getMonthDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * 删除月统计数据
	 */
	private void deleteMonthData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_YEAR);// 取得最近一次成功的明细
		if (detail != null) {
			long maxDetailTime = detail.getTime();
			long farDetailTime = maxDetailTime - getMonthDataReserved();
			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_YEAR,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// 取出最大值
				long delEndTime = farDetail.getTime();// 取出該明細中的時間
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_MONTH);
				log.debug("Finish deleteMonthData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						farDetail.getTime(), 1);
				// 删除年数据统计失败的统计明细
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime - getYearDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * 删除年统计数据
	 */
	private void deleteYearData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_YEAR);// 取得最近一次成功的明细
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getYearDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_YEAR,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// 取出最大值

				long delEndTime = farDetail.getTime();// 取出該明細中的時間
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_YEAR);
				log.debug("Finish deleteYearData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime, 1);
				// 删除年数据统计失败的统计明细
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime - getYearDataReserved(), 0);
			}
		}
	}

	public long getRealTimeDataReserved() {
		return realTimeDataReserved;
	}

	/**
	 * 取得日数据保存时间
	 * 
	 * @return
	 */
	public long getDailyDataReserved() {
		return dailyDataReserved;
	}

	/**
	 * 设置日数据保存时间
	 * 
	 * @param dailyDataReserved
	 */
	public void setDailyDataReserved(long dailyDataReserved) {
		this.dailyDataReserved = dailyDataReserved;
	}

	/**
	 * 取得月数据保存时间
	 * 
	 * @return
	 */
	public long getMonthDataReserved() {
		return monthDataReserved;
	}

	/**
	 * 设置月数据保存时间
	 * 
	 * @param monthDataReserved
	 */
	public void setMonthDataReserved(long monthDataReserved) {
		this.monthDataReserved = monthDataReserved;
	}

	/**
	 * 取得周数据保存时间
	 * 
	 * @return
	 */
	public long getWeekDataReserved() {
		return weekDataReserved;
	}

	/**
	 * 设置周数据保存时间
	 * 
	 * @param weekDataReserved
	 */
	public void setWeekDataReserved(long weekDataReserved) {
		this.weekDataReserved = weekDataReserved;
	}

	/**
	 * 取得年数据保存时间
	 * 
	 * @return
	 */
	public long getYearDataReserved() {
		return yearDataReserved;
	}

	/**
	 * 设置年数据保存时间
	 * 
	 * @param yearDataReserved
	 */
	public void setYearDataReserved(long yearDataReserved) {
		this.yearDataReserved = yearDataReserved;
	}

	/**
	 * 取得统计数据访问接口
	 * 
	 * @return
	 */
	public StatDataDAO getStatDataDAO() {
		return statDataDAO;
	}

	/**
	 * 设置统计数据访问接口
	 * 
	 * @param statDataDAO
	 */
	public void setStatDataDAO(StatDataDAO statDataDAO) {
		this.statDataDAO = statDataDAO;
	}

	/**
	 * 取得统计明细数据访问接口
	 * 
	 * @return
	 */
	public StatDetailDAO getStatDetailDAO() {
		return statDetailDAO;
	}

	/**
	 * 设置统计明细数据访问接口
	 * 
	 * @param statDetailDAO
	 */
	public void setStatDetailDAO(StatDetailDAO statDetailDAO) {
		this.statDetailDAO = statDetailDAO;
	}

}
