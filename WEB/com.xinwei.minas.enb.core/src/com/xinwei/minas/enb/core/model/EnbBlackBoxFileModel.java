package com.xinwei.minas.enb.core.model;

import java.util.Date;

import com.xinwei.minas.core.model.Mo;

public class EnbBlackBoxFileModel extends Mo {

	//enbid
	private String enbId;
	//�ļ��Ĵ���ʱ��
	private Date fileTime;
	//��λԭ��
	private String cause;
	//�ļ�����
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
