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
 * 空中链路载波组Scale信息配置模型
 * 
 * @author jiayi
 * 
 */

public class SCGScaleConfigItem implements Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 时隙
	private Integer tsIndex;

	// BCH Scale
	private Integer bchScale;

	// TCH Scale
	private Integer tchScale;

	public SCGScaleConfigItem() {
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
	 * @return the bchScale
	 */
	public Integer getBchScale() {
		return bchScale;
	}

	/**
	 * @param bchScale
	 *            the bchScale to set
	 */
	public void setBchScale(Integer bchScale) {
		this.bchScale = bchScale;
	}

	/**
	 * @return the tchScale
	 */
	public Integer getTchScale() {
		return tchScale;
	}

	/**
	 * @param tchScale
	 *            the tchScale to set
	 */
	public void setTchScale(Integer tchScale) {
		this.tchScale = tchScale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bchScale == null) ? 0 : bchScale.hashCode());
		result = prime * result
				+ ((tchScale == null) ? 0 : tchScale.hashCode());
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
		SCGScaleConfigItem other = (SCGScaleConfigItem) obj;
		if (bchScale == null) {
			if (other.bchScale != null)
				return false;
		} else if ((bchScale.intValue() - other.bchScale.intValue()) > 1)
			return false;
		if (tchScale == null) {
			if (other.tchScale != null)
				return false;
		} else if ((tchScale.intValue() - other.tchScale.intValue()) > 1)
			return false;
		if (tsIndex == null) {
			if (other.tsIndex != null)
				return false;
		} else if ((tsIndex.intValue() - other.tsIndex) > 1)
			return false;
		return true;
	}

}
