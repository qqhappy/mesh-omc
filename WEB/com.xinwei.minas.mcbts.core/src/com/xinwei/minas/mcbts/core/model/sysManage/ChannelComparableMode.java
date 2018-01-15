/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-27	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * ��վ����ģʽ����ʵ��
 * 
 * @author jiayi
 * 
 */

public class ChannelComparableMode implements Serializable {

	// ��֧�ֹ����ŵ�����
	public static final int PCCH_ONLY = 0;

	// ��֧����ǿ�����ŵ�����
	public static final int PECCH_ONLY = 1;

	// ͬʱ֧�ֹ����ŵ����ú���ǿ�����ŵ�����
	public static final int PCCH_AND_PECCH = 2;

	// ��վ����ģʽ
	private int channelMode;
	
	// �Ƿ���Ҫ�·�
	private boolean needConfig = true;

	// ������
	private int reserve = 0;

	/**
	 * @return the channelMode
	 */
	public int getChannelMode() {
		return channelMode;
	}

	/**
	 * @param channelMode the channelMode to set
	 */
	public void setChannelMode(int channelMode) {
		this.channelMode = channelMode;
	}

	/**
	 * @return the needConfig
	 */
	public boolean isNeedConfig() {
		return needConfig;
	}

	/**
	 * @param needConfig the needConfig to set
	 */
	public void setNeedConfig(boolean needConfig) {
		this.needConfig = needConfig;
	}

	/**
	 * @return the reserve
	 */
	public int getReserve() {
		return reserve;
	}

	/**
	 * @param reserve the reserve to set
	 */
	public void setReserve(int reserve) {
		this.reserve = reserve;
	}
}
