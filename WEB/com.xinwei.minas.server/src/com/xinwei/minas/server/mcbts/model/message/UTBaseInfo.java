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
 * ��Ϣ��UT BASE INFO����վ��Ϣ��
 * 
 * @author fanhaoyu
 * 
 */

public class UTBaseInfo {

	// ��������ID 2
	private Integer homeNetworkId;

	// Ӳ������ 1
	private Integer hardwareType;

	// ������� 1
	private Integer softwareType;

	// ��ǰ����汾��4 Active SW version
	private String activeSWVersion;

	// ��������汾��4 Standby SW version
	private String standbySWVersion;

	// Ӳ���汾�� 16
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
