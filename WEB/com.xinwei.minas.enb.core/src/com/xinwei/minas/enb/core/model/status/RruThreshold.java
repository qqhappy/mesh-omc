/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * RRU����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class RruThreshold implements Serializable {

	private int rackNo;

	private int shelfNo;

	private int slotNo;
	// "פ�������� ��λ��0.1
	private int vswrThres;
	// �����Ӱ��¶����� ȡ��
	private int boardTempThres;
	// ��Ƶͨ���¶����� ȡ��
	private int rfChannelTempThres;

	public int getRackNo() {
		return rackNo;
	}

	public void setRackNo(int rackNo) {
		this.rackNo = rackNo;
	}

	public int getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(int shelfNo) {
		this.shelfNo = shelfNo;
	}

	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	public int getVswrThres() {
		return vswrThres;
	}

	public void setVswrThres(int vswrThres) {
		this.vswrThres = vswrThres;
	}

	public int getBoardTempThres() {
		return boardTempThres;
	}

	public void setBoardTempThres(int boardTempThres) {
		this.boardTempThres = boardTempThres;
	}

	public int getRfChannelTempThres() {
		return rfChannelTempThres;
	}

	public void setRfChannelTempThres(int rfChannelTempThres) {
		this.rfChannelTempThres = rfChannelTempThres;
	}

}
