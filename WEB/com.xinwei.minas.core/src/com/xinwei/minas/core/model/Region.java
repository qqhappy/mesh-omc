/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-10-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;

/**
 * 
 * ����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class Region implements Serializable{
	
	// �������(ȫ��Ψһ,ϵͳ�Զ�����)
	private long regionId;
	
	// ��������
	private String name;
	
	// ��������
	private String desc;
	
	// �ϼ�������ţ����ڵ���ϼ��������Լ��Ϊ-1��
	private long parentRegionId;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentRegionId() {
		return parentRegionId;
	}

	public void setParentRegionId(long parentRegionId) {
		this.parentRegionId = parentRegionId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("id=").append(regionId);
		buf.append(", name=").append(name);
		return buf.toString();
	}
}
