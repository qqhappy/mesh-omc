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
 * 校准基本信息配置模型
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationGeneralConfig implements Serializable, Listable {

	public static final int GEN_COUNT = 8;

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 合成器接收增益
	private Integer synRxGain;

	// 合成器发送增益
	private Integer synTxGain;

	// 射频增益配置
	private List<CalibGenConfigItem> genItemList = new ArrayList<CalibGenConfigItem>();

	public CalibrationGeneralConfig() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	/**
	 * @return the synRxGain
	 */
	public Integer getSynRxGain() {
		return synRxGain;
	}

	/**
	 * @param synRxGain
	 *            the synRxGain to set
	 */
	public void setSynRxGain(Integer synRxGain) {
		this.synRxGain = synRxGain;
	}

	/**
	 * @return the synTxGain
	 */
	public Integer getSynTxGain() {
		return synTxGain;
	}

	/**
	 * @param synTxGain
	 *            the synTxGain to set
	 */
	public void setSynTxGain(Integer synTxGain) {
		this.synTxGain = synTxGain;
	}

	/**
	 * @return the genItemList
	 */
	public List<CalibGenConfigItem> getGenItemList() {
		return genItemList;
	}

	/**
	 * @param genItemList
	 *            the genItemList to set
	 */
	public void setGenItemList(List<CalibGenConfigItem> genItemList) {
		this.genItemList = genItemList;
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
				+ ((genItemList == null) ? 0 : genItemList.hashCode());
		result = prime * result
				+ ((synRxGain == null) ? 0 : synRxGain.hashCode());
		result = prime * result
				+ ((synTxGain == null) ? 0 : synTxGain.hashCode());
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
		CalibrationGeneralConfig other = (CalibrationGeneralConfig) obj;
		if (genItemList == null) {
			if (other.genItemList != null)
				return false;
		} else if (!genItemList.equals(other.genItemList))
			return false;
		if (synRxGain == null) {
			if (other.synRxGain != null)
				return false;
		} else if (!synRxGain.equals(other.synRxGain))
			return false;
		if (synTxGain == null) {
			if (other.synTxGain != null)
				return false;
		} else if (!synTxGain.equals(other.synTxGain))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		// TODO Auto-generated method stub
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// 合成器接收增益
		allProperties.add(new FieldProperty(1,
				"listable.CalibrationGeneralConfig.synRxGain", String
						.valueOf(this.getSynRxGain())));
		// 合成器发送增益
		allProperties.add(new FieldProperty(1,
				"listable.CalibrationGeneralConfig.synTxGain", String
						.valueOf(this.getSynTxGain())));
		// 射频增益配置
		allProperties.add(new FieldProperty(1,
				"listable.CalibrationGeneralConfig.genItemList",
				"listable.CalibrationGeneralConfig.genItem"));
		// 天线序号,接收增益,发送增益,校准结果,发送增益
		for (CalibGenConfigItem gentItem : genItemList) {
			allProperties.add(new FieldProperty(2, String.valueOf(gentItem
					.getAntennaIndex()), gentItem.getRxGain()
					+ "/"
					+ gentItem.getTxGain()
					+ "/"
					+ gentItem.getCalibrationResult()
					+ "/"
					+ (gentItem.getPredH().trim().length() == 0 ? "0"
							: gentItem.getPredH().trim())));
		}

		return allProperties;
	}

	@Override
	public String getBizName() {
		// TODO Auto-generated method stub
		return null;
	}
}
