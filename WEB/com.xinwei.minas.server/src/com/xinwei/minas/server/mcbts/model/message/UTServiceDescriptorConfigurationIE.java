/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

/**
 * 
 * 消息：终端业务属性配置信息
 * 
 * @author fanhaoyu
 * 
 */

public class UTServiceDescriptorConfigurationIE {
	// 类型 1
	private Integer klass;

	// Bit0用于维持带宽标识 1
	private Integer bandFlag;

	// 上行最大带宽 2
	private Integer uploadMaxBandwidth;

	// 上行最小带宽 2
	private Integer uploadMinBandwidth;

	// 下行最大带宽 2
	private Integer downloadMaxBandwidth;

	// 下行最小带宽 2
	private Integer downloadMinBandwidth;

	public Integer getKlass() {
		return klass;
	}

	public void setKlass(Integer klass) {
		this.klass = klass;
	}

	public Integer getBandFlag() {
		return bandFlag;
	}

	public void setBandFlag(Integer bandFlag) {
		this.bandFlag = bandFlag;
	}

	public Integer getUploadMaxBandwidth() {
		return uploadMaxBandwidth;
	}

	public void setUploadMaxBandwidth(Integer uploadMaxBandwidth) {
		this.uploadMaxBandwidth = uploadMaxBandwidth;
	}

	public Integer getUploadMinBandwidth() {
		return uploadMinBandwidth;
	}

	public void setUploadMinBandwidth(Integer uploadMinBandwidth) {
		this.uploadMinBandwidth = uploadMinBandwidth;
	}

	public Integer getDownloadMaxBandwidth() {
		return downloadMaxBandwidth;
	}

	public void setDownloadMaxBandwidth(Integer downloadMaxBandwidth) {
		this.downloadMaxBandwidth = downloadMaxBandwidth;
	}

	public Integer getDownloadMinBandwidth() {
		return downloadMinBandwidth;
	}

	public void setDownloadMinBandwidth(Integer downloadMinBandwidth) {
		this.downloadMinBandwidth = downloadMinBandwidth;
	}

}
