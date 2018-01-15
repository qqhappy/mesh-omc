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
 * 号码分析模型
 * 
 * <p>
 * 号码分析模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class NumAnalyseModel{
	
	//号码分析表索引
	private String na_index;
	
	//号码前缀
	private String na_prefix;
	
	//最小号码长度
	private String na_minlen;
	
	//最大号码长度
	private String na_maxlen;
	
	//号码属性
	private String na_attr;
	
	//出局SIP通道ID
	private String sip_id;

	public String getNa_index()
	{
		return na_index;
	}

	public void setNa_index(String na_index)
	{
		this.na_index = na_index;
	}

	public String getNa_prefix()
	{
		return na_prefix;
	}

	public void setNa_prefix(String na_prefix)
	{
		this.na_prefix = na_prefix;
	}

	public String getNa_minlen()
	{
		return na_minlen;
	}

	public void setNa_minlen(String na_minlen)
	{
		this.na_minlen = na_minlen;
	}

	public String getNa_maxlen()
	{
		return na_maxlen;
	}

	public void setNa_maxlen(String na_maxlen)
	{
		this.na_maxlen = na_maxlen;
	}

	public String getNa_attr()
	{
		return na_attr;
	}

	public void setNa_attr(String na_attr)
	{
		this.na_attr = na_attr;
	}

	public String getSip_id()
	{
		return sip_id;
	}

	public void setSip_id(String sip_id)
	{
		this.sip_id = sip_id;
	}
}
