package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * S1�û���ʱ�Ӳ��������ѯ
 * @author sunzhangbin
 *
 */
public class TimeDelayModel implements Serializable{

	//��ʱʱ��
	private int timeDelay;
	
	//���ؽ��
	private int result;
	
	//������Ϣ
	private String errorMsg;
	
	

	

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getTimeDelay() {
		return timeDelay;
	}

	public void setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}


	
}
