/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;

/**
 * 天线闭锁配置实体类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsAntennaLock implements Serializable {

	// 关闭天线闭锁
	public static final int CLOSE = 0;
	
	// 开启天线闭锁
	public static final int OPEN = 1;
	
	// 记录索引
	private Long idx;

	// MO编号
	private long moId;

	// 闭锁开关
	private int flag;
	
	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}