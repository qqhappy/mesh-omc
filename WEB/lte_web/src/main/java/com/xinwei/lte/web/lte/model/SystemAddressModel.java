/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.model;

/**
 * 
 * 系统地址模型
 * 
 * <p>
 * 系统地址模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SystemAddressModel{
	
	//地址表索引
	private String ip_index;
	
	//记录id
	private String ip_id;
	
	//地址
	private String ip_addr;
	
	//掩码
	private String ip_mask;
	
	//网关
	private String ip_gateway;
	
	//描述
	private String ip_comment;

	//是否是首次登陆 0:首次
	private int first = 0;
	public String getIp_index() {
		return ip_index;
	}

	public void setIp_index(String ip_index) {
		this.ip_index = ip_index;
	}

	public String getIp_id() {
		return ip_id;
	}

	public void setIp_id(String ip_id) {
		this.ip_id = ip_id;
	}

	public String getIp_addr() {
		return ip_addr;
	}

	public void setIp_addr(String ip_addr) {
		this.ip_addr = ip_addr;
	}

	public String getIp_mask() {
		return ip_mask;
	}

	public void setIp_mask(String ip_mask) {
		this.ip_mask = ip_mask;
	}

	public String getIp_gateway() {
		return ip_gateway;
	}

	public void setIp_gateway(String ip_gateway) {
		this.ip_gateway = ip_gateway;
	}

	public String getIp_comment() {
		return ip_comment;
	}

	public void setIp_comment(String ip_comment) {
		this.ip_comment = ip_comment;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}
}
