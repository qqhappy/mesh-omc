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
 * McBts��Ϣ������
 * 
 * @author chenjunhua
 * 
 */

public class McBtsMessageConstants {

	// MA=0���ù�����Ϣ===============
	// У׼�ļ��������֪ͨ
	public static final int MC_CALIB_FILE_UPLOADED_NOTIFY = 0x00a0;

	// ��վIP����֪ͨ
	public static final int MOC_BTS_IP_REQ_NOTIFY = 0x00c1;

	// ��վIP�·�����
	public static final int MOC_BTS_IP_REQ = 0x00c2;
	
	// ��վ��EMS��ѯEMSʱ��
	public static final int MOC_EMS_TIME_REQ = 0x07ec;
	
	// EMS���վӦ��EMSʱ��
	public static final int MOC_EMS_TIME_RESP = 0x07ed;

	// MA=1 ���Ϲ�����Ϣ===============
	// �澯֪ͨ
	public static final int MOC_ALARM_NTFY = 0x0101;

	// �澯�б�����
	public static final int MOC_ALARM_REQUEST = 0x0104;

	// �澯�б�Ӧ��
	public static final int MOC_ALARM_RESPONSE = 0x0105;

	// MA=2 ����ͳ�ƹ�����Ϣ============

	// MA=3 �ն˹�����Ϣ================
	// �ն�ע��֪ͨ
	public static final int MOC_UT_REGISTER_NOTIFY = 0x0301;

	// MA=4 �ļ�������Ϣ================
	// ��վ�汾���ؽ��֪ͨ
	public static final int MOC_MCBTS_FILE_RESULT_RESPONSE = 0x0403;

	// �ն˰汾���ؽ��֪ͨ
	public static final int MOC_UT_FILE_RESULT_RESPONSE = 0x0406;

	// ��վ�汾���ؽ���֪ͨ
	public static final int MOC_MCBTS_FILE_PROGRESS = 0x0432;

	// �����ն��������֪ͨ
	public static final int MOC_UT_FILE_UPGRADE_PROGRESS_NOTIFY = 0x040d;

	// �����ն�������֪ͨ
	public static final int MOC_UT_FILE_UPGRADE_RESULT_NOTIFY = 0x040e;

	// �ն������ϵ�����֪ͨ
	public static final int MOC_UT_UPGRADE_BREAKPOINT_RESUME_NOTIFY = 0x042e;

	// MA=6 ��ȫ������Ϣ================
	// ��վע��֪ͨ
	public static final int MOC_REGISTER_NOTIFY = 0x0601;

	// ��վע������
	public static final int MOC_REGISTER_REQUEST = 0x0602;

	// ��վע��Ӧ��
	public static final int MOC_REGISTER_RESPONSE = 0x0603;

	// ��վ��������
	public static final int MOC_HEARTBEAT_REQUEST = 0x0614;

	// ��վ����Ӧ��
	public static final int MOC_HEARTBEAT_RESPONSE = 0x0615;

	// ��վ��������֪ͨ bts��ems
	public static final int MOC_DOWNLOAD_DATA_NOTIFY = 0x0605;

	public static final int MOC_DOWNLOAD_FINISHED_REQ = 0x060a;

	// MA=7GPS������Ϣ=================
	// GPS����֪ͨ��Ϣ
	public static final int MOC_GPS_DATA_NOTIFY = 0x0701;
	// ��λ����
	public static final int MOC_LOCATION_REQUEST = 0x0750;
	// ��λӦ��
	public static final int MOC_LOCATION_RESPONSE = 0x0751;

	// �����ն��б�Ӧ��
	public static final int MOC_ONLINE_USERLIST_RESPONSE = 0x021b;
	
	// ��վʵʱ�����ϱ���Ϣ
	public static final int MOC_REAL_TIME_PERF_RESPONSE = 0x0215;
	
}
