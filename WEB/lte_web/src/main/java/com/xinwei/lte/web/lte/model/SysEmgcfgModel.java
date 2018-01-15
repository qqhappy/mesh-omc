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
 * 系统紧急呼叫模型
 * 
 * <p>
 * 系统紧急呼叫模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysEmgcfgModel{
	
	//索引
	private String emgsys_index;
	
	//紧急呼叫号码
	private String emgsys_num;
	
	//紧急呼叫号码类别
	private String emgsys_category;	
		
	//紧急呼叫重定向号码
	private String emg_redicnum;

	//紧急呼叫重定向号码优先级
	private String emg_redicpri;

	public String getEmgsys_index()
	{
		return emgsys_index;
	}

	public void setEmgsys_index(String emgsys_index)
	{
		this.emgsys_index = emgsys_index;
	}

	public String getEmgsys_num()
	{
		return emgsys_num;
	}

	public void setEmgsys_num(String emgsys_num)
	{
		this.emgsys_num = emgsys_num;
	}

	public String getEmgsys_category()
	{
		return emgsys_category;
	}

	public void setEmgsys_category(String emgsys_category)
	{
		this.emgsys_category = emgsys_category;
	}

	public String getEmg_redicnum()
	{
		return emg_redicnum;
	}

	public void setEmg_redicnum(String emg_redicnum)
	{
		this.emg_redicnum = emg_redicnum;
	}

	public String getEmg_redicpri()
	{
		return emg_redicpri;
	}

	public void setEmg_redicpri(String emg_redicpri)
	{
		this.emg_redicpri = emg_redicpri;
	}
}
