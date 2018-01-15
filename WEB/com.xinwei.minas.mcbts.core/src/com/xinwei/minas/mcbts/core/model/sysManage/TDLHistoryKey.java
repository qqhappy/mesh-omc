/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

/**
 * 
 * 终端软件下载历史的KEY类 Terminal Version Download History Key
 * 
 * @author tiance
 * 
 */

public class TDLHistoryKey implements java.io.Serializable {
	// 基站ID
	private Long btsId;

	// 终端类型或者基站类型
	private Integer typeId;

	// 版本
	private String version;

	// 文件类型
	private int fileType;

	public TDLHistoryKey() {
	}

	// 用于定位基站类型
	public TDLHistoryKey(Long btsId, String version) {
		this.btsId = btsId;
		this.version = version;
	}

	// 用于定位终端类型
	public TDLHistoryKey(Long btsId, Integer typeId, String version,
			int fileType) {
		this.btsId = btsId;
		this.typeId = typeId;
		this.version = version;
		this.fileType = fileType;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((btsId == null) ? 0 : btsId.hashCode());
		result = prime * result + fileType;
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		TDLHistoryKey other = (TDLHistoryKey) obj;
		if (btsId == null) {
			if (other.btsId != null)
				return false;
		} else if (!btsId.equals(other.btsId))
			return false;
		if (fileType != other.fileType)
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
