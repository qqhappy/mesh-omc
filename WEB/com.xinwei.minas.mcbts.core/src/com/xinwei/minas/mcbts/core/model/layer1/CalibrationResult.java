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
 * 校准基本信息配置模型
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationResult implements Serializable {

	public static final int ANT_COUNT = 8;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 天线参数配置
	private List<AntennaCalibrationConfig> antConfigList = new ArrayList<AntennaCalibrationConfig>();
	// 频率掩码错误标识 频响
	private int frequencyMaskCalibration;
	// 初始相位
	private int initialPhaseCalibration;
	// 发送校准
	private int txCalibration;
	// 增益比较
	private int plusComparison;

	// SYN校准
	private int synSanityCalibration;
	// SYN发送校准
	private int synTxCalibration;
	// SYN初始相位校准
	private int synInitialPhaseCalibration;
	// SYN增益比较
	private int synPlusComparison;
	// 预校正
	private int preDistortion;
	// 预校准接收
	private int precalibrationRx;
	// 预校准发送
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
		// 清空天线参数
		antConfigList.clear();

		// 依次获取天线参数定义
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
