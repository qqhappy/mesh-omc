/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

/**
 * 
 * McBts消息常量类
 * 
 * @author chenjunhua
 * 
 */

public class McBtsMessageConstants {

	// MA=0配置管理消息===============
	// 校准文件传输完成通知
	public static final int MC_CALIB_FILE_UPLOADED_NOTIFY = 0x00a0;

	// 基站IP请求通知
	public static final int MOC_BTS_IP_REQ_NOTIFY = 0x00c1;

	// 基站IP下发请求
	public static final int MOC_BTS_IP_REQ = 0x00c2;
	
	// 基站向EMS查询EMS时间
	public static final int MOC_EMS_TIME_REQ = 0x07ec;
	
	// EMS向基站应答EMS时间
	public static final int MOC_EMS_TIME_RESP = 0x07ed;

	// MA=1 故障管理消息===============
	// 告警通知
	public static final int MOC_ALARM_NTFY = 0x0101;

	// 告警列表请求
	public static final int MOC_ALARM_REQUEST = 0x0104;

	// 告警列表应答
	public static final int MOC_ALARM_RESPONSE = 0x0105;

	// MA=2 性能统计管理消息============

	// MA=3 终端管理消息================
	// 终端注册通知
	public static final int MOC_UT_REGISTER_NOTIFY = 0x0301;

	// MA=4 文件管理消息================
	// 基站版本下载结果通知
	public static final int MOC_MCBTS_FILE_RESULT_RESPONSE = 0x0403;

	// 终端版本下载结果通知
	public static final int MOC_UT_FILE_RESULT_RESPONSE = 0x0406;

	// 基站版本下载进度通知
	public static final int MOC_MCBTS_FILE_PROGRESS = 0x0432;

	// 升级终端软件进度通知
	public static final int MOC_UT_FILE_UPGRADE_PROGRESS_NOTIFY = 0x040d;

	// 升级终端软件结果通知
	public static final int MOC_UT_FILE_UPGRADE_RESULT_NOTIFY = 0x040e;

	// 终端升级断点续传通知
	public static final int MOC_UT_UPGRADE_BREAKPOINT_RESUME_NOTIFY = 0x042e;

	// MA=6 安全管理消息================
	// 基站注册通知
	public static final int MOC_REGISTER_NOTIFY = 0x0601;

	// 基站注册请求
	public static final int MOC_REGISTER_REQUEST = 0x0602;

	// 基站注册应答
	public static final int MOC_REGISTER_RESPONSE = 0x0603;

	// 基站心跳请求
	public static final int MOC_HEARTBEAT_REQUEST = 0x0614;

	// 基站心跳应答
	public static final int MOC_HEARTBEAT_RESPONSE = 0x0615;

	// 基站下载数据通知 bts到ems
	public static final int MOC_DOWNLOAD_DATA_NOTIFY = 0x0605;

	public static final int MOC_DOWNLOAD_FINISHED_REQ = 0x060a;

	// MA=7GPS管理消息=================
	// GPS数据通知消息
	public static final int MOC_GPS_DATA_NOTIFY = 0x0701;
	// 定位请求
	public static final int MOC_LOCATION_REQUEST = 0x0750;
	// 定位应答
	public static final int MOC_LOCATION_RESPONSE = 0x0751;

	// 在线终端列表应答
	public static final int MOC_ONLINE_USERLIST_RESPONSE = 0x021b;
	
	// 基站实时性能上报消息
	public static final int MOC_REAL_TIME_PERF_RESPONSE = 0x0215;
	
}
