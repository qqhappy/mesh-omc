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

public class AntennaCalibItem implements Serializable {

	// 行序号
	private Integer rowIndex;

	// 校准数据
	private Integer carrierData;

	public AntennaCalibItem() {
	}

	/**
	 * @return the rowIndex
	 */
	public Integer getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the carrierData
	 */
	public Integer getCarrierData() {
		return carrierData;
	}

	/**
	 * @param carrierData the carrierData to set
	 */
	public void setCarrierData(Integer carrierData) {
		this.carrierData = carrierData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carrierData == null) ? 0 : carrierData.hashCode());
		result = prime * result
				+ ((rowIndex == null) ? 0 : rowIndex.hashCode());
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
		AntennaCalibItem other = (AntennaCalibItem) obj;
		if (carrierData == null) {
			if (other.carrierData != null)
				return false;
		} else if (!carrierData.equals(other.carrierData))
			return false;
		if (rowIndex == null) {
			if (other.rowIndex != null)
				return false;
		} else if (!rowIndex.equals(other.rowIndex))
			return false;
		return true;
	}
}
