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
 * 视频监控号码分析
 * 
 * <p>
 * 视频监控号码分析
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class VmgNumModel
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
	private String call_number_index;
	
	/**
	 * 唯一标识一个视频监控终端
	 */
	private String call_number;
	
	/**
	 * 监控号码代理到SDC系统鉴权使用，当前SDC系统不使用密码进行鉴权
	 */
	private String auth_password;
	
	/**
	 * 唯一标识一个视频监控设备
	 */
	private String monitor_name;
	
	/**
	 * 唯一标识一个监控设备中的监控终端（摄像头），如IPC监控设备中只有一个摄像头，监控通道号为1
	 */
	private String channel_id;
	
	/**
	 * 描述信息
	 */
	private String comment;

	public String getCall_number_index()
	{
		return call_number_index;
	}

	public void setCall_number_index(String call_number_index)
	{
		this.call_number_index = call_number_index;
	}

	public String getCall_number()
	{
		return call_number;
	}

	public void setCall_number(String call_number)
	{
		this.call_number = call_number;
	}

	public String getAuth_password()
	{
		return auth_password;
	}

	public void setAuth_password(String auth_password)
	{
		this.auth_password = auth_password;
	}

	public String getMonitor_name()
	{
		return monitor_name;
	}

	public void setMonitor_name(String monitor_name)
	{
		this.monitor_name = monitor_name;
	}

	public String getChannel_id()
	{
		return channel_id;
	}

	public void setChannel_id(String channel_id)
	{
		this.channel_id = channel_id;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public int getFirst()
	{
		return first;
	}

	public void setFirst(int first)
	{
		this.first = first;
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

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("VmgNumModel [first=");
		builder.append(first);
		builder.append(", queryType=");
		builder.append(queryType);
		builder.append(", queryValue=");
		builder.append(queryValue);
		builder.append(", call_number_index=");
		builder.append(call_number_index);
		builder.append(", call_number=");
		builder.append(call_number);
		builder.append(", auth_password=");
		builder.append(auth_password);
		builder.append(", monitor_name=");
		builder.append(monitor_name);
		builder.append(", channel_id=");
		builder.append(channel_id);
		builder.append(", comment=");
		builder.append(comment);
		builder.append("]");
		return builder.toString();
	}
	
}
