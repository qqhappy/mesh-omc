/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.corenet;

import java.io.Serializable;

/**
 * 
 * eNB相关全局配置
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbGlobalConfig implements Serializable {

	private Integer coreNetType;

	private String mcc;

	private String mnc;
	// 加密算法
	private String eea;

	// 完整性保护算法
	private String eia;

	// 是否开通视频 0:否 1:是
	private int videoSwitch = -1;

	// 视频服务IP
	private String videoIp;

	// 视频服务端口
	private String videoPort;

	public int getVideoSwitch() {
		return videoSwitch;
	}

	public void setVideoSwitch(int videoSwitch) {
		this.videoSwitch = videoSwitch;
	}

	public String getVideoIp() {
		return videoIp;
	}

	public void setVideoIp(String videoIp) {
		this.videoIp = videoIp;
	}

	public String getVideoPort() {
		return videoPort;
	}

	public void setVideoPort(String videoPort) {
		this.videoPort = videoPort;
	}

	public Integer getCoreNetType() {
		return coreNetType;
	}

	public void setCoreNetType(Integer coreNetType) {
		this.coreNetType = coreNetType;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getEea() {
		return eea;
	}

	public void setEea(String eea) {
		this.eea = eea;
	}

	public String getEia() {
		return eia;
	}

	public void setEia(String eia) {
		this.eia = eia;
	}

}
