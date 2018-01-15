/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.xstat;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * eNB统计性能实体
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatEntity {

	// 标识此实体数据属于何对象
	private long enbId;
	// [STARTT_TIME] 8 M 性能数据采集的起始时间START_TIME
	private long startStatTime;
	// Yyyymmddhhmm6个byte
	// 如：14 06 09 01 12 20 表示时间为：200609011832
	// [END_TIME] 8 M 性能数据采集的终止时间END_TIME，格式同开始时间
	private long endStatTime;
	// [VERSION] 4 M 版本，初始版本为0x00
	private int version;
	// [Rev] 4 M 填全0
	private int reserve;
	// [LEN] 4 M 本字段之后的总长度，不包括本字段
	private int length;
	// [Head CRC] 4 M 文件头校验和
	private int headerCrc;

	private Map<String, Double> itemMap = new HashMap<String, Double>();

	public void addItem(long itemId, double value) {
		itemMap.put(String.valueOf(itemId), value);
	}

	public void addItem(long cellId, long itemId, double value) {
		itemMap.put(cellId + "." + itemId, value);
	}

	public Map<String, Double> getItemMap() {
		return itemMap;
	}

	public boolean hasData() {
		return !itemMap.isEmpty();
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public long getStartStatTime() {
		return startStatTime;
	}

	public void setStartStatTime(long startStatTime) {
		this.startStatTime = startStatTime;
	}

	public long getEndStatTime() {
		return endStatTime;
	}

	public void setEndStatTime(long endStatTime) {
		this.endStatTime = endStatTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getReserve() {
		return reserve;
	}

	public void setReserve(int reserve) {
		this.reserve = reserve;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeaderCrc() {
		return headerCrc;
	}

	public void setHeaderCrc(int headerCrc) {
		this.headerCrc = headerCrc;
	}
}
