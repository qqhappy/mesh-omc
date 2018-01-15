/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * 切换优化开关配置实体
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SwitchOptimizeConfig implements Serializable {

	/**
	 * 不启用
	 */
	public static final int CLOSE = 0;

	/**
	 * 启用
	 */
	public static final int OPEN = 1;

	private Long idx;

	private Integer switchFlag;
	
	// 是否需要下发
	private boolean needConfig = true;

	private Integer reserve = 0;

	public Integer getSwitchFlag() {
		return switchFlag;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getIdx() {
		return idx;
	}

	public void setSwitchFlag(Integer switchFlag) {
		this.switchFlag = switchFlag;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public void setNeedConfig(boolean needConfig) {
		this.needConfig = needConfig;
	}

	public boolean isNeedConfig() {
		return needConfig;
	}

}
