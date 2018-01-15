/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;

/**
 * 
 * 空中链路载波组通道信息配置模型
 * 
 * @author jiayi
 * 
 */

@SuppressWarnings({ "rawtypes", "serial" })
public class PECCHSetting implements Serializable, Comparable {

	public final static int EBCH = 21;
	public final static int EPCH = 22;
	public final static int ERARCH = 23;
	public final static int ERRCH = 24;
	public final static int ERACH = 25;

	public final static int EPCH_SIZE = 8;
	public final static int ERARCH_SIZE = 4;
	public final static int ERRCH_SIZE = 2;

	public final static int PER_EPCH_OCCUPIED_CHANNELS = 2;
	public final static int PER_ERARCH_OCCUPIED_CHANNELS = 2;
	public final static int PER_ERRCH_OCCUPIED_CHANNELS = 1;
	
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 增强公共信道所在的载波组序号
	private Integer scgIndex;

	// PCH组数
	private Integer pchCount;

	// PCH组序号
	private Integer pchIndex;

	// RARCH组数
	private Integer rarchCount;

	// RRCH组数
	private Integer rrchCount;

	public PECCHSetting() {
	}

	/**
	 * @return the idx
	 */
	public Long getIdx() {
		return idx;
	}

	/**
	 * @param idx the idx to set
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
	 * @return the scgIndex
	 */
	public Integer getScgIndex() {
		return scgIndex;
	}

	/**
	 * @param scgIndex
	 *            the scgIndex to set
	 */
	public void setScgIndex(Integer scgIndex) {
		this.scgIndex = scgIndex;
	}

	/**
	 * @return the pchCount
	 */
	public Integer getPchCount() {
		return pchCount;
	}

	/**
	 * @param pchCount the pchCount to set
	 */
	public void setPchCount(Integer pchCount) {
		this.pchCount = pchCount;
	}

	/**
	 * @return the pchIndex
	 */
	public Integer getPchIndex() {
		return pchIndex;
	}

	/**
	 * @param pchIndex the pchIndex to set
	 */
	public void setPchIndex(Integer pchIndex) {
		this.pchIndex = pchIndex;
	}

	/**
	 * @return the rarchCount
	 */
	public Integer getRarchCount() {
		return rarchCount;
	}

	/**
	 * @param rarchCount the rarchCount to set
	 */
	public void setRarchCount(Integer rarchCount) {
		this.rarchCount = rarchCount;
	}

	/**
	 * @return the rrchCount
	 */
	public Integer getRrchCount() {
		return rrchCount;
	}

	/**
	 * @param rrchCount the rrchCount to set
	 */
	public void setRrchCount(Integer rrchCount) {
		this.rrchCount = rrchCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((scgIndex == null) ? 0 : scgIndex.hashCode());
		result = prime * result
				+ ((pchCount == null) ? 0 : pchCount.hashCode());
		result = prime * result
				+ ((pchIndex == null) ? 0 : pchIndex.hashCode());
		result = prime * result
				+ ((rarchCount == null) ? 0 : rarchCount.hashCode());
		result = prime * result
				+ ((rrchCount == null) ? 0 : rrchCount.hashCode());
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
		PECCHSetting other = (PECCHSetting) obj;
		if (scgIndex == null) {
			if (other.scgIndex != null)
				return false;
		} else if (!scgIndex.equals(other.scgIndex))
			return false;
		if (pchCount == null) {
			if (other.pchCount != null)
				return false;
		} else if (!pchCount.equals(other.pchCount))
			return false;
		if (pchIndex == null) {
			if (other.pchIndex != null)
				return false;
		} else if (!pchIndex.equals(other.pchIndex))
			return false;
		if (rarchCount == null) {
			if (other.rarchCount != null)
				return false;
		} else if (!rarchCount.equals(other.rarchCount))
			return false;
		if (rrchCount == null) {
			if (other.rrchCount != null)
				return false;
		} else if (!rrchCount.equals(other.rrchCount))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object arg0) {
		PECCHSetting other = (PECCHSetting) arg0;
		if (this.scgIndex < other.scgIndex)
			return -1;
		if (this.scgIndex > other.scgIndex)
			return 1;
		if (this.pchCount < other.pchCount)
			return -1;
		if (this.pchCount > other.pchCount)
			return 1;
		if (this.pchIndex < other.pchIndex)
			return -1;
		if (this.pchIndex > other.pchIndex)
			return 1;
		if (this.rarchCount < other.rarchCount)
			return -1;
		if (this.rarchCount > other.rarchCount)
			return 1;
		if (this.rrchCount < other.rrchCount)
			return -1;
		if (this.rrchCount > other.rrchCount)
			return 1;
		return 0;
	}
}
