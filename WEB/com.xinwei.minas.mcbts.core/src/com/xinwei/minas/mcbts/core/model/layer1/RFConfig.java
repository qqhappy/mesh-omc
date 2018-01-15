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
 * ��Ƶ��Ϣ����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class RFConfig implements Serializable, Listable {

	public static final int PS_COUNT = 8;

	public static final int CALIBTYPE_ONLINE = 0;

	public static final int CALIBTYPE_FULL = 1;

	// ��¼����
	private Long idx;

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// Ƶ������
	private Integer freqType;

	// Ƶ��ƫ��ֵ
	private Integer freqOffset;

	// ���߹���
	private Integer antennaPower;

	// ���յ�ƽ
	private Integer rxSensitivity;

	// ������ʧ
	private Integer cableLoss;

	// ��������ʧ
	private Integer psLoss;

	// ����������
	private List<PSConfigItem> psConfigList = new ArrayList<PSConfigItem>();

	public RFConfig() {
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
	 * @return the antennaPower
	 */
	public Integer getAntennaPower() {
		return antennaPower;
	}

	/**
	 * @param antennaPower
	 *            the antennaPower to set
	 */
	public void setAntennaPower(Integer antennaPower) {
		this.antennaPower = antennaPower;
	}

	/**
	 * @return the cableLoss
	 */
	public Integer getCableLoss() {
		return cableLoss;
	}

	/**
	 * @param cableLoss
	 *            the cableLoss to set
	 */
	public void setCableLoss(Integer cableLoss) {
		this.cableLoss = cableLoss;
	}

	/**
	 * @return the freqOffset
	 */
	public Integer getFreqOffset() {
		return freqOffset;
	}

	/**
	 * @param freqOffset
	 *            the freqOffset to set
	 */
	public void setFreqOffset(Integer freqOffset) {
		this.freqOffset = freqOffset;
	}

	/**
	 * @return the freqType
	 */
	public Integer getFreqType() {
		return freqType;
	}

	/**
	 * @param freqType
	 *            the freqType to set
	 */
	public void setFreqType(Integer freqType) {
		this.freqType = freqType;
	}

	/**
	 * @return the psLoss
	 */
	public Integer getPsLoss() {
		return psLoss;
	}

	/**
	 * @param psLoss
	 *            the psLoss to set
	 */
	public void setPsLoss(Integer psLoss) {
		this.psLoss = psLoss;
	}

	/**
	 * @return the rxSensitivity
	 */
	public Integer getRxSensitivity() {
		return rxSensitivity;
	}

	/**
	 * @param rxSensitivity
	 *            the rxSensitivity to set
	 */
	public void setRxSensitivity(Integer rxSensitivity) {
		this.rxSensitivity = rxSensitivity;
	}

	/**
	 * @return the psConfigList
	 */
	public List<PSConfigItem> getPsConfigList() {
		return psConfigList;
	}

	/**
	 * @param psConfigList
	 *            the psConfigList to set
	 */
	public void setPsConfigList(List<PSConfigItem> psConfigList) {
		this.psConfigList = psConfigList;
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
				+ ((antennaPower == null) ? 0 : antennaPower.hashCode());
		result = prime * result
				+ ((cableLoss == null) ? 0 : cableLoss.hashCode());
		result = prime * result
				+ ((freqOffset == null) ? 0 : freqOffset.hashCode());
		result = prime * result
				+ ((freqType == null) ? 0 : freqType.hashCode());
		result = prime * result
				+ ((psConfigList == null) ? 0 : psConfigList.hashCode());
		result = prime * result + ((psLoss == null) ? 0 : psLoss.hashCode());
		result = prime * result
				+ ((rxSensitivity == null) ? 0 : rxSensitivity.hashCode());
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
		RFConfig other = (RFConfig) obj;
		if (antennaPower == null) {
			if (other.antennaPower != null)
				return false;
		} else if (!antennaPower.equals(other.antennaPower))
			return false;
		if (cableLoss == null) {
			if (other.cableLoss != null)
				return false;
		} else if (!cableLoss.equals(other.cableLoss))
			return false;
		if (freqOffset == null) {
			if (other.freqOffset != null)
				return false;
		} else if (!freqOffset.equals(other.freqOffset))
			return false;
		if (freqType == null) {
			if (other.freqType != null)
				return false;
		} else if (!freqType.equals(other.freqType))
			return false;
		if (psConfigList == null) {
			if (other.psConfigList != null)
				return false;
		} else if (!psConfigList.equals(other.psConfigList))
			return false;
		if (psLoss == null) {
			if (other.psLoss != null)
				return false;
		} else if (!psLoss.equals(other.psLoss))
			return false;
		if (rxSensitivity == null) {
			if (other.rxSensitivity != null)
				return false;
		} else if (!rxSensitivity.equals(other.rxSensitivity))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {

		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// Ƶ������
//		allProperties.add(new FieldProperty(1, "listable.RFConfig.freqType",
//				String.valueOf(this.getFreqType())));
		// Ƶ��ƫ��ֵ
		allProperties.add(new FieldProperty(1, "listable.RFConfig.freqOffset",
				String.valueOf(this.getFreqOffset())));
		// ���߹���
		allProperties.add(new FieldProperty(1,
				"listable.RFConfig.antennaPower", String.valueOf(this
						.getAntennaPower())));
		// ���յ�ƽ
		allProperties.add(new FieldProperty(1,
				"listable.RFConfig.rxSensitivity", String.valueOf(this
						.getRxSensitivity())));
		// ������ʧ
		allProperties.add(new FieldProperty(1, "listable.RFConfig.cableLoss",
				String.valueOf(this.getCableLoss())));
		// ��������ʧ
		allProperties.add(new FieldProperty(1, "listable.RFConfig.psLoss",
				String.valueOf(this.getPsLoss())));
		// ����������
		allProperties.add(new FieldProperty(0,
				"listable.RFConfig.psConfigList", "listable.RFConfig.PS_Norm"));
		for (PSConfigItem psItem : psConfigList) {
			allProperties.add(new FieldProperty(1, String.valueOf(psItem
					.getAntennaIndex()), psItem.getPsNormX() + "/"
					+ psItem.getPsNormY()));
		}

		return allProperties;
	}

	@Override
	public String getBizName() {
		// TODO Auto-generated method stub
		return null;
	}

}
