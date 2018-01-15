/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.stat.DailyDataManager;
import com.xinwei.minas.server.stat.MonitorManager;
import com.xinwei.minas.server.stat.OriginalDataManager;
import com.xinwei.minas.server.stat.SystemContext;
import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.server.stat.service.RealTimePerfMonitorService;
import com.xinwei.minas.stat.core.DateFormater;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;
import com.xinwei.minas.stat.core.model.TargetAddress;

/**
 * 
 * 实时性能监控服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class RealTimePerfMonitorServiceImpl implements
		RealTimePerfMonitorService {

	private Log log = LogFactory.getLog(RealTimePerfMonitorServiceImpl.class);

	private StatDataDAO statDataDAO;

	@Override
	public void start(String sessionId, MonitorItem item) throws Exception {
		log.info("Start monitoring client. sessionId = " + sessionId
				+ ", MonitorItem=" + item.getKey());
		MonitorManager.getInstance().addMonitor(item, sessionId);
		MonitorManager.getInstance().handshake(sessionId);
		// 向客户端发送数据
		new SendDataThread(sessionId, item).start();
	}

	@Override
	public void stop(String sessionId, MonitorItem item) throws Exception {
		log.info("Stop monitoring client. sessionId = " + sessionId
				+ ", MonitorItem=" + item.getKey());
		MonitorManager.getInstance().removeMonitor(item, sessionId);
	}

	@Override
	public void handshake(String sessionId, MonitorItem item) throws Exception {
		MonitorManager.getInstance().addMonitor(item, sessionId);
		MonitorManager.getInstance().handshake(sessionId);
	}

	class SendDataThread extends Thread {

		private String sessionId;

		private MonitorItem item;

		public SendDataThread(String sessionId, MonitorItem item) {
			this.sessionId = sessionId;
			this.item = item;
		}

		@Override
		public void run() {
			// 获取要发送的实时数据
			List<SingleStatItemData> realtimeDataList = OriginalDataManager
					.getInstance().getDataOfRecentOneHour(item.getBtsId(),
							item.getItemId());
			// 获取要发送的日数据
			List<SingleStatItemData> dailyDataList = DailyDataManager
					.getInstance().getDataOfRecentOneDay(item.getBtsId(),
							item.getItemId());
			long currentTime = System.currentTimeMillis();
			// 获取要发送的周数据
			List<SingleStatItemData> weekDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_WEEK);
			// 获取要发送的月数据
			List<SingleStatItemData> monthDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_MONTH);
			// 获取要发送的年数据
			List<SingleStatItemData> yearDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_YEAR);
			// 获取目的客户端
			List<TargetAddress> targetAddressList = new ArrayList<TargetAddress>();
			LoginUser loginUser = LoginUserCache.getInstance().queryOnlineUser(
					sessionId, false);
			try {
				targetAddressList.add(new TargetAddress(loginUser.getLoginIp(),
						SystemContext.getInstance().getClientUdpPort()));
			} catch (UnknownHostException e) {
				log.error("Unknown targetAddress. sessionId=" + sessionId);
			}
			ClientNotifier clientNotifier = ClientNotifier.getInstance();
			// 发送实时数据
			for (SingleStatItemData itemData : realtimeDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send realtime data error. "
									+ itemData.toString(), e);
				}
			}
			// 发送日数据
			for (SingleStatItemData itemData : dailyDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send daily data error. "
									+ itemData.toString(), e);
				}
			}
			// 发送周数据
			for (SingleStatItemData itemData : weekDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send week data error. "
									+ itemData.toString(), e);
				}
			}
			// 发送月数据
			for (SingleStatItemData itemData : monthDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send month data error. "
									+ itemData.toString(), e);
				}
			}
			// 发送年数据
			for (SingleStatItemData itemData : yearDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send year data error. "
									+ itemData.toString(), e);
				}
			}
		}

		/**
		 * 获取要发送的数据
		 * 
		 * @param currentTime
		 * @param timeType
		 * @return
		 */
		private List<SingleStatItemData> getDataToSend(long currentTime,
				int timeType) {

			// 获取要发送的周数据
			List<SingleStatItemData> dataList = new ArrayList<SingleStatItemData>();
			long beginTime = getBeginTime(currentTime, timeType);
			try {
				List<StatData> statDatas = statDataDAO.getData(item.getBtsId(),
						beginTime, currentTime, timeType);
				if (statDatas != null && !statDatas.isEmpty()) {
					for (StatData statData : statDatas) {
						SingleStatItemData itemData = statData.getItemData(item
								.getItemId());
						if (itemData != null) {
							dataList.add(itemData);
						}
					}
				}
			} catch (Exception e) {
				log.error("query stat data error. timeType=" + timeType, e);
			}
			return dataList;
		}

		private long getBeginTime(long endTime, int timeType) {
			long beginTime = endTime;
			switch (timeType) {
			case StatUtil.COLLECT_TYPE_WEEK:
				beginTime = DateFormater.getLastWeekDay(endTime);
				break;
			case StatUtil.COLLECT_TYPE_MONTH:
				beginTime = DateFormater.getLastMonthDay(endTime);
				break;
			case StatUtil.COLLECT_TYPE_YEAR:
				beginTime = DateFormater.getLastYear(endTime);
				break;
			default:
				break;
			}
			return beginTime;
		}
	}

	public void setStatDataDAO(StatDataDAO statDataDAO) {
		this.statDataDAO = statDataDAO;
	}
}
