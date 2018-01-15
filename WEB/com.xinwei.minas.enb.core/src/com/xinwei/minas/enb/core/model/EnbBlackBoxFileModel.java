package com.xinwei.minas.enb.core.model;

import java.util.Date;

import com.xinwei.minas.core.model.Mo;

public class EnbBlackBoxFileModel extends Mo {

	//enbid
	private String enbId;
	//文件的创建时间
	private Date fileTime;
	//复位原因
	private String cause;
	//文件名字
	private String fileName;
	
	public Date getFileTime() {
		return fileTime;
	}

	public void setFileTime(Date fileTime) {
		this.fileTime = fileTime;
	}

	public String getEnbId() {
		return enbId;
	}

	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
