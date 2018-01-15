package com.xinwei.lte.web.lte.model;

/**
 * 操作结果
 * 
 * @author weixuezheng
 * 
 */
public class OperResult {
	/**
	 * 操作结果 0 成功
	 */
	public static final int SUCCESS = 0;
	// 返回Code，0为成功，其它为失败
	private int retCode = -1;

	// 如果失败，返回错误信息
	private String retMsg;
	/**
	 * 返回的对象
	 */
	private Object returnObject = null;

	/**
	 * 操作结果是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return this.retCode == SUCCESS;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public String toString() {
		return "OperResult [retCode=" + retCode + ", retMsg=" + retMsg
				+ ", returnObject=" + returnObject + "]";
	}

}
