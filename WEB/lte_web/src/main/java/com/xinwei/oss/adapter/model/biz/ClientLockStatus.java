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
 * 前端LockStatus类
 * 
 * @author chenshaohua
 * 
 */

public class ClientLockStatus {

	private String lockName;// 锁名称

	private int lockId;// 锁编号

	private byte lockStatus;// 锁状态

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public int getLockId() {
		return lockId;
	}

	public void setLockId(int lockId) {
		this.lockId = lockId;
	}

	public byte getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(byte lockStatus) {
		this.lockStatus = lockStatus;
	}
}
