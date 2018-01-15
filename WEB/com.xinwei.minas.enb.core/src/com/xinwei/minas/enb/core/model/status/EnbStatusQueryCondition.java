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
 * eNB״̬��ѯ����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatusQueryCondition implements Serializable {

	// �Ժ���Ϣ���в�ѯ
	private String flag;

	private Long enbId;

	private Integer rackNo;

	private Integer shelfNo;

	private Integer slotNo;
	// ��ģ��:��ģ��1����ģ��2��ȫ��
	private Integer moduleNo;
	// ѡ��ͨ�� : ͨ��1 ͨ��2 ͨ��3 ͨ��4 ȫ��
	private Integer channelNo;

	//�տ�������������IP
	private String ipAddress;
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * ����eNB״̬��ѯ����
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
	 * ��������״̬��ѯ����
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
	 * ����RRU��Ƶ״̬��ѯ����
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
	 * ����RRU���״̬��ѯ����
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
	 * ����RRU����״̬��ѯ����
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
	 * ����RRU���޲�ѯ����
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
