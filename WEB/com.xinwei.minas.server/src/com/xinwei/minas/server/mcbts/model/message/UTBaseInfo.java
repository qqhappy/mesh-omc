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
 * 消息：UT BASE INFO（基站信息）
 * 
 * @author fanhaoyu
 * 
 */

public class UTBaseInfo {

	// 归属网络ID 2
	private Integer homeNetworkId;

	// 硬件类型 1
	private Integer hardwareType;

	// 软件类型 1
	private Integer softwareType;

	// 当前软件版本号4 Active SW version
	private String activeSWVersion;

	// 备用软件版本号4 Standby SW version
	private String standbySWVersion;

	// 硬件版本号 16
	private String hardwareVersion;

	public Integer getHomeNetworkId() {
		return homeNetworkId;
	}

	public void setHomeNetworkId(Integer homeNetworkId) {
		this.homeNetworkId = homeNetworkId;
	}

	public Integer getHardwareType() {
		return hardwareType;
	}

	public void setHardwareType(Integer hardwareType) {
		this.hardwareType = hardwareType;
	}

	public Integer getSoftwareType() {
		return softwareType;
	}

	public void setSoftwareType(Integer softwareType) {
		this.softwareType = softwareType;
	}

	public String getActiveSWVersion() {
		return activeSWVersion;
	}

	public void setActiveSWVersion(String activeSWVersion) {
		this.activeSWVersion = activeSWVersion;
	}

	public String getStandbySWVersion() {
		return standbySWVersion;
	}

	public void setStandbySWVersion(String standbySWVersion) {
		this.standbySWVersion = standbySWVersion;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
}
