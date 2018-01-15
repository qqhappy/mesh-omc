/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-04-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

/**
 * 
 * LTE基站消息常量类
 * 
 * @author chenjunhua
 * 
 */

public class EnbMessageConstants {

	// 0 配置管理
	public static final int MA_CONF = 0;

	// 1 故障管理
	public static final int MA_ALARM = 1;

	// 2 性能管理
	public static final int MA_PERF = 2;

	// 3 终端管理
	public static final int MA_UT = 3;

	// 4 文件管理
	public static final int MA_FILE = 4;

	// 5 安全管理
	public static final int MA_SECU = 5;

	// MA=0 配置管理消息
	// 整表配置
	public static final int MOC_FULLTABLECONFIG_CONFIG = 0X0000;
	// 整表反构
	public static final int MOC_FULLTABLEREVERSE_CONFIG = 0X0001;
	// 增量配置
	public static final int MOC_INCREMENTAL_CONFIG = 0x0002;

	// MA=5 安全管理消息================
	// 基站注册通知
	public static final int MOC_REGISTER_NOTIFY = 0x0800;

	// 基站注册请求及应答
	public static final int MOC_REGISTER = 0x0801;

	// 基站心跳请求及应答
	public static final int MOC_HEARTBEAT = 0x0802;

	// 基站下载数据通知 bts到ems
	public static final int MOC_DOWNLOAD_DATA_NOTIFY = 0x0605;

	// 整表配置结果通知
	public static final int MOC_FULLTABLECONFIG_NOTIFY = 0X0003;

	// 整表反构结构通知
	public static final int MOC_FULLTABLEREVERSE_NOTIFY = 0X0004;

	public static final int MOC_DOWNLOAD_FINISHED_REQ = 0x060a;
	/*
	 * 基站版本下载
	 */
	// 基站版本下载的REQ 和 RSP
	public static final int MOC_ENB_VERSION_DOWNLOAD = 0x0400;
	// 基站版本下载进度通知；
	public static final int MOC_ENB_VERSION_PROGRESS_NOTIFY = 0x0401;
	// 基站版本下载结果通知
	public static final int MOC_ENB_VERSION_RESULT_NOTIFY = 0x0402;

	/**
	 * 基站版本升级请求
	 */
	public static final int MOC_ENB_VERSION_UPGRADE = 0x0403;

	/**
	 * RRU版本升级请求
	 */
	public static final int MOC_ENB_RRU_VERSION_UPGRADE = 0x0404;

	// 告警通知请求
	public static final int MOC_ALARM_NTFY = 0x0500;

	// 告警同步
	public static final int MOC_ALARM_SYNC = 0x0501;

	// Action Type
	// 0 － 查询
	// 1 － 配置
	// 2 － 增加
	// 3 － 删除
	// 4 － 修改
	// 0xFF － 其他
	public static final int ACTION_QUERY = 0;

	public static final int ACTION_CONFIG = 1;

	public static final int ACTION_ADD = 2;

	public static final int ACTION_MODIFY = 3;

	public static final int ACTION_DELETE = 4;

	public static final int ACTION_OTHERS = 0xFF;

	// Message Type
	// 0 － 请求
	// 1 － 应答
	// 2 － 通知
	public static final int MESSAGE_REQUEST = 0;

	public static final int MESSAGE_RESPONSE = 1;

	public static final int MESSAGE_NOTIFY = 2;

	/*
	 * 应用传输层承载的总的应用报文层数据包的长度最大为1440字节*256包=368640字节
	 */
	public static final int MESSAGE_MAX_LEN = 1440 * 256;

	/*
	 * 应用传输层承载的单个应用报文层数据包的长度最大为1440字节，超过这个数值，应用传输层需要进行拆包
	 */
	public static final int SINGLE_MAX_LEN = MESSAGE_MAX_LEN;

	/**
	 * 切换基站软件的请求和应答
	 */
	public static final int MOC_SWITCH_ENB_VERSION_REQ_AND_RES = 0x0403;
	/**
	 * RRU软件切换请求和应答
	 */
	public static final int MOC_SWITCH_RRU_VERSION_REQ_AND_RES = 0x0404;

	/**
	 * 基站复位请求和应答
	 */
	public static final int MOC_RESET = 0x0005;

	/**
	 * 复位类型-基站级
	 */
	public static final int RESET_TYPE_BTS = 0;

	/**
	 * 复位类型-单板级
	 */
	public static final int RESET_TYPE_BOARD = 1;

	/**
	 * 基站数据配置自学习请求和应答moc
	 */
	public static final int MOC_ENB_DATA_CONFIG_STUDY_REQ_AND_RES = 0x0008;

	/**
	 * 动态配置项配置请求和应答moc
	 */
	public static final int MOC_STATUS_CONFIG_REQ_AND_RES = 0x0009;

	/**
	 * 动态配置项查询请求和应答moc
	 */
	public static final int MOC_STATUS_QUERY_REQ_AND_RES = 0x000A;

	/**
	 * 0X0600 性能管理开始请求和应答 0XFF 0X00
	 */
	public static final int MOC_REALTIME_MONITOR_START_REQ_AND_RES = 0x0600;

	/**
	 * 0X0601 性能管理结束请求和应答 0XFF 0X00
	 */
	public static final int MOC_REALTIME_MONITOR_STOP_REQ_AND_RES = 0x0601;

	/**
	 * 0X0602 性能数据上报 0XFF 0X02
	 */
	public static final int MOC_REPORT_REALTIME_DATA_NOTIRY = 0x0602;
	
	/**
	 * 资产信息上报
	 */
	public static final int MOC_ASSET_INFO_NOTIFY = 0x000B;

}
