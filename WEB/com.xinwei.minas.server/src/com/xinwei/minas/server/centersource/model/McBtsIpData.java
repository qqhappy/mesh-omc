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
 * ��վIP����ģ��
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsIpData implements Serializable {

	/**
	 * ��վID
	 */
	private long btsId;

	/**
	 * ����IP
	 */
	private String publicIp;

	/**
	 * �����˿�
	 */
	private int publicPort;

	/**
	 * ˽��IP
	 */
	private String privateIp;

	/**
	 * ˽���˿�
	 */
	private int privatePort;

	/**
	 * Ԥ��
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
