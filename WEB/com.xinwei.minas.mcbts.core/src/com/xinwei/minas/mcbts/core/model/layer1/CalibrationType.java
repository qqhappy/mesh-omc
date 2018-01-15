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
 * 校准类型配置模型
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationType implements Serializable, Listable {

	public static final int CALIBTYPE_ONLINE = 0;

	public static final int CALIBTYPE_FULL = 1;

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 校准周期
	private Integer calibPeriod;

	// 校准类型
	private Integer calibType;

	public CalibrationType() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	/**
	 * @return the period
	 */
	public Integer getCalibPeriod() {
		return calibPeriod;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setCalibPeriod(Integer calibPeriod) {
		this.calibPeriod = calibPeriod;
	}

	/**
	 * @return the type
	 */
	public Integer getCalibType() {
		return calibType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setCalibType(Integer calibType) {
		this.calibType = calibType;
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
				+ ((calibPeriod == null) ? 0 : calibPeriod.hashCode());
		result = prime * result
				+ ((calibType == null) ? 0 : calibType.hashCode());
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
		CalibrationType other = (CalibrationType) obj;
		if (calibPeriod == null) {
			if (other.calibPeriod != null)
				return false;
		} else if (!calibPeriod.equals(other.calibPeriod))
			return false;
		if (calibType == null) {
			if (other.calibType != null)
				return false;
		} else if (!calibType.equals(other.calibType))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// 校准周期
		allProperties.add(
				new FieldProperty(0
						,"listable.CalibrationType.calibPeriod"
						, String.valueOf(this.getCalibPeriod())));
		// 校准类型
		allProperties.add(
				new FieldProperty(0
						,"listable.CalibrationType.calibType"
						, String.valueOf(this.getCalibType())));
		
		return allProperties;
	}

	@Override
	public String getBizName() {
		return "mcbts_calibrationtype";
	}
	
}
