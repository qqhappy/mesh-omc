/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-30	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.utils;

/**
 * 
 * 常量标志类
 * 
 * @author chenshaohua
 * 
 */

public class OperationInfo {

	//增加操作
	public static final int ADD_OPER = 0;
	
	//修改操作
	public static final int MODIFY_OPER = 1;
	
	//需要同步
	public static final int NEED_SYNC = 0;
	
	//不需要同步
	public static final int NOT_NEED_SYNC = 1;
}
