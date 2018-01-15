/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-11	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.model;

/**
 * 
 * 呼叫qos
 * 
 * <p>
 * 呼叫qos
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class CallQos{
	
	//呼叫类型 0:语音 1：视频
	private String callqos_type;
	
	//qos级别ID
	private String callqos_qci;
	
	//上行最大带宽，单位K
	private String callqos_uplink_mbr;
	
	//下行最大带宽，单位K
	private String callqos_downlink_mbr;
	
	//上行保证带宽，单位K
	private String callqos_uplink_gbr;
	
	//下行保证带宽，单位K
	private String callqos_downlink_gbr;
	
	public String getCallqos_type()
	{
		return callqos_type;
	}

	public void setCallqos_type(String callqos_type)
	{
		this.callqos_type = callqos_type;
	}

	public String getCallqos_qci()
	{
		return callqos_qci;
	}

	public void setCallqos_qci(String callqos_qci)
	{
		this.callqos_qci = callqos_qci;
	}

	public String getCallqos_uplink_mbr()
	{
		return callqos_uplink_mbr;
	}

	public void setCallqos_uplink_mbr(String callqos_uplink_mbr)
	{
		this.callqos_uplink_mbr = callqos_uplink_mbr;
	}

	public String getCallqos_downlink_mbr()
	{
		return callqos_downlink_mbr;
	}

	public void setCallqos_downlink_mbr(String callqos_downlink_mbr)
	{
		this.callqos_downlink_mbr = callqos_downlink_mbr;
	}

	public String getCallqos_uplink_gbr()
	{
		return callqos_uplink_gbr;
	}

	public void setCallqos_uplink_gbr(String callqos_uplink_gbr)
	{
		this.callqos_uplink_gbr = callqos_uplink_gbr;
	}

	public String getCallqos_downlink_gbr()
	{
		return callqos_downlink_gbr;
	}

	public void setCallqos_downlink_gbr(String callqos_downlink_gbr)
	{
		this.callqos_downlink_gbr = callqos_downlink_gbr;
	}
}
