/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-5	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

/**
 * 
 * 告警表格对象
 * 
 * @author chenlong
 * 
 */

public class AlarmCheckForm {
	
	// 告警级别
	private String level;
	
	// 告警内容
	private String content;
	
	// enb名称
	private String enbName;
	
	// 定位信息
	private String location;
	
	// 状态
	private String status;
	
	// 发生时间
	private String happenTime;
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getEnbName() {
		return enbName;
	}
	
	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getHappenTime() {
		return happenTime;
	}
	
	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}
	
}
