/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.model;

import java.io.Serializable;

/**
 * 
 * Ô¤Í³¼ÆÏî
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class PreStatItem implements Serializable {

	private long moId;

	private String entityType;

	private String entityOid;

	private String itemType;

	private int itemId;

	private long statTime;

	private double statValue;

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public long getStatTime() {
		return statTime;
	}

	public void setStatTime(long startTime) {
		this.statTime = startTime;
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
