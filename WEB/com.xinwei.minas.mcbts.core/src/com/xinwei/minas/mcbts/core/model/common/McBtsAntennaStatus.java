package com.xinwei.minas.mcbts.core.model.common;

import com.xinwei.omp.core.utils.ByteUtils;

public class McBtsAntennaStatus implements java.io.Serializable {

	public static final int LENGTH = 2 * 8;
	// 射频板状态8
	// 硬件版本号
	private int hardwareVersion;
	// 软件版本号
	private int softwareVersion;
	// 板电压值
	private double boardVoltageValue;
	// 板电流值
	private double boardCurrentValue;
	// 塔放电压值
	private double ttaVoltageValue;
	// 塔放电流值
	private double ttaCurrentValue;
	// 发送功率
	private double transmitPowerValue;
	// 天线接收出错次数
	private int receivingErrorCounter;

	public McBtsAntennaStatus() {

	}

	public McBtsAntennaStatus(byte[] buf, int offset) {
		hardwareVersion = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		softwareVersion = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		boardVoltageValue = ByteUtils.toSignedNumber(buf, offset, 2) / 16.0;
		offset += 2;

		boardCurrentValue = ByteUtils.toSignedNumber(buf, offset, 2) / 16.0;
		offset += 2;

		ttaVoltageValue = ByteUtils.toSignedNumber(buf, offset, 2) / 16.0;
		offset += 2;

		ttaCurrentValue = ByteUtils.toSignedNumber(buf, offset, 2) / 16.0;
		offset += 2;

		transmitPowerValue = ByteUtils.toSignedNumber(buf, offset, 2) / 2.0;
		offset += 2;

		receivingErrorCounter = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
	}

	public int getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(int hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public int getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(int softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public double getBoardVoltageValue() {
		return boardVoltageValue;
	}

	public void setBoardVoltageValue(double boardVoltageValue) {
		this.boardVoltageValue = boardVoltageValue;
	}

	public double getBoardCurrentValue() {
		return boardCurrentValue;
	}

	public void setBoardCurrentValue(double boardCurrentValue) {
		this.boardCurrentValue = boardCurrentValue;
	}

	public double getTtaVoltageValue() {
		return ttaVoltageValue;
	}

	public void setTtaVoltageValue(double ttaVoltageValue) {
		this.ttaVoltageValue = ttaVoltageValue;
	}

	public double getTtaCurrentValue() {
		return ttaCurrentValue;
	}

	public void setTtaCurrentValue(double ttaCurrentValue) {
		this.ttaCurrentValue = ttaCurrentValue;
	}

	public double getTransmitPowerValue() {
		return transmitPowerValue;
	}

	public void setTransmitPowerValue(int transmitPowerValue) {
		this.transmitPowerValue = transmitPowerValue;
	}

	public int getReceivingErrorCounter() {
		return receivingErrorCounter;
	}

	public void setReceivingErrorCounter(int receivingErrorCounter) {
		this.receivingErrorCounter = receivingErrorCounter;
	}
}
