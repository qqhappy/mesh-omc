/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.io.Serializable;

import com.xinwei.minas.stat.core.StatUtil;

/**
 * 
 * 监视的项目
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class MonitorItem implements Serializable {

	private long btsId;

	private int itemId;

	public MonitorItem() {
	}

	public MonitorItem(long btsId, int itemId) {
		this.btsId = btsId;
		this.itemId = itemId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (btsId ^ (btsId >>> 32));
		result = prime * result + itemId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonitorItem other = (MonitorItem) obj;
		if (btsId != other.btsId)
			return false;
		if (itemId != other.itemId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MonitorItem [btsId=" + btsId + ", itemId=" + itemId + "]";
	}

	public String getKey() {
		return this.btsId + StatUtil.DEFAULT_SPLIT_CHAR + this.itemId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public long getBtsId() {
		return btsId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemId() {
		return itemId;
	}

}
