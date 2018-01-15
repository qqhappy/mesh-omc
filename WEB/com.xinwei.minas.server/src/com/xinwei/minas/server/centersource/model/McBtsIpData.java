/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.centersource.model;

import java.io.Serializable;

/**
 * 
 * 基站IP数据模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsIpData implements Serializable {

	/**
	 * 基站ID
	 */
	private long btsId;

	/**
	 * 公网IP
	 */
	private String publicIp;

	/**
	 * 公网端口
	 */
	private int publicPort;

	/**
	 * 私网IP
	 */
	private String privateIp;

	/**
	 * 私网端口
	 */
	private int privatePort;

	/**
	 * 预留
	 */
	private String reserve;

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public int getPrivatePort() {
		return privatePort;
	}

	public void setPrivatePort(int privatePort) {
		this.privatePort = privatePort;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	@Override
	public String toString() {
		return "McBtsIpData [btsId=" + btsId + ", publicIp=" + publicIp
				+ ", publicPort=" + publicPort + ", privateIp=" + privateIp
				+ ", privatePort=" + privatePort + ", reserve=" + reserve + "]";
	}

}
