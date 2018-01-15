/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端IPGroups实体类
 * 
 * @author chenshaohua
 * 
 */

public class ClientIPGroups {

	private byte num;

	private ClientIPGroup[] ClientIPGroupArray;

	public byte getNum() {
		return num;
	}

	public void setNum(byte num) {
		this.num = num;
	}

	public ClientIPGroup[] getClientIPGroupArray() {
		return ClientIPGroupArray;
	}

	public void setClientIPGroupArray(ClientIPGroup[] clientIPGroupArray) {
		ClientIPGroupArray = clientIPGroupArray;
	}
}
