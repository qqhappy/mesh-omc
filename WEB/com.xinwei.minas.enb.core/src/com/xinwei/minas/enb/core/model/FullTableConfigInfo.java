package com.xinwei.minas.enb.core.model;

import java.io.Serializable;
import java.util.Date;

public class FullTableConfigInfo implements Serializable{
	
	//������
	public static final int CONFIGING = 0;
	
	//����ʧ��
	public static final int CONFIG_FAIL = 1;
	
	//���óɹ�
	public static final int CONFIG_SUCCESS = 2;
	
	//δ��ʼ����
	public static final int CONFIG_UNSTART = -1;
	
	//���ݿ��ʶ
	private Long idx;
	
	//Enb��ʶ
	private long moId;
	
	//��������״̬
	private int configStatus;
	
	//��ʼʱ��
	private Date startConfigTime;
	
	//������Ϣ
	private String errorMessage;
	
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public long getMoId() {
		return moId;
	}
	public void setMoId(long moId) {
		this.moId = moId;
	}
	
	public Date getStartConfigTime() {
		return startConfigTime;
	}
	public void setStartConfigTime(Date startConfigTime) {
		this.startConfigTime = startConfigTime;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getConfigStatus() {
		return configStatus;
	}
	public void setConfigStatus(int configStatus) {
		this.configStatus = configStatus;
	}
}
