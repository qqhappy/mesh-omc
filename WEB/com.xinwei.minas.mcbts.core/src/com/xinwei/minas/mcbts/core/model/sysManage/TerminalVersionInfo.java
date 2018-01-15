/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * 终端版本的模型
 * 
 * 
 * @author tiance
 * 
 */

public class TerminalVersionInfo {
	public static final int APPTYPE_UNKNOWN = 0;
	public static final int APPTYPE_BOOTLOADER = 1;
	public static final int APPTYPE_WORK = 2;
	public static final int APPTYPE_TESTER = 3;
	public static final int APPTYPE_REPEATER_DSP = 999;

	public static final int APPTYPE_NE_RRU_MCU = 10001;
	public static final int APPTYPE_NE_RRU_FPGA = 10002;

	// public static final int APPTYPE_NE_BBUA = 2;
	// public static final int APPTYPE_NE_RRUA = 3;

	public static final int APPTYPE_NE_BBU_RRU = 2;

	public static final String RRU_MCU_VERSION_PREFIX = "RRU_MCU";
	public static final String RRU_FPGA_VERSION_PREFIX = "RRU_FPGA";

	public static final String BBU_RRU_VERSION_PREFIX = "BBU_RRU";

	// public static final String RRUA_VERSION_PREFIX = "RRUA";
	// public static final String BBUA_VERSION_PREFIX = "BBUA";

	public static final String RPT_CPE_VERSION_PREFIX = "RPT_CPE";
	public static final String RPT_DSP_VERSION_PREFIX = "RPT_BB";
	// public static final String RPT_BOOTLOADER_VERSION_PREFIX =
	// "BOOTLOADER_CPER_RPT";

	private String versionName;
	private Long utTypeId;
	private File versionFile;
	private boolean ut;
	private String filename;
	private byte[] contentStr;
	private String typeNameCN;
	private String typeNameEN;
	private int appType;

	public TerminalVersionInfo() {
	}

	/**
	 * @param typeNameCN
	 *            The typeNameCN to set.
	 */
	public void setTypeNameCN(String typeNameCN) {
		this.typeNameCN = typeNameCN;
	}

	/**
	 * @param typeNameEN
	 *            The typeNameEN to set.
	 */
	public void setTypeNameEN(String typeNameEN) {
		this.typeNameEN = typeNameEN;
	}

	/**
	 * @return Returns the typeName.
	 */
	public String getTypeName() {

		// TODO 判断系统是中文还是英文
		// if ("EN".equalsIgnoreCase(SystemConst.getProperty("platform"))) {
		// return typeNameEN;
		// }
		return typeNameCN;
	}

	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return Returns the utTypeId.
	 */
	public Long getTypeId() {
		return utTypeId;
	}

	/**
	 * @param utTypeId
	 *            The utTypeId to set.
	 */
	public void setTypeId(Long utTypeId) {
		this.utTypeId = utTypeId;
	}

	/**
	 * @return Returns the versionFile.
	 */
	public File getVersionFile() {
		return versionFile;
	}

	/**
	 * @param versionFile
	 *            The versionFile to set.
	 */
	public void setVersionFile(File versionFile) {
		this.versionFile = versionFile;
		this.filename = versionFile.getName();
		getFileContent();
	}

	/**
	 * @return Returns the versionName.
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            The versionName to set.
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return Returns the ut.
	 */
	public boolean isUt() {
		return ut;
	}

	/**
	 * @param ut
	 *            The ut to set.
	 */
	public void setUt(boolean ut) {
		this.ut = ut;
	}

	/**
	 * @return Returns the appType.
	 */
	public int getAppType() {
		return appType;
	}

	/**
	 * @param appType
	 *            The appType to set.
	 */
	public void setAppType(int appType) {
		this.appType = appType;
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

	public byte[] getContentString() {
		return this.contentStr;
	}
}
