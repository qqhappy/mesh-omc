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
 * ͳ�����ݷ��������߳�
 * 
 * @author fanhaoyu
 * 
 */

public class StatThread implements Runnable {

	/**
	 * ��־��¼���
	 */
	private Log log = LogFactory.getLog(StatThread.class);

	/**
	 * ͳ������
	 */
	private int timeType;

	/**
	 * ͳ�����ݷ��ʽӿ�
	 */
	private StatDataDAO statDataDAO;

	/**
	 * ͳ����ϸ���ݷ��ʽӿ�
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * ͳ�����ݷ���ʱ��������λΪ��
	 */
	private long analyzeInterval;

	/**
	 * ͳ�ƿ�ʼʱ��
	 */
	private long startTime;

	/**
	 * ͳ�����ݷ�����
	 */
	private DataAnalyzer analyzer;

	/**
	 * ����ͳ���̹߳��캯��
	 * 
	 * @param type
	 *            ͳ������
	 * @param interval
	 *            ͳ��ʱ����
	 * @param statDataDAO
	 *            ͳ�����ݷ��ʽӿ�
	 * @param statDetailDAO
	 *            ͳ����ϸ���ݷ��ʽӿ�
	 * @param analyzer
	 *            ͳ�����ݷ�����
	 * @param applicationConnector
	 *            �ͻ���������
	 * @param startTime
	 *            ͳ�ƿ�ʼʱ��
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
	 * ��������ͳ��
	 * 
	 * @throws Exception
	 */
	public void stat() throws Exception {
		/**
		 * 1����ѯ��Сʱ�ڵ��������� 2������btsid�����ݷ�Ϊ��������б� 3���Ը����б���з���
		 */
		long endTime = (startTime + getAnalyzeInterval()) * 1000;// ͳ�ƽ�ֹʱ��Ϊ��ǰʱ�䣬����

		log.debug("StatThread start. timeType=" + timeType + ", startTime="
				+ new Date(startTime).toString() + ", endTime="
				+ new Date(endTime).toString());

		// ���챾��ͳ����ϸ��Ϣ���󲢴洢
		StatDetail detail = new StatDetail();
		detail.setTimeType(timeType);
		detail.setTime(endTime);
		detail.setFlag(0);// Ĭ������Ϊʧ��
		detail.setInterval(getAnalyzeInterval());// ����Ϊ����ͳ�������ͳ�Ʒ������
		getStatDetailDAO().saveOrUpdate(detail);// �ȴ洢����ͳ����ϸ

		int subtype = StatUtil.getSubType(timeType);

		// ��ѯ��������Ҫͳ�Ƶ�����
		List<StatData> statData = this.getStatDataDAO().getData(
				startTime * 1000, endTime, subtype);// ���ϼ�ͳ�����͵����ݱ���ȡ���ݣ����ܴ������ݱ���ȡ���´������ݱ���ȡ

		if (statData != null && statData.size() > 0) {
			// ���������ݰ���btsId��Ϊ����б�
			List<List<StatData>> group = groupStatDataByBtsID(statData);
			// ��������bts������
			for (List<StatData> groupList : group) {

				List<StatData> target = getAnalyzer().analyze(groupList,
						timeType, getAnalyzeInterval(), startTime * 1000);
				if (target != null && target.size() > 0) {// �������ͳ���������
					for (StatData data : target) {
						for (SingleStatItemData itemData : data.getItemDatas()) {

							// ��������ݵ�ͳ�������ڱ�����
							if (MonitorManager.getInstance().isMonitoring(
									new MonitorItem(itemData.getBtsId(),
											itemData.getItemId()))) {
								// �������ϱ�����ϵͳ
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
			// ͳ�ƽ����󽫱���ͳ�ƽ������Ϊ�ɹ�����������ݣ�����Ϊͳ��ʧ��
			detail.setFlag(1);
			getStatDetailDAO().saveOrUpdate(detail);// ���±���ͳ����ϸ
		}

	}

	/**
	 * ���ݻ�վ��Ž����ݷ���
	 * 
	 * @param collectedData
	 * @return
	 */
	private List<List<StatData>> groupStatDataByBtsID(
			List<StatData> collectedData) {
		List<List<StatData>> result = new ArrayList<List<StatData>>();// ���������б�
		Hashtable<Long, List<StatData>> table = new Hashtable<Long, List<StatData>>();// �ݴ�����ʹ�õĹ�ϣ��
		for (StatData data : collectedData) {
			long btsId = data.getBtsId();
			List<StatData> list = table.get(btsId);// �ڹ�ϣ���в��ҵ�ǰBTSID���޶�Ӧ����б�
			if (list == null) {// �������BTSID�޶�Ӧ�б��򴴽�
				list = new ArrayList<StatData>();
			}
			list.add(data);
			table.put(btsId, list);
		}
		if (table != null && table.size() > 0) {// �����ϣ���գ��򽫹�ϣ���н���������б�
			result.addAll(table.values());
		}
		return result;
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
	 * @param collectDataDAO
	 */
	public void setStatDataDAO(StatDataDAO collectDataDAO) {
		this.statDataDAO = collectDataDAO;
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

	/**
	 * ȡ��ͳ������
	 * 
	 * @return
	 */
	public int getTimeType() {
		return timeType;
	}

	/**
	 * ����ͳ������
	 * 
	 * @param type
	 */
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	/**
	 * ȡ��ͳ�����ݷ���ʱ����
	 * 
	 * @return
	 */
	public long getAnalyzeInterval() {
		return analyzeInterval;
	}

	/**
	 * ����ͳ�����ݷ���ʱ����
	 * 
	 * @param analyzeInterval
	 */
	public void setAnalyzeInterval(long analyzeInterval) {
		this.analyzeInterval = analyzeInterval;
	}

	/**
	 * ȡ��ͳ�����ݷ�����
	 * 
	 * @return
	 */
	public DataAnalyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * ����ͳ�����ݷ�����
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(DataAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * ȡ��ͳ�ƿ�ʼʱ��
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * ����ͳ�ƿ�ʼʱ��
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
