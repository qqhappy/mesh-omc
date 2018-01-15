/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-16	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * 基站版本模型
 * 
 * 
 * @author chenshaohua
 * 
 */

public class McBtsVersion implements Serializable {

	// 记录索引
	private Long idx;

	// BTS类型ID
	private Integer btsType;

	// BTS类型名称
	private String btsTypeName;

	// 版本名称
	private String versionName;

	// 版本文件名称
	private String fileName;

	// 版本文件类型
	private Integer fileType;
    
	//版本文件内容
	private byte[] fileContent;
	
	public McBtsVersion(){
		
	}
	
	public McBtsVersion(Integer btsType, String btsTypeName,
			String versionName, String fileName, Integer fileType) {
		super();
		this.btsType = btsType;
		this.btsTypeName = btsTypeName;
		this.versionName = versionName;
		this.fileName = fileName;
		this.fileType = fileType;
	}
    
	public McBtsVersion(Long idx, Integer btsType, String btsTypeName,
			String versionName, String fileName, Integer fileType,
			byte[] fileContent) {
		super();
		this.idx = idx;
		this.btsType = btsType;
		this.btsTypeName = btsTypeName;
		this.versionName = versionName;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileContent = fileContent;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getBtsType() {
		return btsType;
	}

	public void setBtsType(Integer btsType) {
		this.btsType = btsType;
	}

	public String getBtsTypeName() {
		return btsTypeName;
	}

	public void setBtsTypeName(String btsTypeName) {
		this.btsTypeName = btsTypeName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}


}
