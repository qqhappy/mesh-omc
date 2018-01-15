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
 * MME TA列表
 * 
 * <p>
 * MME TA列表
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class MmeTaModel{
	
	//索引
	private String mmeta_index;
	
	//ID
	private String mmeta_id;
	
	//标识
	private String mmeta_tai;
	
	//备注
	private String mmeta_comment;

	public String getMmeta_index()
	{
		return mmeta_index;
	}

	public void setMmeta_index(String mmeta_index)
	{
		this.mmeta_index = mmeta_index;
	}

	public String getMmeta_id()
	{
		return mmeta_id;
	}

	public void setMmeta_id(String mmeta_id)
	{
		this.mmeta_id = mmeta_id;
	}

	public String getMmeta_tai()
	{
		return mmeta_tai;
	}

	public void setMmeta_tai(String mmeta_tai)
	{
		this.mmeta_tai = mmeta_tai;
	}

	public String getMmeta_comment()
	{
		return mmeta_comment;
	}

	public void setMmeta_comment(String mmeta_comment)
	{
		this.mmeta_comment = mmeta_comment;
	}
}
