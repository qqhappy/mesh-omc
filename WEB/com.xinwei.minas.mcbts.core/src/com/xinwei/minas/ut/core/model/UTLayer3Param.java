/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;

/**
 * 
 * 终端三层参数模型
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTLayer3Param implements Serializable{

	private final static int DHCP_IP = 0;
	
	private final static int PPPOE_IP = 1;
	
	private final static int FIX_IP = 2;
	
	private String ipAddress; //4
	
	private String mac; //6
	
	private String ipMask; //4
	
	private String ipGateWay; //4
	
	private int dhcpFlag; // 2

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMac() {
		return macFormat(mac);
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIpMask() {
		return ipMask;
	}

	public void setIpMask(String ipMask) {
		this.ipMask = ipMask;
	}

	public String getIpGateWay() {
		return ipGateWay;
	}

	public void setIpGateWay(String ipGateWay) {
		this.ipGateWay = ipGateWay;
	}

	public int getDhcpFlag() {
		return dhcpFlag;
	}

	public void setDhcpFlag(int dhcpFlag) {
		this.dhcpFlag = dhcpFlag;
	}
	
	
	private String macFormat(String mac) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length();) {
			sb.append("-").append(mac.substring(i, i + 2));
			i += 2;
		}
		return sb.toString().substring(1);
	}
}
