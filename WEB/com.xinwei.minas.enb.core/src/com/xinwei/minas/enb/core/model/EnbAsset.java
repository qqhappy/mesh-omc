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
 * 设备资产
 * 
 * @author chenlong
 * 
 */

public class EnbAsset implements Serializable {

	private static final long serialVersionUID = -1239856947839279869L;

	public static final int ASSET_STATUS_NORMAL = 0;

	public static final int ASSET_STATUS_SUSPEND = 1;

	// 主键ID
	private long id;

	// 资产序列号
	private String productionSN;

	// 所在基站的标识
	private long enbId;

	// 资产版本号
	private String hardwareVersion;

	// 资产状态 0:正常 1:暂时停用
	private int status = ASSET_STATUS_NORMAL;

	// 节点类型 1-BBU 2-RRU
	private int nodeType;

	// 位置信息 格式:1-1-1-0(架-框-槽-器件)
	private String locationInfo;

	// 供应商名称
	private String providerName;

	// 生产日期 yyyy-mm-dd
	private long manufactureDate;

	// 开始使用时间 yyyy-mm-dd hh:mm:ss
	private long startTime;

	// 暂停时间 yyyy-mm-dd hh:mm:ss
	private long stopTime;

	// 最近服务日期 yyyy-mm-dd
	private long lastServeTime;

	// 其他信息
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
	 * 将Date转成Long型
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
	 * 将long型格式转换成date的String类型,只支持yyyyMMdd和yyyyMMddhhmmss两种
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
	 * 将byte[]类型的位置信息转换成String,1-1-1-0格式
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
