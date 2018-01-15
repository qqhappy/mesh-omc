package com.xinwei.minas.enb.core.utils;

public class EnbConstantUtils {
	// 整表配置业务名称
	public static final String FULL_TABLE_CONFIG = "full_table_config";

	// 整表反构业务名称
	public static final String FULL_TABLE_REVERSE = "full_table_reverse";

	// enb软件版本升级业务名称
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

	// 软件类型
	public static final String SOFTWARE_TYPE = "softwareType";

	// 版本
	public static final String VERSION = "version";

	// 数据文件路径
	public static final String DATA_FILE_DIRECTORY = "dataFileDirectory";

	// 数据文件名
	public static final String DATA_FILE_NAME = "dataFileName";
	/**
	 * 基站版本升级
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
	 * 机架表
	 */
	public static final String TABLE_NAME_T_RACK = "T_RACK";

	/**
	 * 机框表
	 */
	public static final String TABLE_NAME_T_SHELF = "T_SHELF";

	/**
	 * 机框表-机架名
	 */
	public static final String FIELD_NAME_RACK_NAME = "s8RackName";

	/**
	 * 单板表
	 */
	public static final String TABLE_NAME_T_BOARD = "T_BOARD";

	/**
	 * 字段名机架号
	 */
	public static final String FIELD_NAME_RACKNO = "u8RackNO";

	/**
	 * 字段名机框号
	 */
	public static final String FIELD_NAME_SHELFNO = "u8ShelfNO";

	/**
	 * 字段名槽位号
	 */
	public static final String FIELD_NAME_SLOTNO = "u8SlotNO";

	/**
	 * 字段名单板类型
	 */
	public static final String FIELD_NAME_BDTYPE = "u8BDType";

	/**
	 * 单板类型BBU
	 */
	public static final int BOARD_TYPE_BBU = 1;

	/**
	 * 单板类型RRU
	 */
	public static final int BOARD_TYPE_RRU = 2;

	/**
	 * 拓扑表
	 */
	public static final String TABLE_NAME_T_TOPO = "T_TOPO";

	/**
	 * 拓扑表-主机架号
	 */
	public static final String FIELD_NAME_MRACKNO = "u8MRackNO";

	/**
	 * 拓扑表-主机框号
	 */
	public static final String FIELD_NAME_MSHELFNO = "u8MShelfNO";

	/**
	 * 拓扑表-主槽位号
	 */
	public static final String FIELD_NAME_MSLOTNO = "u8MSlotNO";

	/**
	 * 拓扑表-光口号
	 */
	public static final String FIELD_NAME_FIBER_PORT = "u8FiberPort";

	/**
	 * 拓扑表-从机架号
	 */
	public static final String FIELD_NAME_SRACKNO = "u8SRackNO";

	/**
	 * 拓扑表-从机框号
	 */
	public static final String FIELD_NAME_SSHELFNO = "u8SShelfNO";

	/**
	 * 拓扑表-从槽位号
	 */
	public static final String FIELD_NAME_SSLOTNO = "u8SSlotNO";

	/**
	 * 网管表
	 */
	public static final String TABLE_NAME_T_OMC = "T_OMC";

	/**
	 * OMC表中的网管标识
	 */
	public static final String FIELD_NAME_OMC_ID = "u8OmcID";

	/**
	 * OMC表中的IP标识
	 */
	public static final String FIELD_NAME_ENB_IP_ID = "u8eNBIPID";

	/**
	 * OMC表中的网管IP标识
	 */
	public static final String FIELD_NAME_OMC_SERVER_IP = "au8OmcServerIP";

	/**
	 * OMC表中的网管端口
	 */
	public static final String FIELD_NAME_SRC_PORT = "u16SrcPort";

	/**
	 * OMC表中的基站端口
	 */
	public static final String FIELD_NAME_DST_PORT = "u16DstPort";

	/**
	 * OMC表中的服务质量
	 */
	public static final String FIELD_NAME_QOS = "u8Qos";

	/**
	 * 状态
	 */
	public static final String FIELD_NAME_STATUS = "u32Status";

	/**
	 * 状态-正常
	 */
	public static final int STATUS_NORMAL = 0;

	/**
	 * 状态-不正常
	 */
	public static final int STATUS_ABNORMAL = 1;

	/**
	 * IPV4表
	 */
	public static final String TABLE_NAME_T_IPV4 = "T_IPV4";

	/**
	 * IPV4表中的IP标识
	 */
	public static final String FIELD_NAME_IP_ID = "u8IPID";

	/**
	 * IPV4表中端口标识
	 */
	public static final String FIELD_NAME_IPV4_PORT_ID = "U8PortID";

	/**
	 * IPV4表中--IP地址
	 */
	public static final String FIELD_NAME_IP_ADDR = "au8IPAddr";

	/**
	 * IPV4表中--掩码
	 */
	public static final String FIELD_NAME_NET_MASK = "au8NetMask";

	/**
	 * IPV4表中--网关
	 */
	public static final String FIELD_NAME_GATEWAY = "au8GWAddr";

	/**
	 * 以太网参数表
	 */
	public static final String TABLE_NAME_T_ETHPARA = "T_ETHPARA";

	/**
	 * 以太网参数表--端口标识
	 */
	public static final String FIELD_NAME_PORT_ID = "u8PortID";

	/**
	 * 以太网参数表--端口号
	 */
	public static final String FIELD_NAME_ETH_PORT = "u8EthPort";

	/**
	 * 静态路由表
	 */
	public static final String TABLE_NAME_T_STROUT = "T_STROUT";

	/**
	 * 静态路由表--下一跳
	 */
	public static final String FIELD_NAME_NEXT_HOP = "au8NextHop";

	/**
	 * 流控制传输协议表
	 */
	public static final String TABLE_NAME_T_SCTP = "T_SCTP";

	/**
	 * 流控制传输协议表--源IP地址1
	 */
	public static final String FIELD_NAME_SRC_IP_ID1 = "u8SrcIPID1";

	/**
	 * 流控制传输协议表--源IP地址2
	 */
	public static final String FIELD_NAME_SRC_IP_ID2 = "u8SrcIPID2";

	/**
	 * 流控制传输协议表--源IP地址3
	 */
	public static final String FIELD_NAME_SRC_IP_ID3 = "u8SrcIPID3";

	/**
	 * 流控制传输协议表--源IP地址4
	 */
	public static final String FIELD_NAME_SRC_IP_ID4 = "u8SrcIPID4";
	/**
	 * eNB参数表
	 */
	public static final String TABLE_NAME_T_ENB_PARA = "T_ENB_PARA";

	/**
	 * eNBID
	 */
	public static final String FIELD_NAME_ENB_ID = "u32eNBId";

	/**
	 * eNB名称
	 */
	public static final String FIELD_NAME_ENB_NAME = "au8eNBName";

	/**
	 * eNB参数表-加密算法
	 */
	public static final String FIELD_NAME_EEA = "au8EEA";

	/**
	 * eNB参数表-完整性保护算法
	 */
	public static final String FIELD_NAME_EIA = "au8EIA";

	/**
	 * 集群开关
	 */
	public static final String FIELD_NAME_PTT_ENABLE = "u8PttEnable";

	/**
	 * 小区参数表
	 */
	public static final String TABLE_NAME_T_CELL_PARA = "T_CEL_PARA";

	/**
	 * 小区参数表--小区ID
	 */
	public static final String FIELD_NAME_CELL_ID = "u8CId";

	/**
	 * 小区参数表--跟踪区码
	 */
	public static final String FIELD_NAME_TAC = "u16TAC";

	/**
	 * 小区参数表--移动国家码
	 */
	public static final String FIELD_NAME_MCC = "au8MCC";

	/**
	 * 小区参数表--移动网络码
	 */
	public static final String FIELD_NAME_MNC = "au8MNC";

	/**
	 * 小区参数表--系统带宽
	 */
	public static final String FIELD_NAME_CELL_NAME = "au8CellLable";

	/**
	 * 小区参数表--系统带宽
	 */
	public static final String FIELD_NAME_SYS_BAND_WIDTH = "u8SysBandWidth";

	/**
	 * 小区参数表--子帧配比
	 */
	public static final String FIELD_NAME_ULDLSLOTALLOC = "u8UlDlSlotAlloc";
	/**
	 * 小区参数表--下行天线端口数
	 */
	public static final String FIELD_NAME_DL_ANT_PORT_NUM = "u8DlAntPortNum";
	/**
	 * 小区参数表--同频切换测量配置索引
	 */
	public static final String FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG = "u8IntraFreqHOMeasCfg";

	/**
	 * 小区参数表--A2测量配置索引
	 */
	public static final String FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG = "u8A2ForInterFreqMeasCfg";

	/**
	 * 小区参数表--A1测量配置索引
	 */
	public static final String FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG = "u8A1ForInterFreqMeasCfg";

	/**
	 * 小区参数表--重定向A2测量配置索引
	 */
	public static final String FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG = "u8A2ForRedirectMeasCfg";

	/**
	 * 小区参数表--同频周期测量配置索引
	 */
	public static final String FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG = "u8IntraFreqPrdMeasCfg";
	public static final String FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG_16 = "u16IntraFreqPrdMeasCfg";

	/**
	 * 小区参数表--ICIC的A3测量配置索引
	 */
	public static final String FIELD_NAME_ICIC_A3_MEAS_CFG = "u8IcicA3MeasCfg";

	/**
	 * 小区参数表--小区拓扑号
	 */
	public static final String FIELD_NAME_TOPO_NO = "u8TopoNO";

	/**
	 * 小区参数表--传输模式
	 */
	public static final String FIELD_NAME_UE_TRANS_MODE = "u8UeTransMode";

	/**
	 * 小区上行物理信道配置参数表
	 */
	public static final String TABLE_NAME_T_CELL_PUCH = "T_CEL_PUCH";

	/**
	 * 小区上行物理信道配置参数表--PUCCH format 1/1a/1b循环偏移量
	 */
	public static final String FIELD_NAME_DELTA_PUCCH_SHIFT = "u8DeltaPucchShift";

	/**
	 * 小区上行物理信道配置参数表--PUCCH format 2/2a/2b使用的RB数
	 */
	public static final String FIELD_NAME_RB2 = "u8N_RB2";

	/**
	 * 小区上行物理信道配置参数表--PUCCH SR信道条数
	 */
	public static final String FIELD_NAME_SR_CHN = "u16N_SrChn";

	/**
	 * 小区上行物理信道配置参数表--SRI发送周期
	 */
	public static final String FIELD_NAME_SRI_TRANS_PRD = "u8SRITransPrd";

	/**
	 * 小区上行物理信道配置参数表--PUCCH上报告CQI/PMI的周期
	 */
	public static final String FIELD_NAME_RPT_CQI_PRD = "u8RptCqiPrd";

	/**
	 * 小区上行物理信道配置参数表--SRS带宽配置
	 */
	public static final String FIELD_NAME_SRS_BW_CFG_INDEX = "u8SrsBwCfgIndex";

	/**
	 * PRACH参数配置表
	 */
	public static final String TABLE_NAME_T_CEL_PRACH = "T_CEL_PRACH";

	/**
	 * PRACH参数配置表--逻辑根序列索引
	 */
	public static final String FIELD_NAME_ROOT_SEQ_INDEX = "u16RootSeqIndex";

	/**
	 * PRACH参数配置表--随机接入前导起始RB
	 */
	public static final String FIELD_NAME_PRACH_FREQ_OFFSET = "u8PrachFreqOffset";

	/**
	 * PRACH参数配置表--随机接入前导发送时刻配置索引
	 */
	public static final String FIELD_NAME_PRACH_CFG_INDEX = "u8PrachCfgIndex";

	/**
	 * 小区算法参数表
	 */
	public static final String TABLE_NAME_T_CEL_ALG = "T_CEL_ALG";
	
	
	/**
	 * 小区算法参数表2
	 */
	public static final String TABLE_NAME_T_CEL_ALG2 = "T_CEL_ALG2";
	
	/**
	 * 小区算法参数表2--Pk模式
	 */
	public static final String FIELD_NAME_PK_MODE = "u8Pkmode";
	

	/**
	 * 小区算法参数表--上行预调度RB数
	 */
	public static final String FIELD_NAME_UL_RB_NUM = "u8UlRbNum";

	/**
	 * 小区算法参数表--上行预调度开关
	 */
	public static final String FIELD_NAME_UL_PRE_SCH_ENABLE = "b8UlPreSchEnable";

	/**
	 * 小区算法参数表--下行预调度RB数
	 */
	public static final String FIELD_NAME_DL_RB_NUM = "u8DlRbNum";

	/**
	 * 小区算法参数表--下行预调度开关
	 */
	public static final String FIELD_NAME_DL_PRE_SCH_ENABLE = "b8DlPreSchEnable";

	/**
	 * 小区算法参数表--上行RB分配限制开关
	 */
	public static final String FIELD_NAME_UL_RB_ENABLE = "b8UlRbEnable";

	/**
	 * 小区算法参数表--上行最大分配RB数
	 */
	public static final String FIELD_NAME_UL_MAX_RB_NUM = "u8UlMaxRbNum";

	/**
	 * 小区算法参数表--上行最小分配RB数
	 */
	public static final String FIELD_NAME_UL_MIN_RB_NUM = "u8UlMinRbNum";

	/**
	 * 小区算法参数表--下行RB分配限制开关
	 */
	public static final String FIELD_NAME_DL_RB_ENABLE = "b8DlRbEnable";

	/**
	 * 小区算法参数表--下行最大分配RB数
	 */
	public static final String FIELD_NAME_DL_MAX_RB_NUM = "u8DlMaxRbNum";

	/**
	 * 小区算法参数表--下行最小分配RB数
	 */
	public static final String FIELD_NAME_DL_MIN_RB_NUM = "u8DlMinRbNum";

	/**
	 * 小区算法参数表--CFI配置
	 */
	public static final String FIELD_NAME_CFI = "u8Cfi";

	/**
	 * 小区算法参数表--DL ICIC算法开关
	 */
	public static final String FIELD_NAME_DL_ICIC_SWITCH = "b8DlIcicSwitch";

	/**
	 * 小区算法参数表--DL小区边缘频带bitmap
	 */
	public static final String FIELD_NAME_DL_CEB_BITMAP = "au8DlCebBitmap";

	/**
	 * 小区算法参数表--UL ICIC算法开关
	 */
	public static final String FIELD_NAME_UL_ICIC_SWITCH = "b8UlIcicSwitch";

	/**
	 * 小区算法参数表--UL小区边缘频带bitmap
	 */
	public static final String FIELD_NAME_UL_CEB_BITMAP = "au8UlCebBitmap";

	/**
	 * 小区算法参数表--上行预调度子帧开关
	 */
	public static final String FIELD_NAME_UL_SUBFRM_FLAG = "ab8UlSubfrmFlag";

	/**
	 * 小区算法参数表--下行预调度子帧开关
	 */
	public static final String FIELD_NAME_DL_SUBFRM_FLAG = "ab8DlSubfrmFlag";

	/**
	 * 小区算法参数表--传输方案
	 */
	public static final String FIELD_NAME_TS = "u8TS";

	/**
	 * 中心载频配置表
	 */
	public static final String TABLE_NAME_T_ENB_CTFREQ = "T_ENB_CTFREQ";

	/**
	 * 中心载频配置表--频段指示
	 */
	public static final String FIELD_NAME_FREQ_BAND = "u8FreqBandInd";

	/**
	 * 中心载频配置表--中心频点
	 */
	public static final String FIELD_NAME_CENTER_FREQ = "u32CenterFreq";

	/**
	 * 中心载频配置表--异频切换测量配置索引
	 */
	public static final String FIELD_NAME_INTER_FREQ_HO_MEAS_CFG = "u8InterFreqHOMeasCfg";

	/**
	 * 中心载频配置表--中心频点配置标识
	 */
	public static final String FIELD_NAME_CFG_IDX = "u8CfgIdx";

	/**
	 * 测量配置表
	 */
	public static final String TABLE_NAME_T_ENB_MEASCFG = "T_ENB_MEASCFG";

	/**
	 * 测量配置表--测量配置索引
	 */
	public static final String FIELD_NAME_MEAS_CFG_IDX = "u8MeasCfgIdx";

	/**
	 * 测量配置表--事件标识
	 */
	public static final String FIELD_NAME_EVT_ID = "u8EvtId";

	/**
	 * 小区下行功控参数表
	 */
	public static final String TABLE_NAME_T_CEL_DLPC = "T_CEL_DLPC";

	/**
	 * 小区下行功控参数表--小区最大发射总功率
	 */
	public static final String FIELD_NAME_CELL_TRANS_PWR = "u16CellTransPwr";

	/**
	 * 小区下行功控参数表--小区参考信号功率
	 */
	public static final String FIELD_NAME_CELL_SPE_REF_SIG_PWR = "u16CellSpeRefSigPwr";

	/**
	 * 小区下行功控参数表--PB
	 */
	public static final String FIELD_NAME_PB = "u8PB";

	/**
	 * 小区下行功控参数表--公共级PDSCH与小区RS的功率偏置[PA]
	 */
	public static final String FIELD_NAME_PA_FOR_DTCH = "u8PAForDTCH";

	/**
	 * 邻区关系参数配置表
	 */
	public static final String TABLE_NAME_T_CEL_NBRCEL = "T_CEL_NBRCEL";

	/**
	 * 邻区关系参数配置表--服务小区标识
	 */
	public static final String FIELD_NAME_SVR_CID = "u8SvrCID";

	/**
	 * 邻区关系参数配置表--邻eNB标识
	 */
	public static final String FIELD_NAME_NBR_ENBID = "u32NbreNBID";

	/**
	 * 邻区关系参数配置表--邻小区标识
	 */
	public static final String FIELD_NAME_NBR_CID = "u8NbrCID";

	/**
	 * 邻区关系参数配置表--物理小区标识
	 */
	public static final String FIELD_NAME_PHY_CELL_ID = "u16PhyCellId";

	/**
	 * 邻区关系参数配置表--邻区中心频点索引
	 */
	public static final String FIELD_NAME_CENTER_FREQ_CFG_IDX = "u8CenterFreqCfgIdx";

	/**
	 * 小区集群配置参数
	 * 
	 */
	public static final String TABLE_NAME_T_CEL_PTT = "T_CEL_PTT";

	/**
	 * 小区集群配置参数--集群广播寻呼周期
	 */
	public static final String FIELD_NAME_PTT_BPAGING_CYCLE = "u8PttBPagingCycle";

	/**
	 * 小区集群配置参数--集群广播寻呼帧号
	 */
	public static final String FIELD_NAME_PTT_BPAGING_FN = "u8PttBPagingFN";

	/**
	 * 小区集群配置参数--集群广播寻呼子帧号
	 */
	public static final String FIELD_NAME_PTT_BPAGING_SUB_FN = "u8PttBPagingSubFN";

	/**
	 * 小区集群配置参数--集群下行RB分配限制开关
	 */
	public static final String FIELD_NAME_PTT_DL_RB_ENABLE = "b8PttDlRbEnable";

	/**
	 * 小区集群配置参数--集群下行最大分配RB数
	 */
	public static final String FIELD_NAME_PTT_DL_MAX_RB_NUM = "u8PttDlMaxRbNum";

	/**
	 * 小区集群配置参数--集群组呼的PA配置
	 */
	public static final String FIELD_NAME_PT_FOR_PDSCH = "u8PtForPDSCH";

	/**
	 * SI的调度配置参数表
	 */
	public static final String TABLE_NAME_T_CEL_SISCH = "T_CEL_SISCH";

	/**
	 * SI的调度配置参数表-SI标识
	 */
	public static final String FIELD_NAME_SIID = "u8SIId";

	/**
	 * SI的调度配置参数表-是否包含Sib2
	 */
	public static final String FIELD_NAME_SIB2 = "u8Sib2";

	/**
	 * SI的调度配置参数表-是否包含Sib3
	 */
	public static final String FIELD_NAME_SIB3 = "u8Sib3";

	/**
	 * SI的调度配置参数表-是否包含Sib4
	 */
	public static final String FIELD_NAME_SIB4 = "u8Sib4";

	/**
	 * SI的调度配置参数表-是否包含Sib5
	 */
	public static final String FIELD_NAME_SIB5 = "u8Sib5";

	/**
	 * SI的调度配置参数表-是否包含集群Sib
	 */
	public static final String FIELD_NAME_SIBPTT = "u8SibPtt";


	/**
	 * DRX配置参数表-小区标识
	 */
	public static final String FIELD_NAME_DRX_CELL_ID = "u8CellId";

	/**
	 * 软件包表
	 */
	public static final String TABLE_NAME_T_SWPKG = "T_SWPKG";

	/**
	 * 软件版本信息表
	 */
	public static final String TABLE_NAME_T_SWINFO = "T_SWINFO";

	/**
	 * 软件版本信息表-运行状态(1)Running|(2)Backup
	 */
	public static final String FIELD_NAME_RUNSTATUS = "u8RunStatus";

	/**
	 * 软件版本信息表-运行状态-运行(1)Running
	 */
	public static final int RUNSTATUS_RUNNING = 1;

	/**
	 * 软件版本信息表-运行状态-备用(2)Backup
	 */
	public static final int RUNSTATUS_BACKUP = 2;

	/**
	 * 设备表
	 */
	public static final String TABLE_NAME_T_DEVICE = "T_DEVICE";

	/**
	 * 业务功控参数表
	 */
	public static final String TABLE_NAME_T_ENB_SRVPC = "T_ENB_SRVPC";

	/**
	 * 业务功控参数表-业务级PDSCH与小区RS的功率偏差
	 */
	public static final String FIELD_NAME_PA = "u8PA";

	/**
	 * 环境监控表
	 */
	public static final String TABLE_NAME_T_ENVMON = "T_ENVMON";

	/**
	 * 环境监控表-环境监控编号
	 */
	public static final String FIELD_NAME_ENV_M_NO = "u32EnvMNO";

	/**
	 * 环境监控表-环境监控类型
	 */
	public static final String FIELD_NAME_ENV_M_TYPE = "u32EnvMType";

	/**
	 * 告警参数
	 */
	public static final String TABLE_NAME_T_ALARM_PARA = "T_ALARM_PARA";

	/**
	 * 告警参数-告警码
	 */
	public static final String FIELD_NAME_ALARMID = "u32AlarmID";

	/**
	 * 告警参数-告警子码
	 */
	public static final String FIELD_NAME_FAULTCODE = "u8FaultCode";

	/**
	 * DRX配置参数表
	 */
	public static final String TABLE_NAME_T_CEL_DRX = "T_CEL_DRX";

	/**
	 * DRX配置参数表-DRX使能开关
	 */
	public static final String FIELD_NAME_DRX_ENABLE = "u8DrxEnable";

	/**
	 * SPS配置参数表
	 */
	public static final String TABLE_NAME_T_CEL_SPS = "T_CEL_SPS";
	
	
	/**
	 * T_CEL_ADMIT参数表
	 */
	public static final String TABLE_NAME_T_CEL_ADMIT = "T_CEL_ADMIT";
	

	/**
	 * SPS配置参数表-下行SPS开关
	 */
	public static final String FIELD_NAME_SPS_DOWNLINK_SWICTH = "u8SpsDownLinkSwicth";

	/**
	 * SPS配置参数表-上行SPS开关
	 */
	public static final String FIELD_NAME_SPS_UPLINK_SWICTH = "u8SpsUpLinkSwicth";

	/**
	 * SPS配置参数表-下行SPS ACK PUCCH可用码道资源个数
	 */
	public static final String FIELD_NAME_N_SPSANCHN = "u16N_SpsANChn";

	/**
	 * 小区ID
	 */
	public static final String FIELD_NAME_CELL_ID_ = "u8CellId";

	/**
	 * VLAN表
	 */
	public static final String TABLE_NAME_T_VLAN = "T_VLAN";

	/**
	 * VLAN表-业务类型
	 */
	public static final String FIELD_NAME_SERVICE_TYPE = "u8ServiceType";

	/**
	 * VLAN表-索引
	 */
	public static final String FIELD_NAME_ID = "u8Id";

	/**
	 * VLAN表-VLAN开关
	 */
	public static final String FIELD_NAME_VLAN_TAG = "u8VlanTag";

	/**
	 * VLAN表-VLAN标识
	 */
	public static final String FIELD_NAME_VLAN_ID = "u16VlanId";

	/**
	 * VLAN表-VLAN优先级
	 */
	public static final String FIELD_NAME_VLAN_PRI = "u8VlanPri";
	
	/**
	 * IPV4表-VLAN索引
	 */
	public static final String FIELD_NAME_IPV4_VLAN_INDEX = "u8VlanIndex";
	
	/**
	 * 业务QCI配置表
	 */
	public static final String TABLE_NAME_T_ENB_SRVQCI = "T_ENB_SRVQCI";
	
	/**
	 * 业务QCI配置表-RTP协议头压缩开关
	 */
	public static final String FIELD_NAME_ROHC_RTP = "u8RohcRTP";
	
	/**
	 * 业务QCI配置表-UDP协议头压缩开关
	 */
	public static final String FIELD_NAME_ROHC_UDP = "u8RohcUDP";
	
	/**
	 * 业务QCI配置表-IP协议头压缩开关
	 */
	public static final String FIELD_NAME_ROHC_IP = "u8RohcIP";
	
	
}
