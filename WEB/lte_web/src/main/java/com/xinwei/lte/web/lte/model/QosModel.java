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
 * QOS模型
 * 
 * <p>
 * QOS模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class QosModel{
	
	//索引
	private String qos_index;
	
	//QOS级别[1,9]
	private String qos_id;
	
	//资源类型 0:null; 1:gbr; 2:none-gbr;
	private String qos_type;
	
	//优先级[1,9]
	private String qos_priority;
	
	//最大上行带宽
	private String qos_uplink_mbr;
	
	//最大下行带宽
	private String qos_downlink_mbr;
	
	//上行保证带宽
	private String qos_uplink_gbr;
	
	//下行保证带宽
	private String qos_downlink_gbr;

	public String getQos_index()
	{
		return qos_index;
	}

	public void setQos_index(String qos_index)
	{
		this.qos_index = qos_index;
	}

	public String getQos_id()
	{
		return qos_id;
	}

	public void setQos_id(String qos_id)
	{
		this.qos_id = qos_id;
	}

	public String getQos_type()
	{
		return qos_type;
	}

	public void setQos_type(String qos_type)
	{
		this.qos_type = qos_type;
	}

	public String getQos_priority()
	{
		return qos_priority;
	}

	public void setQos_priority(String qos_priority)
	{
		this.qos_priority = qos_priority;
	}

	public String getQos_uplink_mbr()
	{
		return qos_uplink_mbr;
	}

	public void setQos_uplink_mbr(String qos_uplink_mbr)
	{
		this.qos_uplink_mbr = qos_uplink_mbr;
	}

	public String getQos_downlink_mbr()
	{
		return qos_downlink_mbr;
	}

	public void setQos_downlink_mbr(String qos_downlink_mbr)
	{
		this.qos_downlink_mbr = qos_downlink_mbr;
	}

	public String getQos_uplink_gbr()
	{
		return qos_uplink_gbr;
	}

	public void setQos_uplink_gbr(String qos_uplink_gbr)
	{
		this.qos_uplink_gbr = qos_uplink_gbr;
	}

	public String getQos_downlink_gbr()
	{
		return qos_downlink_gbr;
	}

	public void setQos_downlink_gbr(String qos_downlink_gbr)
	{
		this.qos_downlink_gbr = qos_downlink_gbr;
	}
	
	
}
