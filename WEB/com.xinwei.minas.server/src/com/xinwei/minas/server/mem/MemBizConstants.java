/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-09-02	| fanhaoyu 	| 	create the file                    
 */

package com.xinwei.minas.server.mem;

/**
 * 
 * MEM业务相关常量
 * 
 * @author fanhaoyu
 * 
 */

public class MemBizConstants {

	public final static int MA_MEM = 10;
	// MEM消息上报请求
	public final static int MOC_MEMINFO_REQ = 0x0A01;
	// MEM消息上报应答
	public final static int MOC_MEMINFO_RSP = 0x0A02;

	// 下行MEM综合管理
	public final static int MOC_MEM_MANAGE_REQ = 0x0A03;
	// 上行MEM综合管理
	public final static int MOC_MEM_MANAGE_RSP = 0x0A04;
	// 下行MEM综合管理1
	public final static int MOC_MEM_MANAGE1_REQ = 0x0A05;
	// 上行MEM综合管理1
	public final static int MOC_MEM_MANAGE1_RSP = 0x0A06;
	// 最后一个包标志
	public final static int LAST_PACKAGE = 1;
	// 业务请求消息
	public final static int SERVICE_REQUEST = 0x00;
	// 业务应答消息
	public final static int SERVICE_RESP = 0x01;
	// 操作对象是MEM
	public final static int OPER_OBJ_MEM = 0x02;
	// operType
	public final static int OPERTYPE_MEMINFO = 0x30;

	public final static int OPERTYPE_MEM_MANAGE = 0x31;

	public final static int OPERTYPE_MEM_MANAGE1 = 0x32;

}