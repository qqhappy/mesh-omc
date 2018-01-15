/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-9	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu;

import java.io.Serializable;

/**
 * 
 * 操作签名
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class OperSignature implements Serializable {
	// 门面名称
	private String facade;
	// 方法名
	private String method;
	// 业务名
	private String bizName;
	// 是否通过通用Facade配置
	private boolean genericFlag = false;
	// 是否是特殊业务
	private boolean specialFlag = false;

	public String getFacade() {
		return facade;
	}

	public void setFacade(String facade) {
		this.facade = facade;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public void setGenericFlag(boolean genericFlag) {
		this.genericFlag = genericFlag;
	}

	public boolean isGenericBiz() {
		return genericFlag;
	}

	public void setSpecialFlag(boolean specialFlag) {
		this.specialFlag = specialFlag;
	}

	public boolean isSpecialBiz() {
		return specialFlag;
	}

	@Override
	public String toString() {
		return "OperSignature [facade=" + facade + ", method=" + method
				+ ", bizName=" + bizName + ", genericFlag=" + genericFlag + "]";
	}

}
