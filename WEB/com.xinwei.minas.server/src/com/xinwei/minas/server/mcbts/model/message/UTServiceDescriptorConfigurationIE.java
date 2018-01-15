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
 * ��Ϣ���ն�ҵ������������Ϣ
 * 
 * @author fanhaoyu
 * 
 */

public class UTServiceDescriptorConfigurationIE {
	// ���� 1
	private Integer klass;

	// Bit0����ά�ִ����ʶ 1
	private Integer bandFlag;

	// ���������� 2
	private Integer uploadMaxBandwidth;

	// ������С���� 2
	private Integer uploadMinBandwidth;

	// ���������� 2
	private Integer downloadMaxBandwidth;

	// ������С���� 2
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
