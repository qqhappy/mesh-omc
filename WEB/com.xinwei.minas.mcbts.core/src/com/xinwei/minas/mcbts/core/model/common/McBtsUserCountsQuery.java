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
 * bts用户数查询实体类
 * 
 * @author fangping
 * 
 */

public class McBtsUserCountsQuery implements Serializable {

	private Long idx;

	private Long moId;

	// 注册用户数
	private int registeredUserNum;
	// 当前活动用户数
	private int activeUserNum;

	public int getActiveUserNum() {
		return activeUserNum;
	}

	public Long getIdx() {
		return idx;
	}

	public Long getMoId() {
		return moId;
	}

	public int getRegisteredUserNum() {
		return registeredUserNum;
	}

	public void setActiveUserNum(int activeUserNum) {
		this.activeUserNum = activeUserNum;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public void setRegisteredUserNum(int registeredUserNum) {
		this.registeredUserNum = registeredUserNum;
	}

}
