/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;

/**
 * 
 * 统计分析任务
 * 
 * @author fanhaoyu
 * 
 */

public class StatTask implements Runnable {

	/**
	 * 统计类型
	 */
	private int type;

	/**
	 * 统计开始时间,要求绝对数值,单位秒
	 */
	private long startTime;

	/**
	 * 统计时间间隔,单位秒
	 */
	private long analyzeInterval;

	/**
	 * 统计数据访问接口
	 */
	private StatDataDAO collectDataDAO;

	/**
	 * 统计明细数据访问接口
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * 数据分析器
	 */
	private DataAnalyzer analyzer;

	/**
	 * 默认的统计项
	 */
	private int[] items = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	/**
	 * 统计任务构造函数
	 * 
	 * @param type
	 *            --统计类型
	 * @param items
	 *            --统计项
	 * @param analyzeInterval
	 *            --分析时间间隔
	 * @param collectDataDAO
	 *            --统计数据访问接口
	 * @param statDetailDAO
	 *            --统计明细数据访问接口
	 * @param analyzer
	 *            --数据分析器
	 * @param startTime
	 *            --数据分析开始时间
	 */
	public StatTask(int type, int[] items, long analyzeInterval,
			StatDataDAO collectDataDAO, StatDetailDAO statDetailDAO,
			DataAnalyzer analyzer, long startTime) {
		this.type = type;
		this.analyzeInterval = analyzeInterval;
		this.collectDataDAO = collectDataDAO;
		this.statDetailDAO = statDetailDAO;
		this.items = items;
		this.analyzer = analyzer;
		this.setStartTime(startTime);
	}

	/**
	 * 使用默认的统计项
	 * 
	 * @param type
	 * @param analyzeInterval
	 * @param collectDataDAO
	 * @param statDetailDAO
	 */
	public StatTask(int type, long analyzeInterval, StatDataDAO collectDataDAO,
			StatDetailDAO statDetailDAO, DataAnalyzer analyzer) {
		this.type = type;
		this.analyzeInterval = analyzeInterval;
		this.collectDataDAO = collectDataDAO;
		this.statDetailDAO = statDetailDAO;
		this.analyzer = analyzer;
	}

	public void run() {

		StatThread thread = this.createStatThread(type, analyzeInterval,
				collectDataDAO, statDetailDAO, analyzer, this.startTime);
		new Thread(thread).start();
		startTime += this.getAnalyzeInterval();
	}

	/**
	 * 创建数据统计线程
	 * 
	 * @param item
	 *            --统计项
	 * @param type
	 *            --统计类型
	 * @param analyzeInterval
	 *            --数据分析时间间隔
	 * @param collectDataDAO
	 *            --统计数据访问接口
	 * @param statDetailDAO
	 *            --统计明细数据访问接口
	 * @param dataAnalyzer
	 *            --统计数据分析器
	 * @return
	 */
	public StatThread createStatThread(int type, long analyzeInterval,
			StatDataDAO collectDataDAO, StatDetailDAO statDetailDAO,
			DataAnalyzer dataAnalyzer, long startTime) {
		StatThread thread = new StatThread(type, analyzeInterval,
				collectDataDAO, statDetailDAO, dataAnalyzer, startTime);
		this.setStartTime(startTime);
		return thread;
	}

	/**
	 * 取得统计数据分析时间间隔
	 * 
	 * @return
	 */
	public long getAnalyzeInterval() {
		return analyzeInterval;
	}

	/**
	 * 设置统计数据分析时间间隔
	 * 
	 * @param analyzeInterval
	 */
	public void setAnalyzeInterval(long analyzeInterval) {
		this.analyzeInterval = analyzeInterval;
	}

	/**
	 * 取得统计数据访问接口
	 * 
	 * @return
	 */
	public StatDataDAO getCollectDataDAO() {
		return collectDataDAO;
	}

	/**
	 * 设置统计数据访问接口
	 * 
	 * @param collectDataDAO
	 */
	public void setCollectDataDAO(StatDataDAO collectDataDAO) {
		this.collectDataDAO = collectDataDAO;
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

	/**
	 * 取得统计项
	 * 
	 * @return
	 */
	public int[] getItems() {
		return items;
	}

	/**
	 * 设置统计项
	 * 
	 * @param items
	 */
	public void setItems(int[] items) {
		this.items = items;
	}

	/**
	 * 取得统计数据分析器
	 * 
	 * @return
	 */
	public DataAnalyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * 设置统计数据分析器
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(DataAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * 取得统计类型
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置统计类型
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 取得统计开始时间
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 设置统计开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
