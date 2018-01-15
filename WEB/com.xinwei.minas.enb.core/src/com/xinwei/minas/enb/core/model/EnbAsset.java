/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-19	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * �豸�ʲ�
 * 
 * @author chenlong
 * 
 */

public class EnbAsset implements Serializable {

	private static final long serialVersionUID = -1239856947839279869L;

	public static final int ASSET_STATUS_NORMAL = 0;

	public static final int ASSET_STATUS_SUSPEND = 1;

	// ����ID
	private long id;

	// �ʲ����к�
	private String productionSN;

	// ���ڻ�վ�ı�ʶ
	private long enbId;

	// �ʲ��汾��
	private String hardwareVersion;

	// �ʲ�״̬ 0:���� 1:��ʱͣ��
	private int status = ASSET_STATUS_NORMAL;

	// �ڵ����� 1-BBU 2-RRU
	private int nodeType;

	// λ����Ϣ ��ʽ:1-1-1-0(��-��-��-����)
	private String locationInfo;

	// ��Ӧ������
	private String providerName;

	// �������� yyyy-mm-dd
	private long manufactureDate;

	// ��ʼʹ��ʱ�� yyyy-mm-dd hh:mm:ss
	private long startTime;

	// ��ͣʱ�� yyyy-mm-dd hh:mm:ss
	private long stopTime;

	// ����������� yyyy-mm-dd
	private long lastServeTime;

	// ������Ϣ
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductionSN() {
		return productionSN;
	}

	public void setProductionSN(String productionSN) {
		this.productionSN = productionSN;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public long getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(long manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public long getLastServeTime() {
		return lastServeTime;
	}

	public void setLastServeTime(long lastServeTime) {
		this.lastServeTime = lastServeTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * ��Dateת��Long��
	 * 
	 * @return
	 */
	public static long getLongFromDate(Date date) {
		if (null == date) {
			return 0;
		}
		return Long
				.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
	}

	/**
	 * ��long�͸�ʽת����date��String����,ֻ֧��yyyyMMdd��yyyyMMddhhmmss����
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringFromLongDate(long date) {
		String dateStr = String.valueOf(date);
		if (dateStr.length() < 8) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(dateStr.substring(0, 4));
		result.append("-" + dateStr.substring(4, 6));
		result.append("-" + dateStr.substring(6, 8));
		if (dateStr.length() == 8) {
			return result.toString();
		}
		result.append(" " + dateStr.substring(8, 10));
		result.append(":" + dateStr.substring(10, 12));
		result.append(":" + dateStr.substring(12, 14));
		return result.toString();
	}

	/**
	 * ��byte[]���͵�λ����Ϣת����String,1-1-1-0��ʽ
	 * 
	 * @param bytes
	 * @return
	 */
	public static String getStringLocation(byte[] bytes) {
		if (null == bytes) {
			return "";
		}
		int len = 2;
		int offset = 0;
		int size = bytes.length / len;
		StringBuilder locationInfo = new StringBuilder();
		for (int i = 0; i < size; i++) {
			int locationInt = ByteUtils.toInt(bytes, offset, len);
			if (0 == i) {
				locationInfo.append(locationInt);
			} else {
				locationInfo.append("-" + locationInt);
			}
			offset += len;
		}
		return locationInfo.toString();
	}

	@Override
	public String toString() {
		return "EnbAsset [id=" + id + ", productionSN=" + productionSN
				+ ", enbId=" + enbId + ", hardwareVersion=" + hardwareVersion
				+ ", status=" + status + ", nodeType=" + nodeType
				+ ", locationInfo=" + locationInfo + ", providerName="
				+ providerName + ", manufactureDate=" + manufactureDate
				+ ", startTime=" + startTime + ", stopTime=" + stopTime
				+ ", lastServeTime=" + lastServeTime + ", remark=" + remark
				+ "]";
	}

}
