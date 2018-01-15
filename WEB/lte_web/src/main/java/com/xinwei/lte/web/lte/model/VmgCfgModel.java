/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-6	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.model;

/**
 * 
 * 视频监控设备配置信息
 * 
 * <p>
 * 视频监控设备配置信息
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class VmgCfgModel
{
	//是否是首次登陆 0:首次
	private int first = 0;
	
	//查询类型 1:imsi;2:终端状态
	private String queryType;
	
	//查询值
	private String queryValue;
	
	/**
	 * 索引
	 */
	private String monitor_index;
	
	/**
	 * 唯一标识一个视频监控设备
	 */
	private String monitor_name;
	
	/**
	 * 0---海康1---大华
	 */
	private String monitor_type;
	
	/**
	 * 与视频监控设备对接的远端IP地址
	 */
	private String monitor_ip;
	
	/**
	 * 与视频监控设备对接的远端IP端口号
	 */
	private String monitor_port;
	
	/**
	 * 与视频监控设备对接时，用于登录到视频监控设备的用户名
	 */
	private String user_name;
	
	/**
	 * 与视频监控设备对接时，用于登录到视频监控设备的密码
	 */
	private String user_password;
	
	/**
	 * 描述该监控设备相关信息，如地址、型号等消息
	 */
	private String comment;

	public String getMonitor_index()
	{
		return monitor_index;
	}

	public void setMonitor_index(String monitor_index)
	{
		this.monitor_index = monitor_index;
	}

	public String getMonitor_name()
	{
		return monitor_name;
	}

	public void setMonitor_name(String monitor_name)
	{
		this.monitor_name = monitor_name;
	}

	public String getMonitor_type()
	{
		return monitor_type;
	}

	public void setMonitor_type(String monitor_type)
	{
		this.monitor_type = monitor_type;
	}

	public String getMonitor_ip()
	{
		return monitor_ip;
	}

	public void setMonitor_ip(String monitor_ip)
	{
		this.monitor_ip = monitor_ip;
	}

	public String getMonitor_port()
	{
		return monitor_port;
	}

	public void setMonitor_port(String monitor_port)
	{
		this.monitor_port = monitor_port;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getUser_password()
	{
		return user_password;
	}

	public void setUser_password(String user_password)
	{
		this.user_password = user_password;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getQueryType()
	{
		return queryType;
	}

	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}

	public String getQueryValue()
	{
		return queryValue;
	}

	public void setQueryValue(String queryValue)
	{
		this.queryValue = queryValue;
	}

	public int getFirst()
	{
		return first;
	}

	public void setFirst(int first)
	{
		this.first = first;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("VmgCfgModel [monitor_index=");
		builder.append(monitor_index);
		builder.append(", monitor_name=");
		builder.append(monitor_name);
		builder.append(", monitor_type=");
		builder.append(monitor_type);
		builder.append(", monitor_ip=");
		builder.append(monitor_ip);
		builder.append(", monitor_port=");
		builder.append(monitor_port);
		builder.append(", user_name=");
		builder.append(user_name);
		builder.append(", user_password=");
		builder.append(user_password);
		builder.append(", comment=");
		builder.append(comment);
		builder.append("]");
		return builder.toString();
	}
}
