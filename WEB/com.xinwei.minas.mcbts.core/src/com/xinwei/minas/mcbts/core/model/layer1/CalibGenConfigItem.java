/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;

/**
 * 
 * 校准射频增益信息配置模型
 * 
 * @author chenjunhua
 * 
 */

public class CalibGenConfigItem implements Serializable {

	public final static int LENGTH=28;
	
	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 天线序号
	private Integer antennaIndex;

	// 接收增益
	private Integer rxGain;

	// 发送增益
	private Integer txGain;

	// 校准结果
	private Integer calibrationResult;

	// 发送增益
	private String predH;

	public CalibGenConfigItem() {
	}

	/**
	 * @return the antennaIndex
	 */
	public Integer getAntennaIndex() {
		return antennaIndex;
	}

	/**
	 * @param antennaIndex
	 *            the antennaIndex to set
	 */
	public void setAntennaIndex(Integer antennaIndex) {
		this.antennaIndex = antennaIndex;
	}

	/**
	 * @return the idx
	 */
	public Long getIdx() {
		return idx;
	}

	/**
	 * @param idx
	 *            the idx to set
	 */
	public void setIdx(Long idx) {
		this.idx = idx;
	}

	/**
	 * @return the moId
	 */
	public long getMoId() {
		return moId;
	}

	/**
	 * @param moId
	 *            the moId to set
	 */
	public void setMoId(long moId) {
		this.moId = moId;
	}

	/**
	 * @return the rxGain
	 */
	public Integer getRxGain() {
		return rxGain;
	}

	/**
	 * @param rxGain
	 *            the rxGain to set
	 */
	public void setRxGain(Integer rxGain) {
		this.rxGain = rxGain;
	}

	/**
	 * @return the txGain
	 */
	public Integer getTxGain() {
		return txGain;
	}

	/**
	 * @param txGain
	 *            the txGain to set
	 */
	public void setTxGain(Integer txGain) {
		this.txGain = txGain;
	}

	/**
	 * @return the calibrationResult
	 */
	public Integer getCalibrationResult() {
		return calibrationResult;
	}

	/**
	 * @param calibrationResult
	 *            the calibrationResult to set
	 */
	public void setCalibrationResult(Integer calibrationResult) {
		this.calibrationResult = calibrationResult;
	}

	/**
	 * @return the predH
	 */
	public String getPredH() {
		return predH;
	}

	/**
	 * @param predH
	 *            the predH to set
	 */
	public void setPredH(String predH) {
		this.predH = predH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((antennaIndex == null) ? 0 : antennaIndex.hashCode());
		result = prime
				* result
				+ ((calibrationResult == null) ? 0 : calibrationResult
						.hashCode());
		result = prime * result + ((predH == null) ? 0 : predH.hashCode());
		result = prime * result + ((rxGain == null) ? 0 : rxGain.hashCode());
		result = prime * result + ((txGain == null) ? 0 : txGain.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalibGenConfigItem other = (CalibGenConfigItem) obj;
		if (antennaIndex == null) {
			if (other.antennaIndex != null)
				return false;
		} else if (!antennaIndex.equals(other.antennaIndex))
			return false;
		if (calibrationResult == null) {
			if (other.calibrationResult != null)
				return false;
		} else if (!calibrationResult.equals(other.calibrationResult))
			return false;
		if (predH == null) {
			if (other.predH != null)
				return false;
		} else if (other.predH.trim().length() != 0
				&& !predH.trim().equals(other.predH.trim()))
			return false;
		if (rxGain == null) {
			if (other.rxGain != null)
				return false;
		} else if (!rxGain.equals(other.rxGain))
			return false;
		if (txGain == null) {
			if (other.txGain != null)
				return false;
		} else if (!txGain.equals(other.txGain))
			return false;
		return true;
	}
}
