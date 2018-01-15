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
 * eNB状态查询条件
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatusQueryCondition implements Serializable {

	// 对何信息进行查询
	private String flag;

	private Long enbId;

	private Integer rackNo;

	private Integer shelfNo;

	private Integer slotNo;
	// 光模块:光模块1、光模块2、全部
	private Integer moduleNo;
	// 选择通道 : 通道1 通道2 通道3 通道4 全部
	private Integer channelNo;

	//空口流量测试设置IP
	private String ipAddress;
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * 创建eNB状态查询条件
	 * 
	 * @param enbId
	 * @return
	 */
	public static EnbStatusQueryCondition createEnbStatusConditon(long enbId) {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(EnbStatusConstants.ENB_STATUS);
		condition.setEnbId(enbId);
		return condition;
	}

	/**
	 * 创建单板状态查询条件
	 * 
	 * @param enbId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @return
	 */
	public static EnbStatusQueryCondition createBoardStatusCondition(
			long enbId, int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = createEnbStatusConditon(enbId);
		condition.setFlag(EnbStatusConstants.BOARD_STATUS);
		condition.setRackNo(rackNo);
		condition.setShelfNo(shelfNo);
		condition.setSlotNo(slotNo);
		return condition;
	}

	/**
	 * 创建RRU射频状态查询条件
	 * 
	 * @param enbId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param channelNo
	 * @return
	 */
	public static EnbStatusQueryCondition createRruRfStatusCondition(
			long enbId, int rackNo, int shelfNo, int slotNo, int channelNo) {
		EnbStatusQueryCondition condition = createBoardStatusCondition(enbId,
				rackNo, shelfNo, slotNo);
		condition.setFlag(EnbStatusConstants.RRU_RF_STATUS);
		condition.setChannelNo(channelNo);
		return condition;
	}

	/**
	 * 创建RRU光口状态查询条件
	 * 
	 * @param enbId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param moduleNo
	 * @return
	 */
	public static EnbStatusQueryCondition createRruOpticalStatusCondition(
			long enbId, int rackNo, int shelfNo, int slotNo, int moduleNo) {
		EnbStatusQueryCondition condition = createBoardStatusCondition(enbId,
				rackNo, shelfNo, slotNo);
		condition.setFlag(EnbStatusConstants.RRU_OPTICAL_STATUS);
		condition.setModuleNo(moduleNo);
		return condition;
	}

	/**
	 * 创建RRU运行状态查询条件
	 * 
	 * @param enbId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @return
	 */
	public static EnbStatusQueryCondition createRruRunningStatusCondition(
			long enbId, int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = createBoardStatusCondition(enbId,
				rackNo, shelfNo, slotNo);
		condition.setFlag(EnbStatusConstants.RRU_RUNNING_STATUS);
		return condition;
	}

	/**
	 * 创建RRU门限查询条件
	 * 
	 * @param enbId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @return
	 */
	public static EnbStatusQueryCondition createRruThresholdStatusCondition(
			long enbId, int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = createBoardStatusCondition(enbId,
				rackNo, shelfNo, slotNo);
		condition.setFlag(EnbStatusConstants.RRU_THRESHOLD_STATUS);
		return condition;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

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

	public int getModuleNo() {
		return moduleNo;
	}

	public void setModuleNo(int moduleNo) {
		this.moduleNo = moduleNo;
	}

	public int getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(int channelNo) {
		this.channelNo = channelNo;
	}

}
