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

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * 
 * ΢վ����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class IbtsThreshold implements EnbDynamicInfo {


	// פ�������� ��λ��0.1
	private int vswrThres;
	// PAU���¶�����  ȡ��
	private int boardTempThres;
	// ��Ƶͨ���¶����� ȡ��
	private int rfChannelTempThres;



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
