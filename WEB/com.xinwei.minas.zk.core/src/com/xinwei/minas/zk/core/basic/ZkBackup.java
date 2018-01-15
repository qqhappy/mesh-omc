/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * NK数据备份模型类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class ZkBackup implements Serializable {

	public static final int CREATETYPE_AUTO = 0;

	public static final int CREATETYPE_MANUAL = 1;

	// 备份名称
	private String name;

	// 备份创建时间
	private Date createTime;

	// 备份创建方式，任务自动创建和手动创建
	private int createType;

	// 备份所占空间大小
	private double spaceSize;

	// 备份所包含的SAG群组excel文件名
	private List<String> fileList = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	public double getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(double spaceSize) {
		this.spaceSize = spaceSize;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> filePathList) {
		this.fileList = filePathList;
	}

	public void addFilePath(String filePath) {
		this.fileList.add(filePath);
	}
}