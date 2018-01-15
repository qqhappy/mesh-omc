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
 * 用户参数模板模型
 * 
 * <p>
 * 用户参数模板模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserTemplateModel{
	
	//索引
	private String tmp_index;
	
	//模板ID
	private String tmp_id;
	
	//模板名
	private String tmp_name;
	
	//最大上行带宽
	private String tmp_ul_bw;
	
	//最大下行带宽
	private String temp_dl_bw;
	
	//pdn类型
	private String tmp_pdn_cat;
	
	//接入点名称
	private String tmp_apn;
	
	//APN最大上行带宽
	private String tmp_apn_ulbw;
	
	//APN最大下行带宽
	private String tmp_apn_dlbw;
	
	//QOS等级
	private String tmp_qci;
	
	//ARP优先级
	private String tmp_arp_pri;
	
	//ARP抢占能力
	private String tmp_arp_reave;
	
	//ARP被抢占能力
	private String tmp_arp_reaved;
	
	//默认模块标识 0:no; 1:yes;
	private String tmp_default;
	
	//是否是首次登陆 0:首次
	private int first = 0;
	public String getTmp_arp_pri() {
		return tmp_arp_pri;
	}

	public void setTmp_arp_pri(String tmp_arp_pri) {
		this.tmp_arp_pri = tmp_arp_pri;
	}

	public String getTmp_index()
	{
		return tmp_index;
	}

	public void setTmp_index(String tmp_index)
	{
		this.tmp_index = tmp_index;
	}

	public String getTmp_id()
	{
		return tmp_id;
	}

	public void setTmp_id(String tmp_id)
	{
		this.tmp_id = tmp_id;
	}

	public String getTmp_name()
	{
		return tmp_name;
	}

	public void setTmp_name(String tmp_name)
	{
		this.tmp_name = tmp_name;
	}

	public String getTmp_ul_bw()
	{
		return tmp_ul_bw;
	}

	public void setTmp_ul_bw(String tmp_ul_bw)
	{
		this.tmp_ul_bw = tmp_ul_bw;
	}

	public String getTemp_dl_bw()
	{
		return temp_dl_bw;
	}

	public void setTemp_dl_bw(String temp_dl_bw)
	{
		this.temp_dl_bw = temp_dl_bw;
	}

	public String getTmp_pdn_cat()
	{
		return tmp_pdn_cat;
	}

	public void setTmp_pdn_cat(String tmp_pdn_cat)
	{
		this.tmp_pdn_cat = tmp_pdn_cat;
	}

	public String getTmp_apn()
	{
		return tmp_apn;
	}

	public void setTmp_apn(String tmp_apn)
	{
		this.tmp_apn = tmp_apn;
	}

	public String getTmp_apn_ulbw()
	{
		return tmp_apn_ulbw;
	}

	public void setTmp_apn_ulbw(String tmp_apn_ulbw)
	{
		this.tmp_apn_ulbw = tmp_apn_ulbw;
	}

	public String getTmp_apn_dlbw()
	{
		return tmp_apn_dlbw;
	}

	public void setTmp_apn_dlbw(String tmp_apn_dlbw)
	{
		this.tmp_apn_dlbw = tmp_apn_dlbw;
	}

	public String getTmp_qci()
	{
		return tmp_qci;
	}

	public void setTmp_qci(String tmp_qci)
	{
		this.tmp_qci = tmp_qci;
	}

	public String getTmp_arp_reave()
	{
		return tmp_arp_reave;
	}

	public void setTmp_arp_reave(String tmp_arp_reave)
	{
		this.tmp_arp_reave = tmp_arp_reave;
	}

	public String getTmp_arp_reaved()
	{
		return tmp_arp_reaved;
	}

	public void setTmp_arp_reaved(String tmp_arp_reaved)
	{
		this.tmp_arp_reaved = tmp_arp_reaved;
	}

	public String getTmp_default()
	{
		return tmp_default;
	}

	public void setTmp_default(String tmp_default)
	{
		this.tmp_default = tmp_default;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}
}
