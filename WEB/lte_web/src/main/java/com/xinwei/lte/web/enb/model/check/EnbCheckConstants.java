/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-2	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

/**
 * 
 * 类简要描述
 * 
 * @author Administrator
 * 
 */

public class EnbCheckConstants {
	
	public static final int NORMAL = 0;
	
	public static final int NOT_NORMAL = 1;
	
	public static final int NOT_CHECK = -1;
	
	public static final String ENB_GENERAL_STATE = "enbGeneralState";
	
	public static final String[] FORM_TITLE = { "检查项", "检查结果", "检查结果期望值" };
	
	public static final String[] INDEX_FORM_TITLE = { "基站标识", "基站名称", "协议版本", "实际版本",
			"连接状态", "健康检查结果", "故障摘要" };
	
	public static final String[] ALARM_FORM_TITLE = { "级别", "告警内容", "eNB名称",
		"定位信息", "状态", "发生时间" };
	
}
