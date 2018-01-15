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
 * 系统全局配置模型
 * 
 * <p>
 * 系统全局配置模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysAllConfigModel{
	
	// 设备ID
	private String sdcId = "0";
	
	//SIP系统地址ID
	private String sysconf_sipip_id;
	
	//SIP端口
	private String sysconf_sipport;
	
	//网络标识
	private String sysconf_plmn;
	
	//MEM 组ID
	private String sysconf_groupid;
	
	//MME 组编码
	private String sysconf_mmecode;
	
	//MME系统地址ID
	private String sysconf_mmeip_id;
	
	//GW与RAN对接的IP地址配置
	private String sysconf_ranip_id;
	
	//PDN地址ID
	private String sysconf_pdnip_id;
	
	//APN接入点名。SDC只处理在该APN下的信令数据。
	private String sysconf_apn;
	
	//S1端口号
	private String sysconf_mmes1_port;
	
	//运营商主密钥
	private String sysconf_master_key;
	
	//最大通话时长
	private String sysconf_maxduration;
	
	//是否记录音频 0-NO; 1-YES;
	private int sysconf_decordaudio;
	
	//是否记录视频 0-NO; 1-YES;
	private int sysconf_decordvedio;
	
	//是否保存IM消息 0-NO; 1-YES;
	private int sysconf_decordim;

	//DNS服务器
	private String sysconf_dns_ip;
	
	//PDT port
	private int  pdt_sip_port;
	
	
	

	public int getPdt_sip_port() {
		return pdt_sip_port;
	}

	public void setPdt_sip_port(int pdt_sip_port) {
		this.pdt_sip_port = pdt_sip_port;
	}

	public String getSysconf_sipip_id()
	{
		return sysconf_sipip_id;
	}

	public void setSysconf_sipip_id(String sysconf_sipip_id)
	{
		this.sysconf_sipip_id = sysconf_sipip_id;
	}

	public String getSysconf_sipport()
	{
		return sysconf_sipport;
	}

	public void setSysconf_sipport(String sysconf_sipport)
	{
		this.sysconf_sipport = sysconf_sipport;
	}	
	
	public String getSysconf_plmn()
	{
		return sysconf_plmn;
	}

	public void setSysconf_plmn(String sysconf_plmn)
	{
		this.sysconf_plmn = sysconf_plmn;
	}

	public String getSysconf_groupid()
	{
		return sysconf_groupid;
	}

	public void setSysconf_groupid(String sysconf_groupid)
	{
		this.sysconf_groupid = sysconf_groupid;
	}

	public String getSysconf_mmecode()
	{
		return sysconf_mmecode;
	}

	public void setSysconf_mmecode(String sysconf_mmecode)
	{
		this.sysconf_mmecode = sysconf_mmecode;
	}

	public String getSysconf_mmeip_id()
	{
		return sysconf_mmeip_id;
	}

	public void setSysconf_mmeip_id(String sysconf_mmeip_id)
	{
		this.sysconf_mmeip_id = sysconf_mmeip_id;
	}

	public String getSysconf_pdnip_id()
	{
		return sysconf_pdnip_id;
	}

	public void setSysconf_pdnip_id(String sysconf_pdnip_id)
	{
		this.sysconf_pdnip_id = sysconf_pdnip_id;
	}

	public String getSysconf_apn()
	{
		return sysconf_apn;
	}

	public void setSysconf_apn(String sysconf_apn)
	{
		this.sysconf_apn = sysconf_apn;
	}

	public String getSysconf_master_key()
	{
		return sysconf_master_key;
	}

	public void setSysconf_master_key(String sysconf_master_key)
	{
		this.sysconf_master_key = sysconf_master_key;
	}

	public String getSysconf_maxduration()
	{
		return sysconf_maxduration;
	}

	public void setSysconf_maxduration(String sysconf_maxduration)
	{
		this.sysconf_maxduration = sysconf_maxduration;
	}

	public int getSysconf_decordaudio()
	{
		return sysconf_decordaudio;
	}

	public void setSysconf_decordaudio(int sysconf_decordaudio)
	{
		this.sysconf_decordaudio = sysconf_decordaudio;
	}

	public int getSysconf_decordvedio()
	{
		return sysconf_decordvedio;
	}

	public void setSysconf_decordvedio(int sysconf_decordvedio)
	{
		this.sysconf_decordvedio = sysconf_decordvedio;
	}

	public int getSysconf_decordim()
	{
		return sysconf_decordim;
	}

	public void setSysconf_decordim(int sysconf_decordim)
	{
		this.sysconf_decordim = sysconf_decordim;
	}

	public String getSysconf_ranip_id() {
		return sysconf_ranip_id;
	}

	public void setSysconf_ranip_id(String sysconf_ranip_id) {
		this.sysconf_ranip_id = sysconf_ranip_id;
	}

	public String getSysconf_mmes1_port()
	{
		return sysconf_mmes1_port;
	}

	public void setSysconf_mmes1_port(String sysconf_mmes1_port)
	{
		this.sysconf_mmes1_port = sysconf_mmes1_port;
	}

	public String getSysconf_dns_ip()
	{
		return sysconf_dns_ip;
	}

	public void setSysconf_dns_ip(String sysconf_dns_ip)
	{
		this.sysconf_dns_ip = sysconf_dns_ip;
	}

	public String getSdcId() {
		return sdcId;
	}

	public void setSdcId(String sdcId) {
		this.sdcId = sdcId;
	}	
	
	
	
}
