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

/**
 * 
 * У׼������Ϣ����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationResult implements Serializable {

	public static final int ANT_COUNT = 8;

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// ���߲�������
	private List<AntennaCalibrationConfig> antConfigList = new ArrayList<AntennaCalibrationConfig>();
	// Ƶ����������ʶ Ƶ��
	private int frequencyMaskCalibration;
	// ��ʼ��λ
	private int initialPhaseCalibration;
	// ����У׼
	private int txCalibration;
	// ����Ƚ�
	private int plusComparison;

	// SYNУ׼
	private int synSanityCalibration;
	// SYN����У׼
	private int synTxCalibration;
	// SYN��ʼ��λУ׼
	private int synInitialPhaseCalibration;
	// SYN����Ƚ�
	private int synPlusComparison;
	// ԤУ��
	private int preDistortion;
	// ԤУ׼����
	private int precalibrationRx;
	// ԤУ׼����
	private int precalibrationTx;

	private CalibrationResultGeneralConfig calibrationResultGeneralConfig;

	List<CalibrationResultDataInfo> lstNotiTX_I = new ArrayList<CalibrationResultDataInfo>();
	List<CalibrationResultDataInfo> lstNotiTX_Q = new ArrayList<CalibrationResultDataInfo>();
	List<CalibrationResultDataInfo> lstNotiRX_I = new ArrayList<CalibrationResultDataInfo>();
	List<CalibrationResultDataInfo> lstNotiRX_Q = new ArrayList<CalibrationResultDataInfo>();

	public CalibrationResultGeneralConfig getCalibrationResultGeneralConfig() {
		return calibrationResultGeneralConfig;
	}

	public void setCalibrationResultGeneralConfig(
			CalibrationResultGeneralConfig calibrationResultGeneralConfig) {
		this.calibrationResultGeneralConfig = calibrationResultGeneralConfig;
	}

	public List<CalibrationResultDataInfo> getLstNotiTX_I() {
		return lstNotiTX_I;
	}

	public void setLstNotiTX_I(List<CalibrationResultDataInfo> lstNotiTX_I) {
		this.lstNotiTX_I = lstNotiTX_I;
	}

	public List<CalibrationResultDataInfo> getLstNotiTX_Q() {
		return lstNotiTX_Q;
	}

	public void setLstNotiTX_Q(List<CalibrationResultDataInfo> lstNotiTX_Q) {
		this.lstNotiTX_Q = lstNotiTX_Q;
	}

	public List<CalibrationResultDataInfo> getLstNotiRX_I() {
		return lstNotiRX_I;
	}

	public void setLstNotiRX_I(List<CalibrationResultDataInfo> lstNotiRX_I) {
		this.lstNotiRX_I = lstNotiRX_I;
	}

	public List<CalibrationResultDataInfo> getLstNotiRX_Q() {
		return lstNotiRX_Q;
	}

	public void setLstNotiRX_Q(List<CalibrationResultDataInfo> lstNotiRX_Q) {
		this.lstNotiRX_Q = lstNotiRX_Q;
	}

	public int getFrequencyMaskCalibration() {
		return frequencyMaskCalibration;
	}

	public void setFrequencyMaskCalibration(int frequencyMaskCalibration) {
		this.frequencyMaskCalibration = frequencyMaskCalibration;
	}

	public int getInitialPhaseCalibration() {
		return initialPhaseCalibration;
	}

	public void setInitialPhaseCalibration(int initialPhaseCalibration) {
		this.initialPhaseCalibration = initialPhaseCalibration;
	}

	public int getTxCalibration() {
		return txCalibration;
	}

	public void setTxCalibration(int txCalibration) {
		this.txCalibration = txCalibration;
	}

	public int getPlusComparison() {
		return plusComparison;
	}

	public void setPlusComparison(int plusComparison) {
		this.plusComparison = plusComparison;
	}

	public int getSynSanityCalibration() {
		return synSanityCalibration;
	}

	public void setSynSanityCalibration(int synSanityCalibration) {
		this.synSanityCalibration = synSanityCalibration;
	}

	public int getSynTxCalibration() {
		return synTxCalibration;
	}

	public void setSynTxCalibration(int synTxCalibration) {
		this.synTxCalibration = synTxCalibration;
	}

	public int getSynInitialPhaseCalibration() {
		return synInitialPhaseCalibration;
	}

	public void setSynInitialPhaseCalibration(int synInitialPhaseCalibration) {
		this.synInitialPhaseCalibration = synInitialPhaseCalibration;
	}

	public int getSynPlusComparison() {
		return synPlusComparison;
	}

	public void setSynPlusComparison(int synPlusComparison) {
		this.synPlusComparison = synPlusComparison;
	}

	public int getPreDistortion() {
		return preDistortion;
	}

	public void setPreDistortion(int preDistortion) {
		this.preDistortion = preDistortion;
	}

	public int getPrecalibrationRx() {
		return precalibrationRx;
	}

	public void setPrecalibrationRx(int precalibrationRx) {
		this.precalibrationRx = precalibrationRx;
	}

	public int getPrecalibrationTx() {
		return precalibrationTx;
	}

	public void setPrecalibrationTx(int precalibrationTx) {
		this.precalibrationTx = precalibrationTx;
	}

	public CalibrationResult() {
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	/**
	 * @return the antConfigList
	 */
	public List<AntennaCalibrationConfig> getAntConfigList() {
		return antConfigList;
	}

	public void populateAntennaConfigList() {
		// ������߲���
		antConfigList.clear();

		// ���λ�ȡ���߲�������
		for (int dataType = 0; dataType < 4; dataType++) {
			List<CalibrationResultDataInfo> dataInfoList = null;
			switch (dataType) {
				case 0:
					dataInfoList = lstNotiTX_I;
					break;
				case 1:
					dataInfoList = lstNotiTX_Q;
					break;
				case 2:
					dataInfoList = lstNotiRX_I;
					break;
				case 3:
					dataInfoList = lstNotiRX_Q;
					break;
			}

			for (CalibrationResultDataInfo dataInfo : dataInfoList) {
				AntennaCalibrationConfig config = new AntennaCalibrationConfig();
				config.setAntennaIndex(dataInfo.getAntennalIndex());
				config.setDataType(dataInfo.getType());
				config.setMoId(moId);
				int[] carriData = dataInfo.getCALdataforSubcarrieri();
				for (int i = 0; i < carriData.length; i++) {
					AntennaCalibItem item = new AntennaCalibItem();
					item.setCarrierData(carriData[i]);
					item.setRowIndex(i + dataInfo.getSCstartingIndex());
					config.getAntItemList().add(item);
				}
				antConfigList.add(config);
			}
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
				+ ((antConfigList == null) ? 0 : antConfigList.hashCode());
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
		CalibrationResult other = (CalibrationResult) obj;
		if (antConfigList == null) {
			if (other.antConfigList != null)
				return false;
		} else if (!antConfigList.equals(other.antConfigList))
			return false;
		return true;
	}
}
