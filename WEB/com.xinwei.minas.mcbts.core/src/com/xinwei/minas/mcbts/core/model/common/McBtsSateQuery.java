/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

/**
 * 
 * bts状态查询实体类
 * 
 * @author fangping
 * 
 */

public class McBtsSateQuery implements Serializable {

	private Long idx;

	private Long moId;

	// 当前用户数
	private int currentUserNumber;
	// 可见的卫星个数
	private int satellitesVisible;
	// 跟踪的卫星个数
	private int statellitesTrack;
	// 启动源
	private int bootsource;
	// Bit（使用的射频情况）
	private int rfMask;
	// 当前频踪板温度
	private int syncCardTemperature;
	// 当前主用板温度（暂时保留）
	private int digitalBoardTemperature;
	// 基站运行时间，单位：秒
	private int timePassedSinceBtsBoot;
	// 平均空闲的下行信道数
	private int averageFreeDLChannel;
	// 平均空闲的上行信道数
	private int averageFreeULChannel;
	// 平均空闲的功率
	private int averageFreePowerUsage;

	public int getAverageFreeDLChannel() {
		return averageFreeDLChannel;
	}

	public int getAverageFreePowerUsage() {
		return averageFreePowerUsage;
	}

	public int getAverageFreeULChannel() {
		return averageFreeULChannel;
	}

	public int getCurrentUserNumber() {
		return currentUserNumber;
	}

	public Long getIdx() {
		return idx;
	}

	public Long getMoId() {
		return moId;
	}

	public int getRfMask() {
		return rfMask;
	}

	public int getSatellitesVisible() {
		return satellitesVisible;
	}

	public int getStatellitesTrack() {
		return statellitesTrack;
	}

	public int getSyncCardTemperature() {
		return syncCardTemperature;
	}

	public int getTimePassedSinceBtsBoot() {
		return timePassedSinceBtsBoot;
	}

	public void setAverageFreeDLChannel(int averageFreeDLChannel) {
		this.averageFreeDLChannel = averageFreeDLChannel;
	}

	public void setAverageFreePowerUsage(int averageFreePowerUsage) {
		this.averageFreePowerUsage = averageFreePowerUsage;
	}

	public void setAverageFreeULChannel(int averageFreeULChannel) {
		this.averageFreeULChannel = averageFreeULChannel;
	}

	public void setCurrentUserNumber(int currentUserNumber) {
		this.currentUserNumber = currentUserNumber;
	}

	public int getBootsource() {
		return bootsource;
	}

	public void setBootsource(int bootsource) {
		this.bootsource = bootsource;
	}

	public int getDigitalBoardTemperature() {
		return digitalBoardTemperature;
	}

	public void setDigitalBoardTemperature(int digitalBoardTemperature) {
		this.digitalBoardTemperature = digitalBoardTemperature;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public void setRfMask(int rfMask) {
		this.rfMask = rfMask;
	}

	public void setSatellitesVisible(int satellitesVisible) {
		this.satellitesVisible = satellitesVisible;
	}

	public void setStatellitesTrack(int statellitesTrack) {
		this.statellitesTrack = statellitesTrack;
	}

	public void setSyncCardTemperature(int syncCardTemperature) {
		this.syncCardTemperature = syncCardTemperature;
	}

	public void setTimePassedSinceBtsBoot(int timePassedSinceBtsBoot) {
		this.timePassedSinceBtsBoot = timePassedSinceBtsBoot;
	}
}
