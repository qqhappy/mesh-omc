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
 * SIP路由模型
 * 
 * <p>
 * SIP路由模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SipRouteModel{
	
	//地址表索引
	private String sip_index;
	
	//通道ID
	private String sip_id;
	
	//对端IP
	private String sip_addr;
	
	//对端端口
	private String sip_port;
	
	//是否默认路由
	private String sip_isdefault;
	
	//描述
	private String sip_comment;
	
	//状态
	private String sip_state;
	

	public String getSip_state()
	{
		return sip_state;
	}

	public void setSip_state(String sip_state)
	{
		this.sip_state = sip_state;
	}

	public String getSip_index()
	{
		return sip_index;
	}

	public void setSip_index(String sip_index)
	{
		this.sip_index = sip_index;
	}

	public String getSip_addr()
	{
		return sip_addr;
	}

	public void setSip_addr(String sip_addr)
	{
		this.sip_addr = sip_addr;
	}

	public String getSip_id() {
		return sip_id;
	}

	public void setSip_id(String sip_id) {
		this.sip_id = sip_id;
	}

	public String getSip_port()
	{
		return sip_port;
	}

	public void setSip_port(String sip_port)
	{
		this.sip_port = sip_port;
	}

	public String getSip_isdefault()
	{
		return sip_isdefault;
	}

	public void setSip_isdefault(String sip_isdefault)
	{
		this.sip_isdefault = sip_isdefault;
	}

	public String getSip_comment()
	{
		return sip_comment;
	}

	public void setSip_comment(String sip_comment)
	{
		this.sip_comment = sip_comment;
	}	
}
