package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * S1用户面时延测量结果查询
 * @author sunzhangbin
 *
 */
public class TimeDelayModel implements Serializable{

	//延时时间
	private int timeDelay;
	
	//返回结果
	private int result;
	
	//错误信息
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
