/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-14	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * 地址模型
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class TargetAddress {

	private InetAddress inetAddress;

	private int port;

	public TargetAddress(String ip, int port) throws UnknownHostException {
		inetAddress = InetAddress.getByName(ip);
		this.port = port;
	}

	public TargetAddress(InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "TargetAddress [" + inetAddress.getHostAddress() + ":" + port
				+ "]";
	}

}
