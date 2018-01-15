/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;
import java.util.Date;

/**
 * 邻接表配置失败的实体类
 * @author zhuxiaozhan
 *
 */
public class McBtsNeighborFailed implements Serializable{
	
	//索引值
	private long idx;
	
	//基站moId
	private long moId;
	
	//更新时间
	private Date updateTime;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
}
