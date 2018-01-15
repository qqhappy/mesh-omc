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
 * 基站兼容模式配置实体
 * 
 * @author jiayi
 * 
 */

public class ChannelComparableMode implements Serializable {

	// 仅支持公共信道配置
	public static final int PCCH_ONLY = 0;

	// 仅支持增强公共信道配置
	public static final int PECCH_ONLY = 1;

	// 同时支持公共信道配置和增强公共信道配置
	public static final int PCCH_AND_PECCH = 2;

	// 基站兼容模式
	private int channelMode;
	
	// 是否需要下发
	private boolean needConfig = true;

	// 保留字
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
