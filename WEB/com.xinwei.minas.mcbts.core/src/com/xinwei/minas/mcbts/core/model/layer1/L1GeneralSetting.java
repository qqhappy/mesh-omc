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
import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;


/**
 * 
 * L1配置模型
 * 
 * @author chenjunhua
 * 
 */

public class L1GeneralSetting implements Serializable, Listable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 同步源
	private Long syncSrc;

	// GPS偏差
	private Long gpsOffset;

	// 天线掩码
	private Long antennaMask;

	public L1GeneralSetting() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	/**
	 * @return the antennaMask
	 */
	public Long getAntennaMask() {
		return antennaMask;
	}

	/**
	 * @param antennaMask
	 *            the antennaMask to set
	 */
	public void setAntennaMask(Long antennaMask) {
		this.antennaMask = antennaMask;
	}

	/**
	 * @return the gpsOffset
	 */
	public Long getGpsOffset() {
		return gpsOffset;
	}

	/**
	 * @param gpsOffset
	 *            the gpsOffset to set
	 */
	public void setGpsOffset(Long gpsOffset) {
		this.gpsOffset = gpsOffset;
	}

	/**
	 * @return the syncSrc
	 */
	public Long getSyncSrc() {
		return syncSrc;
	}

	/**
	 * @param syncSrc
	 *            the syncSrc to set
	 */
	public void setSyncSrc(Long syncSrc) {
		this.syncSrc = syncSrc;
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
		result = prime * result
				+ ((antennaMask == null) ? 0 : antennaMask.hashCode());
		result = prime * result
				+ ((gpsOffset == null) ? 0 : gpsOffset.hashCode());
		result = prime * result + ((syncSrc == null) ? 0 : syncSrc.hashCode());
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
		L1GeneralSetting other = (L1GeneralSetting) obj;
		if (antennaMask == null) {
			if (other.antennaMask != null)
				return false;
		} else if (!antennaMask.equals(other.antennaMask))
			return false;
		if (gpsOffset == null) {
			if (other.gpsOffset != null)
				return false;
		} else if (!gpsOffset.equals(other.gpsOffset))
			return false;
		if (syncSrc == null) {
			if (other.syncSrc != null)
				return false;
		} else if (!syncSrc.equals(other.syncSrc))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// 同步源
		allProperties.add(
				new FieldProperty(0
						,"listable.L1GeneralSetting.syncSrc"
						, String.valueOf(this.getSyncSrc())));
		// GPS偏差
		allProperties.add(
				new FieldProperty(0
						,"listable.L1GeneralSetting.gpsOffset"
						, String.valueOf(this.getGpsOffset())));
		// 天线掩码
		allProperties.add(
				new FieldProperty(0
						,"listable.L1GeneralSetting.antennaMask"
						, String.valueOf(this.getAntennaMask())));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "mcbts_l1general";
	}
	
}
