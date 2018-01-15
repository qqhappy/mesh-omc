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
 * MME SI链路模型
 * 
 * <p>
 * MME SI链路模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class MmeS1Model{
	
	//索引
	private String mmes1_index;
	
	//查询条件 1:链路标识；2：基站IP;3：基站端口；4：状态
	private String queryType;
	
	//查询值
	private String queryValue;
	
	//链路标识
	private String mmes1_id;
	
	//基站IP
	private String mmes1_peerip;
	
	//基站IP对应eNB中IPV4表中的IPID
	private String mmes1_peeripId;
	
	//基站端口
	private String mmes1_peerport;
	
	//状态 0:断开 1:连接
	private String mmes1_state;
	
	//链路描述
	private String mmes1_comment;
	
	//基站ID
	private String mmes1_enbid;

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

	public String getMmes1_index()
	{
		return mmes1_index;
	}

	public void setMmes1_index(String mmes1_index)
	{
		this.mmes1_index = mmes1_index;
	}

	public String getMmes1_id()
	{
		return mmes1_id;
	}

	public void setMmes1_id(String mmes1_id)
	{
		this.mmes1_id = mmes1_id;
	}

	public String getMmes1_peerip()
	{
		return mmes1_peerip;
	}

	public void setMmes1_peerip(String mmes1_peerip)
	{
		this.mmes1_peerip = mmes1_peerip;
	}

	public String getMmes1_peerport()
	{
		return mmes1_peerport;
	}

	public void setMmes1_peerport(String mmes1_peerport)
	{
		this.mmes1_peerport = mmes1_peerport;
	}

	public String getMmes1_comment()
	{
		return mmes1_comment;
	}

	public void setMmes1_comment(String mmes1_comment)
	{
		this.mmes1_comment = mmes1_comment;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public String getMmes1_state()
	{
		return mmes1_state;
	}

	public void setMmes1_state(String mmes1_state)
	{
		this.mmes1_state = mmes1_state;
	}

	public String getMmes1_enbid()
	{
		return mmes1_enbid;
	}

	public void setMmes1_enbid(String mmes1_enbid)
	{
		this.mmes1_enbid = mmes1_enbid;
	}

	public String getMmes1_peeripId() {
		return mmes1_peeripId;
	}

	public void setMmes1_peeripId(String mmes1_peeripId) {
		this.mmes1_peeripId = mmes1_peeripId;
	}	
	
}
