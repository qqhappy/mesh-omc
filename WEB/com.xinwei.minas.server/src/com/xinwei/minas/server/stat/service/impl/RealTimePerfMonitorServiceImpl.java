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
 * ʵʱ���ܼ�ط���ӿ�ʵ��
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
		// ��ͻ��˷�������
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
			// ��ȡҪ���͵�ʵʱ����
			List<SingleStatItemData> realtimeDataList = OriginalDataManager
					.getInstance().getDataOfRecentOneHour(item.getBtsId(),
							item.getItemId());
			// ��ȡҪ���͵�������
			List<SingleStatItemData> dailyDataList = DailyDataManager
					.getInstance().getDataOfRecentOneDay(item.getBtsId(),
							item.getItemId());
			long currentTime = System.currentTimeMillis();
			// ��ȡҪ���͵�������
			List<SingleStatItemData> weekDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_WEEK);
			// ��ȡҪ���͵�������
			List<SingleStatItemData> monthDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_MONTH);
			// ��ȡҪ���͵�������
			List<SingleStatItemData> yearDataList = getDataToSend(currentTime,
					StatUtil.COLLECT_TYPE_YEAR);
			// ��ȡĿ�Ŀͻ���
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
			// ����ʵʱ����
			for (SingleStatItemData itemData : realtimeDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send realtime data error. "
									+ itemData.toString(), e);
				}
			}
			// ����������
			for (SingleStatItemData itemData : dailyDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send daily data error. "
									+ itemData.toString(), e);
				}
			}
			// ����������
			for (SingleStatItemData itemData : weekDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send week data error. "
									+ itemData.toString(), e);
				}
			}
			// ����������
			for (SingleStatItemData itemData : monthDataList) {
				try {
					clientNotifier.sendData(itemData, targetAddressList);
				} catch (Exception e) {
					log.error(
							"RealTimePerfMonitorService send month data error. "
									+ itemData.toString(), e);
				}
			}
			// ����������
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
		 * ��ȡҪ���͵�����
		 * 
		 * @param currentTime
		 * @param timeType
		 * @return
		 */
		private List<SingleStatItemData> getDataToSend(long currentTime,
				int timeType) {

			// ��ȡҪ���͵�������
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
