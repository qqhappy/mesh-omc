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
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class IpQos{
	
	//索引
	private String ipqos_index;
	
	//业务IP，IPV4
	private String ipqos_ip;
	
	//业务端口上限
	private String ipqos_max_port;
	
	//业务端口下限
	private String ipqos_min_port;
	
	//QOS级别ID
	private String ipqos_qci;
	
	//上行最大带宽，单位K
	private String ipqos_uplink_mbr;
	
	//下行最大带宽，单位K
	private String ipqos_downlink_mbr;
	
	//上行保证带宽，单位K
	private String ipqos_uplink_gbr;
	
	//下行保证带宽，单位K
	private String ipqos_downlink_gbr;

	public String getIpqos_ip()
	{
		return ipqos_ip;
	}

	public void setIpqos_ip(String ipqos_ip)
	{
		this.ipqos_ip = ipqos_ip;
	}

	public String getIpqos_max_port()
	{
		return ipqos_max_port;
	}

	public void setIpqos_max_port(String ipqos_max_port)
	{
		this.ipqos_max_port = ipqos_max_port;
	}

	public String getIpqos_min_port()
	{
		return ipqos_min_port;
	}

	public void setIpqos_min_port(String ipqos_min_port)
	{
		this.ipqos_min_port = ipqos_min_port;
	}

	public String getIpqos_qci()
	{
		return ipqos_qci;
	}

	public void setIpqos_qci(String ipqos_qci)
	{
		this.ipqos_qci = ipqos_qci;
	}

	public String getIpqos_uplink_mbr()
	{
		return ipqos_uplink_mbr;
	}

	public void setIpqos_uplink_mbr(String ipqos_uplink_mbr)
	{
		this.ipqos_uplink_mbr = ipqos_uplink_mbr;
	}

	public String getIpqos_downlink_mbr()
	{
		return ipqos_downlink_mbr;
	}

	public void setIpqos_downlink_mbr(String ipqos_downlink_mbr)
	{
		this.ipqos_downlink_mbr = ipqos_downlink_mbr;
	}

	public String getIpqos_uplink_gbr()
	{
		return ipqos_uplink_gbr;
	}

	public void setIpqos_uplink_gbr(String ipqos_uplink_gbr)
	{
		this.ipqos_uplink_gbr = ipqos_uplink_gbr;
	}

	public String getIpqos_downlink_gbr()
	{
		return ipqos_downlink_gbr;
	}

	public void setIpqos_downlink_gbr(String ipqos_downlink_gbr)
	{
		this.ipqos_downlink_gbr = ipqos_downlink_gbr;
	}

	public String getIpqos_index()
	{
		return ipqos_index;
	}

	public void setIpqos_index(String ipqos_index)
	{
		this.ipqos_index = ipqos_index;
	}
	
}
