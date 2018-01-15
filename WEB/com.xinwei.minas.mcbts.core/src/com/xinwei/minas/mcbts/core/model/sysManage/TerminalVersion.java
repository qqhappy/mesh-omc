/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * �����ն˵İ汾�Ļ�����Ϣ,��Ӧ���ݿ��terminal_version
 * 
 * @author tiance
 * 
 */

public class TerminalVersion implements java.io.Serializable, Cloneable {
	// �����ն�ǿ�������Ĺ���
	// 0 - ��ǰ�汾; 1 - ���ݰ汾; 2 - ��ǰ�����ݰ汾
	public static final int COMPARE_ACTIVE = 0;
	public static final int COMPARE_STANDBY = 1;
	public static final int COMPARE_BOTH = 2;

	// ������������
	public static final int COMPARE_METHOD_LESS = 0;
	public static final int COMPARE_METHOD_NOT_EQUALS = 1;

	// bootloader���ļ�����
	public static final int FILE_TYPE_BOOTLOADER = 1;

	// ��ӵı�ʶ����:
	public static final int SUCCESS = 1;
	public static final int ERROR_UPLOADING_TO_EMS = 0;// �ϴ���EMS����
	public static final int NO_TYPE = -1; // ���ݿ���û������ն�����
	public static final int ERROR_UPLOADING_TO_FTP = -2;// ��FTP�ϴ��ն˰汾ʱ����
	public static final int EXISTS = -3; // �Ѵ��ڵİ汾
	public static final int NO_FILE_ON_EMS = -4; // ��EMS��û�д��ն˰汾�ļ�
	public static final int BTS_INVALID = -5; // ��վδ���ӻ������߹���

	// �ն����ID
	private Integer typeId;
	// �ն��������
	private String type;
	// �ն�����汾
	private String version;
	// �ն��ļ�����
	private String fileName;
	// �ն��ļ�����
	private String fileType;
	// �ն��ļ�
	private File versionFile;
	// ǿ������ʱ�������İ汾: ��ǰ�汾,���ݰ汾
	protected Integer updateDependency;
	// ǿ������������: ����, ������
	protected Integer updateCondition;

	// �ն��ļ�����
	private byte[] contentStr;

	public TerminalVersion() {
	}

	public TerminalVersion(Integer typeId, String type, String version,
			String fileName, String fileType) {
		this.typeId = typeId;
		this.type = type;
		this.version = version;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setVersionFile(File versionFile) {
		this.versionFile = versionFile;
		getFileContent();
	}

	public Integer getUpdateDependency() {
		return updateDependency;
	}

	public void setUpdateDependency(Integer updateDependency) {
		this.updateDependency = updateDependency;
	}

	public Integer getUpdateCondition() {
		return updateCondition;
	}

	public void setUpdateCondition(Integer updateCondition) {
		this.updateCondition = updateCondition;
	}

	private void getFileContent() {
		FileInputStream fIn = null;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			fIn = new FileInputStream(versionFile);
			int len = 0;
			byte[] content = new byte[1001];
			while ((len = fIn.read(content, 0, 1000)) != -1) {
				out.write(content, 0, len);
			}
			out.flush();
			contentStr = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fIn != null) {
					fIn.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {

			}
		}
	}

	public byte[] getContentStr() {
		return contentStr;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
