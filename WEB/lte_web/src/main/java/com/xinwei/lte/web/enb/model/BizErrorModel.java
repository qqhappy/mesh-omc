package com.xinwei.lte.web.enb.model;

/**
 * eNB业务配置错误的模型
 * @author zhangqiang
 *
 */
public class BizErrorModel {
	
	/**
	 * 错误内容
	 */
	private String error = "";
	
	/**
	 * 错误类型  bizException:0
	 */
	private int errorType = 1;
	
	/**
	 * 错误字段名称
	 */
	private String errorEntity;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public String getErrorEntity() {
		return errorEntity;
	}

	public void setErrorEntity(String errorEntity) {
		this.errorEntity = errorEntity;
	}
	
	
	
}
