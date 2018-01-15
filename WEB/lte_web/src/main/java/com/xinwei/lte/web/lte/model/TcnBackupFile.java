/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-08	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.model;

/**
 * 
 * TCN1000数据库备份文件模型
 * 
 * @author chenjunhua
 * 
 */
public class TcnBackupFile {

	/**
	 * 文件名称
	 */
	private String fileName;
	
	public TcnBackupFile() {
		
	}

	public TcnBackupFile(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
