/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * 单板状态信息
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class BoardStatus implements Serializable {

	private int rackNo;

	private int shelfNo;

	private int slotNo;
	// 单板状态
	private int status;
	// 硬件版本号
	private String hardwareVersion;
	// 单板硬件序列号
	private String hardwareSerialNo;

	public int getRackNo() {
		return rackNo;
	}

	public void setRackNo(int rackNo) {
		this.rackNo = rackNo;
	}

	public int getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(int shelfNo) {
		this.shelfNo = shelfNo;
	}

	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public String getHardwareSerialNo() {
		return hardwareSerialNo;
	}

	public void setHardwareSerialNo(String hardwareSerialNo) {
		this.hardwareSerialNo = hardwareSerialNo;
	}

}
