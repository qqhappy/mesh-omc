/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-16	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.meta;


/**
 * 
 * 适配配置模型Biz
 * 
 * @author chenshaohua
 * 
 */

public class OssBiz {
	
	public static final int ACTION_ADD = 0x01;
	public static final int ACTION_DELETE = 0x02;
	public static final int ACTION_MODIFY = 0x03;
	public static final int ACTION_QUERY = 0x04;
	public static final int ACTION_LIST = 0x05;
	public static final int ACTION_PERCENT = 0x07;
	public static final int ACTION_BATCH_ADD = 0x08;	
	public static final int ACTION_BACKUP = 0x09;
	public static final int ACTION_RESTORE = 0x0A;
	
	
	private String operId;

	private String action;

	private String bizName;

	private OssOperation oss;

	private OssAdapterRequest request;

	private OssAdapterResponse response;
	
	/**
	 * 该业务是否应该被记录日志
	 * @return
	 */
	public boolean shouldLog() {
		int action = getIntAction();
		return (action != ACTION_QUERY && action != ACTION_LIST && action != ACTION_PERCENT);
	}
	
	public int getIntOperId() {
		String operIdStr = getOperId();
		int operIdInt;
		if (operIdStr.startsWith("0x")) {
			operIdInt = Integer.parseInt(operIdStr.substring(2), 16);
		} else {
			operIdInt = Integer.parseInt(operIdStr);
		}
		return operIdInt;
	}
	
	public int getIntAction() {
		String actionStr = getAction();
		int actionInt;
		if (actionStr.startsWith("0x")) {
			actionInt = Integer.parseInt(actionStr.substring(2), 16);
		} else {
			actionInt = Integer.parseInt(actionStr);
		}
		return actionInt;
	}
	
	public int getIntOperType() {
		String ossOperTypeStr = getOss().getOperType();
		int ossOperType;

		if (ossOperTypeStr.startsWith("0x")) {
			ossOperType = Integer.parseInt(ossOperTypeStr.substring(2), 16);
		} else {
			ossOperType = Integer.parseInt(ossOperTypeStr);
		}
		return ossOperType;
	}

	public int getIntOperObject() {
		String ossOperObjectStr = getOss().getOperObject();
		int ossOperObject;
		if (ossOperObjectStr.startsWith("0x")) {
			ossOperObject = Integer.parseInt(ossOperObjectStr.substring(2), 16);
		} else {
			ossOperObject = Integer.parseInt(ossOperObjectStr);
		}

		return ossOperObject;
	}


	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public OssOperation getOss() {
		return oss;
	}

	public void setOss(OssOperation oss) {
		this.oss = oss;
	}

	public OssAdapterRequest getRequest() {
		return request;
	}

	public void setRequest(OssAdapterRequest request) {
		this.request = request;
	}

	public OssAdapterResponse getResponse() {
		return response;
	}

	public void setResponse(OssAdapterResponse response) {
		this.response = response;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
}
