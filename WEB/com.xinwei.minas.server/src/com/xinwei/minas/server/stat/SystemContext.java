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
 * 系统配置参数
 * 
 * @author fanhaoyu
 * 
 */

public class SystemContext {

	private static final SystemContext instance = new SystemContext();

	private SystemContext() {
	}

	/**
	 * 客户端接收数据的UDP端口
	 */
	private int clientUdpPort;

	/**
	 * 服务器端发送数据的UDP端口
	 */
	private int serverUdpPort;

	/**
	 * 处理实时数据的最大线程数
	 */
	private int maxThreadNum = 5;

	/**
	 * 客户端与服务器握手超时时间
	 */
	private long handShakeTimeOut;

	/**
	 * 日数据统计间隔，默认2分钟统计一次
	 */
	private long dailyStatInterval;

	/**
	 * 周数据统计间隔，默认30分钟统计一次
	 */
	private long weekStatInterval;

	/**
	 * 月数据统计间隔，默认60分钟统计一次
	 */
	private long monthStatInterval;

	/**
	 * 年数据统计间隔，默认一天统计一次
	 */
	private long yearStatInterval;

	/**
	 * 失败统计任务重新统计间隔，默认60分钟执行一次
	 */
	private long failedStatTaskInterval;

	/**
	 * 删除统计数据任务执行间隔，默认60分钟执行一次
	 */
	private long deleteStatTaskInterval;// 默认60分钟执行一次

	/**
	 * 实时数据保存时间，默认保存一小时
	 */
	private long realTimeReserved = 1 * 60 * 60 * 1000L;// 实时数据保存时间，默认1小时

	/**
	 * 日数据保存时间，默认保存一天
	 */
	private long dailyReserved = 24 * 60 * 60 * 1000L;// 日数据保存时间，默认1天

	/**
	 * 周数据保存时间，默认保存8天
	 */
	private long weekReserved = 8 * 24 * 60 * 60 * 1000L;// 周数据保存时间，默认8天

	/**
	 * 月数据保存时间，默认保存32天
	 */
	private long monthReserved = 32 * 24 * 60 * 60 * 1000L;// 月数据保存时间，默认32天

	/**
	 * 年数据保存时间，默认保存366天
	 */
	private long yearReserved = 366 * 24 * 60 * 60 * 1000L;// 年数据保存时间默认366天

	/**
	 * 是否将实时数据存库
	 */
	private String saveRealTimeData = "false";

	public static SystemContext getInstance() {
		return instance;
	}

	/**
	 * 获取客户端接收数据的UDP端口
	 * 
	 * @return
	 */
	public int getClientUdpPort() {
		return clientUdpPort;
	}

	/**
	 * 设置客户端接收数据的UDP端口
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
	 * 取得删除统计数据任务执行间隔
	 * 
	 * @return
	 */
	public long getDeleteStatTaskInterval() {
		return deleteStatTaskInterval;
	}

	/**
	 * 设置删除统计数据任务执行间隔
	 * 
	 * @param deleteStatTaskInterval
	 */
	public void setDeleteStatTaskInterval(long deleteStatTaskInterval) {
		this.deleteStatTaskInterval = deleteStatTaskInterval;
	}

	/**
	 * 取得失败的统计数据任务执行间隔
	 * 
	 * @return
	 */
	public long getFailedStatTaskInterval() {
		return failedStatTaskInterval;
	}

	/**
	 * 设置失败统计数据任务执行间隔
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
	 * 取得月统计数据统计间隔
	 * 
	 * @return
	 */
	public long getMonthStatInterval() {
		return monthStatInterval;
	}

	/**
	 * 设置月统计数据统计间隔
	 * 
	 * @param monthStatInterval
	 */
	public void setMonthStatInterval(long monthStatInterval) {
		this.monthStatInterval = monthStatInterval;
	}

	/**
	 * 取得周数据统计间隔
	 * 
	 * @return
	 */
	public long getWeekStatInterval() {
		return weekStatInterval;
	}

	/**
	 * 设置周数据统计间隔
	 * 
	 * @param weekStatInterval
	 */
	public void setWeekStatInterval(long weekStatInterval) {
		this.weekStatInterval = weekStatInterval;
	}

	/**
	 * 取得年数据统计间隔
	 * 
	 * @return
	 */
	public long getYearStatInterval() {
		return yearStatInterval;
	}

	/**
	 * 设置年数据统计间隔
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
	 * 取得日数据保存时间
	 * 
	 * @return
	 */
	public long getDailyReserved() {
		return dailyReserved;
	}

	/**
	 * 设置日数据保存时间
	 * 
	 * @param dailyReserved
	 */
	public void setDailyReserved(long dailyReserved) {
		this.dailyReserved = dailyReserved;
	}

	/**
	 * 取得月数据保存时间
	 * 
	 * @return
	 */
	public long getMonthReserved() {
		return monthReserved;
	}

	/**
	 * 设置月数据保存时间
	 * 
	 * @param monthReserved
	 */
	public void setMonthReserved(long monthReserved) {
		this.monthReserved = monthReserved;
	}

	/**
	 * 取得周数据保存时间
	 * 
	 * @return
	 */
	public long getWeekReserved() {
		return weekReserved;
	}

	/**
	 * 设置周数据保存时间
	 * 
	 * @param weekReserved
	 */
	public void setWeekReserved(long weekReserved) {
		this.weekReserved = weekReserved;
	}

	/**
	 * 取得年数据保存时间
	 * 
	 * @return
	 */
	public long getYearReserved() {
		return yearReserved;
	}

	/**
	 * 设置年数据保存时间
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
