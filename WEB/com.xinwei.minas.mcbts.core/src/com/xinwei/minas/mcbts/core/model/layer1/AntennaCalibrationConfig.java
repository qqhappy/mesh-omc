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
import java.util.Arrays;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 校准天线参数信息配置模型
 * 
 * @author chenjunhua
 * 
 */

public class AntennaCalibrationConfig implements Serializable, Listable {

	public static final int ANT_COUNT = 8;

	public static final int CALCARRIER_COUNT = 640;

	public static final int TXCAL_I = 1;
	public static final int TXCAL_Q = 2;
	public static final int RXCAL_I = 3;
	public static final int RXCAL_Q = 4;

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 天线序号
	private Integer antennaIndex;

	// 数据类型 TXCAL_I/Q, RXCAL_I/Q
	private Integer dataType;

	// 天线参数配置数据库保存信息
	private byte[] detailInfo;

	// 天线参数配置
	private List<AntennaCalibItem> antItemList = new ArrayList<AntennaCalibItem>();

	public AntennaCalibrationConfig() {
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
	 * @return the dataType
	 */
	public Integer getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the antItemList
	 */
	public List<AntennaCalibItem> getAntItemList() {
		return antItemList;
	}

	/**
	 * @param antItemList
	 *            the antItemList to set
	 */
	public void setAntItemList(List<AntennaCalibItem> antItemList) {
		this.antItemList = antItemList;
	}

	/**
	 * @return the detailInfo
	 */
	public byte[] getDetailInfo() {
		return detailInfo;
	}

	/**
	 * @param detailInfo
	 *            the detailInfo to set
	 */
	public void setDetailInfo(byte[] detailInfo) {
		this.detailInfo = detailInfo;
	}

	public void convertBytesToList() {
		if (detailInfo == null) {
			return;
		}
		antItemList.clear();
		int offset = 0;
		for (int i = 0; i < CALCARRIER_COUNT; i++) {
			AntennaCalibItem antItem = new AntennaCalibItem();
			antItem.setRowIndex(i);
			antItem.setCarrierData((int) ByteUtils.toSignedNumber(detailInfo,
					offset, 2));
			offset += 2;

			antItemList.add(antItem);
		}
	}

	public void convertListToBytes() {
		if (antItemList == null) {
			return;
		}
		detailInfo = new byte[antItemList.size() * 2];
		int offset = 0;
		for (AntennaCalibItem antItem : antItemList) {
			ByteUtils.putNumber(detailInfo, offset, antItem.getCarrierData()
					.toString(), 2);
			offset += 2;
		}
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
				+ ((antItemList == null) ? 0 : antItemList.hashCode());
		result = prime * result
				+ ((antennaIndex == null) ? 0 : antennaIndex.hashCode());
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + Arrays.hashCode(detailInfo);
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
		AntennaCalibrationConfig other = (AntennaCalibrationConfig) obj;
		if (antItemList == null) {
			if (other.antItemList != null)
				return false;
		} else if (!antItemList.equals(other.antItemList))
			return false;
		if (antennaIndex == null) {
			if (other.antennaIndex != null)
				return false;
		} else if (!antennaIndex.equals(other.antennaIndex))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (!Arrays.equals(detailInfo, other.detailInfo))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		return null;
	}

	@Override
	public String getBizName() {
		// TODO Auto-generated method stub
		return null;
	}
}
