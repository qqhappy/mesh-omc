/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xinwei.minas.server.stat.analyze.DataAnalyzer;
import com.xinwei.minas.server.stat.analyze.DelStatDataTask;
import com.xinwei.minas.server.stat.analyze.FailStatTask;
import com.xinwei.minas.server.stat.analyze.StatTask;
import com.xinwei.minas.server.stat.analyze.StatThread;
import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;
import com.xinwei.minas.stat.core.DateFormater;
import com.xinwei.minas.stat.core.StatUtil;

/**
 * 
 * ����ͳ��ģ��
 * 
 * @author fanhaoyu
 * 
 */

public class StatModule {

	private static final StatModule instance = new StatModule();

	public static StatModule getInstance() {
		return instance;
	}

	private StatModule() {
	}

	public void initialize() throws Exception {
		initDataManager();
		initTasks();
	}

	private void initTasks() throws Exception {

		// ////////////����ͳ������///////////////////
		DataAnalyzer analyzer = new DataAnalyzer();
		// ��ǰʱ�䣬��
		long currentTime = System.currentTimeMillis() / 1000;
		/***********************************************************************
		 * ����������ͳ������ *
		 **********************************************************************/

		SystemContext systemContext = SystemContext.getInstance();

		StatDataDAO statDataDAO = systemContext.getStatDataDAO();
		StatDetailDAO statDetailDAO = systemContext.getStatDetailDAO();
		// ͳ�Ƽ������λ��
		long weekStatInterval = systemContext.getWeekStatInterval();
		// ͳ�ƿ�ʼʱ�䣬�����ĸ�ʱ��Ƭ��
		long weekStartTimeSeq = currentTime / weekStatInterval;
		// ͳ������ʼʱ�䣬��λ��
		long startWeekStatTime = weekStartTimeSeq * weekStatInterval;
		// ͳ�������ӳ�ִ��ʱ�䣬��λ��
		long startWeekDelay = currentTime - startWeekStatTime + 60
				+ weekStatInterval;

		StatTask weekStatTask = new StatTask(StatUtil.COLLECT_TYPE_WEEK,
				StatUtil.COLLECT_ITEMS, weekStatInterval, statDataDAO,
				statDetailDAO, analyzer, startWeekStatTime);
		/***********************************************************************
		 * ����������ͳ������ *
		 **********************************************************************/
		long monthStatInterval = systemContext.getMonthStatInterval();
		long monthStartTimeSeq = currentTime / monthStatInterval;
		long startMonthStartTime = monthStartTimeSeq * monthStatInterval;
		long startMonthDelay = currentTime - startMonthStartTime
				+ monthStatInterval + 90;

		StatTask monthStatTask = new StatTask(StatUtil.COLLECT_TYPE_MONTH,
				StatUtil.COLLECT_ITEMS, monthStatInterval, statDataDAO,
				statDetailDAO, analyzer, startMonthStartTime);
		/***********************************************************************
		 * ����������ͳ������ *
		 **********************************************************************/
		long yearStatInterval = systemContext.getYearStatInterval();
		long yearStartTimeSeq = currentTime / yearStatInterval;
		long startYearStartTime = yearStartTimeSeq * yearStatInterval;
		long startYearDelay = currentTime - startYearStartTime
				+ yearStatInterval + 300;

		StatTask yearStatTask = new StatTask(StatUtil.COLLECT_TYPE_YEAR,
				StatUtil.COLLECT_ITEMS, yearStatInterval, statDataDAO,
				statDetailDAO, analyzer, startYearStartTime);
		/***********************************************************************
		 * ͳ��δ��ͳ�ƹ������� *
		 **********************************************************************/
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 6, 3,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

		while (true) {
			/**
			 * ��δͳ�ƹ��������ݽ���ͳ��
			 */
			// ȡ����lastDetail;
			Long weekLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_WEEK);// ����
			if (weekLastStatTime == null || weekLastStatTime == 0)// ���δͳ�ƹ�����ͳ�ƴ�20090101�賿��ʼͳ�Ƶ���ǰ������
			{
				weekLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// �����δ
			}
			while (weekLastStatTime - weekStatInterval * 1000 < weekStartTimeSeq * 1000) {

				StatThread thread = new StatThread(StatUtil.COLLECT_TYPE_WEEK,
						weekStatInterval, statDataDAO, statDetailDAO, analyzer,
						(long) (weekLastStatTime / 1000) - weekStatInterval);
				threadPool.execute(thread);
				weekLastStatTime = weekLastStatTime + weekStatInterval * 1000;//
			}

			/**
			 * ��δͳ�ƹ��������ݽ���ͳ��
			 */
			Long monthLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_MONTH);
			if (monthLastStatTime == null || monthLastStatTime == 0)// ���δͳ�ƹ�����ͳ�ƴ�20090101�賿��ʼͳ�Ƶ���ǰ������
			{
				monthLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// �����δ
			}
			while (monthLastStatTime - monthStatInterval * 1000 < monthStartTimeSeq * 1000) {

				StatThread thread = new StatThread(StatUtil.COLLECT_TYPE_MONTH,
						monthStatInterval, statDataDAO, statDetailDAO,
						analyzer, (long) (monthLastStatTime / 1000)
								- monthStatInterval);
				threadPool.execute(thread);
				monthLastStatTime = monthLastStatTime + monthStatInterval
						* 1000;
			}

			/**
			 * ��δͳ�ƹ��������ݽ���ͳ��
			 */
			Long yearLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_YEAR);
			if (yearLastStatTime == null || yearLastStatTime == 0)// ���δͳ�ƹ�����ͳ�ƴ�20090101�賿��ʼͳ�Ƶ���ǰ������
			{
				yearLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// �����δ
			}
			while (yearLastStatTime - yearStatInterval * 1000 < yearStartTimeSeq * 1000) {

				StatThread thread = new StatThread(StatUtil.COLLECT_TYPE_YEAR,
						yearStatInterval, statDataDAO, statDetailDAO, analyzer,
						(long) (yearLastStatTime / 1000) - yearStatInterval);
				threadPool.execute(thread);
				yearLastStatTime = yearLastStatTime + yearStatInterval * 1000;//
			}
			if (true)
				break;
		}

		// ������ͳ������
		ScheduledExecutorService weekService = Executors
				.newSingleThreadScheduledExecutor();
		weekService.scheduleAtFixedRate(weekStatTask, startWeekDelay,
				weekStatInterval, TimeUnit.SECONDS);

		// ������ͳ������
		ScheduledExecutorService monthService = Executors
				.newSingleThreadScheduledExecutor();
		monthService.scheduleAtFixedRate(monthStatTask, startMonthDelay,
				monthStatInterval, TimeUnit.SECONDS);

		// ������ͳ������
		ScheduledExecutorService yearService = Executors
				.newSingleThreadScheduledExecutor();
		yearService.scheduleAtFixedRate(yearStatTask, startYearDelay,
				yearStatInterval, TimeUnit.SECONDS);

		// �����ʧ����������ͳ��ִ�е�����
		long failedStatTaskInterval = systemContext.getFailedStatTaskInterval();
		ScheduledExecutorService failedService = Executors
				.newSingleThreadScheduledExecutor();
		failedService.scheduleAtFixedRate(new FailStatTask(),
				failedStatTaskInterval, failedStatTaskInterval,
				TimeUnit.SECONDS);

		// ����ɾ������
		long realTimeReserved = systemContext.getRealTimeReserved();
		long dailyReserved = systemContext.getDailyReserved();
		long weekReserved = systemContext.getWeekReserved();
		long monthReserved = systemContext.getMonthReserved();
		long yearReserved = systemContext.getYearReserved();
		DelStatDataTask delTask = new DelStatDataTask(realTimeReserved,
				dailyReserved, weekReserved, monthReserved, yearReserved);
		long deleteStatTaskInterval = systemContext.getDeleteStatTaskInterval();
		delTask.setStatDataDAO(statDataDAO);
		delTask.setStatDetailDAO(statDetailDAO);

		ScheduledExecutorService delService = Executors
				.newSingleThreadScheduledExecutor();
		delService.scheduleAtFixedRate(delTask, deleteStatTaskInterval,
				deleteStatTaskInterval, TimeUnit.SECONDS);

	}

	private void initDataManager() throws Exception {
		// ��ʼ��ԭʼ���ݹ�����
		OriginalDataManager.getInstance().initialize(new DataAnalyzer(),
				SystemContext.getInstance().getDailyStatInterval());
		// ��ʼ�������ݹ�����
		DailyDataManager.getInstance().initialize(
				SystemContext.getInstance().getStatDataDAO());
	}

}
