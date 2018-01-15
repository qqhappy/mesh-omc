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
 * 无线IMSI模型
 * 
 * <p>
 * 无线IMSI模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class WirelessImsiModel{
	//索引
	private String imsi_index;
	
	//查询类型 1:imsi;2:终端状态
	private String queryType;
	
	//查询值
	private String queryValue;
	
	//国际移动设备识别码
	private String imsi;
	
	//根密钥
	private String imsi_k;
	
	//中断状态 0：未开户；1：已开户
	private String imsi_uestatus;

	//是否是首次登陆 0:首次
	private int first = 0;
	
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

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public String getImsi_index()
	{
		return imsi_index;
	}

	public void setImsi_index(String imsi_index)
	{
		this.imsi_index = imsi_index;
	}

	public String getImsi()
	{
		return imsi;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getImsi_k()
	{
		return imsi_k;
	}

	public void setImsi_k(String imsi_k)
	{
		this.imsi_k = imsi_k;
	}

	public String getImsi_uestatus()
	{
		return imsi_uestatus;
	}

	public void setImsi_uestatus(String imsi_uestatus)
	{
		this.imsi_uestatus = imsi_uestatus;
	}

}
