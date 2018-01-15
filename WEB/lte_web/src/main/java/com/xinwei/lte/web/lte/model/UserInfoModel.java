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
 * 用户信息模型
 * 
 * <p>
 * 用户信息模型
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserInfoModel{
	
	//索引
	private String usr_index;
	
	//查询类型 1:用户号码;2:绑定的IMSI；3：昵称；4：参数模板；5：号码类型；6：状态：7：优先级
	private String queryType;
	
	//查询值
	private String queryValue;
	
	//用户号码
	private String usr_number;
	
	//用户号码类型 1:终端号码； 2：电话会议号码
	private String usr_numbertype;
	
	//用户昵称
	private String usr_neckname;
	
	//用户密码
	private String usr_password;
	
	//用户信息
	private String usr_comment;
	
	//用户状态 0:停用； 1：启用
	private int usr_state;
	
	//是否绑定IMSI
	private String usr_haveimsi;
	
	//绑定IMSI号码
	private String imsi;
	
	//用户参数模板
	private String tmp_id;
	
	//用户IP地址分配方式；0是不使用静态IP，1是使用静态IP，2是使用静态IP段
	private int ipaddr_allocate_type;
	
	//给终端分配的静态IP，不使用静态IP该值为0，广播地址非法
	private String usr_staticip;
	
	//起始静态IP地址
	private String usr_start_staticip;
	
	//结束静态IP地址
	private String usr_end_staticip;
	
	//鉴权开关 0：不鉴权 1：鉴权
	private String usr_authflag = "0";
	
	//用户状态
	private String lte_usestate;
	
	//单呼业务 0:音频单呼； 1：视频单呼；2：数据业务;3:漏呼短信提醒；4：夜服业务；5：联动话机
	private String srv_sigcalllte_flag;
	
	//组呼业务 0:组呼开关；1：广播呼叫；2：可视组呼;
	private String srv_grpcalllte_flag;
	
	//用户是否开通PGIS业务  0 关闭 1开通
	private String  srv_location_flag;
	
	private String  srv_bai_flag;
	
	//用户优先级
	private String srv_usr_pri;
	
	// 用户录音开关：0-关闭；1-打开
	private String audioRecordFlag;

	//前传业务返回flag
	private String forwardBuzFlag;
	
	//前转条件
	private String fwd_condition;
	
	//夜服类型
	private String fwd_servtype;
	
	//起始时间点
	private String shour;
	
	//结束时间
	private String ehour;
	
	//夜服号码
	private String fwd_number;
	
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

	public String getUsr_index()
	{
		return usr_index;
	}

	public void setUsr_index(String usr_index)
	{
		this.usr_index = usr_index;
	}

	public String getUsr_number()
	{
		return usr_number;
	}

	public void setUsr_number(String usr_number)
	{
		this.usr_number = usr_number;
	}

	public String getUsr_numbertype()
	{
		return usr_numbertype;
	}

	public void setUsr_numbertype(String usr_numbertype)
	{
		this.usr_numbertype = usr_numbertype;
	}

	public String getUsr_neckname()
	{
		return usr_neckname;
	}

	public void setUsr_neckname(String usr_neckname)
	{
		this.usr_neckname = usr_neckname;
	}

	public String getUsr_password()
	{
		return usr_password;
	}

	public void setUsr_password(String usr_password)
	{
		this.usr_password = usr_password;
	}

	public String getUsr_comment()
	{
		return usr_comment;
	}

	public void setUsr_comment(String usr_comment)
	{
		this.usr_comment = usr_comment;
	}

	public int getUsr_state()
	{
		return usr_state;
	}

	public void setUsr_state(int usr_state)
	{
		this.usr_state = usr_state;
	}

	public String getUsr_haveimsi()
	{
		return usr_haveimsi;
	}

	public void setUsr_haveimsi(String usr_haveimsi)
	{
		this.usr_haveimsi = usr_haveimsi;
	}

	public String getImsi()
	{
		return imsi;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getTmp_id()
	{
		return tmp_id;
	}

	public void setTmp_id(String tmp_id)
	{
		this.tmp_id = tmp_id;
	}

	public String getUsr_staticip()
	{
		return usr_staticip;
	}

	public void setUsr_staticip(String usr_staticip)
	{
		this.usr_staticip = usr_staticip;
	}

	public String getLte_usestate()
	{
		return lte_usestate;
	}

	public void setLte_usestate(String lte_usestate)
	{
		this.lte_usestate = lte_usestate;
	}

	public String getSrv_sigcalllte_flag()
	{
		return srv_sigcalllte_flag;
	}

	public void setSrv_sigcalllte_flag(String srv_sigcalllte_flag)
	{
		this.srv_sigcalllte_flag = srv_sigcalllte_flag;
	}

	public String getSrv_grpcalllte_flag()
	{
		return srv_grpcalllte_flag;
	}

	public void setSrv_grpcalllte_flag(String srv_grpcalllte_flag)
	{
		this.srv_grpcalllte_flag = srv_grpcalllte_flag;
	}

	public String getSrv_usr_pri()
	{
		return srv_usr_pri;
	}

	public void setSrv_usr_pri(String srv_usr_pri)
	{
		this.srv_usr_pri = srv_usr_pri;
	}

	public String getFwd_condition()
	{
		return fwd_condition;
	}

	public void setFwd_condition(String fwd_condition)
	{
		this.fwd_condition = fwd_condition;
	}

	public String getFwd_servtype()
	{
		return fwd_servtype;
	}

	public void setFwd_servtype(String fwd_servtype)
	{
		this.fwd_servtype = fwd_servtype;
	}

	public String getShour()
	{
		return shour;
	}

	public void setShour(String shour)
	{
		this.shour = shour;
	}

	public String getEhour()
	{
		return ehour;
	}

	public void setEhour(String ehour)
	{
		this.ehour = ehour;
	}

	public String getFwd_number() {
		return fwd_number;
	}

	public void setFwd_number(String fwd_number) {
		this.fwd_number = fwd_number;
	}

	public int getFirst()
	{
		return first;
	}

	public void setFirst(int first)
	{
		this.first = first;
	}

	public String getForwardBuzFlag()
	{
		return forwardBuzFlag;
	}

	public void setForwardBuzFlag(String forwardBuzFlag)
	{
		this.forwardBuzFlag = forwardBuzFlag;
	}
	
	public String getUsr_authflag()
	{
		return usr_authflag;
	}

	public void setUsr_authflag(String usr_authflag)
	{
		this.usr_authflag = usr_authflag;
	}

	public String getAudioRecordFlag() {
		return audioRecordFlag;
	}

	public void setAudioRecordFlag(String audioRecordFlag) {
		this.audioRecordFlag = audioRecordFlag;
	}

	public int getIpaddr_allocate_type() {
		return ipaddr_allocate_type;
	}

	public void setIpaddr_allocate_type(int ipaddr_allocate_type) {
		this.ipaddr_allocate_type = ipaddr_allocate_type;
	}

	public String getUsr_start_staticip() {
		return usr_start_staticip;
	}

	public void setUsr_start_staticip(String usr_start_staticip) {
		this.usr_start_staticip = usr_start_staticip;
	}

	public String getUsr_end_staticip() {
		return usr_end_staticip;
	}

	public void setUsr_end_staticip(String usr_end_staticip) {
		this.usr_end_staticip = usr_end_staticip;
	}

	public String getSrv_location_flag() {
		return srv_location_flag;
	}

	public void setSrv_location_flag(String srv_location_flag) {
		this.srv_location_flag = srv_location_flag;
	}

	public String getSrv_bai_flag() {
		return srv_bai_flag;
	}

	public void setSrv_bai_flag(String srv_bai_flag) {
		this.srv_bai_flag = srv_bai_flag;
	}
	
	
}
