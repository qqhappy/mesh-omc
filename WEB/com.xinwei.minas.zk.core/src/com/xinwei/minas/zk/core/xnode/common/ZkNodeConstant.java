/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

/**
 * 
 * ZK节点常量
 * 
 * @author chenjunhua
 * 
 */

public class ZkNodeConstant {

	// #define NK_MAX_NODE_NUMS 10240/*ZK树最大节点数量*/
	// #define NK_MAX_DATA_LEN 768 /*当前存储的最大数据长度*//*单个节点最大数据长度*/
	// #define NK_MAX_NODENAME_LEN 128/*单个节点最大全路径长度*/
	// #define NK_MAX_NODESHORTNAME_LEN 28/*单个节点短路径最大长度*/
	// #define NK_MAX_BTSGRP_PER_SAG 20/*单个sag接管的最大组数量 */
	// #define NK_MAX_BTSGRP_PER_SAGGRP 50/*单个sag集群中最大的bts组数量*/
	// #define NK_MAX_SAGNUM_PER_SAGGRP 10/*单个sag集群中最大的sag数量*/
	// #define NK_MAX_BTSNUM_PER_BTSGRP 10/*单个bts组中最大的bts数量*/
	// #define NK_NODE_DATAHDR_LEN 64/*单个节点数据头固定长度*/
	// #define NK_NODE_COMMENT_LEN 60/*单个节点备注区数据长度*/
	// #define NK_NODE_RULEXML_LEN 512/*切换规则文本长度*/
	//
	// #define INVALID_SAGID 0xFFFFFFFF/*invalid sag id*/
	//
	// //default subnode of bts,sag and rulelock
	// #define DEFAULT_NODE_BTSSTATE "BTSStatus"
	// #define DEFAULT_NODE_SAGSTATE "SAGStatus"
	// #define DEFAULT_NODE_SAGLINK "SAGLink"
	// #define DEFAULT_NODE_GROUPEDBTS "GROUPEDBTS"
	// #define DEFAULT_NODE_SAGDATA "SAGData"
	// #define DEFAULT_NODE_RULELOCK "RULELOCK"
	// #define INVALID_SAGID 0xFFFFFFFF
	
	// 永久节点
	public static final int CREATE_MODE_PERSISTENT = 0;
	
	// 临时节点
	public static final int CREATE_MODE_EPHEMERAL = 1;
	
	// 单个sag接管的最大组数量
	public static final int NK_MAX_BTSGRP_PER_SAG = 20;
	
	// 单个sag集群中最大的bts组数量
	public static final int NK_MAX_BTSGRP_PER_SAGGRP = 50;
	
	// 单个sag集群中最大的sag数量
	public static final int NK_MAX_SAGNUM_PER_SAGGRP = 10;
	
	// 单个bts组中最大的bts数量
	public static final int NK_MAX_BTSNUM_PER_BTSGRP = 10;

	// 单个节点备注区数据长度
	public static final int NK_NODE_COMMENT_LEN = 60;

	// 切换规则文本长度
	public static final int NK_NODE_RULEXML_LEN = 512;
	
	// 单个节点最大全路径长度
	public static final int NK_MAX_NODENAME_LEN = 128;

	public static final String CHARSET_US_ASCII = "US-ASCII";

	public static final String SAG_ROOT_NAME = "SAG_ROOT";
	
	public static final String SAG_INFO_NAME = "SAG_INFO";
	
	public static final String RULES_NAME = "RULES";
	
	public static final String BTS_INFO_NAME = "BTS_INFO";
	
	public static final String SERVICESAG_NAME = "SERVICESAG";
	
	public static final String SAG_ROOT_PATH = "/" + SAG_ROOT_NAME;
	
	public static final String BTS_GROUP_NODE_PREFIX = "BTSGRP_";
	
	public static final String SAGDATA_NODE_NAME = "SAGData";
		
	public static final int NODE_TYPE_ROOT = 0;
	public static final int NODE_TYPE_SAG_ROOT = 1;
	public static final int NODE_TYPE_SAG_GROUP = 2;
	public static final int NODE_TYPE_SAG_INFO = 3;
	public static final int NODE_TYPE_RULES = 4;
	public static final int NODE_TYPE_BTS_INFO = 5;
	public static final int NODE_TYPE_BTS_GROUP = 6;
	public static final int NODE_TYPE_BTS = 7;
	public static final int NODE_TYPE_SAG = 8;
	public static final int NODE_TYPE_SAGPAYLOAD = 9;
	public static final int NODE_TYPE_SAGDATA = 10;
	public static final int NODE_TYPE_BTSSTATE = 11;
	public static final int NODE_TYPE_NKCLI = 12;
	public static final int NODE_TYPE_NKCLI_SERVER = 13;
	public static final int NODE_TYPE_NKCLI_CLIENT = 14;
	public static final int NODE_TYPE_SERVSAG = 15;//bts组下服务节点
	public static final int NODE_TYPE_SAGLINK = 16;//bts下的sag链路数据
	public static final int NODE_TYPE_GROUPEDBTS = 17;//sag的服务bts组
	public static final int NODE_TYPE_SAGEXIST = 18;  //sag存在节点，临时节点
	public static final int NODE_TYPE_SAGLINEUP = 19; //排队节点，临时节点    
	public static final int NODE_TYPE_ACCESSGROUP = 20;
	public static final int NODE_TYPE_NULL = 0xFF;

	
	
}
