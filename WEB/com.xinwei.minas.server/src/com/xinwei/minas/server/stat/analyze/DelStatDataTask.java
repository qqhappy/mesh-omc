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
 * ɾ�����ڵ�ͳ����������
 * 
 * @author fanhaoyu
 * 
 */

public class DelStatDataTask implements Runnable {

	private Log log = LogFactory.getLog(DelStatDataTask.class);

	/**
	 * ʵʱ����Ĭ�ϱ���ʱ�䣬��λ����
	 */
	private long realTimeDataReserved;

	/**
	 * ������Ĭ�ϱ���ʱ�䣬��λ����
	 */
	private long dailyDataReserved;

	/**
	 * ������Ĭ�ϱ���ʱ�䣬��λ����
	 */
	private long weekDataReserved;

	/**
	 * ������Ĭ�ϱ���ʱ�䣬��λ����
	 */
	private long monthDataReserved;

	/**
	 * ������Ĭ�ϱ���ʱ�䣬��λ����
	 */
	private long yearDataReserved;

	/**
	 * ͳ����ϸ���ݷ��ʽӿ�
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * ͳ�����ݷ��ʽӿ�
	 */
	private StatDataDAO statDataDAO;

	public void run() {
		work();
	}

	/**
	 * ɾ��ͳ�����������캯��
	 * 
	 * @param daily
	 *            �����ݱ���ʱ��
	 * @param week
	 *            �����ݱ���ʱ��
	 * @param month
	 *            �����ݱ���ʱ��
	 * @param year
	 *            �����ݱ���ʱ��
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
	 * ִ��ɾ������
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
	 * ɾ����ͳ������
	 */
	private void deleteDailyData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_WEEK);// ȡ�����һ�γɹ�����ϸ
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getDailyDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_WEEK,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// ȡ�����ֵ

				long delEndTime = farDetail.getTime();// ȡ��ԓ�����еĕr�g
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_DAILY);
				log.debug("Finish deleteDaliyData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_WEEK,
						farDetail.getTime(), 1);
				// ɾ��������ͳ��ʧ�ܵ�ͳ����ϸ
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_WEEK,
						delEndTime - getWeekDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * ɾ����ͳ������
	 */
	private void deleteWeekData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_MONTH);// ȡ�����һ�γɹ�����ϸ
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getWeekDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_MONTH,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// ȡ�����ֵ

				long delEndTime = farDetail.getTime();// ȡ��ԓ�����еĕr�g
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_WEEK);
				log.debug("Finish deleteWeekData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(
						StatUtil.COLLECT_TYPE_MONTH, farDetail.getTime(), 1);
				// ɾ��������ͳ��ʧ�ܵ�ͳ����ϸ
				getStatDetailDAO().deleteStatDetail(
						StatUtil.COLLECT_TYPE_MONTH,
						delEndTime - getMonthDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * ɾ����ͳ������
	 */
	private void deleteMonthData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_YEAR);// ȡ�����һ�γɹ�����ϸ
		if (detail != null) {
			long maxDetailTime = detail.getTime();
			long farDetailTime = maxDetailTime - getMonthDataReserved();
			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_YEAR,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// ȡ�����ֵ
				long delEndTime = farDetail.getTime();// ȡ��ԓ�����еĕr�g
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_MONTH);
				log.debug("Finish deleteMonthData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						farDetail.getTime(), 1);
				// ɾ��������ͳ��ʧ�ܵ�ͳ����ϸ
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime - getYearDataReserved(), 0);
			}
		}
	}

	/**
	 * 
	 * ɾ����ͳ������
	 */
	private void deleteYearData() throws Exception {
		StatDetail detail = getStatDetailDAO().getMaxSuccessStatDetail(
				StatUtil.COLLECT_TYPE_YEAR);// ȡ�����һ�γɹ�����ϸ
		if (detail != null) {
			long maxDetailTime = detail.getTime();

			long farDetailTime = maxDetailTime - getYearDataReserved();

			List<StatDetail> farDetailList = getStatDetailDAO()
					.getStatDetailBeforeSpecTime(StatUtil.COLLECT_TYPE_YEAR,
							farDetailTime, 1);
			if (farDetailList != null && farDetailList.size() > 0) {
				StatDetail farDetail = farDetailList.get(0);// ȡ�����ֵ

				long delEndTime = farDetail.getTime();// ȡ��ԓ�����еĕr�g
				log.debug("delete data before endTime=" + new Date(delEndTime));

				int delCount = getStatDataDAO().deleteData(delEndTime,
						StatUtil.COLLECT_TYPE_YEAR);
				log.debug("Finish deleteYearData. count=" + delCount);

				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime, 1);
				// ɾ��������ͳ��ʧ�ܵ�ͳ����ϸ
				getStatDetailDAO().deleteStatDetail(StatUtil.COLLECT_TYPE_YEAR,
						delEndTime - getYearDataReserved(), 0);
			}
		}
	}

	public long getRealTimeDataReserved() {
		return realTimeDataReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getDailyDataReserved() {
		return dailyDataReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param dailyDataReserved
	 */
	public void setDailyDataReserved(long dailyDataReserved) {
		this.dailyDataReserved = dailyDataReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getMonthDataReserved() {
		return monthDataReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param monthDataReserved
	 */
	public void setMonthDataReserved(long monthDataReserved) {
		this.monthDataReserved = monthDataReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getWeekDataReserved() {
		return weekDataReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param weekDataReserved
	 */
	public void setWeekDataReserved(long weekDataReserved) {
		this.weekDataReserved = weekDataReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getYearDataReserved() {
		return yearDataReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param yearDataReserved
	 */
	public void setYearDataReserved(long yearDataReserved) {
		this.yearDataReserved = yearDataReserved;
	}

	/**
	 * ȡ��ͳ�����ݷ��ʽӿ�
	 * 
	 * @return
	 */
	public StatDataDAO getStatDataDAO() {
		return statDataDAO;
	}

	/**
	 * ����ͳ�����ݷ��ʽӿ�
	 * 
	 * @param statDataDAO
	 */
	public void setStatDataDAO(StatDataDAO statDataDAO) {
		this.statDataDAO = statDataDAO;
	}

	/**
	 * ȡ��ͳ����ϸ���ݷ��ʽӿ�
	 * 
	 * @return
	 */
	public StatDetailDAO getStatDetailDAO() {
		return statDetailDAO;
	}

	/**
	 * ����ͳ����ϸ���ݷ��ʽӿ�
	 * 
	 * @param statDetailDAO
	 */
	public void setStatDetailDAO(StatDetailDAO statDetailDAO) {
		this.statDetailDAO = statDetailDAO;
	}

}
