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
 * ͬ����Դģ��
 * 
 * @author tiance
 * 
 */

public class Simulcast implements Serializable {

	private long idx;
	// ����ID
	private long districtId;
	// Ƶ��
	private int freqType;
	// Ƶ��ƫ����
	private int freqOffset;
	// �м�Ƶ��
	private double freq;
	// ��СԤ������Դ��
	private int minNum;
	// ���Ԥ������Դ��
	private int maxNum;
	// ���ͬ�������
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
	 * ��֤����ͬ����Ϣӵ����ͬ�ĵ���,Ƶ��
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
	 * ��֤����ͬ����Ϣӵ����ͬ��ֵ
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
