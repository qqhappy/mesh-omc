/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;

import com.xinwei.minas.core.model.Mo;

/**
 * @author chenshaohua
 *
 */
public class TConfAntijammingParam implements Serializable {

	// 记录索引
	private Long idx;
	
	//MO编号（全局唯一,系统自动生成）
	private long moId;
	
	private int jumpFreqSwitch;
	
	private int jumpFreqCIMaxThreshold;
	
	private int jumpFreqCIAvgThreshold;
	
	private int jumpFreqOverThresholdTime;
	
	private int fallLevelSwitch;
	
	private int fallLevelMaxThreshold;
	
	private int fallLevelCIAvgThreshold;
	
	private int fallLevelOverThresholdTime;
	
	private int lowestLevel;

	public int getFallLevelCIAvgThreshold() {
		return fallLevelCIAvgThreshold;
	}

	public void setFallLevelCIAvgThreshold(int fallLevelCIAvgThreshold) {
		this.fallLevelCIAvgThreshold = fallLevelCIAvgThreshold;
	}

	public int getFallLevelMaxThreshold() {
		return fallLevelMaxThreshold;
	}

	public void setFallLevelMaxThreshold(int fallLevelMaxThreshold) {
		this.fallLevelMaxThreshold = fallLevelMaxThreshold;
	}

	public int getFallLevelOverThresholdTime() {
		return fallLevelOverThresholdTime;
	}

	public void setFallLevelOverThresholdTime(int fallLevelOverThresholdTime) {
		this.fallLevelOverThresholdTime = fallLevelOverThresholdTime;
	}

	public int getFallLevelSwitch() {
		return fallLevelSwitch;
	}

	public void setFallLevelSwitch(int fallLevelSwitch) {
		this.fallLevelSwitch = fallLevelSwitch;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public int getJumpFreqCIAvgThreshold() {
		return jumpFreqCIAvgThreshold;
	}

	public void setJumpFreqCIAvgThreshold(int jumpFreqCIAvgThreshold) {
		this.jumpFreqCIAvgThreshold = jumpFreqCIAvgThreshold;
	}

	public int getJumpFreqCIMaxThreshold() {
		return jumpFreqCIMaxThreshold;
	}

	public void setJumpFreqCIMaxThreshold(int jumpFreqCIMaxThreshold) {
		this.jumpFreqCIMaxThreshold = jumpFreqCIMaxThreshold;
	}

	public int getJumpFreqOverThresholdTime() {
		return jumpFreqOverThresholdTime;
	}

	public void setJumpFreqOverThresholdTime(int jumpFreqOverThresholdTime) {
		this.jumpFreqOverThresholdTime = jumpFreqOverThresholdTime;
	}

	public int getJumpFreqSwitch() {
		return jumpFreqSwitch;
	}

	public void setJumpFreqSwitch(int jumpFreqSwitch) {
		this.jumpFreqSwitch = jumpFreqSwitch;
	}

	public int getLowestLevel() {
		return lowestLevel;
	}

	public void setLowestLevel(int lowestLevel) {
		this.lowestLevel = lowestLevel;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fallLevelCIAvgThreshold;
		result = prime * result + fallLevelMaxThreshold;
		result = prime * result + fallLevelOverThresholdTime;
		result = prime * result + fallLevelSwitch;
		result = prime * result + jumpFreqCIAvgThreshold;
		result = prime * result + jumpFreqCIMaxThreshold;
		result = prime * result + jumpFreqOverThresholdTime;
		result = prime * result + jumpFreqSwitch;
		result = prime * result + lowestLevel;
		return result;
	}

	/* (non-Javadoc)
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
		TConfAntijammingParam other = (TConfAntijammingParam) obj;
		if (fallLevelCIAvgThreshold != other.fallLevelCIAvgThreshold)
			return false;
		if (fallLevelMaxThreshold != other.fallLevelMaxThreshold)
			return false;
		if (fallLevelOverThresholdTime != other.fallLevelOverThresholdTime)
			return false;
		if (fallLevelSwitch != other.fallLevelSwitch)
			return false;
		if (jumpFreqCIAvgThreshold != other.jumpFreqCIAvgThreshold)
			return false;
		if (jumpFreqCIMaxThreshold != other.jumpFreqCIMaxThreshold)
			return false;
		if (jumpFreqOverThresholdTime != other.jumpFreqOverThresholdTime)
			return false;
		if (jumpFreqSwitch != other.jumpFreqSwitch)
			return false;
		if (lowestLevel != other.lowestLevel)
			return false;
		return true;
	}
}
