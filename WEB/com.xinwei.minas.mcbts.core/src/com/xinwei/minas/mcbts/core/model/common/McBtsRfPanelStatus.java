/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * rf面板状态实体类
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatus implements Serializable {

	private Long btsId;

	private int antennaNum = 8;

	// 天线状态(8路)
	private McBtsAntennaStatus[] antennaStatuses = new McBtsAntennaStatus[antennaNum];

	// 频综板状态4
	// 频综板硬件版本号
	private int synHardwareVersion;

	// 频综板软件版本号
	private int synSoftwareVersion;

	// 晶振偏差
	private int txxoOffset;

	// 频综板接收出错次数
	private int synReceivingErrorCounter;

	public int getSynSoftwareVersion() {
		return synSoftwareVersion;
	}

	public void setSynSoftwareVersion(int synSoftwareVersion) {
		this.synSoftwareVersion = synSoftwareVersion;
	}

	public McBtsRfPanelStatus() {

	}

	public McBtsRfPanelStatus(Long btsId, byte[] buf) {
		this(btsId, buf, 0);
	}

	public McBtsRfPanelStatus(Long btsId, byte[] buf, int offset) {
		this.setBtsId(btsId);
		// 将字节转换为天线模型

		for (int i = 0; i < antennaNum; i++) {
			McBtsAntennaStatus antennaStatus = new McBtsAntennaStatus(buf,
					offset);
			antennaStatuses[i] = antennaStatus;
			offset += McBtsAntennaStatus.LENGTH;
		}
		// 将字节转换为模型
		synHardwareVersion = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		synSoftwareVersion = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		txxoOffset = (int) ByteUtils.toSignedNumber(buf, offset, 2);
		offset += 2;

		synReceivingErrorCounter = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public McBtsAntennaStatus[] getAntennaStatuses() {
		return antennaStatuses;
	}

	public void setAntennaStatuses(McBtsAntennaStatus[] antennaStatuses) {
		this.antennaStatuses = antennaStatuses;
	}

	public int getSynHardwareVersion() {
		return synHardwareVersion;
	}

	public void setSynHardwareVersion(int synHardwareVersion) {
		this.synHardwareVersion = synHardwareVersion;
	}

	public int getTxxoOffset() {
		return txxoOffset;
	}

	public void setTxxoOffset(int txxoOffset) {
		this.txxoOffset = txxoOffset;
	}

	public int getSynReceivingErrorCounter() {
		return synReceivingErrorCounter;
	}

	public void setSynReceivingErrorCounter(int synReceivingErrorCounter) {
		this.synReceivingErrorCounter = synReceivingErrorCounter;
	}

}
