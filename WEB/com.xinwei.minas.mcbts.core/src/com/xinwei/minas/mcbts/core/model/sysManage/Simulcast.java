/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-16	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * 同播资源模型
 * 
 * @author tiance
 * 
 */

public class Simulcast implements Serializable {

	private long idx;
	// 地域ID
	private long districtId;
	// 频率
	private int freqType;
	// 频率偏移量
	private int freqOffset;
	// 中间频点
	private double freq;
	// 最小预留组资源数
	private int minNum;
	// 最大预留组资源数
	private int maxNum;
	// 最大同播组个数
	private int maxMbmsNum;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public int getFreqType() {
		return freqType;
	}

	public void setFreqType(int freqType) {
		this.freqType = freqType;
	}

	public int getFreqOffset() {
		return freqOffset;
	}

	public void setFreqOffset(int freqOffset) {
		this.freqOffset = freqOffset;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getMaxMbmsNum() {
		return maxMbmsNum;
	}

	public void setMaxMbmsNum(int maxMbmsNum) {
		this.maxMbmsNum = maxMbmsNum;
	}

	/**
	 * 验证两个同播信息拥有相同的地域,频点
	 * 
	 * @param other
	 * @return
	 */
	public boolean sameKeyWith(Simulcast other) {
		if (this == null || other == null)
			return false;

		if (this.districtId != other.districtId)
			return false;

		if (this.freqType != other.freqType)
			return false;

		if (this.freqOffset != other.freqOffset)
			return false;

		return true;
	}

	/**
	 * 验证两个同播信息拥有相同的值
	 * 
	 * @param other
	 * @return
	 */
	public boolean sameValueWith(Simulcast other) {
		if (this == null || other == null)
			return false;

		if (this.minNum != other.minNum)
			return false;

		if (this.maxNum != other.maxNum)
			return false;

		if (this.maxMbmsNum != other.maxMbmsNum)
			return false;

		return true;
	}

}
