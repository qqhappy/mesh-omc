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
 * 地区模型
 * 
 * @author chenjunhua
 * 
 */

public class Region implements Serializable{
	
	// 地区编号(全局唯一,系统自动生成)
	private long regionId;
	
	// 地区名称
	private String name;
	
	// 地区描述
	private String desc;
	
	// 上级地区编号（根节点的上级地区编号约定为-1）
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
