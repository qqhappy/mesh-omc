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
 * ZK�ڵ㳣��
 * 
 * @author chenjunhua
 * 
 */

public class ZkNodeConstant {

	// #define NK_MAX_NODE_NUMS 10240/*ZK�����ڵ�����*/
	// #define NK_MAX_DATA_LEN 768 /*��ǰ�洢��������ݳ���*//*�����ڵ�������ݳ���*/
	// #define NK_MAX_NODENAME_LEN 128/*�����ڵ����ȫ·������*/
	// #define NK_MAX_NODESHORTNAME_LEN 28/*�����ڵ��·����󳤶�*/
	// #define NK_MAX_BTSGRP_PER_SAG 20/*����sag�ӹܵ���������� */
	// #define NK_MAX_BTSGRP_PER_SAGGRP 50/*����sag��Ⱥ������bts������*/
	// #define NK_MAX_SAGNUM_PER_SAGGRP 10/*����sag��Ⱥ������sag����*/
	// #define NK_MAX_BTSNUM_PER_BTSGRP 10/*����bts��������bts����*/
	// #define NK_NODE_DATAHDR_LEN 64/*�����ڵ�����ͷ�̶�����*/
	// #define NK_NODE_COMMENT_LEN 60/*�����ڵ㱸ע�����ݳ���*/
	// #define NK_NODE_RULEXML_LEN 512/*�л������ı�����*/
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
	
	// ���ýڵ�
	public static final int CREATE_MODE_PERSISTENT = 0;
	
	// ��ʱ�ڵ�
	public static final int CREATE_MODE_EPHEMERAL = 1;
	
	// ����sag�ӹܵ����������
	public static final int NK_MAX_BTSGRP_PER_SAG = 20;
	
	// ����sag��Ⱥ������bts������
	public static final int NK_MAX_BTSGRP_PER_SAGGRP = 50;
	
	// ����sag��Ⱥ������sag����
	public static final int NK_MAX_SAGNUM_PER_SAGGRP = 10;
	
	// ����bts��������bts����
	public static final int NK_MAX_BTSNUM_PER_BTSGRP = 10;

	// �����ڵ㱸ע�����ݳ���
	public static final int NK_NODE_COMMENT_LEN = 60;

	// �л������ı�����
	public static final int NK_NODE_RULEXML_LEN = 512;
	
	// �����ڵ����ȫ·������
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
	public static final int NODE_TYPE_SERVSAG = 15;//bts���·���ڵ�
	public static final int NODE_TYPE_SAGLINK = 16;//bts�µ�sag��·����
	public static final int NODE_TYPE_GROUPEDBTS = 17;//sag�ķ���bts��
	public static final int NODE_TYPE_SAGEXIST = 18;  //sag���ڽڵ㣬��ʱ�ڵ�
	public static final int NODE_TYPE_SAGLINEUP = 19; //�Ŷӽڵ㣬��ʱ�ڵ�    
	public static final int NODE_TYPE_ACCESSGROUP = 20;
	public static final int NODE_TYPE_NULL = 0xFF;

	
	
}
