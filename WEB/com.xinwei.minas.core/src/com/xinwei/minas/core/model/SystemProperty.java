/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

/**
 * 
 * 系统属性的模型,对应sys_properties表,此模型只存放服务器级别的内容,所有不序列化
 * 
 * 
 * @author tiance
 * 
 */

public class SystemProperty {
	private Long idx;
	private String category;
	private String sub_category;
	private String property;
	private String value;
	private String description;

	public SystemProperty() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSub_category() {
		return sub_category;
	}

	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setVideoValue(int videoSwitch, String ip, String port) {
		StringBuilder sb = new StringBuilder();
		sb.append(videoSwitch);
		if(null == ip || "".equals(ip)) {
			sb.append(",");
		} else {
			sb.append("," + ip);
		}
		if(null == port || "".equals(port)) {
			sb.append(",");
		} else {
			sb.append(","+port);
		}
		this.value = sb.toString();
	}

	public int getVideoSwitch() {
		if(null == value || "".equals(value)) {
			return 0;
		}
		String[] split = value.split(",");
		if(split.length > 0) {
			return Integer.valueOf(split[0]);
		} else {
			return 0;
		}
	}
	
	
	public String getVideoIp() {
		if(null == value || "".equals(value)) {
			return "";
		}
		String[] split = value.split(",");
		if(split.length > 1) {
			return split[1];
		} else {
			return "";
		}
	}
	
	public String getVideoPort() {
		if(null == value || "".equals(value)) {
			return "";
		}
		String[] split = value.split(",");
		if(split.length > 2) {
			return split[2];
		} else {
			return "";
		}
	}
}
