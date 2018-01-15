/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * eNB״̬��Ϣ��������
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatusConfigCondition implements Serializable {

	private String flag;

	private Long enbId;

	private Integer rackNo;

	private Integer shelfNo;

	private Integer slotNo;

	// ���ſ���
	private Integer powerSwitch;
	// eNBʱ��
	private Long enbTime;
	


	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getEnbId() {
		return this.enbId;
	}

	public void setEnbId(Long enbId) {
		this.enbId = enbId;
	}

	public Integer getRackNo() {
		return rackNo;
	}

	public void setRackNo(Integer rackNo) {
		this.rackNo = rackNo;
	}

	public Integer getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(Integer shelfNo) {
		this.shelfNo = shelfNo;
	}

	public Integer getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(Integer slotNo) {
		this.slotNo = slotNo;
	}

	public Integer getPowerSwitch() {
		return powerSwitch;
	}

	public void setPowerSwitch(Integer powerSwitch) {
		this.powerSwitch = powerSwitch;
	}

	public Long getEnbTime() {
		return enbTime;
	}

	public void setEnbTime(Long enbTime) {
		this.enbTime = enbTime;
	}

}
