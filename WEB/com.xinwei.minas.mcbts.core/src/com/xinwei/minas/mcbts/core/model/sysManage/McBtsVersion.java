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
 * ��վ�汾ģ��
 * 
 * 
 * @author chenshaohua
 * 
 */

public class McBtsVersion implements Serializable {

	// ��¼����
	private Long idx;

	// BTS����ID
	private Integer btsType;

	// BTS��������
	private String btsTypeName;

	// �汾����
	private String versionName;

	// �汾�ļ�����
	private String fileName;

	// �汾�ļ�����
	private Integer fileType;
    
	//�汾�ļ�����
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
