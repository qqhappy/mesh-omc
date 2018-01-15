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
 * 用户IP资源模型
 * 
 * <p>
 * 用户IP资源模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserIpResourceModel{
	
	//索引
	private String snet_index;
	
	//子网ID[1，10]
	private String snet_id;
	
	//起始地址
	private String snet_startip;
	
	//结束地址
	private String snet_endip;
	
	//地址池类型
	private String ippoll_type;

	
	public String getIppoll_type()
	{
		return ippoll_type;
	}

	public void setIppoll_type(String ippoll_type)
	{
		this.ippoll_type = ippoll_type;
	}

	public String getSnet_index()
	{
		return snet_index;
	}

	public void setSnet_index(String snet_index)
	{
		this.snet_index = snet_index;
	}

	public String getSnet_id()
	{
		return snet_id;
	}

	public void setSnet_id(String snet_id)
	{
		this.snet_id = snet_id;
	}

	public String getSnet_startip()
	{
		return snet_startip;
	}

	public void setSnet_startip(String snet_startip)
	{
		this.snet_startip = snet_startip;
	}

	public String getSnet_endip()
	{
		return snet_endip;
	}

	public void setSnet_endip(String snet_endip)
	{
		this.snet_endip = snet_endip;
	}	
}
