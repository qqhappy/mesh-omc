package com.xinwei.minas.enb.core.utils;

public class EnbConstantUtils {
	// ��������ҵ������
	public static final String FULL_TABLE_CONFIG = "full_table_config";

	// ������ҵ������
	public static final String FULL_TABLE_REVERSE = "full_table_reverse";

	// enb����汾����ҵ������
	public static final String ENB_SOFTWARE_UPGRADE = "enb_software_upgrade";

	// ftp ip
	public static final String FTP_IP = "ftpIp";

	// ftp port
	public static final String FTP_PORT = "ftpPort";

	// ftp user name ftpUserName
	public static final String FTP_USER_NAME = "ftpUserName";

	// ftp password ftpPassword
	public static final String FTP_PASSWORD = "ftpPassword";

	// file directory
	public static final String FILE_DIRECTORY = "fileDirectory";

	// file name
	public static final String FILE_NAME = "fileName";

	// �������
	public static final String SOFTWARE_TYPE = "softwareType";

	// �汾
	public static final String VERSION = "version";

	// �����ļ�·��
	public static final String DATA_FILE_DIRECTORY = "dataFileDirectory";

	// �����ļ���
	public static final String DATA_FILE_NAME = "dataFileName";
	/**
	 * ��վ�汾����
	 */
	// SWType
	public static final String SW_TYPE = "SWType";
	// Ho_type
	public static final String Ho_Type = "HoType";
	// BBU
	public static final int BBU = 0x00;
	// RRU
	public static final int RRU = 0x01;
	// BBU+RRU
	public static final int BBU_RRU = 0x02;

	/**
	 * ���ܱ�
	 */
	public static final String TABLE_NAME_T_RACK = "T_RACK";

	/**
	 * �����
	 */
	public static final String TABLE_NAME_T_SHELF = "T_SHELF";

	/**
	 * �����-������
	 */
	public static final String FIELD_NAME_RACK_NAME = "s8RackName";

	/**
	 * �����
	 */
	public static final String TABLE_NAME_T_BOARD = "T_BOARD";

	/**
	 * �ֶ������ܺ�
	 */
	public static final String FIELD_NAME_RACKNO = "u8RackNO";

	/**
	 * �ֶ��������
	 */
	public static final String FIELD_NAME_SHELFNO = "u8ShelfNO";

	/**
	 * �ֶ�����λ��
	 */
	public static final String FIELD_NAME_SLOTNO = "u8SlotNO";

	/**
	 * �ֶ�����������
	 */
	public static final String FIELD_NAME_BDTYPE = "u8BDType";

	/**
	 * ��������BBU
	 */
	public static final int BOARD_TYPE_BBU = 1;

	/**
	 * ��������RRU
	 */
	public static final int BOARD_TYPE_RRU = 2;

	/**
	 * ���˱�
	 */
	public static final String TABLE_NAME_T_TOPO = "T_TOPO";

	/**
	 * ���˱�-�����ܺ�
	 */
	public static final String FIELD_NAME_MRACKNO = "u8MRackNO";

	/**
	 * ���˱�-�������
	 */
	public static final String FIELD_NAME_MSHELFNO = "u8MShelfNO";

	/**
	 * ���˱�-����λ��
	 */
	public static final String FIELD_NAME_MSLOTNO = "u8MSlotNO";

	/**
	 * ���˱�-��ں�
	 */
	public static final String FIELD_NAME_FIBER_PORT = "u8FiberPort";

	/**
	 * ���˱�-�ӻ��ܺ�
	 */
	public static final String FIELD_NAME_SRACKNO = "u8SRackNO";

	/**
	 * ���˱�-�ӻ����
	 */
	public static final String FIELD_NAME_SSHELFNO = "u8SShelfNO";

	/**
	 * ���˱�-�Ӳ�λ��
	 */
	public static final String FIELD_NAME_SSLOTNO = "u8SSlotNO";

	/**
	 * ���ܱ�
	 */
	public static final String TABLE_NAME_T_OMC = "T_OMC";

	/**
	 * OMC���е����ܱ�ʶ
	 */
	public static final String FIELD_NAME_OMC_ID = "u8OmcID";

	/**
	 * OMC���е�IP��ʶ
	 */
	public static final String FIELD_NAME_ENB_IP_ID = "u8eNBIPID";

	/**
	 * OMC���е�����IP��ʶ
	 */
	public static final String FIELD_NAME_OMC_SERVER_IP = "au8OmcServerIP";

	/**
	 * OMC���е����ܶ˿�
	 */
	public static final String FIELD_NAME_SRC_PORT = "u16SrcPort";

	/**
	 * OMC���еĻ�վ�˿�
	 */
	public static final String FIELD_NAME_DST_PORT = "u16DstPort";

	/**
	 * OMC���еķ�������
	 */
	public static final String FIELD_NAME_QOS = "u8Qos";

	/**
	 * ״̬
	 */
	public static final String FIELD_NAME_STATUS = "u32Status";

	/**
	 * ״̬-����
	 */
	public static final int STATUS_NORMAL = 0;

	/**
	 * ״̬-������
	 */
	public static final int STATUS_ABNORMAL = 1;

	/**
	 * IPV4��
	 */
	public static final String TABLE_NAME_T_IPV4 = "T_IPV4";

	/**
	 * IPV4���е�IP��ʶ
	 */
	public static final String FIELD_NAME_IP_ID = "u8IPID";

	/**
	 * IPV4���ж˿ڱ�ʶ
	 */
	public static final String FIELD_NAME_IPV4_PORT_ID = "U8PortID";

	/**
	 * IPV4����--IP��ַ
	 */
	public static final String FIELD_NAME_IP_ADDR = "au8IPAddr";

	/**
	 * IPV4����--����
	 */
	public static final String FIELD_NAME_NET_MASK = "au8NetMask";

	/**
	 * IPV4����--����
	 */
	public static final String FIELD_NAME_GATEWAY = "au8GWAddr";

	/**
	 * ��̫��������
	 */
	public static final String TABLE_NAME_T_ETHPARA = "T_ETHPARA";

	/**
	 * ��̫��������--�˿ڱ�ʶ
	 */
	public static final String FIELD_NAME_PORT_ID = "u8PortID";

	/**
	 * ��̫��������--�˿ں�
	 */
	public static final String FIELD_NAME_ETH_PORT = "u8EthPort";

	/**
	 * ��̬·�ɱ�
	 */
	public static final String TABLE_NAME_T_STROUT = "T_STROUT";

	/**
	 * ��̬·�ɱ�--��һ��
	 */
	public static final String FIELD_NAME_NEXT_HOP = "au8NextHop";

	/**
	 * �����ƴ���Э���
	 */
	public static final String TABLE_NAME_T_SCTP = "T_SCTP";

	/**
	 * �����ƴ���Э���--ԴIP��ַ1
	 */
	public static final String FIELD_NAME_SRC_IP_ID1 = "u8SrcIPID1";

	/**
	 * �����ƴ���Э���--ԴIP��ַ2
	 */
	public static final String FIELD_NAME_SRC_IP_ID2 = "u8SrcIPID2";

	/**
	 * �����ƴ���Э���--ԴIP��ַ3
	 */
	public static final String FIELD_NAME_SRC_IP_ID3 = "u8SrcIPID3";

	/**
	 * �����ƴ���Э���--ԴIP��ַ4
	 */
	public static final String FIELD_NAME_SRC_IP_ID4 = "u8SrcIPID4";
	/**
	 * eNB������
	 */
	public static final String TABLE_NAME_T_ENB_PARA = "T_ENB_PARA";

	/**
	 * eNBID
	 */
	public static final String FIELD_NAME_ENB_ID = "u32eNBId";

	/**
	 * eNB����
	 */
	public static final String FIELD_NAME_ENB_NAME = "au8eNBName";

	/**
	 * eNB������-�����㷨
	 */
	public static final String FIELD_NAME_EEA = "au8EEA";

	/**
	 * eNB������-�����Ա����㷨
	 */
	public static final String FIELD_NAME_EIA = "au8EIA";

	/**
	 * ��Ⱥ����
	 */
	public static final String FIELD_NAME_PTT_ENABLE = "u8PttEnable";

	/**
	 * С��������
	 */
	public static final String TABLE_NAME_T_CELL_PARA = "T_CEL_PARA";

	/**
	 * С��������--С��ID
	 */
	public static final String FIELD_NAME_CELL_ID = "u8CId";

	/**
	 * С��������--��������
	 */
	public static final String FIELD_NAME_TAC = "u16TAC";

	/**
	 * С��������--�ƶ�������
	 */
	public static final String FIELD_NAME_MCC = "au8MCC";

	/**
	 * С��������--�ƶ�������
	 */
	public static final String FIELD_NAME_MNC = "au8MNC";

	/**
	 * С��������--ϵͳ����
	 */
	public static final String FIELD_NAME_CELL_NAME = "au8CellLable";

	/**
	 * С��������--ϵͳ����
	 */
	public static final String FIELD_NAME_SYS_BAND_WIDTH = "u8SysBandWidth";

	/**
	 * С��������--��֡���
	 */
	public static final String FIELD_NAME_ULDLSLOTALLOC = "u8UlDlSlotAlloc";
	/**
	 * С��������--�������߶˿���
	 */
	public static final String FIELD_NAME_DL_ANT_PORT_NUM = "u8DlAntPortNum";
	/**
	 * С��������--ͬƵ�л�������������
	 */
	public static final String FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG = "u8IntraFreqHOMeasCfg";

	/**
	 * С��������--A2������������
	 */
	public static final String FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG = "u8A2ForInterFreqMeasCfg";

	/**
	 * С��������--A1������������
	 */
	public static final String FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG = "u8A1ForInterFreqMeasCfg";

	/**
	 * С��������--�ض���A2������������
	 */
	public static final String FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG = "u8A2ForRedirectMeasCfg";

	/**
	 * С��������--ͬƵ���ڲ�����������
	 */
	public static final String FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG = "u8IntraFreqPrdMeasCfg";
	public static final String FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG_16 = "u16IntraFreqPrdMeasCfg";

	/**
	 * С��������--ICIC��A3������������
	 */
	public static final String FIELD_NAME_ICIC_A3_MEAS_CFG = "u8IcicA3MeasCfg";

	/**
	 * С��������--С�����˺�
	 */
	public static final String FIELD_NAME_TOPO_NO = "u8TopoNO";

	/**
	 * С��������--����ģʽ
	 */
	public static final String FIELD_NAME_UE_TRANS_MODE = "u8UeTransMode";

	/**
	 * С�����������ŵ����ò�����
	 */
	public static final String TABLE_NAME_T_CELL_PUCH = "T_CEL_PUCH";

	/**
	 * С�����������ŵ����ò�����--PUCCH format 1/1a/1bѭ��ƫ����
	 */
	public static final String FIELD_NAME_DELTA_PUCCH_SHIFT = "u8DeltaPucchShift";

	/**
	 * С�����������ŵ����ò�����--PUCCH format 2/2a/2bʹ�õ�RB��
	 */
	public static final String FIELD_NAME_RB2 = "u8N_RB2";

	/**
	 * С�����������ŵ����ò�����--PUCCH SR�ŵ�����
	 */
	public static final String FIELD_NAME_SR_CHN = "u16N_SrChn";

	/**
	 * С�����������ŵ����ò�����--SRI��������
	 */
	public static final String FIELD_NAME_SRI_TRANS_PRD = "u8SRITransPrd";

	/**
	 * С�����������ŵ����ò�����--PUCCH�ϱ���CQI/PMI������
	 */
	public static final String FIELD_NAME_RPT_CQI_PRD = "u8RptCqiPrd";

	/**
	 * С�����������ŵ����ò�����--SRS��������
	 */
	public static final String FIELD_NAME_SRS_BW_CFG_INDEX = "u8SrsBwCfgIndex";

	/**
	 * PRACH�������ñ�
	 */
	public static final String TABLE_NAME_T_CEL_PRACH = "T_CEL_PRACH";

	/**
	 * PRACH�������ñ�--�߼�����������
	 */
	public static final String FIELD_NAME_ROOT_SEQ_INDEX = "u16RootSeqIndex";

	/**
	 * PRACH�������ñ�--�������ǰ����ʼRB
	 */
	public static final String FIELD_NAME_PRACH_FREQ_OFFSET = "u8PrachFreqOffset";

	/**
	 * PRACH�������ñ�--�������ǰ������ʱ����������
	 */
	public static final String FIELD_NAME_PRACH_CFG_INDEX = "u8PrachCfgIndex";

	/**
	 * С���㷨������
	 */
	public static final String TABLE_NAME_T_CEL_ALG = "T_CEL_ALG";
	
	
	/**
	 * С���㷨������2
	 */
	public static final String TABLE_NAME_T_CEL_ALG2 = "T_CEL_ALG2";
	
	/**
	 * С���㷨������2--Pkģʽ
	 */
	public static final String FIELD_NAME_PK_MODE = "u8Pkmode";
	

	/**
	 * С���㷨������--����Ԥ����RB��
	 */
	public static final String FIELD_NAME_UL_RB_NUM = "u8UlRbNum";

	/**
	 * С���㷨������--����Ԥ���ȿ���
	 */
	public static final String FIELD_NAME_UL_PRE_SCH_ENABLE = "b8UlPreSchEnable";

	/**
	 * С���㷨������--����Ԥ����RB��
	 */
	public static final String FIELD_NAME_DL_RB_NUM = "u8DlRbNum";

	/**
	 * С���㷨������--����Ԥ���ȿ���
	 */
	public static final String FIELD_NAME_DL_PRE_SCH_ENABLE = "b8DlPreSchEnable";

	/**
	 * С���㷨������--����RB�������ƿ���
	 */
	public static final String FIELD_NAME_UL_RB_ENABLE = "b8UlRbEnable";

	/**
	 * С���㷨������--����������RB��
	 */
	public static final String FIELD_NAME_UL_MAX_RB_NUM = "u8UlMaxRbNum";

	/**
	 * С���㷨������--������С����RB��
	 */
	public static final String FIELD_NAME_UL_MIN_RB_NUM = "u8UlMinRbNum";

	/**
	 * С���㷨������--����RB�������ƿ���
	 */
	public static final String FIELD_NAME_DL_RB_ENABLE = "b8DlRbEnable";

	/**
	 * С���㷨������--����������RB��
	 */
	public static final String FIELD_NAME_DL_MAX_RB_NUM = "u8DlMaxRbNum";

	/**
	 * С���㷨������--������С����RB��
	 */
	public static final String FIELD_NAME_DL_MIN_RB_NUM = "u8DlMinRbNum";

	/**
	 * С���㷨������--CFI����
	 */
	public static final String FIELD_NAME_CFI = "u8Cfi";

	/**
	 * С���㷨������--DL ICIC�㷨����
	 */
	public static final String FIELD_NAME_DL_ICIC_SWITCH = "b8DlIcicSwitch";

	/**
	 * С���㷨������--DLС����ԵƵ��bitmap
	 */
	public static final String FIELD_NAME_DL_CEB_BITMAP = "au8DlCebBitmap";

	/**
	 * С���㷨������--UL ICIC�㷨����
	 */
	public static final String FIELD_NAME_UL_ICIC_SWITCH = "b8UlIcicSwitch";

	/**
	 * С���㷨������--ULС����ԵƵ��bitmap
	 */
	public static final String FIELD_NAME_UL_CEB_BITMAP = "au8UlCebBitmap";

	/**
	 * С���㷨������--����Ԥ������֡����
	 */
	public static final String FIELD_NAME_UL_SUBFRM_FLAG = "ab8UlSubfrmFlag";

	/**
	 * С���㷨������--����Ԥ������֡����
	 */
	public static final String FIELD_NAME_DL_SUBFRM_FLAG = "ab8DlSubfrmFlag";

	/**
	 * С���㷨������--���䷽��
	 */
	public static final String FIELD_NAME_TS = "u8TS";

	/**
	 * ������Ƶ���ñ�
	 */
	public static final String TABLE_NAME_T_ENB_CTFREQ = "T_ENB_CTFREQ";

	/**
	 * ������Ƶ���ñ�--Ƶ��ָʾ
	 */
	public static final String FIELD_NAME_FREQ_BAND = "u8FreqBandInd";

	/**
	 * ������Ƶ���ñ�--����Ƶ��
	 */
	public static final String FIELD_NAME_CENTER_FREQ = "u32CenterFreq";

	/**
	 * ������Ƶ���ñ�--��Ƶ�л�������������
	 */
	public static final String FIELD_NAME_INTER_FREQ_HO_MEAS_CFG = "u8InterFreqHOMeasCfg";

	/**
	 * ������Ƶ���ñ�--����Ƶ�����ñ�ʶ
	 */
	public static final String FIELD_NAME_CFG_IDX = "u8CfgIdx";

	/**
	 * �������ñ�
	 */
	public static final String TABLE_NAME_T_ENB_MEASCFG = "T_ENB_MEASCFG";

	/**
	 * �������ñ�--������������
	 */
	public static final String FIELD_NAME_MEAS_CFG_IDX = "u8MeasCfgIdx";

	/**
	 * �������ñ�--�¼���ʶ
	 */
	public static final String FIELD_NAME_EVT_ID = "u8EvtId";

	/**
	 * С�����й��ز�����
	 */
	public static final String TABLE_NAME_T_CEL_DLPC = "T_CEL_DLPC";

	/**
	 * С�����й��ز�����--С��������ܹ���
	 */
	public static final String FIELD_NAME_CELL_TRANS_PWR = "u16CellTransPwr";

	/**
	 * С�����й��ز�����--С���ο��źŹ���
	 */
	public static final String FIELD_NAME_CELL_SPE_REF_SIG_PWR = "u16CellSpeRefSigPwr";

	/**
	 * С�����й��ز�����--PB
	 */
	public static final String FIELD_NAME_PB = "u8PB";

	/**
	 * С�����й��ز�����--������PDSCH��С��RS�Ĺ���ƫ��[PA]
	 */
	public static final String FIELD_NAME_PA_FOR_DTCH = "u8PAForDTCH";

	/**
	 * ������ϵ�������ñ�
	 */
	public static final String TABLE_NAME_T_CEL_NBRCEL = "T_CEL_NBRCEL";

	/**
	 * ������ϵ�������ñ�--����С����ʶ
	 */
	public static final String FIELD_NAME_SVR_CID = "u8SvrCID";

	/**
	 * ������ϵ�������ñ�--��eNB��ʶ
	 */
	public static final String FIELD_NAME_NBR_ENBID = "u32NbreNBID";

	/**
	 * ������ϵ�������ñ�--��С����ʶ
	 */
	public static final String FIELD_NAME_NBR_CID = "u8NbrCID";

	/**
	 * ������ϵ�������ñ�--����С����ʶ
	 */
	public static final String FIELD_NAME_PHY_CELL_ID = "u16PhyCellId";

	/**
	 * ������ϵ�������ñ�--��������Ƶ������
	 */
	public static final String FIELD_NAME_CENTER_FREQ_CFG_IDX = "u8CenterFreqCfgIdx";

	/**
	 * С����Ⱥ���ò���
	 * 
	 */
	public static final String TABLE_NAME_T_CEL_PTT = "T_CEL_PTT";

	/**
	 * С����Ⱥ���ò���--��Ⱥ�㲥Ѱ������
	 */
	public static final String FIELD_NAME_PTT_BPAGING_CYCLE = "u8PttBPagingCycle";

	/**
	 * С����Ⱥ���ò���--��Ⱥ�㲥Ѱ��֡��
	 */
	public static final String FIELD_NAME_PTT_BPAGING_FN = "u8PttBPagingFN";

	/**
	 * С����Ⱥ���ò���--��Ⱥ�㲥Ѱ����֡��
	 */
	public static final String FIELD_NAME_PTT_BPAGING_SUB_FN = "u8PttBPagingSubFN";

	/**
	 * С����Ⱥ���ò���--��Ⱥ����RB�������ƿ���
	 */
	public static final String FIELD_NAME_PTT_DL_RB_ENABLE = "b8PttDlRbEnable";

	/**
	 * С����Ⱥ���ò���--��Ⱥ����������RB��
	 */
	public static final String FIELD_NAME_PTT_DL_MAX_RB_NUM = "u8PttDlMaxRbNum";

	/**
	 * С����Ⱥ���ò���--��Ⱥ�����PA����
	 */
	public static final String FIELD_NAME_PT_FOR_PDSCH = "u8PtForPDSCH";

	/**
	 * SI�ĵ������ò�����
	 */
	public static final String TABLE_NAME_T_CEL_SISCH = "T_CEL_SISCH";

	/**
	 * SI�ĵ������ò�����-SI��ʶ
	 */
	public static final String FIELD_NAME_SIID = "u8SIId";

	/**
	 * SI�ĵ������ò�����-�Ƿ����Sib2
	 */
	public static final String FIELD_NAME_SIB2 = "u8Sib2";

	/**
	 * SI�ĵ������ò�����-�Ƿ����Sib3
	 */
	public static final String FIELD_NAME_SIB3 = "u8Sib3";

	/**
	 * SI�ĵ������ò�����-�Ƿ����Sib4
	 */
	public static final String FIELD_NAME_SIB4 = "u8Sib4";

	/**
	 * SI�ĵ������ò�����-�Ƿ����Sib5
	 */
	public static final String FIELD_NAME_SIB5 = "u8Sib5";

	/**
	 * SI�ĵ������ò�����-�Ƿ������ȺSib
	 */
	public static final String FIELD_NAME_SIBPTT = "u8SibPtt";


	/**
	 * DRX���ò�����-С����ʶ
	 */
	public static final String FIELD_NAME_DRX_CELL_ID = "u8CellId";

	/**
	 * �������
	 */
	public static final String TABLE_NAME_T_SWPKG = "T_SWPKG";

	/**
	 * ����汾��Ϣ��
	 */
	public static final String TABLE_NAME_T_SWINFO = "T_SWINFO";

	/**
	 * ����汾��Ϣ��-����״̬(1)Running|(2)Backup
	 */
	public static final String FIELD_NAME_RUNSTATUS = "u8RunStatus";

	/**
	 * ����汾��Ϣ��-����״̬-����(1)Running
	 */
	public static final int RUNSTATUS_RUNNING = 1;

	/**
	 * ����汾��Ϣ��-����״̬-����(2)Backup
	 */
	public static final int RUNSTATUS_BACKUP = 2;

	/**
	 * �豸��
	 */
	public static final String TABLE_NAME_T_DEVICE = "T_DEVICE";

	/**
	 * ҵ�񹦿ز�����
	 */
	public static final String TABLE_NAME_T_ENB_SRVPC = "T_ENB_SRVPC";

	/**
	 * ҵ�񹦿ز�����-ҵ��PDSCH��С��RS�Ĺ���ƫ��
	 */
	public static final String FIELD_NAME_PA = "u8PA";

	/**
	 * ������ر�
	 */
	public static final String TABLE_NAME_T_ENVMON = "T_ENVMON";

	/**
	 * ������ر�-������ر��
	 */
	public static final String FIELD_NAME_ENV_M_NO = "u32EnvMNO";

	/**
	 * ������ر�-�����������
	 */
	public static final String FIELD_NAME_ENV_M_TYPE = "u32EnvMType";

	/**
	 * �澯����
	 */
	public static final String TABLE_NAME_T_ALARM_PARA = "T_ALARM_PARA";

	/**
	 * �澯����-�澯��
	 */
	public static final String FIELD_NAME_ALARMID = "u32AlarmID";

	/**
	 * �澯����-�澯����
	 */
	public static final String FIELD_NAME_FAULTCODE = "u8FaultCode";

	/**
	 * DRX���ò�����
	 */
	public static final String TABLE_NAME_T_CEL_DRX = "T_CEL_DRX";

	/**
	 * DRX���ò�����-DRXʹ�ܿ���
	 */
	public static final String FIELD_NAME_DRX_ENABLE = "u8DrxEnable";

	/**
	 * SPS���ò�����
	 */
	public static final String TABLE_NAME_T_CEL_SPS = "T_CEL_SPS";
	
	
	/**
	 * T_CEL_ADMIT������
	 */
	public static final String TABLE_NAME_T_CEL_ADMIT = "T_CEL_ADMIT";
	

	/**
	 * SPS���ò�����-����SPS����
	 */
	public static final String FIELD_NAME_SPS_DOWNLINK_SWICTH = "u8SpsDownLinkSwicth";

	/**
	 * SPS���ò�����-����SPS����
	 */
	public static final String FIELD_NAME_SPS_UPLINK_SWICTH = "u8SpsUpLinkSwicth";

	/**
	 * SPS���ò�����-����SPS ACK PUCCH���������Դ����
	 */
	public static final String FIELD_NAME_N_SPSANCHN = "u16N_SpsANChn";

	/**
	 * С��ID
	 */
	public static final String FIELD_NAME_CELL_ID_ = "u8CellId";

	/**
	 * VLAN��
	 */
	public static final String TABLE_NAME_T_VLAN = "T_VLAN";

	/**
	 * VLAN��-ҵ������
	 */
	public static final String FIELD_NAME_SERVICE_TYPE = "u8ServiceType";

	/**
	 * VLAN��-����
	 */
	public static final String FIELD_NAME_ID = "u8Id";

	/**
	 * VLAN��-VLAN����
	 */
	public static final String FIELD_NAME_VLAN_TAG = "u8VlanTag";

	/**
	 * VLAN��-VLAN��ʶ
	 */
	public static final String FIELD_NAME_VLAN_ID = "u16VlanId";

	/**
	 * VLAN��-VLAN���ȼ�
	 */
	public static final String FIELD_NAME_VLAN_PRI = "u8VlanPri";
	
	/**
	 * IPV4��-VLAN����
	 */
	public static final String FIELD_NAME_IPV4_VLAN_INDEX = "u8VlanIndex";
	
	/**
	 * ҵ��QCI���ñ�
	 */
	public static final String TABLE_NAME_T_ENB_SRVQCI = "T_ENB_SRVQCI";
	
	/**
	 * ҵ��QCI���ñ�-RTPЭ��ͷѹ������
	 */
	public static final String FIELD_NAME_ROHC_RTP = "u8RohcRTP";
	
	/**
	 * ҵ��QCI���ñ�-UDPЭ��ͷѹ������
	 */
	public static final String FIELD_NAME_ROHC_UDP = "u8RohcUDP";
	
	/**
	 * ҵ��QCI���ñ�-IPЭ��ͷѹ������
	 */
	public static final String FIELD_NAME_ROHC_IP = "u8RohcIP";
	
	
}
