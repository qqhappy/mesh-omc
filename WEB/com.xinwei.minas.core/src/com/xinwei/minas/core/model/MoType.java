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
 * 被管对象类型
 * 
 * @author chenjunhua
 * 
 */

public class MoType implements Serializable{

	// 类型编号
	private int typeId;
	
	// 类型名称
	private String typeName;
	
	
	public MoType(int typeId, String typeName) {
		this.setTypeId(typeId);
		this.setTypeName(typeName);
	}
	

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + typeId;
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoType other = (MoType) obj;
		if (typeId != other.typeId)
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "MoType [typeId=" + typeId + ", typeName=" + typeName + "]";
	}
	
	
	

}
