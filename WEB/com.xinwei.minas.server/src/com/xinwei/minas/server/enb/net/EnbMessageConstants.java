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
 * LTE��վ��Ϣ������
 * 
 * @author chenjunhua
 * 
 */

public class EnbMessageConstants {

	// 0 ���ù���
	public static final int MA_CONF = 0;

	// 1 ���Ϲ���
	public static final int MA_ALARM = 1;

	// 2 ���ܹ���
	public static final int MA_PERF = 2;

	// 3 �ն˹���
	public static final int MA_UT = 3;

	// 4 �ļ�����
	public static final int MA_FILE = 4;

	// 5 ��ȫ����
	public static final int MA_SECU = 5;

	// MA=0 ���ù�����Ϣ
	// ��������
	public static final int MOC_FULLTABLECONFIG_CONFIG = 0X0000;
	// ������
	public static final int MOC_FULLTABLEREVERSE_CONFIG = 0X0001;
	// ��������
	public static final int MOC_INCREMENTAL_CONFIG = 0x0002;

	// MA=5 ��ȫ������Ϣ================
	// ��վע��֪ͨ
	public static final int MOC_REGISTER_NOTIFY = 0x0800;

	// ��վע������Ӧ��
	public static final int MOC_REGISTER = 0x0801;

	// ��վ��������Ӧ��
	public static final int MOC_HEARTBEAT = 0x0802;

	// ��վ��������֪ͨ bts��ems
	public static final int MOC_DOWNLOAD_DATA_NOTIFY = 0x0605;

	// �������ý��֪ͨ
	public static final int MOC_FULLTABLECONFIG_NOTIFY = 0X0003;

	// �������ṹ֪ͨ
	public static final int MOC_FULLTABLEREVERSE_NOTIFY = 0X0004;

	public static final int MOC_DOWNLOAD_FINISHED_REQ = 0x060a;
	/*
	 * ��վ�汾����
	 */
	// ��վ�汾���ص�REQ �� RSP
	public static final int MOC_ENB_VERSION_DOWNLOAD = 0x0400;
	// ��վ�汾���ؽ���֪ͨ��
	public static final int MOC_ENB_VERSION_PROGRESS_NOTIFY = 0x0401;
	// ��վ�汾���ؽ��֪ͨ
	public static final int MOC_ENB_VERSION_RESULT_NOTIFY = 0x0402;

	/**
	 * ��վ�汾��������
	 */
	public static final int MOC_ENB_VERSION_UPGRADE = 0x0403;

	/**
	 * RRU�汾��������
	 */
	public static final int MOC_ENB_RRU_VERSION_UPGRADE = 0x0404;

	// �澯֪ͨ����
	public static final int MOC_ALARM_NTFY = 0x0500;

	// �澯ͬ��
	public static final int MOC_ALARM_SYNC = 0x0501;

	// Action Type
	// 0 �� ��ѯ
	// 1 �� ����
	// 2 �� ����
	// 3 �� ɾ��
	// 4 �� �޸�
	// 0xFF �� ����
	public static final int ACTION_QUERY = 0;

	public static final int ACTION_CONFIG = 1;

	public static final int ACTION_ADD = 2;

	public static final int ACTION_MODIFY = 3;

	public static final int ACTION_DELETE = 4;

	public static final int ACTION_OTHERS = 0xFF;

	// Message Type
	// 0 �� ����
	// 1 �� Ӧ��
	// 2 �� ֪ͨ
	public static final int MESSAGE_REQUEST = 0;

	public static final int MESSAGE_RESPONSE = 1;

	public static final int MESSAGE_NOTIFY = 2;

	/*
	 * Ӧ�ô������ص��ܵ�Ӧ�ñ��Ĳ����ݰ��ĳ������Ϊ1440�ֽ�*256��=368640�ֽ�
	 */
	public static final int MESSAGE_MAX_LEN = 1440 * 256;

	/*
	 * Ӧ�ô������صĵ���Ӧ�ñ��Ĳ����ݰ��ĳ������Ϊ1440�ֽڣ����������ֵ��Ӧ�ô������Ҫ���в��
	 */
	public static final int SINGLE_MAX_LEN = MESSAGE_MAX_LEN;

	/**
	 * �л���վ����������Ӧ��
	 */
	public static final int MOC_SWITCH_ENB_VERSION_REQ_AND_RES = 0x0403;
	/**
	 * RRU����л������Ӧ��
	 */
	public static final int MOC_SWITCH_RRU_VERSION_REQ_AND_RES = 0x0404;

	/**
	 * ��վ��λ�����Ӧ��
	 */
	public static final int MOC_RESET = 0x0005;

	/**
	 * ��λ����-��վ��
	 */
	public static final int RESET_TYPE_BTS = 0;

	/**
	 * ��λ����-���弶
	 */
	public static final int RESET_TYPE_BOARD = 1;

	/**
	 * ��վ����������ѧϰ�����Ӧ��moc
	 */
	public static final int MOC_ENB_DATA_CONFIG_STUDY_REQ_AND_RES = 0x0008;

	/**
	 * ��̬���������������Ӧ��moc
	 */
	public static final int MOC_STATUS_CONFIG_REQ_AND_RES = 0x0009;

	/**
	 * ��̬�������ѯ�����Ӧ��moc
	 */
	public static final int MOC_STATUS_QUERY_REQ_AND_RES = 0x000A;

	/**
	 * 0X0600 ���ܹ���ʼ�����Ӧ�� 0XFF 0X00
	 */
	public static final int MOC_REALTIME_MONITOR_START_REQ_AND_RES = 0x0600;

	/**
	 * 0X0601 ���ܹ�����������Ӧ�� 0XFF 0X00
	 */
	public static final int MOC_REALTIME_MONITOR_STOP_REQ_AND_RES = 0x0601;

	/**
	 * 0X0602 ���������ϱ� 0XFF 0X02
	 */
	public static final int MOC_REPORT_REALTIME_DATA_NOTIRY = 0x0602;
	
	/**
	 * �ʲ���Ϣ�ϱ�
	 */
	public static final int MOC_ASSET_INFO_NOTIFY = 0x000B;

}
