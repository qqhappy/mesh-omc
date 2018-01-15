/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

/**
 * 
 * 告警常量类
 * 
 * @author chenjunhua
 * 
 */

public class AlarmConstants implements java.io.Serializable{

	// 告警状态-恢复
	public static final int RESTORED = 0;

	// 告警状态-告警
	public static final int ALARM = 1;
	
	// 未确认
	public static final int UNCONFIRMED = 0;
	
	// 已确认
	public static final int CONFIRMED = 1;
	
	//自动恢复
	public static final int AUTORESTORE =1;
	//手工恢复
	public static final int MANURESTORE = 0;

}
