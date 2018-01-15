/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;

/**
 * 
 * 终端的网络IP,MAC,DHCP等信息
 * 
 * 
 * @author tiance
 * 
 */

public class UserTerminalNetwork implements Serializable {
	public static final int DHCP_IP = 0x00;
	public static final int PPPOE_IP = 0x01;
	public static final int FIXED_IP = 0x02;

	private String ip;
	private String mac;
	private int dhcp;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return macFormat(mac);
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getDhcp() {
		return dhcp;
	}

	public void setDhcp(int dhcp) {
		this.dhcp = dhcp;
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
