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
 * rf���״̬ʵ����
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatus implements Serializable {

	private Long btsId;

	private int antennaNum = 8;

	// ����״̬(8·)
	private McBtsAntennaStatus[] antennaStatuses = new McBtsAntennaStatus[antennaNum];

	// Ƶ�۰�״̬4
	// Ƶ�۰�Ӳ���汾��
	private int synHardwareVersion;

	// Ƶ�۰�����汾��
	private int synSoftwareVersion;

	// ����ƫ��
	private int txxoOffset;

	// Ƶ�۰���ճ������
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
		// ���ֽ�ת��Ϊ����ģ��

		for (int i = 0; i < antennaNum; i++) {
			McBtsAntennaStatus antennaStatus = new McBtsAntennaStatus(buf,
					offset);
			antennaStatuses[i] = antennaStatus;
			offset += McBtsAntennaStatus.LENGTH;
		}
		// ���ֽ�ת��Ϊģ��
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
