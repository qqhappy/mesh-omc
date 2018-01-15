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
 * 数据统计模块
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

		// ////////////启动统计任务///////////////////
		DataAnalyzer analyzer = new DataAnalyzer();
		// 当前时间，秒
		long currentTime = System.currentTimeMillis() / 1000;
		/***********************************************************************
		 * 启动周数据统计任务 *
		 **********************************************************************/

		SystemContext systemContext = SystemContext.getInstance();

		StatDataDAO statDataDAO = systemContext.getStatDataDAO();
		StatDetailDAO statDetailDAO = systemContext.getStatDetailDAO();
		// 统计间隔，单位秒
		long weekStatInterval = systemContext.getWeekStatInterval();
		// 统计开始时间，落在哪个时间片内
		long weekStartTimeSeq = currentTime / weekStatInterval;
		// 统计任务开始时间，单位秒
		long startWeekStatTime = weekStartTimeSeq * weekStatInterval;
		// 统计任务延迟执行时间，单位秒
		long startWeekDelay = currentTime - startWeekStatTime + 60
				+ weekStatInterval;

		StatTask weekStatTask = new StatTask(StatUtil.COLLECT_TYPE_WEEK,
				StatUtil.COLLECT_ITEMS, weekStatInterval, statDataDAO,
				statDetailDAO, analyzer, startWeekStatTime);
		/***********************************************************************
		 * 启动月数据统计任务 *
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
		 * 启动年数据统计任务 *
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
		 * 统计未曾统计过的数据 *
		 **********************************************************************/
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 6, 3,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

		while (true) {
			/**
			 * 对未统计过的周数据进行统计
			 */
			// 取最大的lastDetail;
			Long weekLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_WEEK);// 毫秒
			if (weekLastStatTime == null || weekLastStatTime == 0)// 如果未统计过，则统计从20090101凌晨开始统计到当前的数据
			{
				weekLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// 如果从未
			}
			while (weekLastStatTime - weekStatInterval * 1000 < weekStartTimeSeq * 1000) {

				StatThread thread = new StatThread(StatUtil.COLLECT_TYPE_WEEK,
						weekStatInterval, statDataDAO, statDetailDAO, analyzer,
						(long) (weekLastStatTime / 1000) - weekStatInterval);
				threadPool.execute(thread);
				weekLastStatTime = weekLastStatTime + weekStatInterval * 1000;//
			}

			/**
			 * 对未统计过的月数据进行统计
			 */
			Long monthLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_MONTH);
			if (monthLastStatTime == null || monthLastStatTime == 0)// 如果未统计过，则统计从20090101凌晨开始统计到当前的数据
			{
				monthLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// 如果从未
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
			 * 对未统计过的年数据进行统计
			 */
			Long yearLastStatTime = statDetailDAO
					.getLatestStatTime(StatUtil.COLLECT_TYPE_YEAR);
			if (yearLastStatTime == null || yearLastStatTime == 0)// 如果未统计过，则统计从20090101凌晨开始统计到当前的数据
			{
				yearLastStatTime = DateFormater.parseDateTime(
						"2009-01-01 00:00:00").getTime();// 如果从未
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

		// 周数据统计任务
		ScheduledExecutorService weekService = Executors
				.newSingleThreadScheduledExecutor();
		weekService.scheduleAtFixedRate(weekStatTask, startWeekDelay,
				weekStatInterval, TimeUnit.SECONDS);

		// 月数据统计任务
		ScheduledExecutorService monthService = Executors
				.newSingleThreadScheduledExecutor();
		monthService.scheduleAtFixedRate(monthStatTask, startMonthDelay,
				monthStatInterval, TimeUnit.SECONDS);

		// 年数据统计任务
		ScheduledExecutorService yearService = Executors
				.newSingleThreadScheduledExecutor();
		yearService.scheduleAtFixedRate(yearStatTask, startYearDelay,
				yearStatInterval, TimeUnit.SECONDS);

		// 构造对失败任务重新统计执行的任务
		long failedStatTaskInterval = systemContext.getFailedStatTaskInterval();
		ScheduledExecutorService failedService = Executors
				.newSingleThreadScheduledExecutor();
		failedService.scheduleAtFixedRate(new FailStatTask(),
				failedStatTaskInterval, failedStatTaskInterval,
				TimeUnit.SECONDS);

		// 数据删除任务
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
		// 初始化原始数据管理器
		OriginalDataManager.getInstance().initialize(new DataAnalyzer(),
				SystemContext.getInstance().getDailyStatInterval());
		// 初始化日数据管理器
		DailyDataManager.getInstance().initialize(
				SystemContext.getInstance().getStatDataDAO());
	}

}
