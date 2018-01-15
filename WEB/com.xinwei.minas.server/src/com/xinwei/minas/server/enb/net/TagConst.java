/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

/**
 * 
 * TAG ������
 * 
 * @author chenjunhua
 * 
 */

public class TagConst {

	// ���TAGʱ��ע�ⲻҪ�ظ���

	public static final int RESULT = 0X0001;

	public static final int ERR_MSG = 0X0002;

	public static final int DB_NAME = 0X0003;

	public static final int TABLE_NAME = 0X0004;

	public static final int SQL_TEXT = 0X0005;

	public static final int ROW_NUM = 0X0006;

	public static final int RESULT_SET = 0X0007;

	/**
	 * ��λ����
	 */
	public static final int RESET_TYPE = 0X000A;

	/**
	 * ���ܺ�
	 */
	public static final int RACK_NO = 0X000B;

	/**
	 * �����
	 */
	public static final int SHELF_NO = 0X000C;

	/**
	 * ��λ��
	 */
	public static final int SLOT_NO = 0X000D;

	public static final int FTP_IP = 0X0400;

	public static final int FTP_PORT = 0X0401;

	public static final int FTP_USER_NAME = 0X0402;

	public static final int FTP_PASSWORD = 0X0403;

	public static final int FILE_DIRECTORY = 0X0404;

	public static final int FILE_NAME = 0X0000;

	/**
	 * ��վ��������
	 */
	public static final int SOFTWARE_TYPE = 0X0405;

	public static final int VERSION = 0X0406;

	public static final int DATA_FILE_DIRECTORY = 0X0008;

	public static final int DATA_FILE_NAME = 0X0009;
	/**
	 * ��վ�汾���ؽ��Ⱥͽ��
	 */
	public static final int ENB_DOWNLOAD_PROGRESS = 0X040A;
	public static final int ENB_DOWNLOAD_RESULT = 0X040B;

	/**
	 * �л�����汾����
	 */
	public static final int SWITCH_SOFTWARE_TYPE = 0X040C;

	/**
	 * RRU�л��������
	 */
	public static final int RRU_SWITCH_SOFTWARE_TYPE = 0X040D;

	// �澯��
	public static final int ALARM_DEF_CODE = 0X0500;
	// �澯����
	public static final int ALARM_DEF_SUB_CODE = 0X0501;
	// �澯��־
	public static final int ALARM_FLAG = 0X0502;
	// �澯λ��
	public static final int ALARM_LOC_ID = 0X0503;
	// �澯����ʱ��
	public static final int ALARM_TIME = 0X0504;
	/**
	 * �澯λ������
	 */
	public static final int ALARM_LOC_TYPE = 0X0505;

	/**
	 * ��ǰ�澯�ܸ���
	 */
	public static final int ALARM_TOTAL_NUM = 0X0506;

	/**
	 * ��ǰ���ݼ���״̬
	 */
	public static final int DATA_LOAD_STATUS = 0X0801;

	/**
	 * ��ѧϰ�Ļ�վ��������
	 */
	public static final int DATA_CONFIG = 0X000E;

	public static final int STATUS_CONFIG_FLAG = 0x0012;
	public static final int STATUS_QUERY_FLAG = 0x0013;

	// eNB����ת�� 0040 0028
	public static final int ENB_FAN_SPEED = 0X0028;

	// �������ݹ��� 0041 0029
	public static final int BOARD_STATUS = 0X0029;

	// RRU��Ƶ״̬��ѯ 0042 002A
	public static final int RRU_RF_STATUS = 0X002A;

	// RRU���޲�ѯ 0043 002B
	public static final int RRU_THRESHOLD = 0X002B;

	// RRU����״̬��ѯ 0044 002C
	public static final int RRU_RUNNING_STATUS = 0X002C;

	// RRU���״̬��ѯ 0045 002D
	public static final int RRU_FIBER_OPTICAL_STATUS = 0X002D;

	// RRU���״̬��ѯ 0045 0034
	public static final int BBU_FIBER_OPTICAL_STATUS = 0X0034;
	// ���ſ���
	public static final int RF_SWITCH = 0X0087;

	// eNB״̬��Ϣ
	public static final int ENB_VISIBLE_SATELLITES = 0x0014;
	public static final int ENB_TRACK_SATELLITES = 0x0015;
	public static final int ENB_POWER_CONSUMPTION = 0x0016;
	public static final int STATE = 0x0017;
	public static final int ENB_RUNNING_TIME = 0x0018;
	public static final int RFLOFREGSTATE= 0x01C2;
	public static final int ENB_TIME = 0x0021;
	public static final int ENB_CLOCK_TYPE = 0x0024;
	public static final int ENB_CLOCK_STATUS = 0x0025;
	public static final int FAN_NO = 0x00eb;
	public static final int FAN_SPEED = 0x00ec;
	public static final int ENB_TEMPERATURE = 0x0089;
	public static final int PORT_WORK_MODE = 0x0090;
	public static final int PORT_RATE = 0x0091;
	public static final int PORT_DUPLEX_MODE = 0x0092;

	// ����״̬��Ϣ
	public static final int RRU_BOARD_NUM = 0x0020;
	public static final int BBU_HARDWARE_VERSION = 0x001c;
	public static final int BBU_PRODUCTION_SN = 0x001d;
	public static final int RRU_HARDWARE_VERSION = 0x001e;
	public static final int RRU_PRODUCTION_SN = 0x001f;

	// RRU��Ƶ״̬
	public static final int CHANNEL_NO = 0x0086;
	public static final int UL_ANT_STATE = 0x01b1;
	public static final int DL_ANT_STATE = 0x01b2;
	public static final int CHANNEL_TEMPERATURE = 0x01b3;
	public static final int CHANNEL_TX_POWER = 0x01b4;
	public static final int CHANNEL_TX_GAIN = 0x01b5;
	public static final int CHANNEL_RX_GAIN = 0x01b6;
	public static final int CHANNEL_RX_POWER = 0x01b7;
	public static final int CHANNEL_VSWR = 0x01b8;
	public static final int UL_POWER_RESULT = 0x01b9;
	public static final int DL_POWER_RESULT = 0x01ba;
	public static final int VSWR_RESULT = 0x01bb;
	public static final int POWERRESULT= 0x00ed;
	public static final int DPDTRAININGRESULT= 0x0019;
	

	// RRU����
	public static final int VSWR_THRESHOLD = 0x01bd;
	public static final int BOARD_TEMP_THRES = 0x01be;
	public static final int CHANNEL_TEMP_THRES = 0x01bf;

	// RRU����״̬
	public static final int RF_LOCAL_FREQ = 0x01c1;
	public static final int RF_LOCAL_FREQ_STATE = 0x01c2;
	public static final int CLOCK_STATE = 0x01c3;
	public static final int IR_WORK_MODE = 0x01c4;
	public static final int RRU_RUNNING_STATE = 0x01c5;
	public static final int RF_MB_TEMPERATURE = 0x01c6;
	public static final int RF_AB_TEMPERATURE = 0x01c7;
	public static final int DPD_TRAINING_RESULT = 0x0019;

	// RRU���״̬
	public static final int RX_POWER = 0x01c9;
	public static final int TX_POWER = 0x01ca;
	public static final int INSERT_STATE = 0x01cb;
	public static final int MODULE_MANUFACTURE = 0x01cc;
	public static final int MODULE_MANUFACTURE1 = 0x01d2;
	public static final int MODULE_TRANS_RATE = 0x01cd;
	public static final int MODULE_TEMPERATURE = 0x01ce;
	public static final int VOLTAGE = 0x01cf;
	public static final int CURRENT = 0x01d0;
	public static final int FIBER_OPTIC_NO = 0x01d1;
	//xGW��ַ�Ը���
	public static final int XCG_ADDRESS_COUNT = 0x0035;
	public static final int xGWAddressLocalIP = 0x01D3;
	public static final int xGWAddressDstIP = 0x01D4;
	public static final int xGWAddressVlanIndex = 0x01D5;

	//������
	public static final int LOCAL_PACKETLOSSRATE = 0x01DC;
	public static final int DST_PACKETLOSSRATE = 0x01DD;
	public static final int TIME_DELAY_COUNT = 0x0037;
	public static final int PACKAGE_TEST_COUNT = 0x0038;
	public static final int TIME_DELAY = 0x01DE;
	
	//�տ��������Բ�ѯ
	public static final int PACKETLENGTH = 0x01DA;
	public static final int FLOWRATE = 0x01DB;
	
	public static final int RRU_CHANNEL_NUM = 0x0088;

	// ��Ƶ״̬ѭ��TAG
	public static final int IBTS_RF_STATUS = 0x002F;
	
	// ���״̬ѭ��TAG
	public static final int IBTS_OPTICAL_STATUS = 0x0032;
	
	//΢��վ�����ѯTAG
	public static final int MBD_HARDWARE_VERSION = 0x008a;
	public static final int MBD_PRODUCTION_SN = 0x008b;
	public static final int PAU_HARDWARE_VERSION = 0x008c;
	public static final int PAU_PRODUCTION_SN = 0x008d;
	
	/**
	 * ʵʱ���������ϱ�ʱ����
	 */
	public static final int REALTIME_INTERVAL = 0x0601;

	/**
	 * ʵʱ��������
	 */
	public static final int REALTIME_REPORT_DATA = 0x0602;
	
	/**
	 * �ʲ���Ϣ�ϱ�
	 */
	public static final int ASSETS_INFO_NOTIFY = 0x0036;
	
	// �ʲ��ڵ�����
	public static final int ASSET_NODE_TYPE = 0x01D6;
	// �ʲ�λ����Ϣ
	public static final int ASSET_LOACTION_INFO = 0x01D7;
	// �ʲ��ṩ����
	public static final int ASSET_PROVIDER_NAME = 0x01D8;
	// �ʲ���������
	public static final int ASSET_MANUFACTURE_DATE = 0x01D9;
	
	
}
