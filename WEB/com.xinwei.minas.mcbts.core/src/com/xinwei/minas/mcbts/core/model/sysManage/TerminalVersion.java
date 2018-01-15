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
 * 描述终端的版本的基本信息,对应数据库的terminal_version
 * 
 * @author tiance
 * 
 */

public class TerminalVersion implements java.io.Serializable, Cloneable {
	// 用于终端强制升级的规则
	// 0 - 当前版本; 1 - 备份版本; 2 - 当前及备份版本
	public static final int COMPARE_ACTIVE = 0;
	public static final int COMPARE_STANDBY = 1;
	public static final int COMPARE_BOTH = 2;

	// 升级触发条件
	public static final int COMPARE_METHOD_LESS = 0;
	public static final int COMPARE_METHOD_NOT_EQUALS = 1;

	// bootloader的文件类型
	public static final int FILE_TYPE_BOOTLOADER = 1;

	// 添加的标识集合:
	public static final int SUCCESS = 1;
	public static final int ERROR_UPLOADING_TO_EMS = 0;// 上传到EMS出错
	public static final int NO_TYPE = -1; // 数据库中没有这个终端类型
	public static final int ERROR_UPLOADING_TO_FTP = -2;// 向FTP上传终端版本时出错
	public static final int EXISTS = -3; // 已存在的版本
	public static final int NO_FILE_ON_EMS = -4; // 在EMS上没有此终端版本文件
	public static final int BTS_INVALID = -5; // 基站未连接或者离线管理

	// 终端软件ID
	private Integer typeId;
	// 终端软件名称
	private String type;
	// 终端软件版本
	private String version;
	// 终端文件名称
	private String fileName;
	// 终端文件类型
	private String fileType;
	// 终端文件
	private File versionFile;
	// 强制升级时所依赖的版本: 当前版本,备份版本
	protected Integer updateDependency;
	// 强制升级的条件: 低于, 不等于
	protected Integer updateCondition;

	// 终端文件内容
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
