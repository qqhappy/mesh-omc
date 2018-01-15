/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.MonitorManager;
import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;
import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * 
 * 统计数据分析处理线程
 * 
 * @author fanhaoyu
 * 
 */

public class StatThread implements Runnable {

	/**
	 * 日志记录句柄
	 */
	private Log log = LogFactory.getLog(StatThread.class);

	/**
	 * 统计类型
	 */
	private int timeType;

	/**
	 * 统计数据访问接口
	 */
	private StatDataDAO statDataDAO;

	/**
	 * 统计明细数据访问接口
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * 统计数据分析时间间隔，单位为秒
	 */
	private long analyzeInterval;

	/**
	 * 统计开始时间
	 */
	private long startTime;

	/**
	 * 统计数据分析器
	 */
	private DataAnalyzer analyzer;

	/**
	 * 数据统计线程构造函数
	 * 
	 * @param type
	 *            统计类型
	 * @param interval
	 *            统计时间间隔
	 * @param statDataDAO
	 *            统计数据访问接口
	 * @param statDetailDAO
	 *            统计明细数据访问接口
	 * @param analyzer
	 *            统计数据分析器
	 * @param applicationConnector
	 *            客户端连接器
	 * @param startTime
	 *            统计开始时间
	 */
	public StatThread(int type, long interval, StatDataDAO statDataDAO,
			StatDetailDAO statDetailDAO, DataAnalyzer analyzer, long startTime) {
		this.timeType = type;
		this.statDataDAO = statDataDAO;
		this.statDetailDAO = statDetailDAO;
		this.analyzeInterval = interval;
		this.analyzer = analyzer;
		this.setStartTime(startTime);
	}

	public void run() {
		try {
			stat();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Stat data error,", e);
		}
	}

	/**
	 * 进行数据统计
	 * 
	 * @throws Exception
	 */
	public void stat() throws Exception {
		/**
		 * 1：查询半小时内的所有数据 2：按照btsid将数据分为多个数据列表 3：对各个列表进行分析
		 */
		long endTime = (startTime + getAnalyzeInterval()) * 1000;// 统计截止时间为当前时间，毫秒

		log.debug("StatThread start. timeType=" + timeType + ", startTime="
				+ new Date(startTime).toString() + ", endTime="
				+ new Date(endTime).toString());

		// 构造本次统计明细信息对象并存储
		StatDetail detail = new StatDetail();
		detail.setTimeType(timeType);
		detail.setTime(endTime);
		detail.setFlag(0);// 默认设置为失败
		detail.setInterval(getAnalyzeInterval());// 设置为本次统计任务的统计分析间隔
		getStatDetailDAO().saveOrUpdate(detail);// 先存储本次统计明细

		int subtype = StatUtil.getSubType(timeType);

		// 查询出所有需要统计的数据
		List<StatData> statData = this.getStatDataDAO().getData(
				startTime * 1000, endTime, subtype);// 从上级统计类型的数据表中取数据，如周从日数据表中取，月从周数据表中取

		if (statData != null && statData.size() > 0) {
			// 将所有数据按照btsId分为多个列表
			List<List<StatData>> group = groupStatDataByBtsID(statData);
			// 分析各个bts的数据
			for (List<StatData> groupList : group) {

				List<StatData> target = getAnalyzer().analyze(groupList,
						timeType, getAnalyzeInterval(), startTime * 1000);
				if (target != null && target.size() > 0) {// 分析后的统计数据入库
					for (StatData data : target) {
						for (SingleStatItemData itemData : data.getItemDatas()) {

							// 如果该数据的统计项正在被监视
							if (MonitorManager.getInstance().isMonitoring(
									new MonitorItem(itemData.getBtsId(),
											itemData.getItemId()))) {
								// 将数据上报分析系统
								try {
									log.debug("Finish stat. Send data to client."
											+ itemData.toString());
									ClientNotifier.getInstance().sendData(
											itemData);
								} catch (Exception e) {
									log.error(
											"StatThread send StatData to client error.",
											e);
								}
							}
						}
						getStatDataDAO().saveData(data);
					}
				}
			}
			// 统计结束后将本次统计结果设置为成功，如果无数据，则认为统计失败
			detail.setFlag(1);
			getStatDetailDAO().saveOrUpdate(detail);// 更新本次统计明细
		}

	}

	/**
	 * 根据基站编号将数据分组
	 * 
	 * @param collectedData
	 * @return
	 */
	private List<List<StatData>> groupStatDataByBtsID(
			List<StatData> collectedData) {
		List<List<StatData>> result = new ArrayList<List<StatData>>();// 声明返回列表
		Hashtable<Long, List<StatData>> table = new Hashtable<Long, List<StatData>>();// 暂存数据使用的哈希表
		for (StatData data : collectedData) {
			long btsId = data.getBtsId();
			List<StatData> list = table.get(btsId);// 在哈希表中查找当前BTSID有无对应存放列表
			if (list == null) {// 如果当期BTSID无对应列表，则创建
				list = new ArrayList<StatData>();
			}
			list.add(data);
			table.put(btsId, list);
		}
		if (table != null && table.size() > 0) {// 如果哈希表不空，则将哈希表中结果放入结果列表
			result.addAll(table.values());
		}
		return result;
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
	 * @param collectDataDAO
	 */
	public void setStatDataDAO(StatDataDAO collectDataDAO) {
		this.statDataDAO = collectDataDAO;
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
	 * 取得统计类型
	 * 
	 * @return
	 */
	public int getTimeType() {
		return timeType;
	}

	/**
	 * 设置统计类型
	 * 
	 * @param type
	 */
	public void setTimeType(int timeType) {
		this.timeType = timeType;
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
