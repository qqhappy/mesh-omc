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
 * 系统全局信息模型
 * 
 * <p>
 * 系统全局信息模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysAllInfoModel{
	
	//系统信息索引
	private String sysinfo_index;
	
	//信息标题
	private String sysinfo_name;
	
	//信息值
	private String sysinfo_value;

	public String getSysinfo_index()
	{
		return sysinfo_index;
	}

	public void setSysinfo_index(String sysinfo_index)
	{
		this.sysinfo_index = sysinfo_index;
	}

	public String getSysinfo_name()
	{
		return sysinfo_name;
	}

	public void setSysinfo_name(String sysinfo_name)
	{
		this.sysinfo_name = sysinfo_name;
	}

	public String getSysinfo_value()
	{
		return sysinfo_value;
	}

	public void setSysinfo_value(String sysinfo_value)
	{
		this.sysinfo_value = sysinfo_value;
	}
}
