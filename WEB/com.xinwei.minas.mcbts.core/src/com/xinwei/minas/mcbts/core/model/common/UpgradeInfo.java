/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-12	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.Date;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * ��վ��������ģ��
 * 
 * 
 * @author tiance
 * 
 */

public class UpgradeInfo implements Serializable {

	public static final int START_IMMEDIATELY = 1;
	public static final int START_SCHEDULED = 0;

	// ��δ��ʼ
	public static final int STATUS_NOT_START = 0;
	// ��������
	public static final int STATUS_DOWNLOADING = 10;
	// ����ʧ��
	public static final int STATUS_DOWNLOAD_FAIL = 20;
	// ���س�ʱ
	public static final int STATUS_DOWNLOAD_TIMEOUT = 30;
	// �������
	public static final int STATUS_DOWNLOAD_FINISH = 40;
	// �����������·�
	public static final int STATUS_UPGRADE = 50;
	// ����ʧ��
	public static final int STATUS_UPGRADE_FAIL = 60;
	// ��վδ����
	public static final int STATUS_MCBTS_DISCONNECTED = 70;
	// ��վ���߹���
	public static final int STATUS_MCBTS_OFFLINE_MANAGE = 80;
	// ��վ������
	public static final int STATUS_MCBTS_NOT_EXISTS = 90;
	// ��������ֹ
	public static final int STATUS_TERMINATED = 100;

	// ����Ŀ��
	public static final int TARGET_BBU = 0;
	public static final int TARGET_RRU = 1;
	public static final int TARGET_BBU_RRU = 2;

	// ���ݿ�洢��ID
	private long idx;
	// ��վmoId
	private long moId;
	// ��վID(16����,��McBts�е�getHexBtsId())
	private String btsId;
	// ��վ����
	private String name;
	// ��վ����
	private int btsType;
	// ״̬
	private int status;
	// ���صİ汾
	private String downloadVersion;
	// ��ǰ�汾,������Զ��վ��ʾBBU�汾
	private String version;
	// mcu�汾
	private String mcuVersion;
	// fpga�汾
	private String fpgaVersion;
	// ������Զ������Ŀ��
	private int target;
	// ԤԼִ��ʱ��
	private Date ScheduledTime;
	// ��ʼ��ʶ(����ִ��/ԤԼִ��)
	private int startSign;
	// ��ʼ��������ʱ��
	private Date startTime;
	// ����ʱ��
	private Date endTime;

	private int errorCode;
	
	//��Ϣ���к�
	private int transactionId;

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getBtsId() {
		return btsId;
	}

	public void setBtsId(String btsId) {
		this.btsId = btsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBtsType() {
		return btsType;
	}

	public void setBtsType(int btsType) {
		this.btsType = btsType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDownloadVersion() {
		return downloadVersion;
	}

	public void setDownloadVersion(String downloadVersion) {
		this.downloadVersion = downloadVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMcuVersion() {
		return mcuVersion;
	}

	public void setMcuVersion(String mcuVersion) {
		this.mcuVersion = mcuVersion;
	}

	public String getFpgaVersion() {
		return fpgaVersion;
	}

	public void setFpgaVersion(String fpgaVersion) {
		this.fpgaVersion = fpgaVersion;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public Date getScheduledTime() {
		return ScheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		ScheduledTime = scheduledTime;
	}

	public int getStartSign() {
		return startSign;
	}

	public void setStartSign(int startSign) {
		this.startSign = startSign;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionId() {
		return transactionId;
	}

}
