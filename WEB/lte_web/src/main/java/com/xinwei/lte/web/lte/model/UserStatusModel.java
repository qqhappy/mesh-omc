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
 * 用户状态模型
 * 
 * <p>
 * 用户状态模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserStatusModel{
	
	//查询条件 1:用户号码；2：当前IP地址;3：IMSI；4：接入状态
	private String queryType;
	
	//查询值
	private String queryValue;
	
	//用户号码
	private String usr_number;
	
	//全局标识，10可见字符
	private String us_guti;
	
	//最大 10可见字符
	private String us_tai;
	
	//LTE接入的移动性管理状态
	private String us_emmstate;
	
	//漫游状态
	private String us_roamstate;
	
	//当前分配给用户的IP地址
	private String us_ip;
	
	//当前APN
	private String us_apn_ip;

	//是否是首次登陆 0:首次
	private int first = 0;
	
	//国际移动设备识别码
	private String imsi;
	
	public String getUsr_number()
	{
		return usr_number;
	}

	public void setUsr_number(String usr_number)
	{
		this.usr_number = usr_number;
	}

	public String getUs_guti()
	{
		return us_guti;
	}

	public void setUs_guti(String us_guti)
	{
		this.us_guti = us_guti;
	}

	public String getUs_tai()
	{
		return us_tai;
	}

	public void setUs_tai(String us_tai)
	{
		this.us_tai = us_tai;
	}

	public String getUs_emmstate()
	{
		return us_emmstate;
	}

	public void setUs_emmstate(String us_emmstate)
	{
		this.us_emmstate = us_emmstate;
	}

	public String getUs_roamstate()
	{
		return us_roamstate;
	}

	public void setUs_roamstate(String us_roamstate)
	{
		this.us_roamstate = us_roamstate;
	}

	public String getUs_ip()
	{
		return us_ip;
	}

	public void setUs_ip(String us_ip)
	{
		this.us_ip = us_ip;
	}

	public String getUs_apn_ip()
	{
		return us_apn_ip;
	}

	public void setUs_apn_ip(String us_apn_ip)
	{
		this.us_apn_ip = us_apn_ip;
	}

	public int getFirst()
	{
		return first;
	}

	public void setFirst(int first)
	{
		this.first = first;
	}

	public String getImsi()
	{
		return imsi;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
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
	
}
