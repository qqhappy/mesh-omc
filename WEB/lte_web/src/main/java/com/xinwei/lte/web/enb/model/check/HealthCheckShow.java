/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-6	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;


/**
 * 
 * 健康检查文件列表显示
 * 
 * 
 * @author chenlong
 * 
 */

public class HealthCheckShow {
	
	private String checkDate;
	
	// 检查站点总数
	private int checkEnbNum;
	
	// 存在故障站点数
	private int breakEnbNum;
	
	// 检查报告文件名
	private String fileName;
	
	public HealthCheckShow() {
		super();
	}
	
	public HealthCheckShow(String checkDate, int checkEnbNum,
			int breakEnbNum, String fileName) {
		super();
		this.checkDate = checkDate;
		this.checkEnbNum = checkEnbNum;
		this.breakEnbNum = breakEnbNum;
		this.fileName = fileName;
	}
	
	public String getCheckDate() {
		return checkDate;
	}
	
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	
	public int getCheckEnbNum() {
		return checkEnbNum;
	}
	
	public void setCheckEnbNum(int checkEnbNum) {
		this.checkEnbNum = checkEnbNum;
	}
	
	public int getBreakEnbNum() {
		return breakEnbNum;
	}
	
	public void setBreakEnbNum(int breakEnbNum) {
		this.breakEnbNum = breakEnbNum;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
