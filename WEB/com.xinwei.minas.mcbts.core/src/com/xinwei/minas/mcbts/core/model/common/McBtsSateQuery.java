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
 * bts״̬��ѯʵ����
 * 
 * @author fangping
 * 
 */

public class McBtsSateQuery implements Serializable {

	private Long idx;

	private Long moId;

	// ��ǰ�û���
	private int currentUserNumber;
	// �ɼ������Ǹ���
	private int satellitesVisible;
	// ���ٵ����Ǹ���
	private int statellitesTrack;
	// ����Դ
	private int bootsource;
	// Bit��ʹ�õ���Ƶ�����
	private int rfMask;
	// ��ǰƵ�ٰ��¶�
	private int syncCardTemperature;
	// ��ǰ���ð��¶ȣ���ʱ������
	private int digitalBoardTemperature;
	// ��վ����ʱ�䣬��λ����
	private int timePassedSinceBtsBoot;
	// ƽ�����е������ŵ���
	private int averageFreeDLChannel;
	// ƽ�����е������ŵ���
	private int averageFreeULChannel;
	// ƽ�����еĹ���
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
