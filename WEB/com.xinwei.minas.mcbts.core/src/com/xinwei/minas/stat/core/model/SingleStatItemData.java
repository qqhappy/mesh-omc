/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 单个统计项的数据
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SingleStatItemData extends AbstractData implements
		Copyable<SingleStatItemData> {

	private long btsId;

	private long collectTime;

	private long interval;

	private int timeType;

	private int itemId;

	private double value;

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		byte[] buf = new byte[22];
		int offset = 0;
		buf[offset++] = (byte) ((btsId >> 24) & 0xff);
		buf[offset++] = (byte) ((btsId >> 16) & 0xff);
		buf[offset++] = (byte) ((btsId >> 8) & 0xff);
		buf[offset++] = (byte) (btsId & 0xff);
		buf[offset++] = (byte) timeType;
		buf[offset++] = (byte) itemId;
		buf[offset++] = (byte) ((interval >> 24) & 0xff);
		buf[offset++] = (byte) ((interval >> 16) & 0xff);
		buf[offset++] = (byte) ((interval >> 8) & 0xff);
		buf[offset++] = (byte) (interval & 0xff);
		ByteUtils.putFloat(buf, offset, (float) value);
		offset += 4;
		ByteUtils.putLong(buf, offset, collectTime);
		return buf;
	}

	public void parse(byte[] buf) {
		int offset = 0;
		btsId = 0;
		btsId += (long) (buf[offset++] & 0xff) << 24;
		btsId += (long) (buf[offset++] & 0xff) << 16;
		btsId += (long) (buf[offset++] & 0xff) << 8;
		btsId += (long) (buf[offset++] & 0xff);

		timeType = (buf[offset++] & 0xff);
		itemId = (buf[offset++] & 0xff);

		interval = 0;
		interval += (long) (buf[offset++] & 0xff) << 24;
		interval += (long) (buf[offset++] & 0xff) << 16;
		interval += (long) (buf[offset++] & 0xff) << 8;
		interval += (long) (buf[offset++] & 0xff);
		value = ByteUtils.toDouble(buf, offset, 4);
		offset += 4;
		collectTime = ByteUtils.toLong(buf, offset, 8);
	}

	@Override
	public String toString() {
		return "SingleStatItemData [btsId=" + btsId + ", collectTime="
				+ collectTime + ", interval=" + interval + ", timeType="
				+ timeType + ", itemId=" + itemId + ", value=" + value + "]";
	}

	@Override
	public SingleStatItemData copy() {
		SingleStatItemData itemData = new SingleStatItemData();
		itemData.setBtsId(btsId);
		itemData.setCollectTime(collectTime);
		itemData.setInterval(interval);
		itemData.setItemId(itemId);
		itemData.setTimeType(timeType);
		itemData.setValue(value);
		List<TargetAddress> udpAddrList = this.getUdpAddressList();
		if (udpAddrList == null)
			itemData.setUdpAddressList(null);
		else {
			List<TargetAddress> addrList = new ArrayList<TargetAddress>();
			if (udpAddrList.size() > 0) {
				for (TargetAddress udpAddress : udpAddrList) {
					addrList.add(udpAddress);
				}
			}
			itemData.setUdpAddressList(addrList);
		}
		return itemData;
	}

}
