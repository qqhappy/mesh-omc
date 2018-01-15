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
 * ����ǩ��
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class OperSignature implements Serializable {
	// ��������
	private String facade;
	// ������
	private String method;
	// ҵ����
	private String bizName;
	// �Ƿ�ͨ��ͨ��Facade����
	private boolean genericFlag = false;
	// �Ƿ�������ҵ��
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
