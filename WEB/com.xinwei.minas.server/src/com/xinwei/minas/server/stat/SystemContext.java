/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;

/**
 * 
 * ϵͳ���ò���
 * 
 * @author fanhaoyu
 * 
 */

public class SystemContext {

	private static final SystemContext instance = new SystemContext();

	private SystemContext() {
	}

	/**
	 * �ͻ��˽������ݵ�UDP�˿�
	 */
	private int clientUdpPort;

	/**
	 * �������˷������ݵ�UDP�˿�
	 */
	private int serverUdpPort;

	/**
	 * ����ʵʱ���ݵ�����߳���
	 */
	private int maxThreadNum = 5;

	/**
	 * �ͻ�������������ֳ�ʱʱ��
	 */
	private long handShakeTimeOut;

	/**
	 * ������ͳ�Ƽ����Ĭ��2����ͳ��һ��
	 */
	private long dailyStatInterval;

	/**
	 * ������ͳ�Ƽ����Ĭ��30����ͳ��һ��
	 */
	private long weekStatInterval;

	/**
	 * ������ͳ�Ƽ����Ĭ��60����ͳ��һ��
	 */
	private long monthStatInterval;

	/**
	 * ������ͳ�Ƽ����Ĭ��һ��ͳ��һ��
	 */
	private long yearStatInterval;

	/**
	 * ʧ��ͳ����������ͳ�Ƽ����Ĭ��60����ִ��һ��
	 */
	private long failedStatTaskInterval;

	/**
	 * ɾ��ͳ����������ִ�м����Ĭ��60����ִ��һ��
	 */
	private long deleteStatTaskInterval;// Ĭ��60����ִ��һ��

	/**
	 * ʵʱ���ݱ���ʱ�䣬Ĭ�ϱ���һСʱ
	 */
	private long realTimeReserved = 1 * 60 * 60 * 1000L;// ʵʱ���ݱ���ʱ�䣬Ĭ��1Сʱ

	/**
	 * �����ݱ���ʱ�䣬Ĭ�ϱ���һ��
	 */
	private long dailyReserved = 24 * 60 * 60 * 1000L;// �����ݱ���ʱ�䣬Ĭ��1��

	/**
	 * �����ݱ���ʱ�䣬Ĭ�ϱ���8��
	 */
	private long weekReserved = 8 * 24 * 60 * 60 * 1000L;// �����ݱ���ʱ�䣬Ĭ��8��

	/**
	 * �����ݱ���ʱ�䣬Ĭ�ϱ���32��
	 */
	private long monthReserved = 32 * 24 * 60 * 60 * 1000L;// �����ݱ���ʱ�䣬Ĭ��32��

	/**
	 * �����ݱ���ʱ�䣬Ĭ�ϱ���366��
	 */
	private long yearReserved = 366 * 24 * 60 * 60 * 1000L;// �����ݱ���ʱ��Ĭ��366��

	/**
	 * �Ƿ�ʵʱ���ݴ��
	 */
	private String saveRealTimeData = "false";

	public static SystemContext getInstance() {
		return instance;
	}

	/**
	 * ��ȡ�ͻ��˽������ݵ�UDP�˿�
	 * 
	 * @return
	 */
	public int getClientUdpPort() {
		return clientUdpPort;
	}

	/**
	 * ���ÿͻ��˽������ݵ�UDP�˿�
	 * 
	 * @param clientUdpPort
	 */
	public void setClientUdpPort(int clientUdpPort) {
		this.clientUdpPort = clientUdpPort;
	}

	public void setServerUdpPort(int serverUdpPort) {
		this.serverUdpPort = serverUdpPort;
	}

	public int getServerUdpPort() {
		return serverUdpPort;
	}

	public void setMaxThreadNum(int maxThreadNum) {
		this.maxThreadNum = maxThreadNum;
	}

	public int getMaxThreadNum() {
		return maxThreadNum;
	}

	public void setHandShakeTimeOut(long handShakeTimeOut) {
		this.handShakeTimeOut = handShakeTimeOut;
	}

	public long getHandShakeTimeOut() {
		return handShakeTimeOut;
	}

	/**
	 * ȡ��ɾ��ͳ����������ִ�м��
	 * 
	 * @return
	 */
	public long getDeleteStatTaskInterval() {
		return deleteStatTaskInterval;
	}

	/**
	 * ����ɾ��ͳ����������ִ�м��
	 * 
	 * @param deleteStatTaskInterval
	 */
	public void setDeleteStatTaskInterval(long deleteStatTaskInterval) {
		this.deleteStatTaskInterval = deleteStatTaskInterval;
	}

	/**
	 * ȡ��ʧ�ܵ�ͳ����������ִ�м��
	 * 
	 * @return
	 */
	public long getFailedStatTaskInterval() {
		return failedStatTaskInterval;
	}

	/**
	 * ����ʧ��ͳ����������ִ�м��
	 * 
	 * @param failedStatTaskInterval
	 */
	public void setFailedStatTaskInterval(long failedStatTaskInterval) {
		this.failedStatTaskInterval = failedStatTaskInterval;
	}

	public void setDailyStatInterval(long dailyStatInterval) {
		this.dailyStatInterval = dailyStatInterval;
	}

	public long getDailyStatInterval() {
		return dailyStatInterval;
	}

	/**
	 * ȡ����ͳ������ͳ�Ƽ��
	 * 
	 * @return
	 */
	public long getMonthStatInterval() {
		return monthStatInterval;
	}

	/**
	 * ������ͳ������ͳ�Ƽ��
	 * 
	 * @param monthStatInterval
	 */
	public void setMonthStatInterval(long monthStatInterval) {
		this.monthStatInterval = monthStatInterval;
	}

	/**
	 * ȡ��������ͳ�Ƽ��
	 * 
	 * @return
	 */
	public long getWeekStatInterval() {
		return weekStatInterval;
	}

	/**
	 * ����������ͳ�Ƽ��
	 * 
	 * @param weekStatInterval
	 */
	public void setWeekStatInterval(long weekStatInterval) {
		this.weekStatInterval = weekStatInterval;
	}

	/**
	 * ȡ��������ͳ�Ƽ��
	 * 
	 * @return
	 */
	public long getYearStatInterval() {
		return yearStatInterval;
	}

	/**
	 * ����������ͳ�Ƽ��
	 * 
	 * @param yearStatInterval
	 */
	public void setYearStatInterval(long yearStatInterval) {
		this.yearStatInterval = yearStatInterval;
	}

	public void setRealTimeReserved(long realTimeReserved) {
		this.realTimeReserved = realTimeReserved;
	}

	public long getRealTimeReserved() {
		return realTimeReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getDailyReserved() {
		return dailyReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param dailyReserved
	 */
	public void setDailyReserved(long dailyReserved) {
		this.dailyReserved = dailyReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getMonthReserved() {
		return monthReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param monthReserved
	 */
	public void setMonthReserved(long monthReserved) {
		this.monthReserved = monthReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getWeekReserved() {
		return weekReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param weekReserved
	 */
	public void setWeekReserved(long weekReserved) {
		this.weekReserved = weekReserved;
	}

	/**
	 * ȡ�������ݱ���ʱ��
	 * 
	 * @return
	 */
	public long getYearReserved() {
		return yearReserved;
	}

	/**
	 * ���������ݱ���ʱ��
	 * 
	 * @param yearReserved
	 */
	public void setYearReserved(long yearReserved) {
		this.yearReserved = yearReserved;
	}

	public void setSaveRealTimeData(String saveRealTimeData) {
		this.saveRealTimeData = saveRealTimeData;
	}

	public boolean saveRealTimeData() {
		return saveRealTimeData.equals("true");
	}

	public StatDetailDAO getStatDetailDAO() {
		return AppContext.getCtx().getBean(StatDetailDAO.class);
	}

	public StatDataDAO getStatDataDAO() {
		return AppContext.getCtx().getBean(StatDataDAO.class);
	}

}
