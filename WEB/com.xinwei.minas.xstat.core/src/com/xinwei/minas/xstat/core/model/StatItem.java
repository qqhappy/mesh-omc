/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.model;

import java.io.Serializable;

/**
 * 
 * 通用统计项数据模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class StatItem implements Serializable {

	private long moId;

	private String entityType;

	private String entityOid;

	private String itemType;

	private int itemId;

	private long startTime;

	private long endTime;

	private double statValue;

	private int version;

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityOid(String entityOid) {
		this.entityOid = entityOid;
	}

	public String getEntityOid() {
		return entityOid;
	}

	public void setStatValue(double statValue) {
		this.statValue = statValue;
	}

	public double getStatValue() {
		return statValue;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemId() {
		return this.itemId;
	}

}
