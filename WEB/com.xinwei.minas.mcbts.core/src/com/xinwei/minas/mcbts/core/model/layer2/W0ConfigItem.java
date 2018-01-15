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
 * 空中链路W0配置模型
 * 
 * @author jiayi
 * 
 */

public class W0ConfigItem implements Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 天线序号
	private Integer antennaIndex;

	// w0_i
	private Double w0I;

	// w0_q
	private Double w0Q;

	public W0ConfigItem() {
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
	 * @return the w0I
	 */
	public Double getW0I() {
		return w0I;
	}

	/**
	 * @param w0i
	 *            the w0I to set
	 */
	public void setW0I(Double w0i) {
		w0I = w0i;
	}

	/**
	 * @return the w0Q
	 */
	public Double getW0Q() {
		return w0Q;
	}

	/**
	 * @param w0q
	 *            the w0Q to set
	 */
	public void setW0Q(Double w0q) {
		w0Q = w0q;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((antennaIndex == null) ? 0 : antennaIndex.hashCode());
		result = prime * result + ((w0I == null) ? 0 : w0I.hashCode());
		result = prime * result + ((w0Q == null) ? 0 : w0Q.hashCode());
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
		W0ConfigItem other = (W0ConfigItem) obj;
		if (antennaIndex == null) {
			if (other.antennaIndex != null)
				return false;
		} else if (!antennaIndex.equals(other.antennaIndex))
			return false;
		if (w0I == null) {
			if (other.w0I != null)
				return false;
		} else if (!w0I.equals(other.w0I))
			return false;
		if (w0Q == null) {
			if (other.w0Q != null)
				return false;
		} else if (!w0Q.equals(other.w0Q))
			return false;
		return true;
	}

}
