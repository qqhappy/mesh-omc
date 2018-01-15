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

public class SCGChannelConfigItem implements Serializable, Comparable {

	public final static int FORBIDDEN = 0;
	public final static int BCH = 1;
	public final static int BCHN1 = 2;
	public final static int RRCH = 3;
	public final static int RRCHN1 = 4;
	public final static int RARCH = 5;
	public final static int RARCHN1 = 6;
	public final static int RACH = 7;
	public final static int RACHN1 = 8;
	public final static int FACH = 9;
	public final static int FACHN1 = 10;
	public final static int RPCH = 11;
	public final static int RPCHN1 = 12;

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 通道类型
	private Integer channelType;

	// 时隙
	private Integer tsIndex;

	// 载波组
	private Integer scgIndex;

	public SCGChannelConfigItem() {
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
	 * @return the channelType
	 */
	public Integer getChannelType() {
		return channelType;
	}

	/**
	 * @param channelType
	 *            the channelType to set
	 */
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	/**
	 * @return the tsIndex
	 */
	public Integer getTsIndex() {
		return tsIndex;
	}

	/**
	 * @param tsIndex
	 *            the tsIndex to set
	 */
	public void setTsIndex(Integer tsIndex) {
		this.tsIndex = tsIndex;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelType == null) ? 0 : channelType.hashCode());
		result = prime * result
				+ ((scgIndex == null) ? 0 : scgIndex.hashCode());
		result = prime * result + ((tsIndex == null) ? 0 : tsIndex.hashCode());
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
		SCGChannelConfigItem other = (SCGChannelConfigItem) obj;
		if (channelType == null) {
			if (other.channelType != null)
				return false;
		} else if (!channelType.equals(other.channelType))
			return false;
		if (scgIndex == null) {
			if (other.scgIndex != null)
				return false;
		} else if (!scgIndex.equals(other.scgIndex))
			return false;
		if (tsIndex == null) {
			if (other.tsIndex != null)
				return false;
		} else if (!tsIndex.equals(other.tsIndex))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object arg0) {
		SCGChannelConfigItem other = (SCGChannelConfigItem) arg0;
		if (this.channelType < other.channelType)
			return -1;
		else if (this.channelType > other.channelType)
			return 1;
		else {
			if (this.tsIndex < other.tsIndex)
				return -1;
			else if (this.tsIndex > other.tsIndex)
				return 1;
			else {
				if (this.scgIndex < other.scgIndex)
					return -1;
				else if (this.scgIndex > other.scgIndex)
					return 1;
			}
		}
		return 0;
	}

}
