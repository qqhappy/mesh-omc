/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;


/**
 * 
 * 前端OdbLockStatus类
 * 
 * @author chenshaohua
 * 
 */

public class ClientOdbLockStatus {

	private ClientLockStatus[] lockStatusItems = new ClientLockStatus[11];// 锁编号及状态列表，目前只有11个锁状态

	public ClientLockStatus[] getLockStatusItems() {
		return lockStatusItems;
	}

	public void setLockStatusItems(ClientLockStatus[] lockStatusItems) {
		this.lockStatusItems = lockStatusItems;
	}

	
}
