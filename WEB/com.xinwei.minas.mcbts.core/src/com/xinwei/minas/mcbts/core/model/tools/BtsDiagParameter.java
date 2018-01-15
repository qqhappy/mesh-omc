/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.tools;

import java.io.Serializable;

/**
 * 
 * ��ϲ���ģ��
 * 
 * @author tiance
 * 
 */

public class BtsDiagParameter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5498502717123545788L;

	//����IP
	private String ip;
	
	//�˿�
	private int port;
	
	//�û���
	private String name;
	
	//����
	private String password;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
