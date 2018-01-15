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
 * eNBͳ������ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatEntity {

	// ��ʶ��ʵ���������ںζ���
	private long enbId;
	// [STARTT_TIME] 8 M �������ݲɼ�����ʼʱ��START_TIME
	private long startStatTime;
	// Yyyymmddhhmm6��byte
	// �磺14 06 09 01 12 20 ��ʾʱ��Ϊ��200609011832
	// [END_TIME] 8 M �������ݲɼ�����ֹʱ��END_TIME����ʽͬ��ʼʱ��
	private long endStatTime;
	// [VERSION] 4 M �汾����ʼ�汾Ϊ0x00
	private int version;
	// [Rev] 4 M ��ȫ0
	private int reserve;
	// [LEN] 4 M ���ֶ�֮����ܳ��ȣ����������ֶ�
	private int length;
	// [Head CRC] 4 M �ļ�ͷУ���
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
