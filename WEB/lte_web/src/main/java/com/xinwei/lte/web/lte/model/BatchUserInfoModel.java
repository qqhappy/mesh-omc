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
 * 批量开户用户信息模型
 * 
 * 
 * @author yinyuelin
 * 
 */

public class BatchUserInfoModel{
	
	//索引
	private String usrIndex;
	
	//查询类型 1:用户号码;2:绑定的IMSI；3：昵称；4：参数模板；5：号码类型；6：状态：7：优先级
	private String queryType;
	
	//查询值
	private String queryValue;
	
	//用户号码
	private String startUserNumber;
	
	//用户号码类型 1:终端号码； 2：电话会议号码
	private String numberType;
		
	//用户状态 0:停用； 1：启用
	private int userState;
	
	//是否绑定IMSI
	private String haveImsi;
	
	//绑定的起始IMSI号码
	private String startImsi;
	
	//用户参数模板
	private String userParamTempletId;
	
	// 鉴权开关: 0-不鉴权；1-鉴权
	private String authFlag = "0";
	
	// 是否开通业务：0-不开通；1-开通
	private String openBizFlag = "0";
	
	// 批量开户的数目(1-10000)
	private String batchCount;

	public String getUsrIndex() {
		return usrIndex;
	}

	public void setUsrIndex(String usrIndex) {
		this.usrIndex = usrIndex;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public String getStartUserNumber() {
		return startUserNumber;
	}

	public void setStartUserNumber(String startUserNumber) {
		this.startUserNumber = startUserNumber;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public String getHaveImsi() {
		return haveImsi;
	}

	public void setHaveImsi(String haveImsi) {
		this.haveImsi = haveImsi;
	}

	public String getStartImsi() {
		return startImsi;
	}

	public void setStartImsi(String startImsi) {
		this.startImsi = startImsi;
	}

	public String getUserParamTempletId() {
		return userParamTempletId;
	}

	public void setUserParamTempletId(String userParamTempletId) {
		this.userParamTempletId = userParamTempletId;
	}

	public String getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(String batchCount) {
		this.batchCount = batchCount;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	public String getOpenBizFlag() {
		return openBizFlag;
	}

	public void setOpenBizFlag(String openBizFlag) {
		this.openBizFlag = openBizFlag;
	}
	
	
	
	

}
