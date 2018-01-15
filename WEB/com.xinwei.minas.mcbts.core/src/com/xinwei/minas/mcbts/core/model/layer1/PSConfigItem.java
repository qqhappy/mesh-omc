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
 * 校准类型配置模型
 * 
 * @author chenjunhua
 * 
 */

public class PSConfigItem implements Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 天线序号
	private Integer antennaIndex;

	// PS_Norm_X
	private Integer psNormX;

	// PS_Norm_Y
	private Integer psNormY;

	public PSConfigItem() {
	}

	/**
	 * @return the antennaIndex
	 */
	public Integer getAntennaIndex() {
		return antennaIndex;
	}

	/**
	 * @param antennaIndex the antennaIndex to set
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
	 * @param moId the moId to set
	 */
	public void setMoId(long moId) {
		this.moId = moId;
	}

	/**
	 * @return the psNormX
	 */
	public Integer getPsNormX() {
		return psNormX;
	}

	/**
	 * @param psNormX the psNormX to set
	 */
	public void setPsNormX(Integer psNormX) {
		this.psNormX = psNormX;
	}

	/**
	 * @return the psNormY
	 */
	public Integer getPsNormY() {
		return psNormY;
	}

	/**
	 * @param psNormY the psNormY to set
	 */
	public void setPsNormY(Integer psNormY) {
		this.psNormY = psNormY;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((antennaIndex == null) ? 0 : antennaIndex.hashCode());
		result = prime * result + ((psNormX == null) ? 0 : psNormX.hashCode());
		result = prime * result + ((psNormY == null) ? 0 : psNormY.hashCode());
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
		PSConfigItem other = (PSConfigItem) obj;
		if (antennaIndex == null) {
			if (other.antennaIndex != null)
				return false;
		} else if (!antennaIndex.equals(other.antennaIndex))
			return false;
		if (psNormX == null) {
			if (other.psNormX != null)
				return false;
		} else if (!psNormX.equals(other.psNormX))
			return false;
		if (psNormY == null) {
			if (other.psNormY != null)
				return false;
		} else if (!psNormY.equals(other.psNormY))
			return false;
		return true;
	}
	
}
