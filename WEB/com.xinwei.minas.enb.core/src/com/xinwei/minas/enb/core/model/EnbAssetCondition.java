/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-21	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import com.xinwei.omp.core.model.biz.PagingCondition;

/**
 * 
 * �ʲ���Ϣ��ѯ����
 * 
 * @author chenlong
 * 
 */

public class EnbAssetCondition extends PagingCondition {
	
	/**
	 * ������ĳ������ѯʱ,������ΪĬ��ֵ
	 */
	
	// ����ID
	private long id = -1;
	
	// ��վID
	private long enbId = -1;
	
	// �ʲ����к�
	private String productionSN = "";
	
	// (�����ʲ����ѯ)�ʲ�״̬ 0:���� 1:��ʱͣ��
	private int status = -1;
	
	// �ڵ�����
	private int nodeType = -1;
	
	// ��ʼʹ��ʱ�䷶Χ-��ʼ
	private long startTime_start = -1;
	
	// ��ʼʹ��ʱ�䷶Χ-����
	private long startTime_end = -1;
	
	// ��ͣʹ��ʱ�䷶Χ-��ʼ
	private long stopTime_start = -1;
	
	// ��ͣʹ��ʱ�䷶Χ-����
	private long stopTime_end = -1;
	
	// (������ʷ���ѯ)ȷ��ֹͣʹ��ʱ�䷶Χ-��ʼ
	private long confirmStopTime_start = -1;
	
	// (������ʷ���ѯ)ȷ��ֹͣʹ��ʱ�䷶Χ-����
	private long confirmStopTime_end = -1;
	
	// (������ʷ���ѯ)ȷ���û�
	private String confirmUser = "";
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getEnbId() {
		return enbId;
	}
	
	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}
	
	public String getProductionSN() {
		return productionSN;
	}
	
	public void setProductionSN(String productionSN) {
		this.productionSN = productionSN;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	
	public long getStartTime_start() {
		return startTime_start;
	}
	
	public void setStartTime_start(long startTime_start) {
		this.startTime_start = startTime_start;
	}
	
	public long getStartTime_end() {
		return startTime_end;
	}
	
	public void setStartTime_end(long startTime_end) {
		this.startTime_end = startTime_end;
	}
	
	public long getStopTime_start() {
		return stopTime_start;
	}
	
	public void setStopTime_start(long stopTime_start) {
		this.stopTime_start = stopTime_start;
	}
	
	public long getStopTime_end() {
		return stopTime_end;
	}
	
	public void setStopTime_end(long stopTime_end) {
		this.stopTime_end = stopTime_end;
	}
	
	public long getConfirmStopTime_start() {
		return confirmStopTime_start;
	}
	
	public void setConfirmStopTime_start(long confirmStopTime_start) {
		this.confirmStopTime_start = confirmStopTime_start;
	}
	
	public long getConfirmStopTime_end() {
		return confirmStopTime_end;
	}
	
	public void setConfirmStopTime_end(long confirmStopTime_end) {
		this.confirmStopTime_end = confirmStopTime_end;
	}
	
	public String getConfirmUser() {
		return confirmUser;
	}
	
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	
}
